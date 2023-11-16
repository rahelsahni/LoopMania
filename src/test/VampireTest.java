package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import unsw.loopmania.BasicEnemy;
import unsw.loopmania.CampfireCard;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.Entity;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Vampire;

public class VampireTest {
    
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
    public void testForcedVampireSpawn() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 0);

        // force spawn a vampire-----------------------------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 1);
    }

    @Test
    public void testMultipleForcedVampireSpawn() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 0);

        // force spawn a vampire-----------------------------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------
 
        assertTrue(world.getEnemies().size() == 1);

        // force spawn another vampire-----------------------------------------
        BasicEnemy enemy2 = new Vampire(new PathPosition(2, orderedPath));
        enemies.add(enemy2);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        assertTrue(world.getEnemies().size() == 2);

        // force spawn last vampire--------------------------------------------
        BasicEnemy enemy3 = new Vampire(new PathPosition(3, orderedPath));
        enemies.add(enemy3);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        assertTrue(world.getEnemies().size() == 3);
    }

    @Test
    public void testVampireMovement() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a vampire-----------------------------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // store old position and run enemy movements
        int oldX = world.getEnemies().get(0).getX();
        int oldY = world.getEnemies().get(0).getY();

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

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() < maxHealth);
    }

    @Test
    public void testKilledVampire() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        assertTrue(world.getEnemies().size() == 1);

        // kill vampire
        world.killEnemy(enemy);
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

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() < maxHealth);

        // kill vampire--------------------------------------------------------
        world.killEnemy(enemy);
        assertTrue(world.getEnemies().size() == 0);

        // reset character health
        character.setHealth(maxHealth);
        //---------------------------------------------------------------------

        // force spawn a vampire 2 spaces away from the character--------------
        BasicEnemy new_enemy = new Vampire(new PathPosition(2, orderedPath));
        enemies.add(new_enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has been attacked
        assertTrue(character.getHealth() < maxHealth);

        // kill vampire--------------------------------------------------------
        world.killEnemy(new_enemy);
        assertTrue(world.getEnemies().size() == 0);

        // reset character health
        character.setHealth(maxHealth);
        //---------------------------------------------------------------------

        // force spawn a vampire 3 spaces away from the character--------------
        BasicEnemy newer_enemy = new Vampire(new PathPosition(3, orderedPath));
        enemies.add(newer_enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has not been attacked
        assertTrue(character.getHealth() == maxHealth);
    }

    @Test
    public void testSupportRadius() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // force spawn a vampire 3 spaces away from the character--------------
        BasicEnemy enemy2 = new Vampire(new PathPosition(3, orderedPath));
        enemies.add(enemy2);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has lost more than at least 2 vampire attacks
        assertTrue(character.getHealth() < (maxHealth - (2 * enemy.getDamagePoints())));
    }

    @Test
    public void testVampireFight() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() < (maxHealth - enemy.getDamagePoints()));

        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() <= (maxHealth - (2 * enemy.getDamagePoints())));

        // --------------------------------------------------------------------

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() <= (maxHealth - (2 * enemy.getDamagePoints())));
        assertTrue(world.getEnemies().size() == 0);
    }

    @Test
    public void testVampireSoldierAttack() {

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

        // force spawn a vampire next to the character----------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(enemy);
        world.setEnemies(enemies);

        // run one tick of the game world
        world.runBattles(true);
        //---------------------------------------------------------------------

        // assert character has not been attacked
        assertTrue(character.getHealth() == maxHealth);
        
        assertTrue(world.getAlliedSoldiers().get(0).getHealth() <= (100 - enemy.getDamagePoints()));
    }

    @Test
    public void testCriticalHit() {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(0, orderedPath));
        int maxHealth = character.getHealth();
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a vampire next to the character-------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(1, orderedPath));
        enemy.setHealthPoints(9999);
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // run one tick of the game world
        List<BasicEnemy> battleEnemies = world.loadBattleEnemies();
        List<Entity> battleAllies = world.loadBattleAllies();
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));
        character.setHealth(maxHealth);

        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));
        character.setHealth(maxHealth);

        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));
        character.setHealth(maxHealth);

        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        // assert that there was a crit hit on this turn, causing extra damage
        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints() - 5));
        character.setHealth(maxHealth);

        // continue checking for 3 more turns to make sure  crit hit lingers
        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        // assert that there was a crit hit on this turn, causing extra damage
        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints() - 8));
        character.setHealth(maxHealth);

        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        // assert that there was a crit hit on this turn, causing extra damage
        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints() - 8));
        character.setHealth(maxHealth);

        // run one tick of the game world
        world.runBattleTurn(battleEnemies, battleAllies);
        //---------------------------------------------------------------------

        // assert that there was no crit hit, since crit hit turns are over
        assertTrue(character.getHealth() == (maxHealth - enemy.getDamagePoints()));
    }

    @Test
    public void testCampfireBehaviour() throws Exception {

        // create world
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        List<BasicEnemy> enemies = new ArrayList<>();
        // --------------------------------------------------------------------

        // create character
        Character character = new Character(new PathPosition(13, orderedPath));
        world.setCharacter(character);
        // --------------------------------------------------------------------

        // force spawn a vampire ----------------------------------------------
        BasicEnemy enemy = new Vampire(new PathPosition(3, orderedPath));
        enemy.setHealthPoints(9999);
        enemies.add(enemy);
        world.setEnemies(enemies);
        // --------------------------------------------------------------------

        // Deploy a campfire
        Card campfireCard = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        world.loadCard(campfireCard);
        world.convertCardToBuildingByCoordinates(0, 0, 1, 1);

        world.runTickMoves();

        // assert the vampire took the shortest path away from the fire
        assertTrue(enemy.getPathPosition().getCurrentPositionInPath() == 4);
    }
}
