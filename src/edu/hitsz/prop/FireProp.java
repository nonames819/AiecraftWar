package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;

import static java.lang.Thread.sleep;

/**
 * 火力道具
 *
 * @author hitsz
 */
public class FireProp extends AbstractProp {
    /**
     * 增强的子弹数目
     */
    private int powerIncreased = 1;

    /**
     * 火力道具生效时间
     */
    private final int EFFECT_TIME = 3000;

    /**
     * 同一时间的增强次数
     */
    private int effectNum;

    public FireProp(int locationX, int locationY, int speedX, int speedY, int powerIncreased) {
        super(locationX, locationY, speedX, speedY);
        this.powerIncreased = powerIncreased;
    }//单一道具的构造函数

    public FireProp(AbstractAircraft aircraft, int powerIncreased) {
        super(aircraft.getLocationX(), aircraft.getLocationY(), aircraft.getSpeedX(), aircraft.getSpeedY());
        this.powerIncreased = powerIncreased;
    }//通过敌机生成道具

    @Override
    public void effect(AbstractAircraft obj) {

        effectNum++;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                boolean isStrengthened = false;
                if (obj.getShootStrategy().getClass() == DirectShootStrategy.class) {
                    obj.setShootStrategy(new ScatterShootStrategy());
                }
                if (obj.getShootNum() < obj.MAX_SHOOT_NUM) {
                    obj.setShootNum(obj.getShootNum() + 1);
                    isStrengthened = true;
                }
                try {
                    sleep(EFFECT_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (isStrengthened)
                {
                    obj.setShootNum(obj.getShootNum() - 1);
                }
            }
        };

        new Thread(r).start();
        effectNum--;
        if(effectNum == 0){
            obj.setShootStrategy(new DirectShootStrategy());
        }

        System.out.println("FireSupply active!");
    }
}
