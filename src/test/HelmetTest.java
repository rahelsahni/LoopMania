package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Helmet;
import unsw.loopmania.LoopManiaWorld;

public class HelmetTest {

    @Test
    public void DamageReductionTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Helmet h = new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(35, h.reduceDamage(50));
    }

    @Test
    public void AttackModifierTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Helmet h = new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(10, h.modifyAttack(15));
        assertEquals(1, h.modifyAttack(6));
        assertEquals(0, h.modifyAttack(5));
        assertEquals(0, h.modifyAttack(1));
        assertEquals(0, h.modifyAttack(0));
    }
}