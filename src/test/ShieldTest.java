package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Shield;
import unsw.loopmania.LoopManiaWorld;

public class ShieldTest {

    @Test
    public void DamageReductionTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Shield s = new Shield(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(8, s.reduceDamage(12));
        assertEquals(1, s.reduceDamage(2));
        assertEquals(1, s.reduceDamage(1));
    }

    @Test
    public void CriticalBiteReductionTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Shield s = new Shield(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(0.1*0.4, s.criticalBiteReduction(0.1));
    }
}
