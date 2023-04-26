package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 普通敌机工厂
 *
 * @author hitsz
 */
public class MobEnemyFactory extends EnemyFactory{

    @Override
    public AbstractEnemy createEnemy() {
        //产生普通敌机
        return new MobEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) ( Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                speedX,
                speedY,
                hp
        );
    }
}
