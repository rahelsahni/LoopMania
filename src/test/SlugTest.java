package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;

public class SlugTest {

    List<Pair<Integer, Integer>> orderedPath = Arrays.asList(new Pair<Integer, Integer>(0, 0), new Pair<Integer, Integer>(1, 0), new Pair<Integer, Integer>(2, 0),
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
    public void testRandomSlugCreation() {
        
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // slug is spawned in the first attempt due to seed
        world.possiblySpawnEnemies();
        assertTrue(world.getEnemies().size() == 1);
        // however, does not spawn again on second attempt, or third
        world.possiblySpawnEnemies();
        assertTrue(world.getEnemies().size() == 1);
        world.possiblySpawnEnemies();
        assertTrue(world.getEnemies().size() == 1);
    }

    @Test
    public void testForcedSlugSpawn() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 0);

        // force spawn a slug--------------------------------------------------
        BasicEnemy enemy = new Slug(new PathPosition(1, orderedPath));
        List<BasicEnemy> enemies = new ArrayList<>();
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 1);
    }

    @Test
    public void testMultipleForcedSlugSpawn() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------
 
        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        // --------------------------------------------------------------------

        assertTrue(world.getEnemies().size() == 0);

        // force spawn a slug--------------------------------------------------
        enemies.add(new Slug(new PathPosition(1, orderedPath)));
        // --------------------------------------------------------------------
 
        world.setEnemies(enemies);
        assertTrue(world.getEnemies().size() == 1);

        // force spawn another slug--------------------------------------------
        enemies.add(new Slug(new PathPosition(2, orderedPath)));
        // --------------------------------------------------------------------

        world.setEnemies(enemies);
        assertTrue(world.getEnemies().size() == 2);

        // force spawn last slug-----------------------------------------------
        enemies.add(new Slug(new PathPosition(3, orderedPath)));
        // --------------------------------------------------------------------

        world.setEnemies(enemies);
        assertTrue(world.getEnemies().size() == 3);
    }

    @Test
    public void testSlugMovement() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a slug--------------------------------------------------
        BasicEnemy enemy = new Slug(new PathPosition(0, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // store old position and run enemy movements
        int oldX = enemy.getX();
        int oldY = enemy.getY();

        world.runTickMoves();

        int newX = world.getEnemies().get(0).getX();
        int newY = world.getEnemies().get(0).getY();

        assertTrue(oldX != newX || oldY != newY);
    }

    @Test
    public void testAttack() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a slug next to the character----------------------------
        enemies.add(new Slug(new PathPosition(1, orderedPath)));
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - 5));
    }

    @Test
    public void testKilledSlug() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // force spawn a slug next to the character----------------------------
        enemies.add(new Slug(new PathPosition(1, orderedPath)));
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        assertTrue(world.getEnemies().size() == 1);

        // kill slug
        world.killEnemy(world.getEnemies().get(0));
        assertTrue(world.getEnemies().size() == 0);
    }

    @Test
    public void testBattleRadius() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a slug next to the character----------------------------
        BasicEnemy enemy = new Slug(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - 5));

        // kill slug-----------------------------------------------------------
        world.killEnemy(enemy);
        enemies.remove(enemy);
        assertTrue(world.getEnemies().size() == 0);

        // reset character health
        character.setHealth(maxHealth);
        //---------------------------------------------------------------------

        // force spawn a slug 2 spaces away from the character-----------------
        BasicEnemy new_enemy = new Slug(new PathPosition(2, orderedPath));
        enemies.add(new_enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has not been attacked
        assertTrue(character.getHealth() == maxHealth);
    }

    @Test
    public void testSlugFight() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a slug next to the character----------------------------
        BasicEnemy enemy = new Slug(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));

        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));
        assertTrue(world.getEnemies().size() == 0);
    }

    @Test
    public void testSlugSoldierAttack() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // add an ally
        world.addAlliedSoldier();
        // --------------------------------------------------------------------

        // force spawn a slug next to the character----------------------------
        BasicEnemy enemy = new Slug(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has not been attacked
        assertTrue(character.getHealth() == maxHealth);
        
        assertTrue(world.getAlliedSoldiers().get(0).getHealth() == (100 - enemy.getDamagePoints()));
    }
}
