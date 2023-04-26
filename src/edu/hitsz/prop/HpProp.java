package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 * 火力道具
 *
 * @author hitsz
 */
public class HpProp extends AbstractProp {
    /**
     * 回复的生命值
     */
    private int hpRecovered;

    public HpProp(int locationX, int locationY, int speedX, int speedY, int hpRecovered) {
        super(locationX, locationY, speedX, speedY);
        this.hpRecovered = hpRecovered;
    }//单一道具的构造函数

    public HpProp(AbstractAircraft aircraft, int hpRecovered) {
        super(aircraft.getLocationX(), aircraft.getLocationY(), aircraft.getSpeedX(), aircraft.getSpeedY());
        this.hpRecovered = hpRecovered;
    }//通过敌机生成道具

    @Override
    public void effect(AbstractAircraft obj) {
        //帮助英雄机恢复生命值
        System.out.println("HpSupply active!");
        obj.increaseHp(hpRecovered);
    }
}
