package unsw.loopmania;

import java.util.Random;

public class Slug extends BasicEnemy {
    
    /**
     * Constructor for Slug enemy type
     * @param position
     */
    public Slug(PathPosition position) {

        super(position);

        this.setHealthPoints(30);
        this.setDamagePoints(5);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
    }

    public void move() {
    
        // this basic enemy moves in a random direction, 50% chance up or down
        int directionChoice = (new Random()).nextInt(2);
        if (directionChoice == 0){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }

    /**
     * Rewards the charcater when defeated in battle
     * Slug gives 10 gold and 50 XP
     */
    public void battleRewards(LoopManiaWorld world) {
        Character character = world.getCharacter();
        character.getGold().setValue(character.getGold().getValue() + 10);
        character.setXp(character.getXp() + 50);
    }

    public int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world) {

        return damageDealt;
    }
}
