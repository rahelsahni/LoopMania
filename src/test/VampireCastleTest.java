package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Vampire;
import unsw.loopmania.VampireCastleCard;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class VampireCastleTest {

    /**
     * Create a simple path and make sure an error is thrown if a vampire 
     * castle is placed on a path-tile or non-path tile not adjacent to path
     */
    @Test
    public void testVampireCastleNonPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Non-path tile adjacent to path is ok
        Card vampireCastleCard = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 0, 1) == null);

        // Path tile throws error
        Card vampireCastleCard1 = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard1);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 1) == null);

        // Non-path tile not adjcant to path throws error
        Card vampireCastleCard2 = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 0, 0) == null);

    }

    /**
     * Ensure that a vampire spawns every 5 cycles that the character completes 
     * @throws Exception
     */
    @Test
    public void testVampireCastleSpawnsOneVampire() throws Exception {
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

        // Deploy a vampire castle
        Card vampireCastleCard = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Check there is no vampire initially
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Vampire.class, e.getClass());
        }

        // Move character around path 3 times (path is 8 tiles long)
        for (int i = 0; i < 24; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there is still no vampire
        enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Vampire.class, e.getClass());
        }

        // Move character around path 2 more times
        for (int i = 0; i < 17; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there is exactly one vampire
        enemies = world.getEnemies();
        boolean found = false;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Vampire.class)) {
                if (found) {
                    assertTrue(false);
                } else {
                    found = true;
                }
            }
        }
        if (!found) { assertTrue(false); }

    }

    /**
     * Ensure that a vampire spawns from every vampire castle for
     * every 5 cycles that the character completes 
     * @throws Exception
     */
    @Test
    public void testVampireCastleSpawnsManyVampires() throws Exception {
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

        // Deploy a vampire castle
        Card vampireCastleCard = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Check there is no vampire initially
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Vampire.class, e.getClass());
        }

        // Move character around path 3 times (path is 8 tiles long)
        for (int i = 0; i < 24; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there is still no vampire
        enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Vampire.class, e.getClass());
        }
        
        // Deploy another vampire castle
        Card vampireCastleCard1 = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard1);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 0);

        // Check there is still no vampire
        enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Vampire.class, e.getClass());
        }

        // Move character around path 2 more times
        for (int i = 0; i < 17; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there are exactly two vampires
        enemies = world.getEnemies();
        int count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Vampire.class)) {
                count++;
            }
        }
        if (count != 2) { assertTrue(false); }

        // Deploy another vampire castle
        Card vampireCastleCard2 = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard2);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 4);

        // Cycle another 5 times
        for (int i = 0; i < 41; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there are exactly five vampires
        enemies = world.getEnemies();
        count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Vampire.class)) {
                count++;
            }
        }
        if (count != 5) { assertTrue(false); }

    }

    /**
     * Test that vampires spawn adjacent to vampire castles
     * @throws Exception
     */
    @Test
    public void testVampireSpawnsAdjacentToCastle() throws Exception {
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

        // Deploy a vampire castle
        Card vampireCastleCard = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        world.loadCard(vampireCastleCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Move character around path 5 times (path is 8 tiles long)
        for (int i = 0; i < 41; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check vampire is adjacent to vampire castle
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Vampire.class)) {
                Vampire vampire = (Vampire) e;
                assertEquals(vampire.getX(), 3);
                assertEquals(vampire.getY(), 2);
                break;
            }
        }

    }
    
}
