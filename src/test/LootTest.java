package test;

import org.junit.Test;
import org.javatuples.Pair;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Vampire;
import unsw.loopmania.Zombie;
import unsw.loopmania.Anduril;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.DoggieCoin;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.Item;
import unsw.loopmania.TheOneRing;
import unsw.loopmania.TreeStump;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LootTest {

    /**
     * Ensure that one card is given to the character
     * after one enemy is defeated in battle
     */
    @Test
    public void testOneCardOneEnemy() {
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

        // Force spawn slug in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        enemies.add(slug);
        world.setEnemies(enemies);

        // Run a battle and kill slug
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);
        
        // Check there is exactly one card in the world
        assertEquals(world.getCardEntities().size(), 1);
        
    }

    /**
     * Ensure that one card per enemy defeated in battle
     * is given to the character
     */
    @Test
    public void testOneCardPerEnemy() {
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

        // Force spawn slug and vampire in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        Vampire vampire = new Vampire(new PathPosition(2, world.getOrderedPath()));
        enemies.add(slug);
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);
        
        // Check there are exactly two cards in the world
        assertEquals(world.getCardEntities().size(), 2);
        
    }

    /**
     * Ensure that one health potion and another item is 
     * given to the character after one enemy is defeated in battle
     */
    @Test
    public void testOnePotionAndItemOneEnemy() {
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

        // Force spawn slug in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        enemies.add(slug);
        world.setEnemies(enemies);

        // Run a battle and kill slug
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);
        
        // Check there are two new items in the world,
        // one of which is a health potion
        assertEquals(world.getUnequippedInventoryItems().size(), 2);
        List<Item> inventory = world.getUnequippedInventoryItems();
        boolean hasPotion = false;
        for (Item i : inventory) {
            if (i instanceof HealthPotion) { hasPotion = true; }
        }
        if (!hasPotion) { assertTrue(false); }
        
    }

    /**
     * Ensure that one health potion and one other item per enemy 
     * defeated in battle is given to the character
     */
    @Test
    public void testOnePotionAndItemPerEnemy() {
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

        // Force spawn slug and vampire in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        Vampire vampire = new Vampire(new PathPosition(2, world.getOrderedPath()));
        enemies.add(slug);
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check there are four new items in the world,
        // two of which are health potions
        assertEquals(world.getUnequippedInventoryItems().size(), 4);
        List<Item> inventory = world.getUnequippedInventoryItems();
        int potionCount = 0;
        for (Item i : inventory) {
            if (i instanceof HealthPotion) { potionCount++; }
        }
        if (potionCount != 2) { assertTrue(false); }
        
    }

    /**
     * Ensure 10 gold is added to the characters balance
     * for each slug defeated
     */
    @Test
    public void testTenGoldPerSlug() {
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
        int originalGold = world.getTotalGold();

        // Force spawn slug in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        enemies.add(slug);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 10
        assertEquals(world.getTotalGold(), originalGold + 10);

        // Force spawn 2 slugs in front of character
        enemies = new ArrayList<>();
        slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        Slug slug2 = new Slug(new PathPosition(7, world.getOrderedPath()));
        enemies.add(slug);
        enemies.add(slug2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 20
        assertEquals(world.getTotalGold(), originalGold + 10 + 20);
        
    }

    /**
     * Ensure 50 experience points are added to the characters
     * balance for each slug defeated
     */
    @Test
    public void testFiftyXpPerSlug() {
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
        int originalXp = character.getXp();

        // Force spawn slug in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        enemies.add(slug);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 50
        assertEquals(character.getXp(), originalXp + 50);

        // Force spawn 2 slugs in front of character
        enemies = new ArrayList<>();
        slug = new Slug(new PathPosition(1, world.getOrderedPath()));
        Slug slug2 = new Slug(new PathPosition(7, world.getOrderedPath()));
        enemies.add(slug);
        enemies.add(slug2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 100
        assertEquals(character.getXp(), originalXp + 50 + 100);
        
    }

    /**
     * Ensure 30 gold is added to the characters balance
     * for each vampire defeated
     */
    @Test
    public void testThirtyGoldPerVampire() {
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
        int originalGold = world.getTotalGold();

        // Force spawn vampire in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 30
        assertEquals(world.getTotalGold(), originalGold + 30);

        // Force spawn 2 vampires in front of character
        enemies = new ArrayList<>();
        vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        Vampire vampire2 = new Vampire(new PathPosition(7, world.getOrderedPath()));
        enemies.add(vampire);
        enemies.add(vampire2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 60
        assertEquals(world.getTotalGold(), originalGold + 30 + 60);
        
    }

    /**
     * Ensure 150 experience points are added to the characters
     * balance for each vampire defeated
     */
    @Test
    public void testHundredFiftyXpPerVampire() {
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
        int originalXp = character.getXp();

        // Force spawn vampire in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 150
        assertEquals(character.getXp(), originalXp + 150);

        // Force spawn 2 vampires in front of character
        enemies = new ArrayList<>();
        vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        Vampire vampire2 = new Vampire(new PathPosition(7, world.getOrderedPath()));
        enemies.add(vampire);
        enemies.add(vampire2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 300
        assertEquals(character.getXp(), originalXp + 150 + 300);
        
    }

    /**
     * Ensure 25 gold is added to the characters balance
     * for each zombie defeated
     */
    @Test
    public void testTwentyFiveGoldPerZombie() {
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
        int originalGold = world.getTotalGold();

        // Force spawn zombie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 25
        assertEquals(world.getTotalGold(), originalGold + 25);

        // Force spawn 2 zombies in front of character
        enemies = new ArrayList<>();
        zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        Zombie zombie2 = new Zombie(new PathPosition(7, world.getOrderedPath()));
        enemies.add(zombie);
        enemies.add(zombie2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 50
        assertEquals(world.getTotalGold(), originalGold + 25 + 50);
        
    }

    /**
     * Ensure 125 experience points are added to the characters
     * balance for each zombie defeated
     */
    @Test
    public void testHundredTwentyFiveXpPerZombie() {
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
        int originalXp = character.getXp();

        // Force spawn zombie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Zombie zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        enemies.add(zombie);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 125
        assertEquals(character.getXp(), originalXp + 125);

        // Force spawn 2 zombies in front of character
        enemies = new ArrayList<>();
        zombie = new Zombie(new PathPosition(1, world.getOrderedPath()));
        Zombie zombie2 = new Zombie(new PathPosition(7, world.getOrderedPath()));
        enemies.add(zombie);
        enemies.add(zombie2);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 250
        assertEquals(character.getXp(), originalXp + 125 + 250);
        
    }

    /**
     * Ensure 100 gold is added to the characters balance
     * after Doggie is defeated
     */
    @Test
    public void testHundredGoldDoggie() {
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
        int originalGold = world.getTotalGold();

        // Force spawn Doggie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Doggie doggie = new Doggie(new PathPosition(1, world.getOrderedPath()));
        doggie.setHealthPoints(10);
        enemies.add(doggie);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 100
        assertEquals(world.getTotalGold(), originalGold + 100);
    
    }

    /**
     * Ensure 1000 experience points are added to the characters
     * balance after Doggie is defeated
     */
    @Test
    public void testThousandXpDoggie() {
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
        int originalXp = character.getXp();

        // Force spawn doggie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Doggie doggie = new Doggie(new PathPosition(1, world.getOrderedPath()));
        doggie.setHealthPoints(10);
        enemies.add(doggie);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 1000
        assertEquals(character.getXp(), originalXp + 1000);

    }

    /**
     * Ensure the DoggieCoin is obtained by the 
     * characters after Doggie is defeated
     */
    @Test
    public void testDoggieCoinAfterDoggie() {
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

        // Force spawn doggie in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        Doggie doggie = new Doggie(new PathPosition(1, world.getOrderedPath()));
        doggie.setHealthPoints(10);
        enemies.add(doggie);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the character has the DoggieCoin
        boolean hasDoggieCoin = false;
        List<Class<?>> createdItems = world.getCreatedItems();
        for (Class<?> item : createdItems) {
            if (item.equals(DoggieCoin.class)) hasDoggieCoin = true;
        }
        if (!hasDoggieCoin) assertTrue(false);

    }

    /**
     * Ensure 500 gold is added to the characters balance
     * after Elan Muske is defeated
     */
    @Test
    public void testFiveHundredGoldElanMuske() {
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
        int originalGold = world.getTotalGold();

        // Force spawn Elan Muske in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        ElanMuske elanMuske = new ElanMuske(new PathPosition(1, world.getOrderedPath()));
        elanMuske.setHealthPoints(10);
        enemies.add(elanMuske);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters gold balance has increased by 500
        assertEquals(world.getTotalGold(), originalGold + 500);

    }

    /**
     * Ensure 2000 experience points are added to the characters
     * balance after Elan Muske is defeated
     */
    @Test
    public void testTwoThousandXpElanMuske() {
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
        int originalXp = character.getXp();

        // Force spawn Elan Muske in front of character
        List<BasicEnemy> enemies = new ArrayList<>();
        ElanMuske elanMuske = new ElanMuske(new PathPosition(1, world.getOrderedPath()));
        elanMuske.setHealthPoints(10);
        enemies.add(elanMuske);
        world.setEnemies(enemies);

        // Run a battle and kill enemies
        world.runBattles(true);
        assertEquals(world.getEnemies().size(), 0);

        // Check the characters Xp balance has increased by 2000
        assertEquals(character.getXp(), originalXp + 2000);

    }

    /**
     * There is a 1% chance of obtaining 
     * The One Ring after a battle
     */
    @Test
    public void testOnePercentChanceOneRing() {
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

        // Seed the function that generates the random numbers, loop
        // through battles 100 times and make sure the one ring is 
        // only ever collected once
        boolean hasRing = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof TheOneRing) { hasRing = true; }
            }
            character.setHealth(250);
        }
        if (!hasRing) assertTrue(false);
        
    }

    /**
     * There is a 1% chance of obtaining 
     * Anduril, Flame of the West, after a battle
     */
    @Test
    public void testOnePercentChanceAnduril() {
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

        // Seed the function that generates the random numbers, loop
        // through battles 100 times and make sure Anduril is 
        // only ever collected once
        boolean hasAnduril = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof Anduril) { hasAnduril = true; }
            }
            character.setHealth(250);
        }
        if (!hasAnduril) assertTrue(false);
        
    }

    /**
     * There is a 1% chance of obtaining 
     * Tree Stump after a battle
     */
    @Test
    public void testOnePercentChanceTreeStump() {
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

        // Seed the function that generates the random numbers, loop
        // through battles 100 times and make sure the tree stump is 
        // only ever collected once
        boolean hasTreeStump = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, world.getOrderedPath()));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof TreeStump) { hasTreeStump = true; }
            }
            character.setHealth(250);
        }
        if (!hasTreeStump) assertTrue(false);
        
    }
    
}
