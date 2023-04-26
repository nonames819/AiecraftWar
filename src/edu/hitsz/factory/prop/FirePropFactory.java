package edu.hitsz.factory.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.FireProp;
/**
 * 火力道具工厂
 *
 * @author hitsz
 */
public class FirePropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft src) {
        return new FireProp(src,5);
    }
}
