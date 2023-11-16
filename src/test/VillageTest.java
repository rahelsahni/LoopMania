package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.VillageCard;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.javatuples.Pair;

public class VillageTest {

    /**
     * Create a simple path and make sure an error is thrown if a village 
     * is placed on a non-path tile
     */
    @Test
    public void testVillagePathTiles() {
        LoopManiaWorld world = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                    new Pair<Integer, Integer>(1, 2),
                                                                    new Pair<Integer, Integer>(1, 3), 
                                                                    new Pair<Integer, Integer>(2, 3),
                                                                    new Pair<Integer, Integer>(3, 3), 
                                                                    new Pair<Integer, Integer>(3, 2),
                                                                    new Pair<Integer, Integer>(3, 1), 
                                                                    new Pair<Integer, Integer>(2, 1)));

        // Path tile is ok
        Card villageCard = world.getCardFactory().createStaticEntity(VillageCard.class, world);
        world.loadCard(villageCard);
        assertDoesNotThrow(() -> {
            world.convertCardToBuildingByCoordinates(0, 0, 1, 3);
        });

        // Non-Path tile throws error
        Card villageCard1 = world.getCardFactory().createStaticEntity(VillageCard.class, world);
        world.loadCard(villageCard1);
        assertThrows(Exception.class, () -> {
            world.convertCardToBuildingByCoordinates(0, 0, 4, 2);
        });

        // Hero Castle tile throws error
        Character character = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(character);
        world.setHeroCastle();
        Card villageCard2 = world.getCardFactory().createStaticEntity(VillageCard.class, world);
        world.loadCard(villageCard2);
        assertThrows(Exception.class, () -> {
            world.convertCardToBuildingByCoordinates(0, 0, 1, 1);
        });

    }

    /**
     * Ensure that the character regains 50HP when 
     * passing through the village
     * @throws Exception
     */  
    @Test
    public void testVillageHealsCharacter() throws Exception {
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

        // Deploy a village
        Card villageCard = world.getCardFactory().createStaticEntity(VillageCard.class, world);
        world.loadCard(villageCard);
        world.convertCardToBuildingByCoordinates(0, 0, 2, 3);

        // Make character have 1 HP
        character.setHealth(1);

        // Move character to one square past village
        for (int i = 0; i < 4; i++) {
            world.runTickMoves();
        }

        // Check character has 51 HP
        assertEquals(character.getHealth(), 51);

    }
    
}
