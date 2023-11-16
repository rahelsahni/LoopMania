package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Sword;
import unsw.loopmania.LoopManiaWorld;

public class SwordTest {

    @Test
    public void AttackModifierTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Sword s = new Sword(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(35, s.modifyAttack(15));
    }
}
