package edu.hitsz.aircraft.enemy;

import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Boss敌机
 * 可射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractEnemy {


    public static int power = 20;
    public static final List<Double> PROPRATE = new LinkedList<>();

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setDirection(1);
        setPower(BossEnemy.power);
        setShootNum(3);
        setShootStrategy(new ScatterShootStrategy());
    }

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp,
                      int power, int direction, int shootNum)
    {
        super(locationX, locationY, speedX, speedY, hp);
        setPower(power);
        setShootNum(shootNum);
        setDirection(direction);
    }

    @Override
    public AbstractProp dropProps(List<PropFactory> factory) {
        double rand = Math.random();
        int type = 0;
        while(type<factory.size()){
            rand -= PROPRATE.get(type);
            if(rand <= 0){
                AbstractProp prop = factory.get(type).createProp(this);
                prop.setSpeed(3,3);
                return prop;
            }  //根据概率掉落道具
            type++;
        }
        //种类遍历结束，不掉落道具
        return null;
    }

}
