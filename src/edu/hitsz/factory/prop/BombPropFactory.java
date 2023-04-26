package edu.hitsz.factory.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;
/**
 * 炸弹道具工厂
 *
 * @author hitsz
 */
public class BombPropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft src) {
        return new BombProp(src);
    }
}
