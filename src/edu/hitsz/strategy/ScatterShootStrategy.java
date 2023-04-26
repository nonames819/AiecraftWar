package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author hitsz
 */
public class ScatterShootStrategy extends ShootStrategy{

    @Override
    public List<BaseBullet> implyShootStrategy(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = generateBulletList(aircraft);

        int shootNum = aircraft.getShootNum();
        int locationX = aircraft.getLocationX();
        int locationY = aircraft.getLocationY();
        int speedY = aircraft.getSpeedY();
        int direction = aircraft.getDirection();

        // 设置子弹位置
        int spY = speedY + direction * 5;

        for(int i = 0; i < shootNum; i++) {
            int spX = i * 2 - shootNum + 1;
            //bullets.get(i).setLocation(locationX + (i * 2 - shootNum + 1) * 10,locationY + direction * 2);
            bullets.get(i).setSpeed(spX, spY);
        }

        return bullets;
    }
}
