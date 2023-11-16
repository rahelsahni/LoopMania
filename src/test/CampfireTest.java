package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.CampfireCard;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Vampire;
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

public class CampfireTest {

    /**
     * Create a simple path and make sure an error is thrown if a campfire 
     * is placed on a path tile
     */
    @Test
    public void testCampfireNonPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                    new Pair<Integer, Integer>(1, 2),
                                                                    new Pair<Integer, Integer>(1, 3), 
                                                                    new Pair<Integer, Integer>(2, 3),
                                                                    new Pair<Integer, Integer>(3, 3), 
                                                                    new Pair<Integer, Integer>(3, 2),
                                                                    new Pair<Integer, Integer>(3, 1), 
                                                                    new Pair<Integer, Integer>(2, 1)));

        // Any non-path tile is ok
        Card campfireCard = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 0, 2)  == null);

        Card campfireCard1 = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard1);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 4, 4) == null);

        // Path tile throws error
        Card campfireCard2 = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 3) == null);

    }

    /**
     * Ensure that a campfire multiplies the character's attack value
     * by 2 when there is a battle within the campfire's support radius
     * @throws Exception
     */  
    @Test
    public void testCampfireDoublesDamage() throws Exception {
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
        
        // Deploy a campfire
        Card campfireCard = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard);
        world.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        // Move character to within support radius of campfire
        for (int i = 0; i < 5; i++) {
            world.runTickMoves();
        }
        
        // Force spawn vampire in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(6, world.getOrderedPath()));
        int originalHealth = vampire.getHealthPoints();
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Setup a battle and run for first tick
        enemies = world.loadBattleEnemies();
        List<Entity> allies = world.loadBattleAllies();
        world.runBattleTurn(enemies, allies);
        // Test if vampire's health = originalHealth - 2*characterDamage
        assertEquals(vampire.getHealthPoints(), originalHealth - 2*20);

    }

    /**
     * Ensure that a campfire does not multiply the character's attack 
     * value when a battle is outside the support radius
     * @throws Exception
     */  
    @Test
    public void testCampfireOutsideSupport() throws Exception {
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
        
        // Deploy a campfire
        Card campfireCard = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard);
        world.convertCardToBuildingByCoordinates(0, 0, 0, 0);

        // Move character to outside support radius of campfire
        for (int i = 0; i < 7; i++) {
            world.runTickMoves();
        }
        
        // Force spawn zombie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(8, world.getOrderedPath()));
        int originalHealth = zombie.getHealthPoints();
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Setup a battle and run for first tick
        enemies = world.loadBattleEnemies();
        List<Entity> allies = world.loadBattleAllies();
        world.runBattleTurn(enemies, allies);
        // Test if zombie's health = originalHealth - 2*characterDamage
        assertEquals(zombie.getHealthPoints(), originalHealth - 20);

    }
    
}
