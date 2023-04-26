package edu.hitsz.bullet;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BaseBulletTest {

    private BaseBullet baseBullet;

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");

    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
    }

    @Test
    void forward() {
        //测试子弹运动后是否存在、以及超出边界后是否会消失
        System.out.println("**--- Test forward method executed ---**");
        System.out.println("The maximum Y value is: "+Main.WINDOW_HEIGHT);
        //创建子弹对象
        int locationX = Main.WINDOW_WIDTH/2;
        int locationY = Main.WINDOW_HEIGHT/2;
        int speedX = 0;
        int speedY = 50;
        baseBullet = new BaseBullet(locationX,locationY,speedX,speedY,10);
        while(locationY<=Main.WINDOW_HEIGHT){
            System.out.println("Location Y is : "+locationY);
            assertEquals(locationY,baseBullet.getLocationY());
            assertTrue(!(baseBullet.notValid()));
            baseBullet.forward();
            locationY += speedY;
        }
        System.out.println("Location Y is : "+locationY);
        assertTrue(baseBullet.notValid());
    }

    @Test
    void crash() {
        //判断子弹是否与英雄机相撞
        System.out.println("**--- Test crash method executed ---**");
        //获取英雄机唯一实例
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        BaseBullet bullet = new EnemyBullet(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 10, 30);
        assertTrue(bullet.crash(heroAircraft));
    }
}