package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity implements AllyAttack{

    final int maxHealth;
    private int startPositionX;
    private int startPositionY;
    private int health;
    private int attackDamage;
    private int criticalBiteNum;
    private Gold gold;
    private int xp;
    private int stunNum;
    
    public Character(PathPosition position) {
        super(position);
        startPositionX = position.getX().get();
        startPositionY = position.getY().get();
        health = 250;
        attackDamage = 20;
        gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        xp = 0;
        criticalBiteNum = 0;
        maxHealth = 250;
        stunNum = 0;
    }

    public int getHealth() {
        return health;
    }
    
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health = health;
        }
    }

    public int getAttackDamage() {
        return attackDamage;
    }
    
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    
    public Gold getGold() {
        return gold;
    }

    public int getStunNum() {
        return stunNum;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }
    
    public int getXp() {
        return xp;
    }
    
    public void setXp(int xp) {
        this.xp = xp;
    }
    
    public int getCriticalBiteNum() {
        return this.criticalBiteNum;
    }
    
    public void setCriticalBiteNum(int criticalBiteNum) {
        this.criticalBiteNum = criticalBiteNum;
    }
    
    /**
     * Determines if the character is at their
     * starting point of the loop
     * @return true if at start of loop, false otherwise
     */
    public boolean isAtStart() {
        if (startPositionX == getX() && startPositionY == getY()) {
            return true;
        }
        return false;
    }

    public void attackEnemy(BasicEnemy enemy, LoopManiaWorld world) {
        int characterDamage = getAttackDamage();
        if (getStunNum() <= 0) {
            if (world.getEquippedWeapon() != null) {
                if (world.getEquippedWeapon() instanceof BasicWeapon || world.getEquippedWeapon() instanceof Anduril) {
                    if (world.getEquippedWeapon() instanceof Anduril) {
                        Anduril weapon = (Anduril) world.getEquippedWeapon();
                        characterDamage = weapon.modifyAttack(characterDamage);
                        if (enemy instanceof ElanMuske || enemy instanceof Doggie) {
                            characterDamage = characterDamage*3;
                        }
                    } else {
                        BasicWeapon weapon = (BasicWeapon) world.getEquippedWeapon();
                        characterDamage = weapon.modifyAttack(characterDamage);
                        if (enemy instanceof Vampire && weapon instanceof Stake) {
                            characterDamage = characterDamage + 30;
                        }
                    } 
                }
            }

            if (world.getEquippedHelmet() != null) {
                characterDamage = world.getEquippedHelmet().modifyAttack(characterDamage);
            }

            if (world.campfireInRange()) {
                characterDamage = characterDamage*2;
            }

            int newEnemyHealth = enemy.getHealthPoints() - characterDamage;
            enemy.setHealthPoints(newEnemyHealth);
        }
    }

    public void stunned() {

        stunNum = 2;
    }

    public void decreaseStun() {
        
        this.stunNum--;
    }
}
