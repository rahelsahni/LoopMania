package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.Anduril;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.Item;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Vampire;

public class AndurilTest {

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
    public void testOnePercentChanceAnduril() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // Seed the function that generates random numbers, loop
        // through battles 100 times and make sure that Anduril is collected
        boolean hasAnduril = false;
        for (int i = 0; i < 100; i++) {
            List<BasicEnemy> enemies = new ArrayList<>();
            Slug slug = new Slug(new PathPosition(1, orderedPath));
            enemies.add(slug);
            world.setEnemies(enemies);
            world.runBattles(true);
            List<Item> inventory = world.getUnequippedInventoryItems();
            for (Item item : inventory) {
                if (item instanceof Anduril) {
                    hasAnduril = true;
                }
            }
        }
        assertEquals(hasAnduril, isAndurilInInventory(world));
    }

    @Test
    public void testAndurilEquip() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);

        // add Anduril and test it is in the inventory
        Anduril anduril = (Anduril) world.addUnequippedItem(world.getItemFactory().createStaticEntity(Anduril.class, world));
        assertEquals(isAndurilInInventory(world), true);
        // Equip Anduril and test it is the equipped weapon
        world.equipWeapon(anduril.getX(), anduril.getY(), 1, 1);
        assertEquals(world.getEquippedWeapon() instanceof Anduril, true);
    }
    
    @Test 
    public void testAndurilDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        Anduril anduril = (Anduril) world.addUnequippedItem(world.getItemFactory().createStaticEntity(Anduril.class, world));
        world.equipWeapon(anduril.getX(), anduril.getY(), 1, 1);
        List<BasicEnemy> enemies = new ArrayList<>();
        Vampire vampire = new Vampire(new PathPosition(1, orderedPath));
        enemies.add(vampire);
        world.setEnemies(enemies);

        // Attack Vampire with Anduril
        int vampireInitialHealth = vampire.getHealthPoints();
        int andurilDamage = anduril.modifyAttack(character.getAttackDamage());
        assertEquals(andurilDamage, 60);
        character.attackEnemy(vampire, world);
        assertEquals(vampire.getHealthPoints(), vampireInitialHealth - character.getAttackDamage() - anduril.modifyAttack(0));
        assertEquals(vampire.getHealthPoints(), 0);
    }

    @Test 
    public void testAndurilBossDamage() {
        // Setup
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        Anduril anduril = (Anduril) world.addUnequippedItem(world.getItemFactory().createStaticEntity(Anduril.class, world));
        world.equipWeapon(anduril.getX(), anduril.getY(), 1, 1);
        List<BasicEnemy> enemies = new ArrayList<>();
        
        // Test against Elan Muske
        ElanMuske elanMuske = new ElanMuske(new PathPosition(1, orderedPath));
        enemies.add(elanMuske);
        world.setEnemies(enemies);

        // Attack Elan with Anduril
        int elanInitialHealth = elanMuske.getHealthPoints();
        int andurilDamage = anduril.modifyAttack(character.getAttackDamage());
        assertEquals(andurilDamage, 60);
        character.attackEnemy(elanMuske, world);
        assertEquals(elanMuske.getHealthPoints(), elanInitialHealth - 3*(character.getAttackDamage() + anduril.modifyAttack(0)));
        assertEquals(elanMuske.getHealthPoints(), 24820);
        elanMuske.destroy();

        // Test against Doggie
        Doggie doggie = new Doggie(new PathPosition(1, orderedPath));
        enemies.add(doggie);
        world.setEnemies(enemies);

        // Attack Doggie with Anduril
        int doggieInitialHealth = doggie.getHealthPoints();
        character.attackEnemy(doggie, world);
        assertEquals(doggie.getHealthPoints(), doggieInitialHealth - 3*(character.getAttackDamage() + anduril.modifyAttack(0)));
        assertEquals(doggie.getHealthPoints(), 9820);
    }

    // Helper

    public boolean isAndurilInInventory(LoopManiaWorld world) {
        for (Item item : world.getUnequippedInventoryItems()) {
            if (item instanceof Anduril) {
                return true;
            }
        }
        return false;
    }
}
