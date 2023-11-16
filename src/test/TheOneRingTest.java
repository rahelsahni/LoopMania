package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.TheOneRing;
import unsw.loopmania.Vampire;
import unsw.loopmania.Item;
import unsw.loopmania.Entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class TheOneRingTest {

    /**
     * Ensure there is a 1% chance of the one ring
     * being collected after a battle
     */
    @Test
    public void testOnePercentChanceOneRing() {
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

        // Seed the function that generates the random numbers, loop
        // through battles 100 times and make sure the one ring is 
        // only ever collected once
        boolean hasRing = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof TheOneRing) { hasRing = true; }
            }
            character.setHealth(250);
        }
        if (!hasRing) { assertTrue(false); }
        
    }

    /**
     * The one ring revives the character after dying
     * if it is in the characters inventory, and is destroyed
     */
    @Test
    public void testRevival() {
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

        // Add The One Ring to inventory
        world.addUnequippedItem(world.getItemFactory().createStaticEntity(TheOneRing.class, world));

        // Kill character
        character.setHealth(1);
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        Vampire vampire2 = new Vampire(new PathPosition(7, world.getOrderedPath()));
        enemies.add(vampire);
        enemies.add(vampire2);
        world.setEnemies(enemies);
        enemies = world.loadBattleEnemies();
        List<Entity> allies = world.loadBattleAllies();
        world.runBattleTurn(enemies, allies);

        // Check health of character is full
        assertEquals(character.getHealth(), 250);

        // Check inventory does not contain the one ring
        assertTrue(!world.oneRingExists());

    }

    /**
     * The character can only hold one The One Ring at a time
     */
    @Test
    public void testOneTheOneRing() {
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

        // Add The One Ring to inventory
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            character.setHealth(250);
            // Clean out inventory
            List<Item> inventory = world.getUnequippedInventoryItems();
            List<Item> removeItems = new ArrayList<>();
            for (Item item : inventory) {
                if (!(item instanceof TheOneRing)) { removeItems.add(item); }
            }
            for (Item item : removeItems) {
                inventory.remove(item);
            }
            world.setUnequippedInventoryItems(inventory);
        }
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        // Add another The One Ring
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            character.setHealth(250);
            // Clean out inventory
            List<Item> inventory = world.getUnequippedInventoryItems();
            List<Item> removeItems = new ArrayList<>();
            for (Item item : inventory) {
                if (!(item instanceof TheOneRing)) { removeItems.add(item); }
            }
            for (Item item : removeItems) {
                inventory.remove(item);
            }
            world.setUnequippedInventoryItems(inventory);
        }
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

    }
    
}
