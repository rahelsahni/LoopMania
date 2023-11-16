package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Staff;
import unsw.loopmania.LoopManiaWorld;

public class StaffTest {

    @Test
    public void AttackModifierTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Staff s = new Staff(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(18, s.modifyAttack(15));
    }

    @Test
    public void TranceTest(){
        // This is a stub as don't have the implementation for combat and enemies yet.
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Staff s = new Staff(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        s.setTranceChance(1);
        // Create a vampire here, assign it to the variable "v"
        //assertTrue(s.attemptTrance(v));
    }
}