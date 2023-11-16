package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Stake;
import unsw.loopmania.LoopManiaWorld;

public class StakeTest {

    @Test
    public void AttackModifierTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Stake s = new Stake(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        // TODO rewrite since logic for stake damage on vampire is now in character attack 
        assertEquals(45, s.modifyAttack(15));
        assertEquals(25, s.modifyAttack(15));
    }
}