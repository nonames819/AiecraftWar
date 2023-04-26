package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
/**
 * 敌机工厂接口
 *
 * @author hitsz
 */
public abstract class EnemyFactory {
    int speedX = 0;

    int speedY = 5;

    int hp = 30;

    /**
     * 创建敌机
     * @return 创建的敌机
     */
    public abstract AbstractEnemy createEnemy();

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public int getHp() {
        return hp;
    }
}
