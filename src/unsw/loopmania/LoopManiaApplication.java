package unsw.loopmania;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {
    // TODO = possibly add other menus?

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;
    private FXMLLoader gameLoader;
    private Parent gameRoot;
    private Stage primaryStage;
    private Parent mainMenuRoot;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json");
        mainController = loopManiaLoader.loadController();
        gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        mainMenuRoot = menuLoader.load();
        mainMenuController.playMusic();

        // load the help menu
        HelpMenuController helpMenuController = new HelpMenuController();
        FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("HelpMenuView.fxml"));
        helpLoader.setController(helpMenuController);
        Parent helpRoot = helpLoader.load();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);
        scene.getRoot().setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");
        
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
            if (mainController.player != null) {
                mainController.stopMusic();
            }
            mainMenuController.playMusic();
        });
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
            mainController.setMainMenuPlayer(mainMenuController.getMediaPlayer());
            //mainController.playNormalMusic();
        });
        mainMenuController.setHelpSwitcher(() -> {
            switchToRoot(scene, helpRoot, primaryStage);
        });
        mainMenuController.setStage(primaryStage);
        helpMenuController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        
        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        if (root.equals(gameRoot) && mainController.getGameOver()) {
            try {
                LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json");
                mainController = loopManiaLoader.loadController();
                gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
                gameLoader.setController(mainController);
                gameRoot = gameLoader.load();
                root = gameRoot;
                mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
            } catch (IOException e) {

            }
        }
        scene.setRoot(root);
        root.requestFocus();
        scene.getRoot().setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
