package edu.hitsz.factory.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.HpProp;

/**
 * 加血道具工厂
 *
 * @author hitsz
 */
public class HpPropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft src) {
        return new HpProp(src,10);
    }
}
