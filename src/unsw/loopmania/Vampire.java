package unsw.loopmania;

import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

public class Vampire extends BasicEnemy {

    /**
     * Constuctor for Vampire enemy type
     * @param position
     */
    public Vampire(PathPosition position) {
        super(position);

        this.setHealthPoints(60);
        this.setDamagePoints(25);
        this.setBattleRadius(2);
        this.setSupportRadius(3);
    }

    /**
     * Overloaded method for vampires to run from campfires
     * @param world
     */
    public void move(LoopManiaWorld world) {
    
        List<CampfireBuilding> campfires = world.getCampfires();

        // if vampire is not in radius of any campfire, or there are no campfires
        if (campfires.size() == 0 || !inCampfire(campfires, this.getPosition())) {
            move();
        }
        // this checks how many steps forwards or backwards a vampire needs to take
        // to run away from a campfire
        else {

            // get current position of the vampire
            PathPosition currPosition = this.getPathPosition();
            List<Pair<Integer, Integer>> orderedPath = currPosition.getOrderedPath();
            int currentPositionInPath = currPosition.getCurrentPositionInPath();

            // simulate how many moves down the path it will take
            // to leave the radius (or radii) of campfires
            PathPosition downTmp = new PathPosition(currentPositionInPath, orderedPath);
            int moveDownNum = 0;
            while (inCampfire(campfires, downTmp)) {

                downTmp.moveDownPath();
                moveDownNum++;
            }

            // simulate how many moves up the path it will take
            // to leave the radius (or radii) of campfires
            PathPosition upTmp = new PathPosition(currentPositionInPath, orderedPath);
            int moveUpNum = 0;
            while (inCampfire(campfires, upTmp)) {

                upTmp.moveUpPath();
                moveUpNum++;
            }

            // if they're equidistant, move anywhere
            if (moveDownNum == moveUpNum) {
                move();
            }
            // if moving down is shorter, move down
            else if (moveDownNum < moveUpNum) {
                moveDownPath();
            }
            // if moving up is shorter, move up
            else if (moveUpNum < moveDownNum) {
                moveUpPath();
            }
        }
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
     * Boolean helper function to check if the given position
     * is in the radius of any campire in campfires list
     * @param campfires
     * @param position
     * @return true if position is in radius of campfire; false otherwise
     */
    public boolean inCampfire(List <CampfireBuilding> campfires, PathPosition position) {

        int radius = 3;

        for (CampfireBuilding current : campfires) {
            if (Math.pow((position.getX().get()-current.getX()), 2) +  Math.pow((position.getY().get()-current.getY()), 2) <= Math.pow(radius,2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Rewards the charcater when defeated in battle
     * Vampire gives 30 gold and 150 XP
     */
    public void battleRewards(LoopManiaWorld world) {
        Character character = world.getCharacter();
        character.getGold().setValue(character.getGold().getValue() + 30);
        character.setXp(character.getXp() + 150);
    }

    @Override
    public int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world) {
        
        // if character has TreeStump, critical vampire attacks cannot occur
        if (!(world.getEquippedShield() instanceof TreeStump)) {
            // if the enemy attacking is a vampire and hits a critical hit
            if (character.getCriticalBiteNum() > 0) {

                // increase damange by random number
                // between 5 and 9
                int criticalDamage = rand3.nextInt(5) + 5;

                damageDealt = damageDealt + criticalDamage;

                // decrease remaining crit turns
                int oldCritBiteNum = character.getCriticalBiteNum() - 1;

                character.setCriticalBiteNum(oldCritBiteNum);
            }
            else if (choice == 0) {

                // set how many turns the character is affected 
                // by the critical hit. a random number between
                // 1 and 3. only done if the character has 0
                // crit turns affected currently
                if (character.getCriticalBiteNum() <= 0) {

                    int criticalTurnNum = rand2.nextInt(3) + 1;

                    character.setCriticalBiteNum(criticalTurnNum);
                }
                
                // increase damange by random number
                // between 5 and 9
                int criticalDamage = rand3.nextInt(5) + 5;

                damageDealt = damageDealt + criticalDamage;

                // decrease remaining crit turns
                int oldCritBiteNum = character.getCriticalBiteNum() - 1;

                character.setCriticalBiteNum(oldCritBiteNum);
            }
        }
        return damageDealt;
    }
}
