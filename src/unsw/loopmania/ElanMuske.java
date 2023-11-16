package unsw.loopmania;

import java.util.Random;

public class ElanMuske extends BasicEnemy {
    
    public ElanMuske(PathPosition position) {

        super(position);
        this.setHealthPoints(25000);
        this.setDamagePoints(50);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
    }

    public void move() {
        
        // this basic enemy moves in a random direction, 50% chance up or down
        int directionChoice = (new Random()).nextInt(3);
        if (directionChoice == 0 || directionChoice == 2){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }

    /**
     * Rewards the charcater when defeated in battle
     * Elan Muske gives 500 gold, 2000 XP and decreases price of DoggieCoin
     */
    public void battleRewards(LoopManiaWorld world) {
        Character character = world.getCharacter();
        character.getGold().setValue(character.getGold().getValue() + 500);
        character.setXp(character.getXp() + 2000);
        //TODO: dramatically decrease price of DoggieCoin, if one exists
        // world.getNonSpecifiedEntities().get(0).setPrice(1);
    }

    public int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world) {

        for (BasicEnemy current : world.getEnemies()) {

            if (world.inRadius(current, this, this.getSupportRadius())) {

                int currEnemyHealth = current.getHealthPoints();
                currEnemyHealth = currEnemyHealth + 10;
                current.setHealthPoints(currEnemyHealth);
            }
        }

        return damageDealt;
    }
}
