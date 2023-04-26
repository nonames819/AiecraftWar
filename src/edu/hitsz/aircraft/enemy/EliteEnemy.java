package edu.hitsz.aircraft.enemy;

import edu.hitsz.application.Main;
import edu.hitsz.application.game.DifficultGame;
import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 可射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractEnemy {


    public static final List<Double> PROPRATE = new LinkedList<>();

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setDirection(1);
        setPower(10);
        if(Main.game.getClass().equals(DifficultGame.class)){
            setShootNum(2);
            setShootStrategy(new ScatterShootStrategy());
        }else{
            setShootNum(1);
            setShootStrategy(new DirectShootStrategy());
        }
    }

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp,
                      int power, int direction, int shootNum)
    {
        super(locationX, locationY, speedX, speedY, hp);
        setPower(power);
        setShootNum(shootNum);
        setDirection(direction);
        setShootStrategy(new DirectShootStrategy());
    }

    @Override
    public AbstractProp dropProps(List<PropFactory> factory) {
        double rand = Math.random();
        int type = 0;
        while(type<factory.size()){
            rand -= PROPRATE.get(type);
            if(rand <= 0){
                return factory.get(type).createProp(this);
            }  //根据概率掉落道具
            type++;
        }
        //种类遍历结束，不掉落道具
        return null;
    }
}
