package edu.hitsz.factory.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.AbstractProp;

/**
 * 道具工厂接口
 *
 * @author hitsz
 */
public interface PropFactory {
    /**
     * 创建道具
     * @param src 产生道具的敌机
     * @return 创建出的道具
     */
    public abstract AbstractProp createProp(AbstractAircraft src);
}
