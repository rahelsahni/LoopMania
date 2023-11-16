package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

import unsw.loopmania.LoopManiaWorld;

import unsw.loopmania.Character;
import unsw.loopmania.Helmet;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Staff;
import unsw.loopmania.Item;
import unsw.loopmania.Sword;

public class SellItemsTest {

    List<Pair<Integer,Integer>> orderedPath = Arrays.asList(
        new Pair<Integer, Integer>(0, 0), new Pair<Integer, Integer>(1, 0), new Pair<Integer, Integer>(2, 0),
        new Pair<Integer, Integer>(2, 1), new Pair<Integer, Integer>(3, 1), new Pair<Integer, Integer>(4, 1),
        new Pair<Integer, Integer>(4, 0), new Pair<Integer, Integer>(5, 0), new Pair<Integer, Integer>(6, 0), 
        new Pair<Integer, Integer>(7, 0), new Pair<Integer, Integer>(7, 1), new Pair<Integer, Integer>(7, 2), 
        new Pair<Integer, Integer>(7, 3), new Pair<Integer, Integer>(7, 4), new Pair<Integer, Integer>(7, 5), 
        new Pair<Integer, Integer>(7, 6), new Pair<Integer, Integer>(7, 7), new Pair<Integer, Integer>(7, 8), 
        new Pair<Integer, Integer>(7, 9), new Pair<Integer, Integer>(6, 9), new Pair<Integer, Integer>(5, 9), 
        new Pair<Integer, Integer>(4, 9), new Pair<Integer, Integer>(4, 8), new Pair<Integer, Integer>(4, 7), 
        new Pair<Integer, Integer>(5, 7), new Pair<Integer, Integer>(5, 6), new Pair<Integer, Integer>(5, 5), 
        new Pair<Integer, Integer>(5, 4), new Pair<Integer, Integer>(5, 3), new Pair<Integer, Integer>(4, 3), 
        new Pair<Integer, Integer>(3, 3), new Pair<Integer, Integer>(2, 3), new Pair<Integer, Integer>(2, 4), 
        new Pair<Integer, Integer>(2, 5), new Pair<Integer, Integer>(2, 6), new Pair<Integer, Integer>(2, 7), 
        new Pair<Integer, Integer>(2, 8), new Pair<Integer, Integer>(2, 9), new Pair<Integer, Integer>(2, 10), 
        new Pair<Integer, Integer>(2, 11), new Pair<Integer, Integer>(3, 11), new Pair<Integer, Integer>(4, 11), 
        new Pair<Integer, Integer>(5, 11), new Pair<Integer, Integer>(6, 11), new Pair<Integer, Integer>(7, 11), 
        new Pair<Integer, Integer>(7, 12), new Pair<Integer, Integer>(7, 13), new Pair<Integer, Integer>(6, 13), 
        new Pair<Integer, Integer>(5, 13), new Pair<Integer, Integer>(4, 13), new Pair<Integer, Integer>(3, 13), 
        new Pair<Integer, Integer>(2, 13), new Pair<Integer, Integer>(1, 13), new Pair<Integer, Integer>(0, 13), 
        new Pair<Integer, Integer>(0, 12), new Pair<Integer, Integer>(0, 11), new Pair<Integer, Integer>(0, 10), 
        new Pair<Integer, Integer>(0, 9), new Pair<Integer, Integer>(0, 8), new Pair<Integer, Integer>(0, 7), 
        new Pair<Integer, Integer>(0, 6), new Pair<Integer, Integer>(0, 5), new Pair<Integer, Integer>(0, 4), 
        new Pair<Integer, Integer>(0, 3), new Pair<Integer, Integer>(0, 2), new Pair<Integer, Integer>(0, 1));

    @Test
    public void testRemoveSoldItem() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        world.setTotalGold(0);
        
        // Add a Sword
        Item sword = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Sword.class, world));
        // Test that the Sword has been added
        assertEquals(world.getUnequippedInventoryItems().size(), 1);
        assertEquals(world.getUnequippedInventoryItems().get(0), sword);

        // Sell the Sword
        world.sellItem(Sword.class);
        // Test that there 
        assertEquals(world.getUnequippedInventoryItems().size(), 0);
        assertEquals(world.getUnequippedInventoryItems(), new ArrayList<>());
    }

    @Test
    public void testGoldAfterSoldItem() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        world.setTotalGold(0);
        // Test that 
        assertEquals(world.getTotalGold(), 0);
        // Add a Sword
        Item sword = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Sword.class, world));

        // Sell the Sword
        int initialGoldValue = world.getTotalGold();
        world.sellItem(Sword.class);
        // Test 
        assertTrue(world.getTotalGold() != initialGoldValue);
        assertEquals(world.getTotalGold(), 100);
    }

    @Test
    public void testFailSellItem() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        world.setTotalGold(0);
        
        // Create a sword but do not add to inventory
        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForItem();
        Sword sword = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        assertEquals(world.getUnequippedInventoryItems().size(), 0);
        // Create a helmet and add to inventory
        Item helmet = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Helmet.class, world));
        assertEquals(world.getUnequippedInventoryItems().size(), 1);
        assertEquals(world.getUnequippedInventoryItems().get(0), helmet);
        
        // attempt to sell the sword but does nothing
        world.sellItem(Sword.class);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);
        assertEquals(world.getUnequippedInventoryItems().get(0), helmet);

    }

    @Test
    public void testSellMultipleItems() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        world.setTotalGold(0);
        
        // Create a helmet, sword and staff
        Item helmet = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Helmet.class, world));
        Item sword = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Sword.class, world));
        Item staff = world.addUnequippedItem(world.getItemFactory().createStaticEntity(Staff.class, world));
        assertEquals(world.getUnequippedInventoryItems().size(), 3);
        assertEquals(world.getUnequippedInventoryItems().get(0), helmet);
        
        // sell the helmet and staff
        world.sellItem(Helmet.class);
        world.sellItem(Staff.class);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);    
        assertEquals(world.getTotalGold(), 300);

        // sell the sword
        world.sellItem(Sword.class);
        assertEquals(world.getUnequippedInventoryItems().size(), 0);    
        assertEquals(world.getTotalGold(), 400);

    }
}
