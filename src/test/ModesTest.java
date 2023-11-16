package test;

import org.junit.Test;
import org.javatuples.Pair;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Armour;
import unsw.loopmania.Character;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.Shield;
import unsw.loopmania.TheOneRing;
import unsw.loopmania.Helmet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class ModesTest {
    
    /**
     * Ensure that only one health potion can be purchased
     * from the shop at a time when in survival mode
     */
    @Test
    public void testSurvivalMode() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setTotalGold(1000);
        world.setMode("survival");

        // Successfully buy a health potion
        world.checkForShop();
        assertTrue(world.buyItem(HealthPotion.class) instanceof HealthPotion);
        assertEquals(world.getTotalGold(), 900);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        // Unsuccessfully buy another in the same shop iteration
        assertNull(world.buyItem(HealthPotion.class));

        // Successfully buy a shield
        assertTrue(world.buyItem(Shield.class) instanceof Shield);

    }

    /**
     * Ensure that only one protective gear can be purchased
     * from the shop at a time when in berserker mode
     */
    @Test
    public void testBerserkerModeArmour() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setTotalGold(1000);
        world.setMode("berserker");

        // Successfully buy armour
        world.checkForShop();
        assertTrue(world.buyItem(Armour.class) instanceof Armour);
        assertEquals(world.getTotalGold(), 600);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        // Unsuccessfully buy another armour
        assertNull(world.buyItem(Armour.class));

        // Unsuccessfully buy a shield
        assertNull(world.buyItem(Shield.class));

        // Unsuccessfully buy a helmet
        assertNull(world.buyItem(Helmet.class));

    }

    @Test
    public void testBerserkerModeShield() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setTotalGold(1000);
        world.setMode("berserker");

        // Successfully buy a shield
        world.checkForShop();
        assertTrue(world.buyItem(Shield.class) instanceof Shield);
        assertEquals(world.getTotalGold(), 600);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        // Unsuccessfully buy another a shield
        assertNull(world.buyItem(Shield.class));

        // Unsuccessfully buy armour
        assertNull(world.buyItem(Armour.class));

        // Unsuccessfully buy a helmet
        assertNull(world.buyItem(Helmet.class));

    }

    @Test
    public void testBerserkerModeHelmet() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setTotalGold(1000);
        world.setMode("berserker");

        // Successfully buy a helmet
        world.checkForShop();
        assertTrue(world.buyItem(Helmet.class) instanceof Helmet);
        assertEquals(world.getTotalGold(), 700);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        // Unsuccessfully buy another a helmet
        assertNull(world.buyItem(Helmet.class));

        // Unsuccessfully buy armout
        assertNull(world.buyItem(Armour.class));

        // Unsuccessfully buy a shield
        assertNull(world.buyItem(Shield.class));

    }

    /**
     * Check 
     */
    @Test
    public void testConfusingModeRingWithAnduril() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setTotalGold(1000);
        world.setMode("confusing");

        // Add The One Ring to inventory
        world.addUnequippedItem(world.getItemFactory().createStaticEntity(TheOneRing.class, world));

        // Check it has properties of Anduril
        // i.e. check triple damage to bosses

        // Check it has properties of The One Ring
        // i.e. check revival

    }

}
