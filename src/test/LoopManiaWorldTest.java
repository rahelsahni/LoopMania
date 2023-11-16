package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.javatuples.Pair;

import org.junit.jupiter.api.Test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Sword;
import unsw.loopmania.Stake;
import unsw.loopmania.Staff;
import unsw.loopmania.Armour;
import unsw.loopmania.Shield;
import unsw.loopmania.Helmet;
import unsw.loopmania.CampfireCard;
import unsw.loopmania.VampireCastleCard;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;

public class LoopManiaWorldTest {
    
    @Test
    public void EquipWeaponTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Sword s = (Sword) d.addUnequippedItem(d.getItemFactory().createStaticEntity(Sword.class, d));
        assertTrue(d.equipWeapon(s.getX(), s.getY(), 1, 1) instanceof Sword);
    }

    @Test
    public void EquipArmourTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Armour a = (Armour) d.addUnequippedItem(d.getItemFactory().createStaticEntity(Armour.class, d));
        assertTrue(d.equipArmour(a.getX(), a.getY(), 1, 1) instanceof Armour);
    }

    @Test
    public void EquipShieldTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Shield s = (Shield) d.addUnequippedItem(d.getItemFactory().createStaticEntity(Shield.class, d));
        assertTrue(d.equipShield(s.getX(), s.getY(), 1, 1) instanceof Shield);
    }

    @Test
    public void EquipHelmetTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        Helmet h = (Helmet) d.addUnequippedItem(d.getItemFactory().createStaticEntity(Helmet.class, d));
        assertTrue(d.equipHelmet(h.getX(), h.getY(), 1, 1) instanceof Helmet);
    }
    
    @Test
    public void successfulBuyTest(){
        LoopManiaWorld d = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        Character character = new Character(new PathPosition(0, d.getOrderedPath()));
        d.setCharacter(character);
        d.setTotalGold(1000);
        assertTrue(d.buyItem(Sword.class) instanceof Sword);
        assertEquals(d.getTotalGold(), 800);
        assertEquals(d.getUnequippedInventoryItems().size(), 1);
        assertTrue(d.getUnequippedInventoryItems().get(0) instanceof Sword);
        assertTrue(d.buyItem(Stake.class) instanceof Stake);
        assertEquals(d.getTotalGold(), 500);
        assertEquals(d.getUnequippedInventoryItems().size(), 2);
        assertTrue(d.getUnequippedInventoryItems().get(0) instanceof Sword);
        assertTrue(d.getUnequippedInventoryItems().get(1) instanceof Stake);
    }

    // Maybe disable the button if the player doesn't have enough money?
    @Test
    public void unsuccessfulBuyTest(){
        LoopManiaWorld d = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        Character character = new Character(new PathPosition(0, d.getOrderedPath()));
        d.setCharacter(character);
        d.setTotalGold(100);
        assertEquals(d.buyItem(Sword.class), null);
        assertEquals(d.getTotalGold(), 100);
        assertEquals(d.getUnequippedInventoryItems().size(), 0);
    }

    /* This test is uneccesary, view assumptions.md for details (basically the frontend handles what can and can't be bought, and leaving the backend open makes it easier to add/remove items from the shop)
    // It also uses old methods and wouldn't work in the current build
    @Test
    public void unbuyableBuyTest(){
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>());
        d.setTotalGold(1000);
        assertTrue(d.buyItem("theOneRing", 1000) instanceof Item);
        assertEquals(d.getTotalGold(), 1000);
        assertEquals(d.getUnequippedInventoryItems().size(), 0);
    }
    */

    @Test
    public void cardRemovalTest(){
        LoopManiaWorld d = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        Character character = new Character(new PathPosition(0, d.getOrderedPath()));
        d.setCharacter(character);
        CampfireCard c = (CampfireCard) d.getCardFactory().createStaticEntity(CampfireCard.class, d);
        d.loadCard(c);
        VampireCastleCard v = (VampireCastleCard) d.getCardFactory().createStaticEntity(VampireCastleCard.class, d);
        d.loadCard(v);
        for (int i = 0; i < 7; i++) {
            d.loadCard((VampireCastleCard) d.getCardFactory().createStaticEntity(VampireCastleCard.class, d));
        }
        assertEquals(d.getCardEntities().size(), 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(d.getCardEntities().get(i) instanceof VampireCastleCard);
        }
        assertEquals(d.getTotalGold(), 40);
        assertEquals(d.getCharacter().getXp(), 400);
    }


    @Test
    public void itemRemovalTest(){
        LoopManiaWorld d = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        Character character = new Character(new PathPosition(0, d.getOrderedPath()));
        d.setCharacter(character);
        d.addUnequippedItem(d.getItemFactory().createStaticEntity(Sword.class, d));
        for (int i = 0; i < 16; i++) {
            d.addUnequippedItem(d.getItemFactory().createStaticEntity(Staff.class, d));
        }

        assertEquals(d.getUnequippedInventoryItems().size(), 16);
        for (int i = 0; i < 16; i++) {
            assertTrue(d.getUnequippedInventoryItems().get(i) instanceof Staff);
        }

        assertEquals(d.getTotalGold(), 10);
        assertEquals(d.getCharacter().getXp(), 100);

    }

    @Test
    public void bothRemovalTest(){
        LoopManiaWorld d = new LoopManiaWorld(5, 5, Arrays.asList(new Pair<Integer, Integer>(1, 1), 
                                                                      new Pair<Integer, Integer>(1, 2),
                                                                      new Pair<Integer, Integer>(1, 3), 
                                                                      new Pair<Integer, Integer>(2, 3),
                                                                      new Pair<Integer, Integer>(3, 3), 
                                                                      new Pair<Integer, Integer>(3, 2),
                                                                      new Pair<Integer, Integer>(3, 1), 
                                                                      new Pair<Integer, Integer>(2, 1)));
        Character character = new Character(new PathPosition(0, d.getOrderedPath()));
        d.setCharacter(character);
        CampfireCard c = (CampfireCard) d.getCardFactory().createStaticEntity(CampfireCard.class, d);
        d.loadCard(c);
        VampireCastleCard v = (VampireCastleCard) d.getCardFactory().createStaticEntity(VampireCastleCard.class, d);
        d.loadCard(v);
        for (int i = 0; i < 7; i++) {
            d.loadCard((VampireCastleCard) d.getCardFactory().createStaticEntity(VampireCastleCard.class, d));
        }
        assertEquals(d.getCardEntities().size(), 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(d.getCardEntities().get(i) instanceof VampireCastleCard);
        }
        assertEquals(d.getTotalGold(), 40);
        assertEquals(d.getCharacter().getXp(), 400);

        d.addUnequippedItem(d.getItemFactory().createStaticEntity(Sword.class, d));
        for (int i = 0; i < 10; i++) {
            d.addUnequippedItem(d.getItemFactory().createStaticEntity(Staff.class, d));
        }
        assertEquals(d.getUnequippedInventoryItems().size(), 11);
    }

}
