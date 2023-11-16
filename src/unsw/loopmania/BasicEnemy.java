package unsw.loopmania;

import java.util.Random;

/**
 * a superclass for enemies in the world
 */
public abstract class BasicEnemy extends MovingEntity {

    private int healthPoints;
    private int damagePoints;
    private int battleRadius;
    private int supportRadius;
    private int tranceNum;
    protected Random rand = new Random(6);
    protected Random rand2 = new Random(21);
    protected Random rand3 = new Random(42);

    /**
     * Constructor for basic enemies
     * @param position
     */
    public BasicEnemy(PathPosition position) {
        super(position);
        this.setTranceNum(0);
    }

    /**
     * Getter for basic_enemy health points
     * @return enemy health points
     */
    public int getHealthPoints() {
        return this.healthPoints;
    }

    /**
     * Getter for basic_enemy damage points
     * @return enemy damage points
     */
    public int getDamagePoints() {
        return this.damagePoints;
    }

    /**
     * Getter for basic_enemy battle radius
     * @return enemy battle radius
     */
    public int getBattleRadius() {
        return this.battleRadius;
    }

    /**
     * Getter for basic_enemy support radius
     * @return enemy support radius
     */
    public int getSupportRadius() {
        return this.supportRadius;
    }

    /**
     * Getter for basic_enemy trance number
     * @return rounds left in enemy trance
     */
    public int getTranceNum() {
        return this.tranceNum;
    }

    /**
     * Setter for basic_enemy health points
     * @param healthPoints
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Setter for basic_enemy damage points
     * @param damagePoints
     */
    public void setDamagePoints(int damagePoints) {
        this.damagePoints = damagePoints;
    }

    /**
     * Setter for basic_enemy battle radius
     * @param battleRadius
     */
    public void setBattleRadius(int battleRadius) {
        this.battleRadius = battleRadius;
    }

    /**
     * Setter for basic_enemy support radius
     * @param supportRadius
     */
    public void setSupportRadius(int supportRadius) {
        this.supportRadius = supportRadius;
    }

    /**
     * Setter for basic_enemy trance number
     * @param tranceNum
     */
    public void setTranceNum(int tranceNum) {
        this.tranceNum = tranceNum;
    }

     /**
      * Move basic enemy along path tile
      */
    public abstract void move();

    /**
     * Reward the charcter after defeat
     * @param character
     */
    public abstract void battleRewards(LoopManiaWorld world);

    /**
     * Attack a given character
     * @param character
     */
    public void attack(Character character, Armour equippedArmour, Item equippedShield, Helmet equippedHelmet, LoopManiaWorld world) {

        if (this.getTranceNum() > 0) {

            int currNum = this.getTranceNum();
            currNum = currNum - 1;
            this.setTranceNum(currNum);
            return;
        }

        int damageDealt = getDamagePoints();
        int currCharacterHealth = character.getHealth();

        int choice = rand.nextInt(9);
        damageDealt = criticalBite(damageDealt, character, choice, world);

        if (equippedHelmet != null) {
            damageDealt = equippedHelmet.reduceDamage(damageDealt);
        }
        if (equippedArmour != null) {
            damageDealt = equippedArmour.reduceDamage(damageDealt);
        }
        if (equippedShield != null) {
            if (equippedShield instanceof Shield) {
                damageDealt = ((Shield) equippedShield).reduceDamage(damageDealt);
            } else if (equippedShield instanceof TreeStump) {
                if (this instanceof Doggie || this instanceof ElanMuske) {
                    damageDealt = ((TreeStump) equippedShield).reduceDamage(damageDealt)*3/2;
                } else {
                    damageDealt = ((TreeStump) equippedShield).reduceDamage(damageDealt);
                }
            }
        }

        character.setHealth(currCharacterHealth - damageDealt);
    }

    /**
     * Attack a given allied soldier
     * @param soldier
     * @param world
     */
    public void attackAlliedSoldier(AlliedSoldier soldier, LoopManiaWorld world) {

        int damageDealt = this.getDamagePoints();
        int currSoldierHealth = soldier.getHealth();

        int choice = rand.nextInt(9);

        if (this.getTranceNum() == 0) {
            // if the enemy is a zombie and hits a crit
            // convert the allied soldier to a zombie
            if (this instanceof Zombie && choice == 1) {

                soldier.setHealth(0);

                // create a new zombie and spawn zombie into the world
                Character character = world.getCharacter();
                PathPosition characterPos = character.getPathPosition();
                PathPosition zombiePos = new PathPosition(characterPos.getCurrentPositionInPath(), characterPos.getOrderedPath());
                zombiePos.moveDownPath();
                Zombie new_zombie = new Zombie(zombiePos);

                world.getEnemies().add(new_zombie);
            }   
            
            // otherwise just attack as normal
            else {
                soldier.setHealth(currSoldierHealth - damageDealt);
            }
        }
    }

    /**
     * Setter function for if an enemy comes under a trance from a staff
     * @param turnNum
     */
    public void inTrance(int turnNum) {

        this.tranceNum = turnNum;
    }

    /**
     * Function to check if any additional damage is done, due to a critical hit
     * @param damageDealt
     * @param character
     * @param choice
     * @param world
     * @return extra damage to be done
     */
    public abstract int criticalBite(int damageDealt, Character character, int choice, LoopManiaWorld world);
}
