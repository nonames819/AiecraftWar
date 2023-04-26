package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 最大生命值
     */
    protected int maxHp;
    /**
     * 生命值
     */
    protected int hp;
    /**
     * 子弹威力
     */
    protected int power;
    /**
     * 子弹射击数量，0 即为不发射
     */
    protected int shootNum;
    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    protected int direction;

    /**
     * 射击策略
     */
    protected ShootStrategy shootStrategy;

    /**
     * 最大子弹数量
     */
    public final int MAX_SHOOT_NUM = 7;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        this(locationX, locationY, speedX, speedY, hp, 0, 0, 0);
    }


    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp,
                            int power, int shootNum, int direction) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.power = power;
        this.direction = direction;
        this.shootNum = shootNum;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int increase){
        hp += increase;
        if (hp > maxHp){
            hp = maxHp;
        }
    }//捡到道具加血的时候用

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getShootNum() {
        return shootNum;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public  List<BaseBullet> shoot(){
        if (shootStrategy == null) {
            return new LinkedList<>();
        } else {
            return shootStrategy.implyShootStrategy(this);
        }
    }

}


