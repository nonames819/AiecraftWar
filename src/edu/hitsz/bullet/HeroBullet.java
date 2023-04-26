package edu.hitsz.bullet;

/**
 * @Author hitsz
 */
public class HeroBullet extends BaseBullet {

    private int power = 20;

    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

}
