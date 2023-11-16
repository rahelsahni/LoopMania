package unsw.loopmania;

import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

public class ItemFactory implements StaticEntityFactory {

    /**
     * Creates a new random Item and returns it so 
     * it can be placed as a StaticEntity in the world
     * @param world
     * @return newItem
     */
    @Override
    public Item createRandomStaticEntity(LoopManiaWorld world) {
        Item newItem = null;
        Random random = new Random();
        int itemChoice = random.nextInt(6);
        switch (itemChoice) {
            case 0:
                newItem = createStaticEntity(Sword.class, world);
                break;
            case 1:
                newItem = createStaticEntity(Stake.class, world);
                break;
            case 2:
                newItem = createStaticEntity(Staff.class, world);
                break;
            case 3:
                newItem = createStaticEntity(Armour.class, world);
                break;
            case 4:
                newItem = createStaticEntity(Shield.class, world);
                break;
            case 5:
                newItem = createStaticEntity(Helmet.class, world);
        }
        return newItem;
    }

    /**
     * Creates a new Item of type itemClass and returns it so 
     * it can be placed as a StaticEntity in the world
     * @param itemClass
     * @param world
     * @return newItem
     */
    @Override
    public Item createStaticEntity(Class<?> itemClass, LoopManiaWorld world) {
        Pair<Integer, Integer> firstAvailableSlot = world.itemEntitiesFull();
        Item newItem = null;
        if (itemClass.equals(Sword.class)) {
            newItem = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Stake.class)) {
            newItem = new Stake(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Staff.class)) {
            newItem = new Staff(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Armour.class)) {
            newItem = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Shield.class)) {
            newItem = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Helmet.class)) {
            newItem = new Helmet(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(HealthPotion.class)) {
            newItem = new HealthPotion(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(TheOneRing.class)) {
            newItem = new TheOneRing(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(Anduril.class)) {
            newItem = new Anduril(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(TreeStump.class)) {
            newItem = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        } else if (itemClass.equals(DoggieCoin.class)) {
            newItem = new DoggieCoin(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        }
        return newItem;
    }
    
}
