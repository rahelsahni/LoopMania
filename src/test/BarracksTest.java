package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BarracksCard;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.HealthPotion;
import unsw.loopmania.Item;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

public class BarracksTest {

    /**
     * Create a simple path and make sure an error is thrown if a 
     * barracks is placed on a non-path tile
     */
    @Test
    public void testBarracksPathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                    new Pair<Integer, Integer>(1, 2),
                                                                    new Pair<Integer, Integer>(1, 3), 
                                                                    new Pair<Integer, Integer>(2, 3),
                                                                    new Pair<Integer, Integer>(3, 3), 
                                                                    new Pair<Integer, Integer>(3, 2),
                                                                    new Pair<Integer, Integer>(3, 1), 
                                                                    new Pair<Integer, Integer>(2, 1)));

        // Path tile is ok
        Card barracksCard = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        world.loadCard(barracksCard);
        assertFalse(world.convertCardToBuildingByCoordinates(0, 0, 1, 3) == null);

        // Non-Path tile is not ok
        Card barracksCard1 = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        world.loadCard(barracksCard1);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 4, 2) == null);

        // Hero Castle tile throws error
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setHeroCastle();
        Card barracksCard2 = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        world.loadCard(barracksCard2);
        assertTrue(world.convertCardToBuildingByCoordinates(0, 0, 1, 1) == null);

    }

    /**
     * Ensure that the character gains an allied soldier 
     * when passing through the barracks
     * @throws Exception
     */  
    @Test
    public void testBarracksGivesOneAlly() throws Exception {
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

        // Deploy a barracks
        Card barracksCard = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        world.loadCard(barracksCard);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 3);

        // Move character to one square past barracks
        for (int i = 0; i < 4; i++) {
            world.runTickMoves();
        }

        // Check character has an allied soldier
        // Need to create class called AlliedSoldier -> List<AlliedSoldier> alliedSoldiers; in LoopManiaWorld
        assertEquals(world.getAlliedSoldiers().size(), 1);

    }

    /**
     * Ensure that the character can only have 4 allied
     * soldiers at a time, to conform with width of inventory
     * @throws Exception
     */  
    @Test
    public void testBarracksGivesMaxAllies() throws Exception {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));

        // Create a character and store its gold, xp and inventory size
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        int originalGold = character.getGold().getValue();
        int originalXp = character.getXp();
        int originalInventorySize = world.getUnequippedInventoryItems().size();

        // Deploy a barracks
        Card barracksCard = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        world.loadCard(barracksCard);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 3);

        // Move character to one square past barracks 4 times
        for (int i = 0; i < 32; i++) {
            world.runTickMoves();
        }

        // Check character has four allied soldiers
        assertEquals(world.getAlliedSoldiers().size(), 4);

        // Move character past barracks one more time
        for (int i = 0; i < 8; i++) {
            world.runTickMoves();
        }

        // Check character still has four allied soldiers, 
        // but also 10 gold, 100 xp, a new item and health potion
        assertEquals(world.getAlliedSoldiers().size(), 4);
        assertEquals(character.getGold().getValue(), originalGold + 10);
        assertEquals(character.getXp(), originalXp + 100);
        List<Item> inventory = world.getUnequippedInventoryItems();
        assertEquals(inventory.size(), originalInventorySize + 2);
        int healthPotion = 0;
        for (Item item : inventory) {
            if (item.getClass().equals(HealthPotion.class)) {
                healthPotion++;
            }
        }
        if (healthPotion != 1) { assertTrue(false); }

    }
    
}
