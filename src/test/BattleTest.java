package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.AlliedSoldier;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Building;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.TowerBuilding;
import unsw.loopmania.Vampire;
import unsw.loopmania.Zombie;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class BattleTest {

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
    public void testSlugBattleRadius() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Slug not in range
        BasicEnemy slug = new Slug(new PathPosition(10, orderedPath));
        enemies.add(slug);
        world.setEnemies(enemies);
        // Test that no battles are possible given Slug is out of range
        assertEquals(world.existsBattles(), false);

        // Advance character and move Slug towards Character
        moveCharacterDownXTimes(character, 1);
        moveEnemyUpXTimes(slug, 1);
        // Test that still no battles are possible given distance is 2 and slug.battleRadius = 1
        assertEquals(world.existsBattles(), false);


        // Advance character in to radius of Slug
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists
        assertTrue(world.existsBattles());
    }

    @Test
    public void testVampireBattleRadius() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Vampires not in range
        BasicEnemy vampire1 = new Vampire(new PathPosition(10, orderedPath));
        BasicEnemy vampire2 = new Vampire(new PathPosition(13, orderedPath));
        enemies.add(vampire1);
        enemies.add(vampire2);
        world.setEnemies(enemies);
        // Test that no battles are possible given Vampires are out of range
        assertEquals(world.existsBattles(), false);

        // Advance Character to 7
        moveCharacterDownXTimes(character, 1);
        // Test that still no battles are possible
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists despite not being in a straight path
        assertEquals(world.existsBattles(), true);

        // Advance Character to 9
        moveCharacterDownXTimes(character, 1);
        // Test that battle still exists since distance is 1
        assertEquals(world.existsBattles(), true);

        // Destroy Vampire1 so no longer there and vampire2 is distance of 4
        world.getEnemies().remove(vampire1);
        // Test that no battles are possible
        assertEquals(world.existsBattles(), false);

        // Advance Character to 10 and move Vampire to12
        moveCharacterDownXTimes(character, 1);
        moveEnemyUpXTimes(vampire2, 1);
        // Test that a battle exists given straight distance of 2
        assertEquals(world.existsBattles(), true); 
    }

    @Test
    public void testZombieBattleRadius() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Zombies not in range
        BasicEnemy zombie1 = new Zombie(new PathPosition(10, orderedPath));
        BasicEnemy zombie2 = new Zombie(new PathPosition(13, orderedPath));
        enemies.add(zombie1);
        enemies.add(zombie2);
        world.setEnemies(enemies);
        // Test that no battles are possible given Zombies are out of range
        assertEquals(world.existsBattles(), false);

        // Advance Character to 7
        moveCharacterDownXTimes(character, 1);
        // Test that still no battles are possible
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists despite not being in a straight path
        assertEquals(world.existsBattles(), true);

        // Advance Character to 9
        moveCharacterDownXTimes(character, 1);
        // Test that battle still exists since distance is 1
        assertEquals(world.existsBattles(), true);

        // Destroy Zombie1 so no longer there and Zombie2 is distance of 4
        world.getEnemies().remove(zombie1);
        // Test that no battles are possible
        assertEquals(world.existsBattles(), false);

        // Advance Character to 10 and move Zombie2 to12
        moveCharacterDownXTimes(character, 1);
        moveEnemyUpXTimes(zombie2, 1);
        // Test that a battle exists given straight distance of 2
        assertEquals(world.existsBattles(), true); 
    }

    @Test
    public void testVampireSupportRadius() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Zombie and Vampire not in range
        BasicEnemy zombie = new Zombie(new PathPosition(10, orderedPath));
        BasicEnemy vampire = new Vampire(new PathPosition(12, orderedPath));
        enemies.add(zombie);
        enemies.add(vampire);
        world.setEnemies(enemies);
        // Test that no battles are possible given Enemies are out of range
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 2);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);
        // Test that the battle only involves the zombie (vampire not in support range)
        assertEquals(world.loadBattleEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), zombie);

        // Advance Character to 9
        moveCharacterDownXTimes(character, 1);
        // Test that battle still exists since distance is 1
        assertEquals(world.existsBattles(), true);
        // Test that Vampire has joined the battle automatically
        assertEquals(world.loadBattleEnemies().size(), 2);
        assertEquals(world.loadBattleEnemies().get(1), vampire);
        
        // Run the battle that should happen between the character and zombie supported by vampire
        world.runBattles(true);
        // Test that zombie has been killed and vampire still exists
        assertEquals(world.getEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), vampire);
        // Test that no battles are possible
        assertEquals(world.existsBattles(), false);
        // Test that the player took 1x zombie damage and 1x vampire damage
        assertEquals(character.getHealth(), 250 - zombie.getDamagePoints() - vampire.getDamagePoints());

        // Advance Character to 10
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists given straight distance of 2
        assertEquals(world.existsBattles(), true); 
        world.runBattles(true);
    }

    @Test
    public void testTowerSupportRadius() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        List<Building> buildings = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Slugs not in range
        BasicEnemy slug1 = new Slug(new PathPosition(8, orderedPath));
        BasicEnemy slug2 = new Slug(new PathPosition(9, orderedPath));
        enemies.add(slug1);
        enemies.add(slug2);
        world.setEnemies(enemies);
        // Add TowerBuilding at (6,3)
        TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(6), new SimpleIntegerProperty(3));
        buildings.add(tower);
        world.setBuildings(buildings);
        // Test no battles
        assertEquals(world.existsBattles(), false);

        // Advance Character to 7 and Slug1 to 7
        moveCharacterDownXTimes(character, 1);
        moveEnemyUpXTimes(slug1, 1);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);
        // Test that the battle only involves Slug1 and not Tower or Slug2
        assertEquals(world.loadBattleEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), slug1);
        assertEquals(world.loadBattleAllies().size(), 1);
        assertEquals(world.loadBattleAllies().get(0), character);
        // Run the battle
        world.runBattles(true);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);
        // Test that the battle only involves Slug1 and not Tower or Slug2
        assertEquals(world.loadBattleEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), slug2);
        assertEquals(world.loadBattleAllies().size(), 2);
        assertEquals(world.loadBattleAllies().get(0), character);
        assertEquals(world.loadBattleAllies().get(1), tower);

        // Run one turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        // Test that Slug2 has been damaged by the character and the tower once each
        BasicEnemy dummySlug = new Slug(new PathPosition(9, orderedPath));
        character.attackEnemy(dummySlug, world);
        tower.attackEnemy(dummySlug, world);
        assertEquals(slug2.getHealthPoints(), dummySlug.getHealthPoints());
    }
    
    @Test
    public void testSoldierJoinsBattle() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        List<AlliedSoldier> alliedSoldiers = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Slugs not in range
        BasicEnemy slug1 = new Slug(new PathPosition(8, orderedPath));
        BasicEnemy slug2 = new Slug(new PathPosition(10, orderedPath));
        enemies.add(slug1);
        enemies.add(slug2);
        world.setEnemies(enemies);
        // Add AlliedSoldier
        AlliedSoldier soldier1 = new AlliedSoldier(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        alliedSoldiers.add(soldier1);
        world.setAlliedSoldiers(alliedSoldiers);
        // Test no battles
        assertEquals(world.existsBattles(), false);

        // Advance Character to 7
        moveCharacterDownXTimes(character, 1);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);
        // Test that the battle involves Slug1, and both Character and Soldier
        assertEquals(world.loadBattleEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), slug1);
        assertEquals(world.loadBattleAllies().size(), 2);
        assertEquals(world.loadBattleAllies().get(0), character);
        assertEquals(world.loadBattleAllies().get(1), soldier1);
        // Run the battle
        world.runBattles(true);

        // Add second AlliedSoldier
        AlliedSoldier soldier2 = new AlliedSoldier(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        alliedSoldiers.add(soldier2);
        world.setAlliedSoldiers(alliedSoldiers);
        // Test that world has two soldiers
        assertEquals(world.getAlliedSoldiers().size(), 2);
        // Advance Character to 9
        moveCharacterDownXTimes(character, 2);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);
        // Test that the battle only involves Slug2, and all of Character, Soldier 1, and Soldier 2
        assertEquals(world.loadBattleEnemies().size(), 1);
        assertEquals(world.loadBattleEnemies().get(0), slug2);
        assertEquals(world.loadBattleAllies().size(), 3);
        assertEquals(world.loadBattleAllies().get(0), character);
        assertEquals(world.loadBattleAllies().get(1), soldier1);
        assertEquals(world.loadBattleAllies().get(2), soldier2);

        // Run one turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        // Test that Slug2 has been damaged by the character and each soldier once each
        BasicEnemy dummySlug = new Slug(new PathPosition(8, orderedPath));
        character.attackEnemy(dummySlug, world);
        soldier1.attackEnemy(dummySlug, world);
        soldier2.attackEnemy(dummySlug, world);
        assertEquals(slug2.getHealthPoints(), dummySlug.getHealthPoints());
    }

    @Test
    public void testCharacterAttackDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        /// Add Slugs not in range
        BasicEnemy slug1 = new Slug(new PathPosition(8, orderedPath));
        BasicEnemy slug2 = new Slug(new PathPosition(9, orderedPath));
        enemies.add(slug1);
        enemies.add(slug2);
        world.setEnemies(enemies);
        // Test that no battles are possible given Slugs are out of range
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 2);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);

        // Run first turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        BasicEnemy dummySlug1 = new Slug(new PathPosition(8, orderedPath));
        BasicEnemy dummySlug2 = new Slug(new PathPosition(8, orderedPath));
        character.attackEnemy(dummySlug1, world);
        // Test that Slug1 has been damaged by the character by 20HP
        assertEquals(slug1.getHealthPoints(), dummySlug1.getHealthPoints());
        assertEquals(slug1.getHealthPoints(), dummySlug2.getHealthPoints() - 20);
        // Test that Slug2 has not been damaged yet
        assertEquals(slug2.getHealthPoints(), dummySlug2.getHealthPoints());

        // Run second turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        character.attackEnemy(dummySlug1, world);
        // Test that Slug1 has been destroyed 
        assertEquals(world.getEnemies().size(), 1);
        assertEquals(world.getEnemies().get(0), slug2);
        // Test that dummySlug1's health is -10HP
        assertEquals(dummySlug1.getHealthPoints(), -10);
        // Test that Slug2 has still not been damaged yet
        assertEquals(slug2.getHealthPoints(), dummySlug2.getHealthPoints());

        // Run third turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        character.attackEnemy(dummySlug2, world);
        // Test that dummySlug2's health is 10HP
        assertEquals(dummySlug2.getHealthPoints(), 10);
        // Test that Slug2 has been damaged by the character by 20HP
        assertEquals(slug2.getHealthPoints(), dummySlug2.getHealthPoints());
    }

    @Test
    public void testTowerAttackDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        List<Building> buildings = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Slug not in range
        BasicEnemy slug = new Slug(new PathPosition(9, orderedPath));
        enemies.add(slug);
        world.setEnemies(enemies);
        // Add TowerBuilding at (6,3)
        TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(6), new SimpleIntegerProperty(3));
        buildings.add(tower);
        world.setBuildings(buildings);
        // Test no battles
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 2);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);

        // Run one turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        BasicEnemy dummySlug = new Slug(new PathPosition(7, orderedPath));
        character.attackEnemy(dummySlug, world);
        tower.attackEnemy(dummySlug, world);
        // Test that dummySlug has been damaged by 25HP in total
        assertEquals(dummySlug.getHealthPoints(), 5);
        assertEquals(slug.getHealthPoints(), dummySlug.getHealthPoints());
    }

    @Test
    public void testSoldierAttackDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        List<BasicEnemy> enemies = new ArrayList<>();
        List<AlliedSoldier> alliedSoldiers = new ArrayList<>();
        // Advance 6 ticks
        runXGameTicks(world, 6);
        
        // Add Slug not in range
        BasicEnemy slug = new Slug(new PathPosition(9, orderedPath));
        enemies.add(slug);
        world.setEnemies(enemies);
        // Add AlliedSoldier
        AlliedSoldier soldier = new AlliedSoldier(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        alliedSoldiers.add(soldier);
        world.setAlliedSoldiers(alliedSoldiers);
        // Test no battles
        assertEquals(world.existsBattles(), false);

        // Advance Character to 8
        moveCharacterDownXTimes(character, 2);
        // Test that a battle exists
        assertEquals(world.existsBattles(), true);

        
        // Run one turn of battle
        world.runBattleTurn(world.loadBattleEnemies(), world.loadBattleAllies());
        BasicEnemy dummySlug = new Slug(new PathPosition(7, orderedPath));
        character.attackEnemy(dummySlug, world);
        soldier.attackEnemy(dummySlug, world);
        // Test that dummySlug has been damaged by 22HP in total
        assertEquals(dummySlug.getHealthPoints(), 8);
        assertEquals(slug.getHealthPoints(), dummySlug.getHealthPoints());
    }

    // Helper Functions

    public void moveCharacterDownXTimes(Character character, int x) {
        for (int i = 0; i < x; i++) {
            character.moveDownPath();
        }
    }

    public void moveEnemyDownXTimes(BasicEnemy enemy, int x) {
        for (int i = 0; i < x; i++) {
            enemy.moveDownPath();
        }
    }

    public void moveEnemyUpXTimes(BasicEnemy enemy, int x) {
        for (int i = 0; i < x; i++) {
            enemy.moveUpPath();
        }
    }

    public void runXTickMoves(LoopManiaWorld world, int x) {
        for (int i = 0; i < x; i++) {
            world.runTickMoves();
        }
    }

    public List<BasicEnemy> runXGameTicks(LoopManiaWorld world, int x) {
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            world.runTickMoves();
            defeatedEnemies = world.runBattles(true);
        }
        return defeatedEnemies;
    }
}
