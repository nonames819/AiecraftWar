package edu.hitsz.application.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.database.Score;
import edu.hitsz.database.ScoreDaoImpl;
import edu.hitsz.factory.enemy.BossEnemyFactory;
import edu.hitsz.factory.enemy.EliteEnemyFactory;
import edu.hitsz.factory.enemy.EnemyFactory;
import edu.hitsz.factory.enemy.MobEnemyFactory;
import edu.hitsz.factory.prop.BombPropFactory;
import edu.hitsz.factory.prop.FirePropFactory;
import edu.hitsz.factory.prop.HpPropFactory;
import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.observer.BombReminder;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static edu.hitsz.application.Main.score;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    protected int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    protected final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;
    protected final List<EnemyFactory> enemyFactories;
    protected final List<PropFactory> propFactories;
    protected EnemyFactory bossFactory;

    protected final BombReminder reminder;
    
    protected int enemyMaxNumber;

    protected boolean gameOverFlag = false;
    protected int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration;
    protected int cycleTime = 0;

    /**
     * Boss机出现阈值
     */
    protected int boundaryValue;
    protected int times = 1;

    protected final List<Double> enemyRate = new LinkedList<>();

    protected abstract void setEnemyFactories();
    protected abstract void setPropsFactories();
    protected abstract void setBoundaryValue();
    protected abstract void setEnemyMaxNumber();
    protected abstract void setCycleDuration();
    protected abstract void setHeroAircraft();
    protected void extraProcess(){};

    public int getEnemyMaxNumber() {
        return enemyMaxNumber;
    }

    public Game() {
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        enemyFactories = new LinkedList<>();
        propFactories = new LinkedList<>();
        bossFactory = new BossEnemyFactory();

        //观察者模式通知对象
        reminder = BombProp.getReminder();

        setBoundaryValue();
        setEnemyMaxNumber();
        setHeroAircraft();
        setEnemyFactories();
        setPropsFactories();
        setCycleDuration();
        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {


        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                createEnemy();

                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                Main.MUSIC_MANAGER.runForOnce(MusicManager.GAME_OVER);
                System.out.println("Game Over!");

                synchronized (this) {
                    this.notifyAll();
                }

            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }


    //***********************
    //      Action 各部分
    //***********************

    protected boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected void createEnemy() {
        // 新敌机产生
        if (enemyAircrafts.size() < enemyMaxNumber) {
            int type = 0;
            double rand = Math.random();
            while (type < enemyFactories.size()) {
                rand -= enemyRate.get(type);
                if (rand <= 0) {
                    AbstractEnemy enemy = enemyFactories.get(type).createEnemy();
                    enemyAircrafts.add(enemy);
                    reminder.addObserver(enemy);
                }  //根据概率产生敌机
                type++;
            }
        }

        //产生Boss机
        if (score.getScore() >= times * boundaryValue && boundaryValue > 0) {
            boolean flag = false;
            //判断是否有Boss机存在
            for (AbstractEnemy enemy : enemyAircrafts) {
                if (enemy.getClass() == BossEnemy.class) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                extraProcess();
                AbstractEnemy enemy = bossFactory.createEnemy();
                enemyAircrafts.add(enemy);
                System.out.println("Boss机出现，血量："+enemy.getHp()+"，攻击力："+enemy.getPower()+"，节奏（刷新周期）："+cycleDuration);
                Main.MUSIC_MANAGER.runForever(MusicManager.BOSS);
                times += 1;
            }
        }
    }

    protected void shootAction() {
        // 敌机射击
        for (AbstractAircraft enemyAicraft : enemyAircrafts) {
            List<BaseBullet> bullets = enemyAicraft.shoot();
            enemyBullets.addAll(bullets);
            for (BaseBullet bullet : bullets){
                reminder.addObserver((EnemyBullet)bullet);
            }
        }

        // 英雄射击
        List<BaseBullet> heroShoot = heroAircraft.shoot();
        if (!heroShoot.isEmpty()) {
            heroBullets.addAll(heroAircraft.shoot());
        }
    }

    protected void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    public void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    } //道具移动


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    protected void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;//当子弹超出屏幕时忽略
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }

        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    Main.MUSIC_MANAGER.runForOnce(MusicManager.BULLET_HIT);
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft.getClass().equals(BossEnemy.class)) {
                            // 停止音乐
                            System.out.println("BOSS: "+enemyAircraft.getHp());
                            Main.MUSIC_MANAGER.stop(MusicManager.BOSS);
                        }

                        AbstractProp tempProp = enemyAircraft.dropProps(propFactories);
                        if (tempProp != null) {
                            props.add(tempProp);
                        }  //当生成了道具时，将其加入道具列
                        score.setScore(score.getScore() + 10);
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        //我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.crash(heroAircraft)) {
                if(prop.getClass().equals(BombProp.class)){
                    Main.MUSIC_MANAGER.runForOnce(MusicManager.BOMB);
                }
                Main.MUSIC_MANAGER.runForOnce(MusicManager.GET_SUPPLY);
                prop.vanish();
                prop.effect(heroAircraft);
            }
        }
        reminder.deleteAll();

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }



    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，再绘制道具，最后绘制飞机
        // 这样子弹和道具显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);

        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    protected void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    protected void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + score.getScore(), x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
        y = y + 20;
        g.drawString("LEVEL:" + this.times, x, y);

    }


}
