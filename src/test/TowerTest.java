package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.TowerCard;
import unsw.loopmania.Zombie;
import unsw.loopmania.Entity;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class TowerTest {

    /**
     * Create a simple path and make sure an error is thrown if a tower 
     * is placed on a path-tile or non-path tile not adjacent to path
     */
    @Test
    public void testTowerNonPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Non-path tile adjacent to path is ok
        Card towerCard = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 0, 1) == null);

        // Path tile throws error
        Card towerCard1 = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard1);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 1) == null);

        // Non-path tile not adjcant to path throws error
        Card towerCard2 = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 0, 0) == null);

    }

    /**
     * Ensure that a tower attacks enemies when a battle commences in
     * its support radius
     * @throws Exception
     */
    @Test
    public void testTowerAttacksEnemies() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(7, 7, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(1, 4),
                                                                      new Pair<Integer, Integer>(1, 5), 
                                                                      new Pair<Integer, Integer>(2, 5),
                                                                      new Pair<Integer, Integer>(3, 5), 
                                                                      new Pair<Integer, Integer>(4, 5),
                                                                      new Pair<Integer, Integer>(5, 5), 
                                                                      new Pair<Integer, Integer>(5, 4),
                                                                      new Pair<Integer, Integer>(5, 3), 
                                                                      new Pair<Integer, Integer>(5, 2),
                                                                      new Pair<Integer, Integer>(5, 1), 
                                                                      new Pair<Integer, Integer>(4, 1),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);

        // Deploy a tower
        Card towerCard = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard);
        world.convertCardToBuildingByCoordinates(0, 0, 3, 2);

        // Move character to within support radius of tower
        for (int i = 0; i < 12; i++) {
            world.runTickMoves();
        }

        // Force spawn zombie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(14, world.getOrderedPath()));
        int originalHealth = zombie.getHealthPoints();
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Setup a battle and run for first tick
        enemies = world.loadBattleEnemies();
        List<Entity> allies = world.loadBattleAllies();
        world.runBattleTurn(enemies, allies);
        // Test if zombie's health = originalHealth - (characterDamage + towerDamage)
        assertEquals(zombie.getHealthPoints(), originalHealth - (20 + 5));

    }

    /**
     * Ensure that a tower attacks enemies when a battle commences in
     * its support radius
     * @throws Exception
     */
    @Test
    public void testTowerDoesNotAttackOutsideRadius() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(7, 7, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(1, 4),
                                                                      new Pair<Integer, Integer>(1, 5), 
                                                                      new Pair<Integer, Integer>(2, 5),
                                                                      new Pair<Integer, Integer>(3, 5), 
                                                                      new Pair<Integer, Integer>(4, 5),
                                                                      new Pair<Integer, Integer>(5, 5), 
                                                                      new Pair<Integer, Integer>(5, 4),
                                                                      new Pair<Integer, Integer>(5, 3), 
                                                                      new Pair<Integer, Integer>(5, 2),
                                                                      new Pair<Integer, Integer>(5, 1), 
                                                                      new Pair<Integer, Integer>(4, 1),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);

        // Deploy a tower
        Card towerCard = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard);
        world.convertCardToBuildingByCoordinates(0, 0, 1, 0);

        // Move character to outside support radius of tower
        for (int i = 0; i < 11; i++) {
            world.runTickMoves();
        }

        // Force spawn zombie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(12, world.getOrderedPath()));
        int originalHealth = zombie.getHealthPoints();
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Setup a battle and run for first tick
        enemies = world.loadBattleEnemies();
        List<Entity> allies = world.loadBattleAllies();
        world.runBattleTurn(enemies, allies);
        // Test if zombie's health = originalHealth - (characterDamage + towerDamage)
        assertEquals(zombie.getHealthPoints(), originalHealth - 20);
    }

    /**
     * Ensure enemies aren't attacked when they are 
     * near a tower with no battle occurring
     * @throws Exception
     */
    @Test
    public void testTowerDoesNotAttackNoBattle() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(7, 7, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(1, 4),
                                                                      new Pair<Integer, Integer>(1, 5), 
                                                                      new Pair<Integer, Integer>(2, 5),
                                                                      new Pair<Integer, Integer>(3, 5), 
                                                                      new Pair<Integer, Integer>(4, 5),
                                                                      new Pair<Integer, Integer>(5, 5), 
                                                                      new Pair<Integer, Integer>(5, 4),
                                                                      new Pair<Integer, Integer>(5, 3), 
                                                                      new Pair<Integer, Integer>(5, 2),
                                                                      new Pair<Integer, Integer>(5, 1), 
                                                                      new Pair<Integer, Integer>(4, 1),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        
        // Create a character
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        
        // Deploy a tower far away
        Card towerCard = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        world.loadCard(towerCard);
        world.convertCardToBuildingByCoordinates(0, 0, 5, 0);

        // Force spawn zombie near tower
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(8, world.getOrderedPath()));
        int originalHealth = zombie.getHealthPoints();
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Move world one tick and ensure zombie's health is same
        world.runTickMoves();
        world.runBattles(true);
        assertEquals(zombie.getHealthPoints(), originalHealth);
    }
    
}
