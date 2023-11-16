package unsw.loopmania;

import java.util.Random;

public class Zombie extends BasicEnemy {
    
    private boolean moves;

    /**
     * Constructor for Zombie enemy type
     * @param position
     */
    public Zombie(PathPosition position) {
        super(position);

        this.moves = false;
        this.setHealthPoints(30);
        this.setDamagePoints(15);
        this.setBattleRadius(2);
        this.setSupportRadius(2);
    }

    public void move() {

        // this moves boolean staggers zombie movement, so that
        // a zombie only moves every 2 ticks
        if (moves) {

            // this basic enemy moves in a random direction, 50% chance up or down
            int directionChoice = (new Random()).nextInt(2);
            if (directionChoice == 0){
                moveUpPath();
            }
            else if (directionChoice == 1){
                moveDownPath();
            }

            moves = false;
        }
        else if (!moves) {
            moves = true;
        }
    }

    /**
     * Rewards the charcater when defeated in battle
     * Zombie gives 25 gold and 125 XP
     */
    public void battleRewards(LoopManiaWorld world) {
        Character character = world.getCharacter();
        character.getGold().setValue(character.getGold().getValue() + 25);
        character.setXp(character.getXp() + 125);
    }

    public int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world) {

        // if the enemy is a zombie and hits a crit
        // increase damage by ~50%
        if (choice == 1) {

            damageDealt = damageDealt + 7;
        }

        return damageDealt;
    }
}
