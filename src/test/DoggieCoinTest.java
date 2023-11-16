package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.DoggieCoin;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class DoggieCoinTest {

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
    public void testDoggieCoinReceived() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Spawn doggie
        Doggie doggie = new Doggie(new PathPosition(1, orderedPath));
        enemies.add(doggie);
        world.setEnemies(enemies);
        assertEquals(isDoggieInEnemies(world), true);

        // defeat doggie
        doggie.setHealthPoints(10);
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        assertEquals(isDoggieInEnemies(world), false);
        assertEquals(world.checkItemInUnequippedInventory(DoggieCoin.class), true);

    }

    @Test
    public void testValueFluctuation() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        DoggieCoin doggieCoin = (DoggieCoin) world.getItemFactory().createStaticEntity(DoggieCoin.class, world);
        world.addUnequippedItem(doggieCoin);
        assertEquals(doggieCoin.getValue(), 500);
        world.runTickMoves();

        assertEquals(doggieCoin.getValue(), 550);

        // test after one cycle
        double v = Math.sin(1);
        v = 450*v + 550;
        doggieCoin.fluctuateValue(1);
        assertEquals(doggieCoin.getValue(), (int) v);
    }

    @Test
    public void testValueWhenElan() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        DoggieCoin doggieCoin = (DoggieCoin) world.getItemFactory().createStaticEntity(DoggieCoin.class, world);
        world.addUnequippedItem(doggieCoin);
        assertEquals(doggieCoin.getValue(), 500);
        // spawn elan
        world.setCycles(40);
        character.setXp(10000);
        world.possiblySpawnEnemies();
        int lastIndex = world.getEnemies().size() - 1;
        assertEquals(world.getEnemies().get(lastIndex).getClass(), ElanMuske.class);

        // check doggiecoin value
        assertEquals(doggieCoin.getValue() > 1000, true);
        assertEquals(doggieCoin.getValue(), 13725);
    }

    @Test
    public void testValueAfterElan() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        DoggieCoin doggieCoin = (DoggieCoin) world.getItemFactory().createStaticEntity(DoggieCoin.class, world);
        world.addUnequippedItem(doggieCoin);
        assertEquals(doggieCoin.getValue(), 500);
        // spawn elan
        world.setCycles(40);
        character.setXp(10000);
        world.possiblySpawnEnemies();
        int lastIndex = world.getEnemies().size() - 1;
        BasicEnemy e = world.getEnemies().get(lastIndex);
        assertEquals(e.getClass(), ElanMuske.class);

        // check doggiecoin value
        assertEquals(doggieCoin.getValue() > 1000, true);
        assertEquals(doggieCoin.getValue(), 13725);

        // move elan to character
        e.setPathPosition(new PathPosition(1, orderedPath));

        // kill elan
        world.getEnemies().get(lastIndex).setHealthPoints(1);
        world.runBattles(true);
        assertEquals(world.getElanKilled(), true);

        // test doggieCoin reduced value
        assertEquals(doggieCoin.getValue() <= 100, true);
    }

    // helper

    public boolean isDoggieInEnemies(LoopManiaWorld world) {
        for (BasicEnemy enemy : world.getEnemies()) {
            if (enemy instanceof Doggie) {
                return true;
            }
        }
        return false;
    }

}
