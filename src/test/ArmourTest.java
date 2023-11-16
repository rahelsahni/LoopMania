package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Armour;
import unsw.loopmania.LoopManiaWorld;

public class ArmourTest {

    @Test
    public void DamageReductionTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Armour a = new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(5, a.reduceDamage(10));
    }
}