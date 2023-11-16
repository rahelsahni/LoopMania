package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

public class CardFactory implements StaticEntityFactory {

    /**
     * Creates a new random Card and returns it so 
     * it can be placed as a StaticEntity in the world
     * @param world
     * @return newCard
     */
    @Override
    public Card createRandomStaticEntity(LoopManiaWorld world) {
        Card newCard = null;
        Random random = new Random();
        int cardChoice = random.nextInt(7);
        switch (cardChoice) {
            case 0:
                newCard = createStaticEntity(VampireCastleCard.class, world);
            case 1:
                newCard = createStaticEntity(ZombiePitCard.class, world);
            case 2:
                newCard = createStaticEntity(TowerCard.class, world);
            case 3:
                newCard = createStaticEntity(VillageCard.class, world);
            case 4:
                newCard = createStaticEntity(BarracksCard.class, world);
            case 5:
                newCard = createStaticEntity(TrapCard.class, world);
            case 6:
                newCard = createStaticEntity(CampfireCard.class, world);
        }
        return newCard;
        
    }

    /**
     * Creates a new Card of type cardClass and returns it so 
     * it can be placed as a StaticEntity in the world
     * @param cardClass
     * @param world
     * @return newCard
     */
    @Override
    public Card createStaticEntity(Class<?> cardClass, LoopManiaWorld world) {
        world.cardEntitiesFull();
        Card newCard = null;
        if (cardClass.equals(VampireCastleCard.class)) {
            newCard = new VampireCastleCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(ZombiePitCard.class)) {
            newCard = new ZombiePitCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(TowerCard.class)){
            newCard = new TowerCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(VillageCard.class)) {
            newCard = new VillageCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(BarracksCard.class)){
            newCard = new BarracksCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(TrapCard.class)) {
            newCard = new TrapCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        } else if (cardClass.equals(CampfireCard.class)) {
            newCard = new CampfireCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        }
        return newCard;
    }
    
}
