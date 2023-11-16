package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Vampire;
import unsw.loopmania.TrapCard;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class TrapTest {

    /**
     * Create a simple path and make sure an error is thrown if a trap 
     * is placed on a non-path tile
     */
    @Test
    public void testTrapPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                    new Pair<Integer, Integer>(1, 2),
                                                                    new Pair<Integer, Integer>(1, 3), 
                                                                    new Pair<Integer, Integer>(2, 3),
                                                                    new Pair<Integer, Integer>(3, 3), 
                                                                    new Pair<Integer, Integer>(3, 2),
                                                                    new Pair<Integer, Integer>(3, 1), 
                                                                    new Pair<Integer, Integer>(2, 1)));

        // Path tile is ok
        Card trapCard = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 1, 3) == null);

        // Non-Path tile throws error
        Card trapCard1 = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard1);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 4, 2) == null);

        // Hero Castle tile throws error
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setHeroCastle();
        Card trapCard2 = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 1) == null);

    }

    /**
     * Ensure that a trap deals 50 HP to an enemy
     * when an enemy steps on it, and is destroyed afterwards
     * @throws Exception
     */  
    @Test
    public void testTrapDealsDamage() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Deploy two traps
        Card trapCard = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard);
        world.convertCardToBuildingByCoordinates(0, 0, 1, 3);
        Card trapCard1 = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard1);
        world.convertCardToBuildingByCoordinates(0, 0, 3, 3);
        
        // Force spawn vampire in between traps
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(3, world.getOrderedPath()));
        int originalHealth = vampire.getHealthPoints();
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Move vampire onto one of the traps
        world.moveBasicEnemies();

        // Check HP of Vampire has been reduced by 50
        assertEquals(vampire.getHealthPoints(), originalHealth - 50);
        
        // Check trap has been destroyed
        assertEquals(world.getBuildings().size(), 1);

    }

    /**
     * Ensure that a trap kills an enemy with low HP, 
     * and is destroyed afterwards
     * @throws Exception
     */  
    @Test
    public void testTrapKills() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Deploy two traps
        Card trapCard = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard);
        world.convertCardToBuildingByCoordinates(0, 0, 1, 3);
        Card trapCard1 = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        world.loadCard(trapCard1);
        world.convertCardToBuildingByCoordinates(0, 0, 3, 3);
        
        // Force spawn slug in between traps
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(3, world.getOrderedPath()));
        enemies.add(slug);
        world.setEnemies(enemies);

        // Move slug onto one of the traps
        world.moveBasicEnemies();

        // Check slug is dead
        assertEquals(world.getEnemies().size(), 0);
        
        // Check trap has been destroyed
        assertEquals(world.getBuildings().size(), 1);

    }
    
}
