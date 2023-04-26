package edu.hitsz.aircraft.enemy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.observer.BombReminder;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;


/**
 * 所有敌机的抽象父类
 *
 * @author hitsz
 */
public abstract class AbstractEnemy extends AbstractAircraft implements BombObserver {



    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }


    /**
     * 掉落道具的抽象方法
     * @param factory 道具产生工厂
     */
    public AbstractProp dropProps(List<PropFactory> factory) {
        return null;
    }

    @Override
    public void update() {
        this.vanish();
    }

    @Override
    public boolean invalid() {
        return this.notValid();
    }
}
