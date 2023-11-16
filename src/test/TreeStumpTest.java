package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.TreeStump;
import unsw.loopmania.Zombie;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.Item;
import unsw.loopmania.LoopManiaWorld;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;

public class TreeStumpTest {

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
    public void testOnePercentChanceTreeStump() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // Seed the function that generates random numbers, loop
        // through battles 100 times and make sure that TreeStump is collected
        boolean hasTreeStump = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, orderedPath));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof TreeStump) {
                    hasTreeStump = true;
                }
            }
        }
        assertEquals(hasTreeStump, isTreeStumpInInventory(world));
    }

    @Test
    public void testTreeStumpEquip() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // add TreeStump and test it is in the inventory
        TreeStump treeStump = (TreeStump) world.addUnequippedItem(world.getItemFactory().createStaticEntity(TreeStump.class, world));
        assertEquals(isTreeStumpInInventory(world), true);
        // Equip TreeStump and test it is the equipped weapon
        world.equipShield(treeStump.getX(), treeStump.getY(), 1, 1);
        assertEquals(world.getEquippedShield() instanceof TreeStump, true);
    }
    
    @Test 
    public void testTreeStumpDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        TreeStump treeStump = (TreeStump) world.addUnequippedItem(world.getItemFactory().createStaticEntity(TreeStump.class, world));
        world.equipShield(treeStump.getX(), treeStump.getY(), 1, 1);
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(1, orderedPath));
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Attack character who has TreeStump
        int zombieDamage = zombie.getDamagePoints();
        int characterInitialHealth = character.getHealth();
        assertEquals(zombieDamage, 15);
        zombie.attack(character, world.getEquippedArmour(), world.getEquippedShield(), world.getEquippedHelmet(), world);
        assertEquals(character.getHealth(), characterInitialHealth - zombieDamage*1/3);
        assertEquals(character.getHealth(), characterInitialHealth - treeStump.reduceDamage(zombie.getDamagePoints()));
        assertEquals(character.getHealth(), 245);
    }

    @Test 
    public void testTreeStumpBossDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        TreeStump treeStump = (TreeStump) world.addUnequippedItem(world.getItemFactory().createStaticEntity(TreeStump.class, world));
        world.equipShield(treeStump.getX(), treeStump.getY(), 1, 1);
        List<BasicEnemy> enemies = new ArrayList<>();

        // Test against Elan Muske
        ElanMuske elanMuske = new ElanMuske(new PathPosition(1, orderedPath));
        enemies.add(elanMuske);
        world.setEnemies(enemies);

        // Attack character who has TreeStump
        int elanDamage = elanMuske.getDamagePoints();
        int characterInitialHealth = character.getHealth();
        assertEquals(elanDamage, 50);
        elanMuske.attack(character, world.getEquippedArmour(), world.getEquippedShield(), world.getEquippedHelmet(), world);
        assertEquals(character.getHealth(), characterInitialHealth - elanDamage*1/2);
        assertEquals(character.getHealth(), characterInitialHealth - treeStump.reduceDamage(elanMuske.getDamagePoints())*3/2);
        assertEquals(character.getHealth(), 225);
        elanMuske.destroy();

        // Test against Doggie
        Doggie doggie = new Doggie(new PathPosition(1, orderedPath));
        enemies.add(doggie);
        world.setEnemies(enemies);

        // Attack character who has TreeStump
        characterInitialHealth = character.getHealth();
        int doggieDamage = doggie.getDamagePoints();
        assertEquals(doggieDamage, 30);
        doggie.attack(character, world.getEquippedArmour(), world.getEquippedShield(), world.getEquippedHelmet(), world);
        assertEquals(character.getHealth(), characterInitialHealth - doggieDamage*1/2);
        assertEquals(character.getHealth(), characterInitialHealth - treeStump.reduceDamage(doggie.getDamagePoints())*3/2);
        assertEquals(character.getHealth(), 210);
    }
    
    
    // Helper

    public boolean isTreeStumpInInventory(LoopManiaWorld world) {
        for (Item item : world.getUnequippedInventoryItems()) {
            if (item instanceof TreeStump) {
                return true;
            }
        }
        return false;
    }
}
