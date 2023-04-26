package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HpPropTest {

    private HpProp hpProp;
    static HeroAircraft heroAircraft;
    private int hpRecovered = 10;

    @BeforeAll
    static void getHeroAircraft() {
        heroAircraft = HeroAircraft.getInstance();
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        hpProp = new HpProp(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 10, hpRecovered);
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
    }

    @Test
    void effect() {
        //判断加血道具是否生效
        System.out.println("**--- Test effect method executed ---**");
        heroAircraft.decreaseHp(heroAircraft.getHp());
        //先对英雄机血量清零，并设置tempHp用于比较
        int tempHp = 0;
        System.out.println("The initial Hp of the hero aircraft is: " + heroAircraft.getHp());
        while (heroAircraft.getHp() < 100) {
            hpProp.effect(heroAircraft);
            tempHp += hpRecovered;
            if (tempHp > 100) {
                tempHp = 100;
            }
            assertEquals(tempHp, heroAircraft.getHp());
            System.out.println("The temporary Hp of the hero aircraft is: " + heroAircraft.getHp());
        }
    }

    @Test
    void crash() {
        //判断碰撞是否生效
        System.out.println("**--- Test crash method executed ---**");
        assertTrue(hpProp.crash(heroAircraft));
    }
}