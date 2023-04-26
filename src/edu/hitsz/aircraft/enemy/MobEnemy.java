package edu.hitsz.aircraft.enemy;

import edu.hitsz.strategy.DirectShootStrategy;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemy {


    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setShootNum(0);
        setShootStrategy(new DirectShootStrategy());
    }

}
