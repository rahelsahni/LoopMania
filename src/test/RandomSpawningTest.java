package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Character;
import unsw.loopmania.Gold;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.Item;
import unsw.loopmania.PathPosition;

import unsw.loopmania.Armour;
import unsw.loopmania.Helmet;
import unsw.loopmania.Shield;
import unsw.loopmania.Staff;
import unsw.loopmania.Stake;
import unsw.loopmania.Sword;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class RandomSpawningTest {

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
    public void testSpawnGold() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // Spawn Gold
        PathPosition pathPosition = new PathPosition(7, orderedPath);
        Gold gold = (Gold) world.spawnItem(Gold.class, pathPosition.getX() , pathPosition.getY());
        gold.setValue(20);
        world.getItemsOnPath().add(gold);
        // Test spawn was successfull
        assertEquals(world.getItemsOnPath().size(), 1);
    }

    @Test
    public void testSpawnHealthPotion() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // Spawn HealthPotion
        PathPosition pathPosition = new PathPosition(6, orderedPath);
        HealthPotion healthPotion = (HealthPotion) world.spawnItem(HealthPotion.class, pathPosition.getX() , pathPosition.getY());

        world.getItemsOnPath().add(healthPotion);
        // Test spawn was successfull
        assertEquals(world.getItemsOnPath().size(), 1);
    }

    @Test
    public void testOnlySpawnGoldHealthPotions() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        PathPosition pathPosition = new PathPosition(6, orderedPath);

        // Attempt to spawn other items
        List<Item> items = new ArrayList<>();
        Armour armour = (Armour) world.spawnItem(Armour.class, pathPosition.getX() , pathPosition.getY());
        Helmet helmet = (Helmet) world.spawnItem(Helmet.class, pathPosition.getX() , pathPosition.getY());
        Shield shield = (Shield) world.spawnItem(Shield.class, pathPosition.getX() , pathPosition.getY());
        Staff staff = (Staff) world.spawnItem(Staff.class, pathPosition.getX() , pathPosition.getY());
        Stake stake = (Stake) world.spawnItem(Stake.class, pathPosition.getX() , pathPosition.getY());
        Sword sword = (Sword) world.spawnItem(Sword.class, pathPosition.getX() , pathPosition.getY());
        items.add(armour);
        items.add(helmet);
        items.add(shield);
        items.add(staff);
        items.add(stake);
        items.add(sword);
        world.setItemsOnPath(items);

        // Test none succeeded
        assertEquals(armour, null);
        assertEquals(helmet, null);
        assertEquals(shield, null);
        assertEquals(staff, null);
        assertEquals(stake, null);
        assertEquals(sword, null);
    }

}
