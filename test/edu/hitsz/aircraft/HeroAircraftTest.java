package edu.hitsz.aircraft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    static HeroAircraft heroAircraft = null;

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        heroAircraft = null;
    }

    @Test
    void getInstance() {
        System.out.println("**--- Test getInstance method executed ---**");
        if (heroAircraft == null) {
            // 唯一实例未被创建时，应该创建新单例
            assertNotEquals(null, heroAircraft = HeroAircraft.getInstance());
        } else {
            // 唯一实例已创建时，获取单例后单例应该相同
            assertEquals(heroAircraft, HeroAircraft.getInstance());
        }
    }

    final int[] shootNumbers = {0, 1, 3, 10};

    @Test
    void shoot() {
        System.out.println("**--- Test shoot method executed ---**");
        //判断每次发射的子弹数目是否与设定值相等
        for (int shootNumber : shootNumbers) {
            System.out.println("The number set is:" + shootNumber);
            heroAircraft.setShootNum(shootNumber);
            assertEquals(shootNumber, heroAircraft.shoot().size());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, 120})
    void increaseHp(int increase) {
        System.out.println("**--- Test increaseHp method executed ---**");
        System.out.println("The amount of the Hp increased is: " + increase);

        //血量清零，方便后续操作
        heroAircraft.decreaseHp(heroAircraft.getHp());
        int tempHp = heroAircraft.getHp();
        heroAircraft.increaseHp(increase);
        //模拟增加血量过程
        tempHp += increase;
        if (tempHp > heroAircraft.maxHp) {
            tempHp = heroAircraft.maxHp;
        }
        assertTrue(heroAircraft.getHp() <= heroAircraft.maxHp);
        assertEquals(tempHp, heroAircraft.getHp());
    }

}