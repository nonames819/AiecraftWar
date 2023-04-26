package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.observer.BombReminder;

/**
 * 炸弹道具
 *
 * @author hitsz
 */
public class BombProp extends AbstractProp {

    private static final BombReminder reminder = new BombReminder();

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }//单一道具的构造函数

    public BombProp(AbstractAircraft aircraft) {
        super(aircraft.getLocationX(), aircraft.getLocationY(), aircraft.getSpeedX(), aircraft.getSpeedY());
    }//通过敌机生成道具

    @Override
    public void effect(AbstractAircraft obj) {
        Main.score.setScore(Main.score.getScore()+Main.game.getEnemyMaxNumber()*10);
        reminder.notifyObservers();
        Main.MUSIC_MANAGER.runForOnce(MusicManager.BOMB);
        System.out.println("BombSupply active!");
    }

    public static BombReminder getReminder(){
        return reminder;
    }
}
