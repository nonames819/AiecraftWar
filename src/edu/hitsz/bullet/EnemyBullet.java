package edu.hitsz.bullet;

import edu.hitsz.observer.BombObserver;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements BombObserver {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
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
