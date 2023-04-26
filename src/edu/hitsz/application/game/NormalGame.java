package edu.hitsz.application.game;

import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.enemy.BossEnemyFactory;
import edu.hitsz.factory.enemy.EliteEnemyFactory;
import edu.hitsz.factory.enemy.EnemyFactory;
import edu.hitsz.factory.enemy.MobEnemyFactory;
import edu.hitsz.factory.prop.BombPropFactory;
import edu.hitsz.factory.prop.FirePropFactory;
import edu.hitsz.factory.prop.HpPropFactory;

import java.io.IOException;

public class NormalGame extends Game{

    //英雄机信息
    private final int HERO_HP = 300;
    private final int HERO_POWER = 20;

    //普通敌机信息
    private final double MOB_RATE = 0.7;
    private final int MOB_SPEEDX = 0;
    private final int MOB_SPEEDY = 3;
    private final int MOB_HP = 40;

    //精英敌机信息
    private final double ELITE_RATE = 0.3;
    private final int ELITE_SPEEDX = 1;
    private final int ELITE_SPEEDY = 2;
    private final int ELITE_HP = 60;

    //道具产生概率
    private final double HP_PROP_RATE = 0.3;
    private final double FIRE_PROP_RATE = 0.2;
    private final double BOMB_PROP_RATE = 0.2;

    //Boss敌机信息
    private final int BOSS_SPEEDX = 3;
    private final int BOSS_SPEEDY = 0;
    private final int BOSS_HP = 200;

    //Boss机产生阈值
    private final int BOUNDARY_VALUE = 300;

    //最多敌机数量
    private final int ENEMY_MAX_NUM = 4;

    //刷新周期
    private final int CYCLE_DURATION = 550;

    public NormalGame() throws IOException {
        super();
        ImageManager.setBackgroundImage("src/images/bg3.jpg");
    }

    @Override
    protected void setHeroAircraft() {
        heroAircraft.setHp(HERO_HP);
        heroAircraft.setMaxHp(HERO_HP);
        heroAircraft.setPower(HERO_POWER);
    }

    @Override
    protected void setEnemyFactories() {
        EnemyFactory enemyFactory;

        //普通敌机
        enemyFactory = new MobEnemyFactory();
        enemyFactory.setSpeedX(MOB_SPEEDX);
        enemyFactory.setSpeedY(MOB_SPEEDY);
        enemyFactory.setHp(MOB_HP);
        enemyFactories.add(enemyFactory);
        enemyRate.add(MOB_RATE);

        // 精英敌机
        enemyFactory = new EliteEnemyFactory();
        enemyFactories.add(enemyFactory);
        enemyFactory.setSpeedX(ELITE_SPEEDX);
        enemyFactory.setSpeedY(ELITE_SPEEDY);
        enemyFactory.setHp(ELITE_HP);
        enemyRate.add(ELITE_RATE);

        // Boss 敌机
        bossFactory = new BossEnemyFactory();
        this.bossFactory.setSpeedX(BOSS_SPEEDX);
        this.bossFactory.setSpeedY(BOSS_SPEEDY);
        this.bossFactory.setHp(BOSS_HP);
    }

    @Override
    protected void setPropsFactories() {
        propFactories.add(new BombPropFactory());
        propFactories.add(new FirePropFactory());
        propFactories.add(new HpPropFactory());
        EliteEnemy.PROPRATE.add(BOMB_PROP_RATE);
        EliteEnemy.PROPRATE.add(FIRE_PROP_RATE);
        EliteEnemy.PROPRATE.add(HP_PROP_RATE);
        BossEnemy.PROPRATE.add(BOMB_PROP_RATE);
        BossEnemy.PROPRATE.add(FIRE_PROP_RATE);
        BossEnemy.PROPRATE.add(HP_PROP_RATE);
    }

    @Override
    protected void setBoundaryValue() {
        this.boundaryValue = BOUNDARY_VALUE;
    }

    @Override
    protected void setEnemyMaxNumber() {
        this.enemyMaxNumber = ENEMY_MAX_NUM;
    }

    @Override
    protected void setCycleDuration() {
        this.cycleDuration = CYCLE_DURATION;
    }

    @Override
    protected void extraProcess() {
        //加快刷新频率
        if(cycleDuration > 500){
            cycleDuration -= 10;
        }
    }
}
