package unsw.loopmania;

import java.util.List;
import java.util.Random;

public class Doggie extends BasicEnemy {
    
    Random stunChance = new Random(3000);

    /**
     * Constructor for Doggie boss
     * @param position
     */
    public Doggie(PathPosition position) {

        super(position);
        this.setHealthPoints(10000);
        this.setDamagePoints(30);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
    }

    public void move() {

        // this basic enemy moves in a random direction, 25% up or down, 50% it stays
        int directionChoice = (new Random()).nextInt(4);
        if (directionChoice == 0){
            moveUpPath();
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
            moveDownPath();
        }
    }

    /**
     * Rewards the charcater when defeated in battle
     * Doggie gives 100 gold, 1000 XP and DoggieCoin
     */
    public void battleRewards(LoopManiaWorld world) {
        Character character = world.getCharacter();
        character.getGold().setValue(character.getGold().getValue() + 100);
        character.setXp(character.getXp() + 1000);
        List<Class<?>> createdItems = world.getCreatedItems();
        createdItems.add(DoggieCoin.class);
        world.setCreatedItems(createdItems);
    }

    public int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world) {
        int chance = 5;
        if (world.getEquippedShield() instanceof TreeStump) {

            chance = 25;
        }
        int stunChoice = stunChance.nextInt(chance);
        // added extra so that there is a 3/25 (12%) chance (60% less than usual 20%) if TreeStump equipped
        if (stunChoice == 3 || (chance == 25 && (stunChoice == 1 || stunChoice == 2))) {

            character.stunned();
        }

        return damageDealt;
    }
}
