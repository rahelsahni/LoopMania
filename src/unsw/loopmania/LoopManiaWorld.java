package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * number of cycles completed by the character
     */
    private int cycles;
    private int cyclesSinceShop;
    private int shopCycles;
    private boolean vampireHasSpawnedOnCycle;
    private boolean zombieHasSpawnedOnCycle;
    private boolean doggieHasSpawnedOnCycle;
    private boolean doggieKilled;
    private boolean elanHasSpawnedOnCycle;
    private boolean elanKilled;
    private Random rand = new Random(0);
    private Random randOneRing = new Random(0);
    private Random randAnduril = new Random(3);
    private Random randTreeStump = new Random(8);
    
    private LogicalExpression worldGoal;
    private String worldGoalString;

    /**
     * Various game modes. 
     * No more than one can be true at a time
     */
    private boolean isSurvivalMode;
    private boolean isBerserkerMode;
    private boolean isConfusingMode;
    private boolean hasBoughtHealthPotion;
    private boolean hasBoughtProtection;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    private List<BasicEnemy> enemies;

    private List<Card> cardEntities;
    private CardFactory cardFactory;

    private List<Item> unequippedInventoryItems;
    private ItemFactory itemFactory;

    private List<Building> buildingEntities;

    private List<AlliedSoldier> alliedSoldiers;

    private List<Item> itemsOnPath;

    private Item equippedWeapon;

    private Armour equippedArmour;

    private Item equippedShield;

    private Helmet equippedHelmet;

    private List<Class<?>> createdItems;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        cycles = 0;
        cyclesSinceShop = 0;
        shopCycles = 1;
        vampireHasSpawnedOnCycle = false;
        zombieHasSpawnedOnCycle = false;
        doggieHasSpawnedOnCycle = false;
        doggieKilled = false;
        elanHasSpawnedOnCycle = false;
        elanKilled = false;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        cardFactory = new CardFactory();
        unequippedInventoryItems = new ArrayList<>();
        itemFactory = new ItemFactory();
        this.orderedPath = orderedPath;
        buildingEntities = new ArrayList<>();
        alliedSoldiers = new ArrayList<>();
        itemsOnPath = new ArrayList<>();
        equippedWeapon = null;
        equippedArmour = null;
        equippedShield = null;
        equippedHelmet = null;
        createdItems = new ArrayList<>();
        isSurvivalMode = false;
        isBerserkerMode = false;
        isConfusingMode = false;
        hasBoughtHealthPotion = false;
        hasBoughtProtection = false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Character getCharacter() {
        return character;
    }

    public List<Pair<Integer, Integer>> getOrderedPath() {
        return orderedPath;
    }

    public List<BasicEnemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<BasicEnemy> e) {
        enemies = e;
    }

    public List<Building> getBuildings() {
        return buildingEntities;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildingEntities = buildings;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getCyclesUntilShop() {
        return shopCycles - cyclesSinceShop;        
    }

    public List<Class<?>> getCreatedItems(){
        return createdItems;
    }

    public void setCreatedItems(List<Class<?>> createdItems){
        this.createdItems = createdItems;
    }

    public void clearCreatedItems() {
        createdItems = new ArrayList<>();
    }

    public List<Card> getCardEntities() {
        return cardEntities;
    }

    public CardFactory getCardFactory() {
        return cardFactory;
    }

    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    public List<AlliedSoldier> getAlliedSoldiers() {
        return alliedSoldiers;
    }
    
    public void setAlliedSoldiers(List<AlliedSoldier> soldiers) {
        this.alliedSoldiers = soldiers;
    }

    public int getTotalGold() {
        return character.getGold().getValue();
    }

    public void setTotalGold(int newGoldValue) {
        character.getGold().setValue(newGoldValue);
    }

    public List<Item> getUnequippedInventoryItems() {
        return unequippedInventoryItems;
    }

    public List<Item> getItemsOnPath() {
        return itemsOnPath;
    }

    public void setItemsOnPath(List<Item> items) {
        itemsOnPath = items;
    }
    
    public void setUnequippedInventoryItems(List<Item> inventory) {
        unequippedInventoryItems = inventory;
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public Armour getEquippedArmour() {
        return equippedArmour;
    }

    public Item getEquippedShield() {
        return equippedShield;
    }

    public Helmet getEquippedHelmet() {
        return equippedHelmet;
    }

    public List<CampfireBuilding> getCampfires() {
        List<CampfireBuilding> campfires = new ArrayList<>();
        for (Building b : buildingEntities) {
            if (b instanceof CampfireBuilding) {
                campfires.add((CampfireBuilding) b);
            }
        }
        return campfires;
    }

    public boolean getElanKilled() {
        return elanKilled;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }
    
    /**
     * add the hero castle to building entities
     */
    public void setHeroCastle() {
        buildingEntities.add(new HeroCastle(new SimpleIntegerProperty(character.getX()), new SimpleIntegerProperty(character.getY())));
    }

    /**
     * get the hero castle coordinates
     */
    public Pair<Integer, Integer> getHeroCastleCoords() {
        Pair<Integer, Integer> coords = null;
        for (Building b : buildingEntities) {
            if (b.getClass().equals(HeroCastle.class)) {
                coords = new Pair<Integer, Integer>(b.getX(), b.getY());
            }
        }
        return coords;
    }
    
    /**
     * Set the game mode of this game
     * @param mode
     */
    public void setMode(String mode) {
        if (mode.equals("survival")) {
            isSurvivalMode = true;
        } else if (mode.equals("berserker")) {
            isBerserkerMode = true;
        } else if (mode.equals("confusing")) {
            isConfusingMode = true;
        }
    }

    public String getMode() {
        if (isSurvivalMode) {
            return "survival";
        } else if (isBerserkerMode) {
            return "berserker";
        } else if (isConfusingMode) {
            return "confusing";
        } 
        return "standard";
    }
    
    public void setHasBoughtHealthPotion(boolean b) {
        hasBoughtHealthPotion = b;
    }

    public void setHasBoughtProtection(boolean b) {
        hasBoughtProtection = b;
    }

    public boolean getHasBoughtHealthPotion() {
        return hasBoughtHealthPotion;
    }

    public boolean getHasBoughtProtection() {
        return hasBoughtProtection;
    }

    public void setWorldGoal(LogicalExpression goal) {

        this.worldGoal = goal;
    }

    public LogicalExpression getWorldGoal() {

        return worldGoal;
    }

    public void setWorldGoalString(String goal) {

        this.worldGoalString = goal;
    }

    public String getWorldGoalString() {

        return worldGoalString;
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        // TODO = if more specialised types being added from main menu, add more methods like this with specific input types...
        nonSpecifiedEntities.add(entity);
    }

    /**
     * Spawns items on the path if the conditions warrant it 
     * @return list of items to be spawned
     */
    public List<Item> possiblySpawnItems() {
        List<Item> spawningItems = new ArrayList<>();
        // Create gold item
        Pair<Integer, Integer> goldPos = possiblyGetItemSpawnPosition();
        if (goldPos != null) {
            PathPosition pathPosition = new PathPosition(orderedPath.indexOf(goldPos), orderedPath);
            Gold gold = (Gold) spawnItem(Gold.class, pathPosition.getX(), pathPosition.getY());
            // Randomly decide gold amount
            int value = ((new Random().nextInt(90)) / 10)*10 + 10;
            if (value < 10) {
                value = 10;
            }
            gold.setValue(value);
            itemsOnPath.add(gold);
            spawningItems.add(gold);
        }
        // Create healthPotion item
        Pair<Integer, Integer> healthPotionPos = possiblyGetItemSpawnPosition();
        if (healthPotionPos != null) {
            PathPosition pathPosition = new PathPosition(orderedPath.indexOf(healthPotionPos), orderedPath);
            HealthPotion healthPotion = (HealthPotion) spawnItem(HealthPotion.class, pathPosition.getX(), pathPosition.getY());
            itemsOnPath.add(healthPotion);
            spawningItems.add(healthPotion);
        }
        return spawningItems;
    }

    /**
     * Spawns an item on the path of type itemClass
     * @param itemClass
     * @param x
     * @param y
     * @return newly spawned item
     */
    public Item spawnItem(Class<?> itemClass, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        if (itemClass.equals(Gold.class)) {
            return new Gold(x,y);
        } else if (itemClass.equals(HealthPotion.class)) {
            return new HealthPotion(x, y);
        } else {
            return null;
        }
    }

    /**
     * Get item spawn positions
     * @return spawn coordinates
     */
    private Pair<Integer, Integer> possiblyGetItemSpawnPosition() {
        // has a chance spawning a Gold or HealthPotion item on a tile the character isn't on or immediately before or after (currently space required = 2)...
        int choice = new Random().nextInt(40);
        
        if ((choice == 4)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }
            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(new Random().nextInt(orderedPathSpawnCandidates.size()));
            return spawnPosition;
        }
        return null;
    }

    /**
     * Adds item to characters inventory when it is
     * walked over. Then removes item from the path
     */
    public void pickUpItems() {
        Item item = checkCharacterOnItem();
        if (item instanceof Gold) {
            Gold gold = (Gold) item;
            setTotalGold(getTotalGold() + gold.getValue());
            itemsOnPath.remove(item);
            item.destroy();
        } else if (item instanceof HealthPotion) {
            createdItems.add(HealthPotion.class);
            itemsOnPath.remove(item);
            item.destroy();
        }
    }

    /**
     * Checks if a character has encountered
     * an item on the path
     * @return item encountered
     */
    public Item checkCharacterOnItem() {
        for (Item item : itemsOnPath) {
            if (item.getX() == character.getX() && item.getY() == character.getY()) {
                return item;
            }
        }
        return null;
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies(){
        List<BasicEnemy> spawningEnemies = new ArrayList<>();

        // Randomly spawn slugs
        Pair<Integer, Integer> slugPos = possiblyGetSlugSpawnPosition();
        if (slugPos != null){
            int indexInPath = orderedPath.indexOf(slugPos);
            Slug slug = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(slug);
            spawningEnemies.add(slug);
        }

        // Spawn vampires
        List<Pair<Integer, Integer>> vampirePos = getEnemyBuildingSpawnPosition(VampireCastleBuilding.class);
        if (vampirePos.size() != 0 && (cycles % 5 == 0 && cycles != 0) && !vampireHasSpawnedOnCycle) {
            for (Pair<Integer, Integer> pos : vampirePos) {
                int indexInPath = orderedPath.indexOf(pos);
                Vampire vampire = new Vampire(new PathPosition(indexInPath, orderedPath));
                enemies.add(vampire);
                spawningEnemies.add(vampire);
            }
            vampireHasSpawnedOnCycle = true;
        }

        // Spawn zombies
        List<Pair<Integer, Integer>> zombiePos = getEnemyBuildingSpawnPosition(ZombiePitBuilding.class);
        if (zombiePos.size() != 0 && cycles != 0 && !zombieHasSpawnedOnCycle) {
            for (Pair<Integer, Integer> pos : zombiePos) {
                int indexInPath = orderedPath.indexOf(pos);
                Zombie zombie = new Zombie(new PathPosition(indexInPath, orderedPath));
                enemies.add(zombie);
                spawningEnemies.add(zombie);
            }
            zombieHasSpawnedOnCycle = true;
        }

        // Spawn Doggie boss
        Pair<Integer, Integer> doggiePos = getBossSpawnPosition();
        if (cycles == 20 && doggieHasSpawnedOnCycle == false) {

            int indexInPath = orderedPath.indexOf(doggiePos);
            Doggie doggie = new Doggie(new PathPosition(indexInPath, orderedPath));
            enemies.add(doggie);
            spawningEnemies.add(doggie);
            doggieHasSpawnedOnCycle = true;
        }

        // Spawn Elan boss
        Pair<Integer, Integer> elanPos = getBossSpawnPosition();
        if (cycles >= 40 && character.getXp() >= 10000 && elanHasSpawnedOnCycle == false) {

            int indexInPath = orderedPath.indexOf(elanPos);
            ElanMuske elanMuske = new ElanMuske(new PathPosition(indexInPath, orderedPath));
            enemies.add(elanMuske);
            spawningEnemies.add(elanMuske);
            elanHasSpawnedOnCycle = true;
            fluctuateDoggieCoin(5000, 10000);
        }
        
        return spawningEnemies;
    }


    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(BasicEnemy enemy){
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * kill an allied soldier
     * @param alliedSoldier allied soldier to be killed
     */
    public void killAlliedSoldier(AlliedSoldier soldier) {
        soldier.destroy();
        alliedSoldiers.remove(soldier);
    }

    /**
     * Attempt to run battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles(boolean backend) {
        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        // Find if the character is within the radius of an enemy
        if (existsBattles()) {
            // Load enemies and allies that will participate in the battle
            List<BasicEnemy> battleEnemies = loadBattleEnemies();
            List<Entity> battleAllies = loadBattleAllies();
            // Allies and Enemies take turns doing battle until all enemies are defeated or character loses all health
            while (battleEnemies.size() > 0 && character.getHealth() > 0 && existsBattles(battleEnemies)) {
                List<BasicEnemy> tmp = runBattleTurn(battleEnemies, battleAllies);
                defeatedEnemies.addAll(tmp);
                battleEnemies.removeAll(tmp);
            }
        }
        for (BasicEnemy e : defeatedEnemies) {
            if (backend) battleRewards(e);
            killEnemy(e);
        }

        character.decreaseStun();

        return defeatedEnemies;
    }

    /**
     * Give the corresponding rewards to the character after
     * defeating an enemy in battle.
     * 1 random card, 1 random item, 1 health potion
     * 1% chance of The One Ring, 1% chance of Anduril, flame of the West, 1% chance of Tree Stump
     * @param enemy
     */
    public void battleRewards(BasicEnemy enemy) {
        loadCard(cardFactory.createRandomStaticEntity(this));
        addUnequippedItem(itemFactory.createRandomStaticEntity(this));
        int oneRingChoice = randOneRing.nextInt(100);
        if (oneRingChoice == 69 && !oneRingExists()) {
            addUnequippedItem(itemFactory.createStaticEntity(TheOneRing.class, this));
        }
        int andurilChoice = randAnduril.nextInt(100);
        if (andurilChoice == 42) {
            addUnequippedItem(itemFactory.createStaticEntity(Anduril.class, this));
        }
        int treeStumpChoice = randTreeStump.nextInt(100);
        if (treeStumpChoice == 9) {
            addUnequippedItem(itemFactory.createStaticEntity(TreeStump.class, this));
        }
        addUnequippedItem(itemFactory.createStaticEntity(HealthPotion.class, this));
        enemy.battleRewards(this);
    }

    /**
     * Determines if the one ring exists in the inventory
     * @return true if one ring exists, false otherwise
     */
    public boolean oneRingExists() {
        for (Item item : unequippedInventoryItems) {
            if (item instanceof TheOneRing) return true;
        }
        return false;
    }

    /**
     * Returns the one ring
     * @return true if one ring exists, false otherwise
     */
    public Item getOneRing() {
        for (Item item : unequippedInventoryItems) {
            if (item instanceof TheOneRing) return item;
        }
        return null;
    }

    /**
     * Destroys the one ring after being used
     */
    public void removeOneRing() {
        Item removeRing = getOneRing();
        removeRing.destroy();
        unequippedInventoryItems.remove(removeRing);
    }

    /**
     * Runs each battle round individually (each ally has one turn followed by each remaining enemy)
     * @param battleEnemies
     * @param battleAllies
     * @return
     */
    public List<BasicEnemy> runBattleTurn(List<BasicEnemy> battleEnemies, List<Entity> battleAllies) {
        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        for (Entity ally : battleAllies) {
            if (battleEnemies.size() > 0) {
                // Attacks the first available enemy - customizable - with damage depending on ally class
                BasicEnemy e = battleEnemies.get(0); 
                if (ally instanceof Character) {
                    Character c = (Character) ally;
                    c.attackEnemy(e, this);
                }  else if (ally instanceof AlliedSoldier) {
                    AlliedSoldier a = (AlliedSoldier) ally;
                    a.attackEnemy(e, this);
                } else if (ally instanceof TowerBuilding) {
                    TowerBuilding t = (TowerBuilding) ally;
                    t.attackEnemy(e, this);
                }
                // Kills enemies once defeated
                if (e.getHealthPoints() <= 0) {
                    defeatedEnemies.add(e);
                    battleEnemies.remove(e);
                    killEnemy(e);
                }
            }
        }
        // check that current enemies involved have character within their battle radius i.e. supporting enemies will stop attacking
        if (existsBattles(battleEnemies)) {
            for (BasicEnemy enemy : battleEnemies) {
                // Attacks alliedSoldiers first, then the character once all allied soldiers are dead
                if (alliedSoldiers.size() == 0) {
                    enemy.attack(character, equippedArmour, equippedShield, equippedHelmet, this);
                    // kills character once defeated
                    if (character.getHealth() <= 0) {
                        if (!oneRingExists()) { 
                            character.destroy(); 
                        } else {
                            TheOneRing ring = (TheOneRing) getOneRing();
                            ring.revive(character);
                            removeOneRing();
                        }
                        break;
                    }
                } else {
                    AlliedSoldier soldier = getAlliedSoldiers().get(0);
                    enemy.attackAlliedSoldier(soldier, this);
                    // Kills soldiers once defeated
                    if (soldier.getHealth() <= 0) {
                        battleAllies.remove(soldier);
                        killAlliedSoldier(soldier);
                    }
                } 
            }
        }

        for (BasicEnemy current : defeatedEnemies) {

            if (current instanceof Doggie) {

                doggieKilled = true;
                addUnequippedItem(itemFactory.createStaticEntity(DoggieCoin.class, this));
            }
            if (current instanceof ElanMuske) {

                elanKilled = true;
                fluctuateDoggieCoin(45,55);

            }
        }

        return defeatedEnemies;
    }

    /**
     * Finds all enemies where the character is within their battle or support radius
     * Called assuming a battle exists 
     * @return a list of enemies that will battle the character and its allies
     */
    public List<BasicEnemy> loadBattleEnemies() {
        List<BasicEnemy> battleEnemies = new ArrayList<>();
        for (BasicEnemy e : enemies) {
            if (inRadius(character, e, e.getBattleRadius()) || inRadius(character, e, e.getSupportRadius())){
                battleEnemies.add(e);                
            }
        }
        return battleEnemies;
    }

    /**
     * Finds all allies who can battle alongside the character or support the character if in range.
     * @return a list of Character, AlliedSoldier, or TowerBuilding Entities that will battle against the enemies
     */
    public List<Entity> loadBattleAllies() {
        List<Entity> battleAllies = new ArrayList<>();
        battleAllies.add(character);
        battleAllies.addAll(alliedSoldiers);
        for (Building b : buildingEntities) {
            if (b instanceof TowerBuilding) {
                TowerBuilding t = (TowerBuilding) b;
                if (inRadius(character, t, t.getSupportRadius())) {
                    battleAllies.add(b);
                }
            }
        }
        return battleAllies;
    }

    public boolean campfireInRange() {

        for (CampfireBuilding c : getCampfires()) {
            // replace 3 with c.getRadius()
            if (inRadius(character, c, 3)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks whether the character is within the given entity's given circular radius
     * @param character
     * @param e
     * @param radius
     * @return true or false
     */
    public boolean inRadius(Entity e1, Entity e2, int radius) {
        // Pythagoras: a^2+b^2 < radius^2 to see if character is within the radius of an entity
        if (Math.pow((e1.getX()-e2.getX()), 2) +  Math.pow((e1.getY()-e2.getY()), 2) <= Math.pow(radius,2)) {
            return true;
        }
        return false;
    }

    /**
     * Checks within the world state whether there is an enemy who has the character within its battle radius
     * @return true or false
     */
    public boolean existsBattles() {
        for (BasicEnemy e : enemies) {
            if (inRadius(character, e, e.getBattleRadius())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks within the world state whether there is an enemy who has the character within its battle radius
     * @return true or false
     */
    public boolean existsBattles(List<BasicEnemy> eList) {
        for (BasicEnemy e : eList) {
            if (inRadius(character, e, e.getBattleRadius())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is called before a new card is created
     * to check if there is room for the new card.
     * Removes the oldest card if not.
     */
    public void cardEntitiesFull () {
        if (cardEntities.size() >= getWidth()){
            Character character = getCharacter();
            setTotalGold(getTotalGold() + 10);
            character.setXp(character.getXp() + 100);
            createdItems.add(this.getClass());
            createdItems.add(HealthPotion.class);
            removeCard(0);
        }
    }

    /**
     * A generalised loadCard function that takes in a card object
     * and adds it to the world. Returns the card entity for controller use
     * @param card
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(Card card) {
        cardEntities.add(card);
        return card;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index){
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * Add an item to the world given an item object
     * @param newItem
     * @return an item to be spawned in the controller as a JavaFX node
     */
    public Item addUnequippedItem(Item newItem){
        unequippedInventoryItems.add(newItem);
        return newItem;
    }

    /**
     * Checks if inventory is full before adding a new item.
     * If full, remove oldest item
     * @return first available slot in inventory
     */
    public Pair<Integer, Integer> itemEntitiesFull () {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            Character character = getCharacter();
            setTotalGold(getTotalGold() + 10);
            character.setXp(character.getXp() + 100);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        return firstAvailableSlot;
    }

    /**
     * Purchase an item from the Hero's Castle
     * @param itemType
     * @param price
     * @return purchased item
     */
    public Item buyItem(Class<?> itemType){
        if (itemType.equals(HealthPotion.class) && isSurvivalMode && hasBoughtHealthPotion) return null;
        if ((itemType.equals(Armour.class) || itemType.equals(Shield.class) || itemType.equals(Helmet.class)) && 
            isBerserkerMode && hasBoughtProtection) return null;
        Item newItem = itemFactory.createStaticEntity(itemType, this);
        if (getTotalGold() < newItem.getPurchasePrice()) {
            return null;
        }
        addUnequippedItem(newItem);
        if (newItem != null) {
            setTotalGold(getTotalGold() - newItem.getPurchasePrice());
            if (newItem instanceof HealthPotion) hasBoughtHealthPotion = true;
            if (newItem instanceof Armour || 
                newItem instanceof Shield ||
                newItem instanceof Helmet) hasBoughtProtection = true;
        }
        
        return newItem;
    }
    
    /**
     * Sell an item in exchange for gold
     * @param item
     */
    public void sellItem(Class<?> itemType) {
        if (getTotalGold() < 99999) {     
            Item item = getFirstItem(itemType);
            if (item != null) {
                setTotalGold(getTotalGold() + item.getSellPrice());
                removeUnequippedInventoryItem(item);
            }
        }
    }

    public Item getFirstItem(Class<?> itemType) {
        for (Item i : unequippedInventoryItems) {
            if (i.getClass().equals(itemType)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Checks whether the given item is in the unequipped inventory 
     * @param item
     * @return true or false
     */
    public boolean checkItemInUnequippedInventory(Class<?> itemType) {
        for (Item i : unequippedInventoryItems) {
            if (i.getClass().equals(itemType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * add an allied soldier after passing through barracks.
     * Spawn it in the world and return the allied soldier entity
     * @return an allied soldier to be spawned in the controller
     */
    public AlliedSoldier addAlliedSoldier() {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForAlly();
        if (firstAvailableSlot == null) {
            removeAllyByPosition(0);
            firstAvailableSlot = getFirstAvailableSlotForAlly();
            setTotalGold(getTotalGold() + 10);
            character.setXp(character.getXp() + 100);
            createdItems.add(this.getClass());
            createdItems.add(HealthPotion.class);
        }
        AlliedSoldier alliedSoldier = new AlliedSoldier(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), 
                                                        new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        alliedSoldiers.add(alliedSoldier);
        return alliedSoldier;
    }

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    public Item equipWeapon(int initialX, int initialY, int newX, int newY) {
        Entity unequippedWeapon = getUnequippedInventoryItemEntityByCoordinates(initialX, initialY);
        if (equippedWeapon != null) {
            equippedWeapon.destroy();
        }

        if (unequippedWeapon instanceof Sword) {
            equippedWeapon = new Sword(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        } else if (unequippedWeapon instanceof Stake) {
            equippedWeapon = new Stake(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        } else if (unequippedWeapon instanceof Staff) {
            equippedWeapon = new Staff(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        } else if (unequippedWeapon instanceof Anduril) {
            equippedWeapon = new Anduril(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        }
        removeUnequippedInventoryItem(unequippedWeapon);
        return equippedWeapon;
    }

    public Armour equipArmour(int initialX, int initialY, int newX, int newY) {
        Entity unequippedArmour = getUnequippedInventoryItemEntityByCoordinates(initialX, initialY);
        if (equippedArmour != null) {
            equippedArmour.destroy();
        }
        equippedArmour = new Armour(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        removeUnequippedInventoryItem(unequippedArmour);
        return equippedArmour;
    }

    public Item equipShield(int initialX, int initialY, int newX, int newY) {
        Entity unequippedShield = getUnequippedInventoryItemEntityByCoordinates(initialX, initialY);
        if (equippedShield != null) {
            equippedShield.destroy();
        }

        if (unequippedShield instanceof Shield) {
            equippedShield = new Shield(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        } else if (unequippedShield instanceof TreeStump) {
            equippedShield = new TreeStump(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        }
        removeUnequippedInventoryItem(unequippedShield);
        return equippedShield;
    }

    public Helmet equipHelmet(int initialX, int initialY, int newX, int newY) {
        Entity unequippedHelmet = getUnequippedInventoryItemEntityByCoordinates(initialX, initialY);
        if (equippedHelmet != null) {
            equippedHelmet.destroy();
        }
        equippedHelmet = new Helmet(new SimpleIntegerProperty(newX), new SimpleIntegerProperty(newY));
        removeUnequippedInventoryItem(unequippedHelmet);
        return equippedHelmet;
    }
    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        character.moveDownPath();
        character.decreaseStun();
        checkCharacterOnBuilding();
        pickUpItems();
        moveBasicEnemies();
        fluctuateDoggieCoin();
        if (character.isAtStart()) {
            cycles++;
            cyclesSinceShop++;
            vampireHasSpawnedOnCycle = false;
            zombieHasSpawnedOnCycle = false;
        }
    }

    /**
     * fluctuate doggieCoin with the preexisting range
     * @param value
     */
    public void fluctuateDoggieCoin() {
        for (Item item : unequippedInventoryItems) {
            if (item instanceof DoggieCoin) {
                DoggieCoin doggieCoin = (DoggieCoin) item;
                doggieCoin.fluctuateValue(this.cycles);
            }
        }
    }

    /**
     * Method overload to fluctuate doggieCoin with a specific amplitude and intercept
     * E.g. amp = 50 and int = 50 will make it fluctuate between 0 and 100, starting at 50
     * @param value
     */
    public void fluctuateDoggieCoin(int amplitude, int intercept) {
        for (Item item : unequippedInventoryItems) {
            if (item instanceof DoggieCoin) {
                DoggieCoin doggieCoin = (DoggieCoin) item;
                doggieCoin.setAmplitude(amplitude);
                doggieCoin.setIntercept(intercept);
                doggieCoin.fluctuateValue(this.cycles);
            }
        }
    }

    public boolean checkForShop() {
        if (cyclesSinceShop == shopCycles) {
            cyclesSinceShop = 0;
            shopCycles += 1;
            hasBoughtHealthPotion = false;
            hasBoughtProtection = false;
            return true;
        }
        return false;
    }

    /**
     * check if the character is on a path tile containg
     * a building, so the corresponding benefits can be granted
     */
    public void checkCharacterOnBuilding() {
        PathPosition pos = character.getPosition();
        for (Building building : buildingEntities) {
            if (building.getX() == pos.getX().get() && building.getY() == pos.getY().get()) {
                building.characterOnBuilding(this, character);
            }
        }
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    public void removeUnequippedInventoryItem(Entity item){
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y){
        for (Entity e: unequippedInventoryItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * return an allied soldier by x and y coordinates
     * assumes that no 2 soldiers share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return allied soldier at the input position
     */
    private AlliedSoldier getAlliedSoldierByCoordinates(int x, int y) {
        for (AlliedSoldier a : alliedSoldiers) {
            if ((a.getX() == x) && (a.getY() == y)) {
                return a;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index){
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * remove allied soldier at a particular index in the allied soldiers list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeAllyByPosition(int index) {
        AlliedSoldier alliedSoldier = alliedSoldiers.get(index);
        int x = alliedSoldier.getX();
        alliedSoldier.destroy();
        alliedSoldiers.remove(0);
        shiftAlliesDownFromXCoordinate(x);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */
    public Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y=0; y<unequippedInventoryHeight; y++){
            for (int x=0; x<unequippedInventoryWidth; x++){
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }
    
    /**
     * get the first pair of x,y coordinates which don't have an allied soldier
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForAlly() {
        for (int x = 0; x < unequippedInventoryWidth; x++) {
            if (getAlliedSoldierByCoordinates(x, 0) == null) {
                return new Pair<Integer, Integer>(x, 0);
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card c: cardEntities){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    /**
     * shift allied soldier coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftAlliesDownFromXCoordinate(int x) {
        for (AlliedSoldier a : alliedSoldiers) {
            if (a.getX() >= x) {
                a.x().set(a.getX() - 1);
            }
        }
    }

    /**
     * move all enemies
     */
    public void moveBasicEnemies() {
        // check if traps exist
        List<TrapBuilding> traps = new ArrayList<>();
        for (Building b : buildingEntities) {
            if (b instanceof TrapBuilding) {
                traps.add((TrapBuilding) b);
            }
        }
        List<BasicEnemy> deadTrappedEnemies = new ArrayList<BasicEnemy>();
        for (BasicEnemy e: enemies){
            if (e instanceof Vampire) {

                Vampire vampire = (Vampire) e;
                vampire.move(this);
            }
            else {
                e.move();
            }
            for (TrapBuilding trap : traps) {
                if (e.getX() == trap.getX() && e.getY() == trap.getY()) {
                    e.setHealthPoints(e.getHealthPoints() - 50);
                    trap.destroy();
                    buildingEntities.remove(trap);
                    if (e.getHealthPoints() <= 0) {
                        deadTrappedEnemies.add(e);
                    }
                }
            }
        }
        for (BasicEnemy e : deadTrappedEnemies) {
            killEnemy(e);
        }
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * @return null if random choice is that wont be spawning an enemy or it isn't possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetSlugSpawnPosition(){
        // TODO = modify this
        
        // has a chance spawning a basic enemy on a tile the character isn't on or immediately before or after (currently space required = 2)...
        int choice = rand.nextInt(10); // TODO = change based on spec... currently low value for dev purposes...
        // TODO = change based on spec
        if ((choice == 0)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    private Pair<Integer, Integer> getBossSpawnPosition(){
        
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        // inclusive start and exclusive end of range of positions not allowed
        int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 3)%orderedPath.size();
        // note terminating condition has to be != rather than < since wrap around...
        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // choose random choice
        Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

        return spawnPosition;
    }

    /**
     * Get position directly next to enemy building that lies on a path.
     * If multiple randomly pick one
     * @return coordinate pair of spawn point
     */
    private List<Pair<Integer, Integer>> getEnemyBuildingSpawnPosition(Class<?> buildingClass){
        List<Pair<Integer, Integer>> spawnPointCandidates = new ArrayList<>();
        // For each building of type passed into function, get a random adjacent path
        // to spawn the relevant enemy
        for (Building b : buildingEntities) {
            if (b.getClass().equals(buildingClass)) {
                List<Pair<Integer, Integer>> adjacent = adjacentPaths(b.getX(), b.getY());
                Random rand = new Random();
                spawnPointCandidates.add(adjacent.get(rand.nextInt(adjacent.size())));
            }
        }

        return spawnPointCandidates;
    }

    /**
     * remove a card by its x, y coordinates
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)){
                card = c;
                break;
            }
        }
        
        // now spawn building but only add if it's placement is valid using placement strategy
        Building newBuilding = card.spawnBuilding(buildingNodeX, buildingNodeY);
        Pair<Integer, Integer> buildingCoords = new Pair<Integer, Integer>(buildingNodeX, buildingNodeY);
        List<Pair<Integer, Integer>> adjacent = adjacentPaths(buildingNodeX, buildingNodeY);
        if (card.isValidPlacement(buildingCoords, orderedPath, adjacent, buildingEntities)) {
            buildingEntities.add(newBuilding);
        } else {
            return null;
        }
        
        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    /**
     * Return a list of coordinates of all adjacent path tiles, 
     * given an x and y coordinate
     * @param x
     * @param y
     * @return list of pairs of integers
     */
    public List<Pair<Integer, Integer>> adjacentPaths(int x, int y) {
        List<Pair<Integer, Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i < orderedPath.size(); i++) {
            Pair<Integer, Integer> current = orderedPath.get(i);
            if (((current.getValue0() == x) && (current.getValue1() == y - 1)) || 
                ((current.getValue0() == x) && (current.getValue1() == y + 1)) || 
                ((current.getValue0() == x - 1) && (current.getValue1() == y)) || 
                ((current.getValue0() == x + 1) && (current.getValue1() == y))) {
                    adjacent.add(current);
            }
        }

        return adjacent;
    }

    public Boolean getBossesKilled() {

        if (doggieKilled == true && elanKilled == true) {

            return true;
        }

        return false;
    }
}
