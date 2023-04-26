package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

import java.util.Random;

/**
 * 精英敌机工厂
 *
 * @author hitsz
 */
public class EliteEnemyFactory extends EnemyFactory{

    @Override
    public AbstractEnemy createEnemy() {
        double rand =  Math.random();
        int flag = 0;
        if(rand < 0.4){
            flag = -1;
        }else {
            flag = 1;
        }

        //产生精英敌机
        return new EliteEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) ( Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                flag*speedX,
                speedY,
                hp
        );
    }
}
