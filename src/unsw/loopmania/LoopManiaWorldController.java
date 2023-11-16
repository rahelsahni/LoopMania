package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.EnumMap;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;

import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private GridPane shop;

    @FXML
    private GridPane sell;

    @FXML
    private HBox healthBar;

    @FXML
    private VBox rightMenu;

    @FXML
    private VBox gamemodeSelect;

    @FXML 
    private HBox gameInterface;

    @FXML
    private VBox gameOverScreen;

    @FXML
    private VBox goal;

    @FXML
    private Button gamemodeExitButton;

    @FXML
    private Button okButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button gameOverExitButton;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    private Timeline pauseTimeline;

    private Image vampireCastleCardImage;
    private Image zombiePitCardImage;
    private Image towerCardImage;
    private Image villageCardImage;
    private Image barracksCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;
    private Image slugImage;
    private Image vampireImage;
    private Image zombieImage;
    private Image doggieImage;
    private Image elanImage;
    private Image swordImage;
    private Image stakeImage;
    private Image staffImage;
    private Image armourImage;
    private Image shieldImage;
    private Image goldImage;
    private Image helmetImage;
    private Image healthPotionImage;
    private Image heartImage;
    private Image heroCastleImage;
    private Image vampireCastleBuildingImage;
    private Image zombiePitBuildingImage;
    private Image towerBuildingImage;
    private Image villageBuildingImage;
    private Image barracksBuildingImage;
    private Image trapBuildingImage;
    private Image campfireBuildingImage;
    private Image theOneRingImage;
    private Image alliedSoldierImage;
    private Image andurilImage;
    private Image treeStumpImage;
    private Image doggieCoinImage;
    private Media gameplayMusic;
    MediaPlayer player;
    private MediaPlayer shopPlayer;
    private MediaPlayer mainMenuPlayer;
    private MediaPlayer goldSound = new MediaPlayer(new Media((new File("resources/sfx/gold.mp3")).toURI().toString()));
    private MediaPlayer shopCloseSound = new MediaPlayer(new Media((new File("resources/sfx/shopClose.mp3")).toURI().toString()));
    private MediaPlayer shopOpenSound = new MediaPlayer(new Media((new File("resources/sfx/shopOpen.mp3")).toURI().toString()));
    private MediaPlayer zombieDeathSound = new MediaPlayer(new Media((new File("resources/sfx/zombieDeath.mp3")).toURI().toString()));
    private MediaPlayer slugDeathSound = new MediaPlayer(new Media((new File("resources/sfx/slugDeath.mp3")).toURI().toString()));
    private MediaPlayer vampireDeathSound = new MediaPlayer(new Media((new File("resources/sfx/vampireDeath.mp3")).toURI().toString()));
    private MediaPlayer doggieDeathSound = new MediaPlayer(new Media((new File("resources/sfx/doggieDeath.mp3")).toURI().toString()));
    private MediaPlayer elanDeathSound = new MediaPlayer(new Media((new File("resources/sfx/elanDeath.mp3")).toURI().toString()));
    private MediaPlayer fireSound = new MediaPlayer(new Media((new File("resources/sfx/fire.mp3")).toURI().toString()));
    private MediaPlayer swordSound = new MediaPlayer(new Media((new File("resources/sfx/sword.mp3")).toURI().toString()));
    private MediaPlayer shieldSound = new MediaPlayer(new Media((new File("resources/sfx/shield.mp3")).toURI().toString()));
    private MediaPlayer armorSound = new MediaPlayer(new Media((new File("resources/sfx/armor.mp3")).toURI().toString()));
    private MediaPlayer staffSound = new MediaPlayer(new Media((new File("resources/sfx/staff.mp3")).toURI().toString()));
    private MediaPlayer barracksSound = new MediaPlayer(new Media((new File("resources/sfx/barracks.mp3")).toURI().toString()));
    private MediaPlayer towerSound = new MediaPlayer(new Media((new File("resources/sfx/tower.mp3")).toURI().toString()));
    private MediaPlayer villageSound = new MediaPlayer(new Media((new File("resources/sfx/village.mp3")).toURI().toString()));
    private MediaPlayer trapSound = new MediaPlayer(new Media((new File("resources/sfx/trap.mp3")).toURI().toString()));
    private MediaPlayer zombiePitSound = new MediaPlayer(new Media((new File("resources/sfx/zombiePit.mp3")).toURI().toString()));
    private MediaPlayer vampireCastleSound = new MediaPlayer(new Media((new File("resources/sfx/vampireCastle.mp3")).toURI().toString()));
    private MediaPlayer healthPotionSound = new MediaPlayer(new Media((new File("resources/sfx/healthPotion.mp3")).toURI().toString()));

    private ArrayList<ImageView> alliedSoldierList;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the building/item which should be dropped
    private ImageView currentlyDraggedImage;
    
    private StaticEntity currentlyDraggedEntity;

    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private Rectangle healthRectangle;

    private Label goldLabel;

    private Label experienceLabel;

    private Label pauseLabel;

    private Label cycleLabel;

    private Label cycleShopLabel;

    private Label gameOverLabel;

    private Label goalLabel;

    private Button doggieCoinSellButton;

    private double tickSpeed;

    private boolean gameOver;

    private boolean initialGoal;

    private String fontURL;

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        heroCastleImage = new Image((new File("src/images/heros_castle.png")).toURI().toString());
        slugImage = new Image((new File("src/images/slug.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        doggieImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanImage = new Image((new File("src/images/ElanMuske.png")).toURI().toString());
        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        goldImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        heartImage = new Image((new File("src/images/heart.png")).toURI().toString());
        healthPotionImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        vampireCastleBuildingImage = new Image((new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        zombiePitBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());
        alliedSoldierImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/treeStump.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin.png")).toURI().toString());
        currentlyDraggedImage = null;
        currentlyDraggedType = null;
        currentlyDraggedEntity = null;
        healthRectangle = new Rectangle(100, 32);
        goldLabel = new Label();
        experienceLabel = new Label(Integer.toString(0));
        alliedSoldierList = new ArrayList<ImageView>();
        cycleLabel = new Label("Cycles: 0");
        cycleShopLabel = new Label("Next shop: 1");
        tickSpeed = 0.3;
        gameOver = false;
        initialGoal = true;
        fontURL = "file:resources/fonts/antiquity-print.ttf";

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {
        // TODO = load more images/entities during initialization

        Font font = Font.loadFont(fontURL, 16);

        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (including all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        anchorPaneRoot.setBackground(new Background((new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        // create gamemode select screen

        Button standardButton = new Button("Standard");
        standardButton.setFont(Font.loadFont(fontURL, 12));
        standardButton.setPrefSize(128, 32);
        Label standardLabel = new Label("The standard experience");
        standardLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        standardLabel.setPadding(new Insets(8, 0, 24, 0));
        standardLabel.setFont(font);

        Button survivalButton = new Button("Survival");
        survivalButton.setFont(Font.loadFont(fontURL, 12));
        survivalButton.setPrefSize(128, 32);
        Label survivalLabel = new Label("Can only buy 1 health\n     potion per shop");
        survivalLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        survivalLabel.setPadding(new Insets(8, 0, 24, 0));
        survivalLabel.setFont(font);

        Button berserkerButton = new Button("Berserker");
        berserkerButton.setFont(Font.loadFont(fontURL, 12));
        berserkerButton.setPrefSize(128, 32);
        Label berserkerLabel = new Label("  Can only buy 1 piece of\nprotective gear per shop");
        berserkerLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        berserkerLabel.setPadding(new Insets(8, 0, 24, 0));
        berserkerLabel.setFont(font);

        Button confusingButton = new Button("Confusing");
        confusingButton.setFont(Font.loadFont(fontURL, 12));
        confusingButton.setPrefSize(128, 32);
        Label confusingLabel = new Label("Rare items also have the effect\n    of another random rare item");
        confusingLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        confusingLabel.setPadding(new Insets(8, 0, 24, 0));
        confusingLabel.setFont(font);
        
        EventHandler<ActionEvent> standardEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                startGame();
            }
        };

        EventHandler<ActionEvent> survivalEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.setMode("survival");
                startGame();
            }
        };

        EventHandler<ActionEvent> berserkerEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.setMode("berserker");
                startGame();
            }
        };

        EventHandler<ActionEvent> confusingEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.setMode("confusing");
                startGame();
            }
        };

        standardButton.setOnAction(standardEvent);
        survivalButton.setOnAction(survivalEvent);
        berserkerButton.setOnAction(berserkerEvent);
        confusingButton.setOnAction(confusingEvent);

        gamemodeSelect.getChildren().add(0, standardButton);
        gamemodeSelect.getChildren().add(1, standardLabel);
        gamemodeSelect.getChildren().add(2, survivalButton);
        gamemodeSelect.getChildren().add(3, survivalLabel);
        gamemodeSelect.getChildren().add(4, berserkerButton);
        gamemodeSelect.getChildren().add(5, berserkerLabel);
        gamemodeSelect.getChildren().add(6, confusingButton);
        gamemodeSelect.getChildren().add(7, confusingLabel);
        gameInterface.setVisible(false);

        // create goal screen
        font = Font.loadFont(fontURL, 24);
        goalLabel = new Label(world.getWorldGoalString());
        goalLabel.setFont(font);
        goalLabel.setWrapText(true);
        goalLabel.setMaxWidth(300);
        goalLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        goal.getChildren().add(0, goalLabel);
        goal.setVisible(false);

        // create the pause indicator
        font = Font.loadFont(fontURL, 20);
        pauseLabel = new Label("PAUSED");
        pauseLabel.setFont(font);
        pauseLabel.setPadding(new Insets(0, 0, 0, 6));
        pauseLabel.setVisible(false);
        pauseLabel.setTextFill(Paint.valueOf("#FF0000"));
        pauseLabel.setEffect(new Glow());
        rightMenu.getChildren().add(3, pauseLabel);
        
        // create the health bar
        healthBar.getChildren().add(new ImageView(heartImage));
        StackPane healthMeter = new StackPane();
        healthMeter.getChildren().add(new Rectangle(100, 32, Paint.valueOf("#000000")));
        healthRectangle.setFill(Paint.valueOf("#00FF00"));
        healthMeter.getChildren().add(healthRectangle);
        healthBar.getChildren().add(healthMeter);
        healthBar.setAlignment(Pos.CENTER_LEFT);
        healthMeter.setAlignment(Pos.CENTER_LEFT);

        // create the gold count
        font = Font.loadFont(fontURL, 24);
        goldLabel = new Label("0");
        goldLabel.setAlignment(Pos.CENTER_LEFT);
        goldLabel.setFont(font);
        goldLabel.setPadding(new Insets(-10, 0, 0, 12));
        goldLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        rightMenu.getChildren().add(new HBox(new ImageView(goldImage), goldLabel));

        // create the experience count
        font = Font.loadFont(fontURL, 16);
        Label experienceIcon = new Label("XP");
        experienceIcon.setAlignment(Pos.CENTER_LEFT);
        experienceIcon.setFont(font);
        experienceIcon.setTextFill(Paint.valueOf("#00FF00"));
        font = Font.loadFont(fontURL, 24);
        experienceLabel.setAlignment(Pos.CENTER_LEFT);
        experienceLabel.setFont(font);
        experienceLabel.setPadding(new Insets(-13, 0, 0, 10));
        experienceLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        HBox experienceBox = new HBox(experienceIcon, experienceLabel);
        rightMenu.getChildren().add(experienceBox);

        // create the cycle counter
        font = Font.loadFont(fontURL, 14);
        cycleLabel.setAlignment(Pos.CENTER_LEFT);
        cycleLabel.setFont(font);
        cycleLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        cycleLabel.setPadding(new Insets(-5, 0, 0, 6));
        cycleShopLabel.setAlignment(Pos.CENTER_LEFT);
        cycleShopLabel.setFont(font);
        cycleShopLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        cycleShopLabel.setPadding(new Insets(0, 0, 6, 6));
        rightMenu.getChildren().add(cycleLabel);
        rightMenu.getChildren().add(cycleShopLabel);

        // create speed button
        Button speedButton = new Button("Fast Speed");
        speedButton.setFont(Font.loadFont(fontURL, 10));
        speedButton.setPrefWidth(132);

        EventHandler<ActionEvent> speedEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if (speedButton.getText().equals("Fast Speed")) {
                    tickSpeed = 0.15;
                    speedButton.setText("Regular Speed");
                } else {
                    tickSpeed = 0.3;
                    speedButton.setText("Fast Speed");
                }
                if (!isPaused) {
                    timeline.stop();
                    startTimer();
                }
                squares.requestFocus();
            }
        };

        speedButton.setOnAction(speedEvent);

        rightMenu.getChildren().add(speedButton);

        // create the goal button
        Button goalButton = new Button("Goal");
        goalButton.setFont(Font.loadFont(fontURL, 10));
        goalButton.setPrefWidth(132);

        EventHandler<ActionEvent> goalEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                goal.setVisible(true);
                gameInterface.setVisible(false);
                pause();
            }
        };

        goalButton.setOnAction(goalEvent);

        rightMenu.getChildren().add(goalButton);

        // create the allied soldier section
        GridPane alliedSoldiers = new GridPane();
        for(int i = 0; i < 4; i+= 1) {
            ImageView alliedSoldier = new ImageView(alliedSoldierImage);
            alliedSoldierList.add(alliedSoldier);
            alliedSoldier.setVisible(false);
            alliedSoldiers.add(alliedSoldier, i, 0);
        }
        //alliedSoldiers.setPadding(new Insets(14, 0, 0, 0));
        rightMenu.getChildren().add(alliedSoldiers);

        // setup the shop
        shop.setVisible(false);
        sell.setVisible(false);

        font = Font.loadFont(fontURL, 12);
        Button switchToSellButton = new Button("Switch to Sell Menu");
        switchToSellButton.setFont(font);
        switchToSellButton.setPrefSize(256, 64);
        Button buyButton1 = new Button("200 Gold", new ImageView(swordImage));
        buyButton1.setFont(font);
        buyButton1.setPrefSize(128, 64);
        Button buyButton2 = new Button("300 Gold", new ImageView(stakeImage));
        buyButton2.setFont(font);
        buyButton2.setPrefSize(128, 64);
        Button buyButton3 = new Button("300 Gold", new ImageView(staffImage));
        buyButton3.setFont(font);
        buyButton3.setPrefSize(128, 64);
        Button buyButton4 = new Button("400 Gold", new ImageView(armourImage));
        buyButton4.setFont(font);
        buyButton4.setPrefSize(128, 64);
        Button buyButton5 = new Button("400 Gold", new ImageView(shieldImage));
        buyButton5.setFont(font);
        buyButton5.setPrefSize(128, 64);
        Button buyButton6 = new Button("300 Gold", new ImageView(helmetImage));
        buyButton6.setFont(font);
        buyButton6.setPrefSize(128, 64);
        Button buyButton7 = new Button("100 Gold", new ImageView(healthPotionImage));
        buyButton7.setFont(font);
        buyButton7.setPrefSize(128, 64);
        Button buyButton8 = new Button("Leave");
        buyButton8.setFont(font);
        buyButton8.setPrefSize(128, 64);

        Button switchToBuyButton = new Button("Switch to Buy Menu");
        switchToBuyButton.setFont(font);
        switchToBuyButton.setPrefSize(256, 64);
        Button sellButton1 = new Button("100 Gold", new ImageView(swordImage));
        sellButton1.setFont(font);
        sellButton1.setPrefSize(128, 64);
        Button sellButton2 = new Button("150 Gold", new ImageView(stakeImage));
        sellButton2.setFont(font);
        sellButton2.setPrefSize(128, 64);
        Button sellButton3 = new Button("150 Gold", new ImageView(staffImage));
        sellButton3.setFont(font);
        sellButton3.setPrefSize(128, 64);
        Button sellButton4 = new Button("200 Gold", new ImageView(armourImage));
        sellButton4.setFont(font);
        sellButton4.setPrefSize(128, 64);
        Button sellButton5 = new Button("200 Gold", new ImageView(shieldImage));
        sellButton5.setFont(font);
        sellButton5.setPrefSize(128, 64);
        Button sellButton6 = new Button("150 Gold", new ImageView(helmetImage));
        sellButton6.setFont(font);
        sellButton6.setPrefSize(128, 64);
        Button sellButton7 = new Button("50 Gold", new ImageView(healthPotionImage));
        sellButton7.setFont(font);
        sellButton7.setPrefSize(128, 64);
        Button sellButton8 = new Button("1000 Gold", new ImageView(theOneRingImage));
        sellButton8.setFont(font);
        sellButton8.setPrefSize(128, 64);
        Button sellButton9 = new Button("500 Gold", new ImageView(andurilImage));
        sellButton9.setFont(font);
        sellButton9.setPrefSize(128, 64);
        Button sellButton10 = new Button("500 Gold", new ImageView(treeStumpImage));
        sellButton10.setFont(font);
        sellButton10.setPrefSize(128, 64);
        doggieCoinSellButton = new Button("? Gold", new ImageView(doggieCoinImage));
        doggieCoinSellButton.setFont(font);
        doggieCoinSellButton.setPrefSize(128, 64);
        Button sellButton12 = new Button("Leave");
        sellButton12.setFont(font);
        sellButton12.setPrefSize(128, 64);


        EventHandler<ActionEvent> switchShopEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if (shop.isVisible()) {
                    shop.setVisible(false);
                    sell.setVisible(true);
                } else {
                    shop.setVisible(true);
                    sell.setVisible(false);
                }
            }
        };

        EventHandler<ActionEvent> buyEvent1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Sword.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> buyEvent2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Stake.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> buyEvent3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Staff.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> buyEvent4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Armour.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };
        
        EventHandler<ActionEvent> buyEvent5 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Shield.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> buyEvent6 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(Helmet.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> buyEvent7 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                onLoad(world.buyItem(HealthPotion.class), false);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> leaveShopEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                closeShop();
                shopCloseSound.stop();
                shopCloseSound.setVolume(0.2);
                shopCloseSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Sword.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Stake.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Staff.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Armour.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };
        
        EventHandler<ActionEvent> sellEvent5 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Shield.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent6 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Helmet.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent7 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(HealthPotion.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent8 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // world.sellItem(TheOneRing.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };
        EventHandler<ActionEvent> sellEvent9 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(Anduril.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent10 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(TreeStump.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        EventHandler<ActionEvent> sellEvent11 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                world.sellItem(DoggieCoin.class);
                checkShop();
                updateStats();
                goldSound.stop();
                goldSound.play();
            }
        };

        switchToSellButton.setOnAction(switchShopEvent);
        buyButton1.setOnAction(buyEvent1);
        buyButton2.setOnAction(buyEvent2);
        buyButton3.setOnAction(buyEvent3);
        buyButton4.setOnAction(buyEvent4);
        buyButton5.setOnAction(buyEvent5);
        buyButton6.setOnAction(buyEvent6);
        buyButton7.setOnAction(buyEvent7);
        buyButton8.setOnAction(leaveShopEvent);

        switchToBuyButton.setOnAction(switchShopEvent);
        sellButton1.setOnAction(sellEvent1);
        sellButton2.setOnAction(sellEvent2);
        sellButton3.setOnAction(sellEvent3);
        sellButton4.setOnAction(sellEvent4);
        sellButton5.setOnAction(sellEvent5);
        sellButton6.setOnAction(sellEvent6);
        sellButton7.setOnAction(sellEvent7);
        sellButton8.setOnAction(sellEvent8);
        sellButton9.setOnAction(sellEvent9);
        sellButton10.setOnAction(sellEvent10);
        doggieCoinSellButton.setOnAction(sellEvent11);
        sellButton12.setOnAction(leaveShopEvent);

        shop.add(switchToSellButton, 0, 0, 2, 1);
        shop.add(buyButton1, 0, 1);
        shop.add(buyButton2, 0, 2);
        shop.add(buyButton3, 0, 3);
        shop.add(buyButton4, 0, 4);
        shop.add(buyButton5, 1, 1);
        shop.add(buyButton6, 1, 2);
        shop.add(buyButton7, 1, 3);
        shop.add(buyButton8, 1, 4);

        sell.add(switchToBuyButton, 0, 0, 2, 1);
        sell.add(sellButton1, 0, 1);
        sell.add(sellButton2, 0, 2);
        sell.add(sellButton3, 0, 3);
        sell.add(sellButton4, 0, 4);
        sell.add(sellButton5, 1, 1);
        sell.add(sellButton6, 1, 2);
        sell.add(sellButton7, 1, 3);
        sell.add(sellButton8, 1, 4);
        sell.add(sellButton9, 0, 5);
        sell.add(sellButton10, 0, 6);
        sell.add(doggieCoinSellButton, 1, 5);
        sell.add(sellButton12, 1, 6);

        // setup gameOverScreen
        font = Font.loadFont(fontURL, 48);
        gameOverLabel = new Label("VICTORY");
        gameOverLabel.setAlignment(Pos.CENTER);
        gameOverLabel.setFont(font);
        gameOverLabel.setTextFill(Paint.valueOf("#00FF00"));
        gameOverLabel.setPadding(new Insets(0, 0, 0, 10));
        gameOverScreen.getChildren().add(0, gameOverLabel);
        gameOverScreen.setVisible(false);

        // set button fonts
        font = Font.loadFont(fontURL,12);
        gamemodeExitButton.setFont(font);
        okButton.setFont(font);
        gameOverExitButton.setFont(font);
        font = Font.loadFont(fontURL,10);
        exitButton.setFont(font);
        exitButton.setPrefWidth(132);

    }

    /**
     * create and run the timer
     */
    public void startTimer(){
        // TODO = handle more aspects of the behaviour required by the specification
        
        if (gameOver || (goal.isVisible() && initialGoal == false)) {
            return;
        }

        if (shop.isVisible() || sell.isVisible()) {
            if (pauseTimeline != null && pauseTimeline.getStatus().equals(Status.STOPPED)) {
                pauseTimeline.play();
            }
            return;
        }

        System.out.println("starting timer");
        
        isPaused = false;

        if (pauseTimeline != null) {
            pauseTimeline.stop();
        }

        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(tickSpeed), event -> {
            world.runTickMoves();
            if (world.getWorldGoal().evaluate(world)) {
                victory();
                return;
            }
            if (world.checkForShop()) {
                openShop();
                shopOpenSound.stop();
                shopOpenSound.setVolume(0.2);
                shopOpenSound.play();
            }
            updateHealth();
            updateStats();
            updateCycles();
            updateAlliedSoldiers();
            if (world.getCreatedItems() != null) {
                for (Class<?> item: world.getCreatedItems()) {
                    if (item.equals(Sword.class)) {
                        loadSword();
                    } else if (item.equals(Stake.class)) {
                        loadStake();
                    } else if (item.equals(Staff.class)) {
                        loadStaff();
                    } else if (item.equals(Armour.class)) {
                        loadArmour();
                    } else if (item.equals(Shield.class)) {
                        loadShield();
                    } else if (item.equals(Helmet.class)) {
                        loadHelmet();
                    } else if (item.equals(HealthPotion.class)) {
                        loadHealthPotion();
                    } 
                }
                world.clearCreatedItems();
            }
            List<BasicEnemy> defeatedEnemies = world.runBattles(false);
            for (BasicEnemy e: defeatedEnemies){
                reactToEnemyDefeat(e);
                if (e instanceof Zombie) {
                    zombieDeathSound.stop();
                    zombieDeathSound.setVolume(0.1);
                    zombieDeathSound.play();
                }
                else if (e instanceof Slug) {
                    slugDeathSound.stop();
                    slugDeathSound.setVolume(0.2);
                    slugDeathSound.play();
                }
                else if (e instanceof Vampire) {
                    vampireDeathSound.stop();
                    vampireDeathSound.setVolume(0.1);
                    vampireDeathSound.play();
                }
                else if (e instanceof Doggie) {
                    doggieDeathSound.stop();
                    doggieDeathSound.setVolume(0.1);
                    doggieDeathSound.play();
                }
                else if (e instanceof ElanMuske) {
                    elanDeathSound.stop();
                    elanDeathSound.setVolume(0.2);
                    elanDeathSound.play();
                }
            }
            List<Item> newItems = world.possiblySpawnItems();
            for (Item item : newItems) {
                if (item.getClass().equals(HealthPotion.class)) {
                    onLoad((HealthPotion) item);
                } else {
                    onLoad((Gold) item);
                }
            }
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy: newEnemies){
                if (newEnemy.getClass().equals(Slug.class)) {
                    onLoad((Slug) newEnemy);
                } else if (newEnemy.getClass().equals(Vampire.class)) {
                    onLoad((Vampire) newEnemy);
                } else if (newEnemy.getClass().equals(Zombie.class)) {
                    onLoad((Zombie) newEnemy);
                } else if (newEnemy.getClass().equals(Doggie.class)) {
                    onLoad((Doggie) newEnemy);
                } else if (newEnemy.getClass().equals(ElanMuske.class)) {
                    onLoad((ElanMuske) newEnemy);
                }
            }
            printThreadingNotes("HANDLED TIMER");
        }));
        pauseLabel.setVisible(false);
        timeline.setCycleCount(Animation.INDEFINITE);
        if (!gamemodeSelect.isVisible()) {
            timeline.play();
        }
    }

    public void defeat() {
        gameOverLabel.setText("DEFEAT");
        gameOverLabel.setTextFill(Paint.valueOf("#FF0000"));
        stopMusic();
        playDefeatMusic();
        endGame();
    }

    public void victory() {
        gameOverLabel.setText("VICTORY");
        gameOverLabel.setTextFill(Paint.valueOf("#00FF00"));
        stopMusic();
        playVictoryMusic();
        endGame();
    }

    public void endGame() {
        gameOver = true;
        gameOverScreen.setVisible(true);
        gameInterface.setEffect(new GaussianBlur());
        timeline.stop();
        if (pauseTimeline != null) {
            pauseTimeline.stop();
        }
    }

    /**
     * create and run pause animatino timer
     */
    public void startPauseAnimationTimer(){
        System.out.println("starting pause animation");
        // trigger pause animation. JavaFX will target framerate of 1 second
        pauseTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            pauseLabel.setVisible(!pauseLabel.isVisible());
            printThreadingNotes("HANDLED PAUSE TIMER");
        }));
        pauseTimeline.setCycleCount(Animation.INDEFINITE);
        pauseTimeline.play();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        isPaused = true;
        pauseLabel.setVisible(true);
        System.out.println("pausing");
        if (timeline == null) {
            return;
        }
        timeline.stop();
        if (pauseTimeline == null || pauseTimeline.getStatus().equals(Status.STOPPED)) {
            startPauseAnimationTimer();
        } 
    }

    public void terminate(){
        pause();
    }

    public void startGame(){
        gamemodeSelect.setVisible(false);
        goal.setVisible(true);
        mainMenuPlayer.stop();
        playNormalMusic();
    }

    public void openShop(){
        world.setHasBoughtHealthPotion(false);
        world.setHasBoughtProtection(false);
        checkShop();
        pause();
        shop.setVisible(true);
        squares.setVisible(false);
        cards.setVisible(false);
        player.pause();
        playShopMusic();
    }

    public void closeShop(){
        shop.setVisible(false);
        sell.setVisible(false);
        squares.setVisible(true);
        cards.setVisible(true);
        squares.requestFocus();
        shopPlayer.stop();
        player.play();
    }

    public void updateHealth(){
        double healthPoints = world.getCharacter().getHealth()/2.5;
        
        if (world.getCharacter().getHealth() <= 0) {
            healthRectangle.setWidth(0);
            defeat();
            return;
        }

        if (healthPoints <= 1) {
            healthPoints = 1;
        }
        int colourVal = (int) Math.round(healthPoints);
        String colourString = "";
        String gradientString = "";
        if (colourVal > 50) {
            gradientString = Integer.toHexString((int) Math.round((100 - colourVal) * 2 * 2.55));
            if (gradientString.length() == 1) {
                colourString = "#0" + gradientString + "FF00";
            } else {
                colourString = "#" + gradientString + "FF00";
            }
        } else {
            gradientString = Integer.toHexString((int) Math.round(colourVal * 2 * 2.55));
            if (gradientString.length() == 1) {
                colourString = "#FF0" + gradientString + "00";
            } else {
                colourString = "#FF" + gradientString + "00";
            }
        }
        healthRectangle.setFill(Paint.valueOf(colourString));
        healthRectangle.setWidth(healthPoints);
    }

    public void updateStats(){
        goldLabel.setText(Integer.toString(world.getTotalGold()));
        experienceLabel.setText(Integer.toString(world.getCharacter().getXp()));
    }

    public void updateCycles(){
        cycleLabel.setText("Cycles: " + world.getCycles());
        if (shop.isVisible() || sell.isVisible()) {
            cycleShopLabel.setText("Next shop: 0");
        } else {
            cycleShopLabel.setText("Next shop: " + world.getCyclesUntilShop());
        }
        if (player.getStatus().equals(MediaPlayer.Status.STOPPED)) {
            playNormalMusic();
        }
    }

    public void checkShop(){
        int gold = world.getTotalGold();
        shop.getChildren().get(1).setDisable(false);
        shop.getChildren().get(2).setDisable(false);
        shop.getChildren().get(3).setDisable(false);
        shop.getChildren().get(4).setDisable(false);
        shop.getChildren().get(5).setDisable(false);
        shop.getChildren().get(6).setDisable(false);
        shop.getChildren().get(7).setDisable(false);
        
        if (world.getMode().equals("survival") && world.getHasBoughtHealthPotion()) {
            shop.getChildren().get(7).setDisable(true);
        }
        if (world.getMode().equals("berserker") && world.getHasBoughtProtection()) {
            shop.getChildren().get(4).setDisable(true);
            shop.getChildren().get(5).setDisable(true);
            shop.getChildren().get(6).setDisable(true);
        }

        if (gold < 400) {
            shop.getChildren().get(4).setDisable(true);
            shop.getChildren().get(5).setDisable(true);
        }
        if (gold < 300) {
            shop.getChildren().get(2).setDisable(true);
            shop.getChildren().get(3).setDisable(true);
            shop.getChildren().get(6).setDisable(true);
        }
        if (gold < 200) {
            shop.getChildren().get(1).setDisable(true);
        }
        if (gold < 100) {
            shop.getChildren().get(7).setDisable(true);
        }

        sell.getChildren().get(1).setDisable(!world.checkItemInUnequippedInventory(Sword.class));
        sell.getChildren().get(2).setDisable(!world.checkItemInUnequippedInventory(Stake.class));
        sell.getChildren().get(3).setDisable(!world.checkItemInUnequippedInventory(Staff.class));
        sell.getChildren().get(4).setDisable(!world.checkItemInUnequippedInventory(Armour.class));
        sell.getChildren().get(5).setDisable(!world.checkItemInUnequippedInventory(Shield.class));
        sell.getChildren().get(6).setDisable(!world.checkItemInUnequippedInventory(Helmet.class));
        sell.getChildren().get(7).setDisable(!world.checkItemInUnequippedInventory(HealthPotion.class));
        sell.getChildren().get(8).setDisable(!world.checkItemInUnequippedInventory(TheOneRing.class));
        sell.getChildren().get(9).setDisable(!world.checkItemInUnequippedInventory(Anduril.class));
        sell.getChildren().get(10).setDisable(!world.checkItemInUnequippedInventory(TreeStump.class));
        sell.getChildren().get(11).setDisable(!world.checkItemInUnequippedInventory(DoggieCoin.class));
        if (world.checkItemInUnequippedInventory(DoggieCoin.class)) {
            DoggieCoin d = (DoggieCoin) world.getFirstItem(DoggieCoin.class);
            doggieCoinSellButton.setText(d.getValue() + "Gold");
        } else {
            doggieCoinSellButton.setText("? Gold");
        }

    }

    public void updateAlliedSoldiers() {
        for (int i = 0; i < alliedSoldierList.size(); i += 1) {
            if (i >= world.getAlliedSoldiers().size()) {
                alliedSoldierList.get(i).setVisible(false);
            } else {
                alliedSoldierList.get(i).setVisible(true);
            }
        }
    }

    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private void loadVampireCard() {
        Card newCard = world.getCardFactory().createStaticEntity(VampireCastleCard.class, world);
        VampireCastleCard vampireCastleCard = (VampireCastleCard) world.loadCard(newCard);
        onLoad(vampireCastleCard);
    }

    /**
     * load a zombie card from the world, and pair it with an image in the GUI
     */
    private void loadZombieCard() {
        Card newCard = world.getCardFactory().createStaticEntity(ZombiePitCard.class, world);
        ZombiePitCard zombiePitCard = (ZombiePitCard) world.loadCard(newCard);
        onLoad(zombiePitCard);
    }

    /**
     * load a tower card from the world, and pair it with an image in the GUI
     */
    private void loadTowerCard() {
        Card newCard = world.getCardFactory().createStaticEntity(TowerCard.class, world);
        TowerCard towerCard = (TowerCard) world.loadCard(newCard);
        onLoad(towerCard);
    }

    /**
     * load a village card from the world, and pair it with an image in the GUI
     */
    private void loadVillageCard() {
        Card newCard = world.getCardFactory().createStaticEntity(VillageCard.class, world);
        VillageCard villageCard = (VillageCard) world.loadCard(newCard);
        onLoad(villageCard);
    }

     /**
     * load a barracks card from the world, and pair it with an image in the GUI
     */
    private void loadBarracksCard() {
        Card newCard = world.getCardFactory().createStaticEntity(BarracksCard.class, world);
        BarracksCard barracksCard = (BarracksCard) world.loadCard(newCard);
        onLoad(barracksCard);
    }

    /**
     * load a trap card from the world, and pair it with an image in the GUI
     */
    private void loadTrapCard() {
        Card newCard = world.getCardFactory().createStaticEntity(TrapCard.class, world);
        TrapCard trapCard = (TrapCard) world.loadCard(newCard);
        onLoad(trapCard);
    }

    /**
     * load a campfire card from the world, and pair it with an image in the GUI
     */
    private void loadCampfireCard() {
        Card newCard = world.getCardFactory().createStaticEntity(CampfireCard.class, world);
        CampfireCard campfireCard = (CampfireCard) world.loadCard(newCard);
        onLoad(campfireCard);
    }

    /**
     * load a sword from the world, and pair it with an image in the GUI
     */
    private void loadSword(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Sword.class, world);
        Sword sword = (Sword) world.addUnequippedItem(newItem);
        onLoad(sword, false);
    }

    /**
     * load a stake from the world, and pair it with an image in the GUI
     */
    private void loadStake(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Stake.class, world);
        Stake stake = (Stake) world.addUnequippedItem(newItem);
        onLoad(stake, false);
    }

    /**
     * load a staff from the world, and pair it with an image in the GUI
     */
    private void loadStaff(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Staff.class, world);
        Staff staff = (Staff) world.addUnequippedItem(newItem);
        onLoad(staff, false);
    }

    /**
     * load armour from the world, and pair it with an image in the GUI
     */
    private void loadArmour(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Armour.class, world);
        Armour armour = (Armour) world.addUnequippedItem(newItem);
        onLoad(armour, false);
    }

    /**
     * load a shield from the world, and pair it with an image in the GUI
     */
    private void loadShield(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Shield.class, world);
        Shield shield = (Shield) world.addUnequippedItem(newItem);
        onLoad(shield, false);
    }

    /**
     * load a helmet from the world, and pair it with an image in the GUI
     */
    private void loadHelmet(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(Helmet.class, world);
        Helmet helmet = (Helmet) world.addUnequippedItem(newItem);
        onLoad(helmet, false);
    }

    /**
     * load a health potion from the world, and pair it with an image in the GUI
     */
    private void loadHealthPotion(){
        // TODO = load more types of weapon
        // start by getting first available coordinates
        Item newItem = world.getItemFactory().createStaticEntity(HealthPotion.class, world);
        HealthPotion healthPotion = (HealthPotion) world.addUnequippedItem(newItem);
        onLoad(healthPotion, false);
    }

    /**
     * load the one ring into the world, and pair it with an image in the  GUI
     */
    private void loadTheOneRing() {
        Item newItem = world.getItemFactory().createStaticEntity(TheOneRing.class, world);
        TheOneRing theOneRing = (TheOneRing) world.addUnequippedItem(newItem);
        onLoad(theOneRing);
    }

    /**
     * load the one ring into the world, and pair it with an image in the  GUI
     */
    private void loadAnduril() {
        Item newItem = world.getItemFactory().createStaticEntity(Anduril.class, world);
        Anduril anduril = (Anduril) world.addUnequippedItem(newItem);
        onLoad(anduril, false);
    }

    /**
     * load the one ring into the world, and pair it with an image in the  GUI
     */
    private void loadTreeStump() {
        Item newItem = world.getItemFactory().createStaticEntity(TreeStump.class, world);
        TreeStump treeStump = (TreeStump) world.addUnequippedItem(newItem);
        onLoad(treeStump, false);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning items/experience/gold
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy){
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        Random rand = new Random();
        int cardChoice = rand.nextInt(7);
        switch (cardChoice) {
            case 0:
                loadVampireCard();
                break;
            case 1:
                loadZombieCard();
                break;
            case 2:
                loadTowerCard();
                break;
            case 3:
                loadVillageCard();
                break;
            case 4:
                loadBarracksCard();
                break;
            case 5:
                loadTrapCard();
                break;
            case 6:
                loadCampfireCard();
        }
        int itemChoice = rand.nextInt(6);
        switch (itemChoice) {
            case 0:
                loadSword();
                break;
            case 1:
                loadStake();
                break;
            case 2:
                loadStaff();
                break;
            case 3:
                loadArmour();
                break;
            case 4:
                loadShield();
                break;
            case 5:
                loadHelmet();
        }
        int oneRingChoice = rand.nextInt(100);
        int andurilChoice = rand.nextInt(100);
        int treeStumpChoice = rand.nextInt(100);
        if (oneRingChoice == 69) { loadTheOneRing(); }
        if (andurilChoice == 42) { loadAnduril(); }
        if (treeStumpChoice == 99) { loadTreeStump(); }
        loadHealthPotion();

        if (enemy instanceof Slug) {
            world.getCharacter().setXp(world.getCharacter().getXp() + 50);
            world.setTotalGold(world.getTotalGold() + 10);
        } else if (enemy instanceof Vampire) {
            world.getCharacter().setXp(world.getCharacter().getXp() + 150);
            world.setTotalGold(world.getTotalGold() + 30);
        } else if (enemy instanceof Zombie) {
            world.getCharacter().setXp(world.getCharacter().getXp() + 125);
            world.setTotalGold(world.getTotalGold() + 25);
        } 

        updateHealth();
        updateStats();
        updateAlliedSoldiers();

        if (world.getCreatedItems() != null) {
            for (Class<?> item: world.getCreatedItems()) {
                if (item.equals(Sword.class)) {
                    loadSword();
                } else if (item.equals(Stake.class)) {
                    loadStake();
                } else if (item.equals(Staff.class)) {
                    loadStaff();
                } else if (item.equals(Armour.class)) {
                    loadArmour();
                } else if (item.equals(Shield.class)) {
                    loadShield();
                } else if (item.equals(Helmet.class)) {
                    loadHelmet();
                } else if (item.equals(HealthPotion.class)) {
                    loadHealthPotion();
                } 
            }
            world.clearCreatedItems();
        }

    }

    /**
     * load a vampire castle card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param vampireCastleCard
     */
    private void onLoad(VampireCastleCard vampireCastleCard) {
        ImageView view = new ImageView(vampireCastleCardImage);

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, vampireCastleCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(vampireCastleCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a zombie pit card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param zombiePitCard
     */
    private void onLoad(ZombiePitCard zombiePitCard) {
        ImageView view = new ImageView(zombiePitCardImage);

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, zombiePitCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(zombiePitCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a tower card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param towerCard
     */
    private void onLoad(TowerCard towerCard) {
        ImageView view = new ImageView(towerCardImage);
        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, towerCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(towerCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a village card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param villageCard
     */
    private void onLoad(VillageCard villageCard) {
        ImageView view = new ImageView(villageCardImage);
        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, villageCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(villageCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a barracks card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param barracksCard
     */
    private void onLoad(BarracksCard barracksCard) {
        ImageView view = new ImageView(barracksCardImage);
        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, barracksCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(barracksCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a trap card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param trapCard
     */
    private void onLoad(TrapCard trapCard) {
        ImageView view = new ImageView(trapCardImage);

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, trapCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(trapCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a campfire card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param campfireCard
     */
    private void onLoad(CampfireCard campfireCard) {
        ImageView view = new ImageView(campfireCardImage);
        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, campfireCard, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(campfireCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a gold into the GUI
     * @param gold
     */
    private void onLoad(Gold gold) {
        ImageView view = new ImageView(goldImage);
        addEntity(gold, view);
        squares.getChildren().add(view);
    }

    /**
     * load a healthPotion into the GUI
     * @param healthPotion
     */
    private void onLoad(HealthPotion healthPotion) {
        ImageView view = new ImageView(healthPotionImage);
        addEntity(healthPotion, view);
        squares.getChildren().add(view);
    }

    /**
     * load a sword into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param item
     */
    private void onLoad(Item item, boolean equipped) {
        ImageView view;
        // Add rare items here
        if (item instanceof Sword) {
            view = new ImageView(swordImage);
        } else if (item instanceof Stake) {
            view = new ImageView(stakeImage);
        } else if (item instanceof Staff) {
            view = new ImageView(staffImage);
        } else if (item instanceof Armour) {
            view = new ImageView(armourImage);
        } else if (item instanceof Shield) {
            view = new ImageView(shieldImage);
        } else if (item instanceof Helmet) {
            view = new ImageView(helmetImage);
        } else /*if (item instanceof HealthPotion)*/ {
            view = new ImageView(healthPotionImage);
        }
        // Add rare items here
        if (equipped) {
            if (item instanceof BasicWeapon) {
                equippedItems.add(view, 0, 1);
            } else if (item instanceof Armour) {
                equippedItems.add(view, 1, 1);
            } else if (item instanceof Shield) {
                equippedItems.add(view, 2, 1);
            } else if (item instanceof Helmet) {
                equippedItems.add(view, 1, 0);
            }
        } else {
            unequippedInventory.getChildren().add(view);
            if (item instanceof BasicWeapon || item instanceof BasicArmour) {
                addDragEventHandlers(view, item, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
            }
        }
        addEntity(item, view);
    }

    /**
     * load the one ring into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param theOneRing
     */
    private void onLoad(TheOneRing theOneRing) {
        ImageView view = new ImageView(theOneRingImage);
        addDragEventHandlers(view, theOneRing, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(theOneRing, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a slug into the GUI
     * @param enemy
     */
    private void onLoad(Slug slug) {
        ImageView view = new ImageView(slugImage);
        addEntity(slug, view);
        squares.getChildren().add(view);
    }

    /**
     * load a vampire into the GUI
     * @param enemy
     */
    private void onLoad(Vampire vampire) {
        ImageView view = new ImageView(vampireImage);
        addEntity(vampire, view);
        squares.getChildren().add(view);
    }

    /**
     * load a zombie into the GUI
     * @param enemy
     */
    private void onLoad(Zombie zombie) {
        ImageView view = new ImageView(zombieImage);
        addEntity(zombie, view);
        squares.getChildren().add(view);
    }

    private void onLoad(Doggie doggie) {
        ImageView view = new ImageView(doggieImage);
        addEntity(doggie, view);
        squares.getChildren().add(view);
    }

    private void onLoad(ElanMuske elan) {
        ImageView view = new ImageView(elanImage);
        addEntity(elan, view);
        squares.getChildren().add(view);
    }

    /**
     * load buildings into the GUI
     * @param building
     */
    private void onLoad(Building building){
        ImageView view = null;
        if (building.getClass().equals(VampireCastleBuilding.class)) {
            view = new ImageView(vampireCastleBuildingImage);
        } else if (building.getClass().equals(ZombiePitBuilding.class)) {
            view = new ImageView(zombiePitBuildingImage);
        } else if (building.getClass().equals(TowerBuilding.class)) {
            view = new ImageView(towerBuildingImage);
        } else if (building.getClass().equals(VillageBuilding.class)) {
            view = new ImageView(villageBuildingImage);
        } else if (building.getClass().equals(BarracksBuilding.class)) {
            view = new ImageView(barracksBuildingImage);
        } else if (building.getClass().equals(TrapBuilding.class)) {
            view  = new ImageView(trapBuildingImage);
        } else if (building.getClass().equals(CampfireBuilding.class)) {
            view = new ImageView(campfireBuildingImage);
        } else if (building.getClass().equals(HeroCastle.class)) {
            view = new ImageView(heroCastleImage);
        }
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        // TODO = be more selective about where something can be dropped
        // for example, in the specification, villages can only be dropped on path, whilst vampire castles cannot go on the path

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // TODO = for being more selective about where something can be dropped, consider applying additional if-statement logic
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    boolean incorrectSlot = false;
                    if(node != targetGridPane && db.hasImage()){
                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType){
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                Card card = (Card) currentlyDraggedEntity;
                                if (!card.isValidPlacement(new Pair<Integer,Integer>(x, y), world.getOrderedPath(), world.adjacentPaths(x, y), world.getBuildings())) {
                                    incorrectSlot = true;
                                    break;
                                }
                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                onLoad(newBuilding);
                                break;
                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                if (currentlyDraggedEntity instanceof BasicWeapon || currentlyDraggedEntity instanceof Anduril) {
                                    if (x != 0 || y != 1) {
                                        incorrectSlot = true;
                                        break;
                                    }
                                    Item weapon;
                                    if (currentlyDraggedEntity.equals(Anduril.class)) {
                                        weapon = (Anduril) equipWeapon(nodeX, nodeY, x, y);
                                    } else {
                                        weapon = (BasicWeapon) equipWeapon(nodeX, nodeY, x, y);
                                    }
                                    onLoad(weapon, true);
                                } else if (currentlyDraggedEntity instanceof Armour) {
                                    if (x != 1 || y != 1) {
                                        incorrectSlot = true;
                                        break;
                                    }
                                    Armour armour = equipArmour(nodeX, nodeY, x, y);
                                    onLoad(armour, true);
                                } else if (currentlyDraggedEntity instanceof Shield || currentlyDraggedEntity instanceof TreeStump) {
                                    if (x != 2 || y != 1) {
                                        incorrectSlot = true;
                                        break;
                                    }
                                    Item shield;
                                    if (currentlyDraggedEntity.equals(TreeStump.class)) {
                                        shield = (TreeStump) equipShield(nodeX, nodeY, x, y);
                                    } else {
                                        shield = (Shield) equipShield(nodeX, nodeY, x, y);
                                    }
                                    onLoad(shield, true);
                                } else if (currentlyDraggedEntity instanceof Helmet) {
                                    if (x != 1 || y != 0) {
                                        incorrectSlot = true;
                                        break;
                                    }
                                    Helmet helmet = equipHelmet(nodeX, nodeY, x, y);
                                    onLoad(helmet, true);
                                }
                                break;
                            default:
                                break;
                        }
                        
                        if (incorrectSlot == true) {
                            currentlyDraggedImage.setVisible(true);
                        } else {
                            node.setOpacity(1);
                        }

                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    } else {
                        currentlyDraggedImage.setVisible(true);
                        printThreadingNotes("DRAG DROPPED ON NON GRIDPANE HANDLED");
                    }
                    draggedEntity.setVisible(false);
                    draggedEntity.setMouseTransparent(false);
                    // remove drag event handlers before setting currently dragged image to null
                    currentlyDraggedImage = null;
                    currentlyDraggedType = null;
                    currentlyDraggedEntity = null;
                    
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    private Item equipWeapon(int initialX, int initialY, int newX, int newY) {
        return world.equipWeapon(initialX, initialY, newX, newY);
    }

    private Armour equipArmour(int initialX, int initialY, int newX, int newY) {
        return world.equipArmour(initialX, initialY, newX, newY);
    }

    private Item equipShield(int initialX, int initialY, int newX, int newY) {
        return world.equipShield(initialX, initialY, newX, newY);
    }

    private Helmet equipHelmet(int initialX, int initialY, int newX, int newY) {
        return world.equipHelmet(initialX, initialY, newX, newY);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in the unequipped inventory gridpane
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, StaticEntity entity, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                currentlyDraggedEntity = entity;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                switch (draggableType){
                    case CARD:
                        if (entity instanceof VampireCastleCard) {
                            draggedEntity.setImage(vampireCastleCardImage);
                            vampireCastleSound.stop();
                            vampireCastleSound.setVolume(0.2);
                            vampireCastleSound.play();
                        }  else if (entity instanceof ZombiePitCard) {
                            draggedEntity.setImage(zombiePitCardImage);
                            zombiePitSound.stop();
                            zombiePitSound.setVolume(0.2);
                            zombiePitSound.play();
                        }  else if (entity instanceof TowerCard) {
                            draggedEntity.setImage(towerCardImage);
                            towerSound.stop();
                            towerSound.setVolume(0.2);
                            towerSound.play();
                        }  else if (entity instanceof VillageCard) {
                            draggedEntity.setImage(villageCardImage);
                            villageSound.stop();
                            villageSound.setVolume(0.2);
                            villageSound.play();
                        }  else if (entity instanceof BarracksCard) {
                            draggedEntity.setImage(barracksCardImage);
                            barracksSound.stop();
                            barracksSound.setVolume(0.2);
                            barracksSound.play();
                        }  else if (entity instanceof TrapCard) {
                            draggedEntity.setImage(trapCardImage);
                            trapSound.stop();
                            trapSound.setVolume(0.2);
                            trapSound.play();
                        }  else if (entity instanceof CampfireCard) {
                            draggedEntity.setImage(campfireCardImage);
                            fireSound.stop();
                            fireSound.setVolume(0.2);
                            fireSound.play();
                        }
                        break;
                    case ITEM:
                        if (entity instanceof Sword) {
                            draggedEntity.setImage(swordImage);
                            swordSound.stop();
                            swordSound.setVolume(0.2);
                            swordSound.play();
                        } else if (entity instanceof Stake) {
                            draggedEntity.setImage(stakeImage);
                            swordSound.stop();
                            swordSound.setVolume(0.2);
                            swordSound.play();
                        } else if (entity instanceof Staff) {
                            draggedEntity.setImage(staffImage);
                            staffSound.stop();
                            staffSound.setVolume(0.2);
                            staffSound.play();
                        } else if (entity instanceof Armour) {
                            draggedEntity.setImage(armourImage);
                            armorSound.stop();
                            armorSound.setVolume(0.2);
                            armorSound.play();
                        } else if (entity instanceof Shield) {
                            draggedEntity.setImage(shieldImage);
                            shieldSound.stop();
                            shieldSound.setVolume(0.2);
                            shieldSound.play();
                        } else if (entity instanceof Helmet) {
                            draggedEntity.setImage(helmetImage);
                            armorSound.stop();
                            armorSound.setVolume(0.2);
                            armorSound.play();
                        } 
                        // Add rare items here
                    default:
                        break;
                }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                boolean validLocation = true;
                                int x = GridPane.getColumnIndex(n);
                                int y = GridPane.getRowIndex(n);
                                if (currentlyDraggedEntity instanceof BasicWeapon || currentlyDraggedEntity instanceof Anduril) {
                                    if (x != 0 || y != 1) {
                                        validLocation = false;
                                    }

                                } else if (currentlyDraggedEntity instanceof Armour) {
                                    if (x != 1 || y != 1) {
                                        validLocation = false;
                                    }

                                } else if (currentlyDraggedEntity instanceof Shield || currentlyDraggedEntity instanceof TreeStump) {
                                    if (x != 2 || y != 1) {
                                        validLocation = false;
                                    }

                                } else if (currentlyDraggedEntity instanceof Helmet) {
                                    if (x != 1 || y != 0) {
                                        validLocation = false;
                                    }

                                } else if (currentlyDraggedEntity instanceof Card) {
                                    Card card = (Card) currentlyDraggedEntity;
                                    if (!card.isValidPlacement(new Pair<Integer,Integer>(x, y), world.getOrderedPath(), world.adjacentPaths(x, y), world.getBuildings())) {
                                        validLocation = false;
                                    }
                                }

                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage() && validLocation == true){
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you could program the game so if the new highlight location is invalid the highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
        case SPACE:
            if (shop.isVisible() || sell.isVisible() || gamemodeSelect.isVisible() || goal.isVisible() || gameOver) {
                break;
            }
            if (isPaused){
                startTimer();
            }
            else{
                pause();
            }
            break;
        case H:
            if (gameOver || goal.isVisible()) {
                break;
            }    
            for (Item item : world.getUnequippedInventoryItems()) {
                if (item instanceof HealthPotion) {
                    world.getCharacter().setHealth(250);
                    updateHealth();
                    world.removeUnequippedInventoryItem(item);
                    healthPotionSound.stop();
                    healthPotionSound.setVolume(0.2);
                    healthPotionSound.play();
                    break;
                }
            }
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        // TODO = possibly set other menu switchers
        if (!gamemodeSelect.isVisible()) {
            pause();
            pauseTimeline.stop();
        }
        mainMenuSwitcher.switchMenu();
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void returnToGame() throws IOException {
        startTimer();
        goal.setVisible(false);
        gameInterface.setVisible(true);
        squares.requestFocus();
        initialGoal = false;
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
    }

    // media player to play normal gameplay music
    public void playNormalMusic() {

        gameplayMusic = new Media((new File("resources/music/gameplay.mp3")).toURI().toString());
        player = new MediaPlayer(gameplayMusic);
        player.setOnEndOfMedia(new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
        });
        player.play();
        double volume = 0.15;
        player.setVolume(volume);
    }

    // media player to play defeat screen music
    public void playDefeatMusic() {

        gameplayMusic = new Media((new File("resources/music/defeat.mp3")).toURI().toString());
        player = new MediaPlayer(gameplayMusic);
        player.setOnEndOfMedia(new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
        });
        player.play();
        double volume = 0.2;
        player.setVolume(volume);
    }

    // media player to play victory screen music
    public void playVictoryMusic() {

        gameplayMusic = new Media((new File("resources/music/win.mp3")).toURI().toString());
        player = new MediaPlayer(gameplayMusic);
        player.setOnEndOfMedia(new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
        });
        player.play();
        double volume = 0.2;
        player.setVolume(volume);
    }

    // media player to play shop screen music
    public void playShopMusic() {

        gameplayMusic = new Media((new File("resources/music/shop.mp3")).toURI().toString());
        shopPlayer = new MediaPlayer(gameplayMusic);
        shopPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
        });
        shopPlayer.play();
        double volume = 0.2;
        shopPlayer.setVolume(volume);
    }

    public void stopMusic() {

        player.stop();
    }

    public void setMainMenuPlayer(MediaPlayer mainMenuMusic) {

        this.mainMenuPlayer = mainMenuMusic;
    }
}
