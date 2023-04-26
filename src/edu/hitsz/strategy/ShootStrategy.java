package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hitsz
 */
public abstract class ShootStrategy {
    public List<BaseBullet> generateBulletList(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();

        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int speedX = aircraft.getSpeedX();
        int speedY = aircraft.getSpeedY();
        int power = aircraft.getPower();
        int shootNum = aircraft.getShootNum();

        if(aircraft.getClass().equals(HeroAircraft.class)){
            for(int i=0; i<shootNum; i++){
                BaseBullet baseBullet = new HeroBullet(locationX,locationY, speedX, speedY, power);
                bullets.add(baseBullet);
            }
        }else{
            for(int i=0; i<shootNum; i++){
                BaseBullet baseBullet = new EnemyBullet(locationX,locationY, speedX, speedY, power);
                bullets.add(baseBullet);
            }
        }

        return bullets;
    }

    /**
     * 设置射击策略
     * @param aircraft 发射子弹的飞行器
     * @return List<BaseBullet>
     */
    public abstract List<BaseBullet> implyShootStrategy(AbstractAircraft aircraft);

}
