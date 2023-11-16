package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Zombie;
import unsw.loopmania.ZombiePitCard;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class ZombiePitTest {

    /**
     * Create a simple path and make sure an error is thrown if a zombie 
     * pit is placed on a path-tile or non-path tile not adjacent to path
     */
    @Test
    public void testZombiePitNonPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Non-path tile adjacent to path is ok
        Card zombiePitCard = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 0, 1) == null);

        // Path tile throws error
        Card zombiePitCard1 = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard1);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 1) == null);

        // Non-path tile not adjcant to path throws error
        Card zombiePitCard2 = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 0, 0) == null);

    }

    /**
     * Ensure that a zombie spawns every cycle that the character completes 
     * @throws Exception
     */
    @Test
    public void testZombiePitSpawnsOneZombie() throws Exception {
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

        // Deploy a zombie pit
        Card zombiePitCard = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Check there is no zombie initially
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Zombie.class, e.getClass());
        }

        // Move character halfway around path (path is 8 tiles long)
        for (int i = 0; i < 4; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there is still no zombie
        enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Zombie.class, e.getClass());
        }

        // Move character all the way around path
        for (int i = 0; i < 6; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there is exactly one zombie
        enemies = world.getEnemies();
        boolean found = false;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Zombie.class)) {
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
     * Ensure that a zombie spawns from every zombie pit for 
     * every cycle that the character completes 
     * @throws Exception
     */
    @Test
    public void testZombiePitSpawnsManyZombies() throws Exception {
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

        // Deploy a zombie pit
        Card zombiePitCard = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Check there is no zombie initially
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            assertNotEquals(Zombie.class, e.getClass());
        }

        // Move character around path once (path is 8 tiles long)
        for (int i = 0; i < 9; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();
        
        // Deploy another zombie pit
        Card zombiePitCard1 = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard1);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 0);

        // Check there is one zombie
        enemies = world.getEnemies();
        boolean found = false;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Zombie.class)) {
                if (found) {
                    assertTrue(false);
                } else {
                    found = true;
                }
            }
        }
        if (!found) { assertTrue(false); }

        // Move character around path once
        for (int i = 0; i < 8; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there are exactly 3 zombies
        enemies = world.getEnemies();
        int count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Zombie.class)) {
                count++;
            }
        }
        if (count != 3) { assertTrue(false); }

        // Deploy another zombie pit
        Card zombiePitCard2 = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard2);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 4);

        // Cycle one more time
        for (int i = 0; i < 8; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check there are exactly 6 zombies
        enemies = world.getEnemies();
        count = 0;
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Zombie.class)) {
                count++;
            }
        }
        if (count != 6) { assertTrue(false); }

    }

    /**
     * Test that zombies spawn adjacent to zombie pits
     * @throws Exception
     */
    @Test
    public void testZombieSpawnsAdjacentToPit() throws Exception {
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

        // Deploy a zombie pit
        Card zombiePitCard = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        world.loadCard(zombiePitCard);
        world.convertCardToBuildingByCoordinates(0, 0, 4, 2);

        // Move character around path once (path is 8 tiles long)
        for (int i = 0; i < 8; i++) {
            world.runTickMoves();
        }
        world.possiblySpawnEnemies();

        // Check zombie is adjacent to zombie pit
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            if (e.getClass().equals(Zombie.class)) {
                Zombie zombie = (Zombie) e;
                assertEquals(zombie.getX(), 3);
                assertEquals(zombie.getY(), 2);
                break;
            }
        }

    }
    
}
