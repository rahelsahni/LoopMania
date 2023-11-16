package unsw.loopmania;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * controller for the main menu.
 * TODO = you could extend this, for example with a settings menu, or a menu to load particular maps.
 */
public class MainMenuController {
    
    @FXML
    private Button startGameButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button exitButton;
   
    @FXML
    private VBox box;

    private Media mainMenuMusic;
    private MediaPlayer player;
    private boolean switched = false;

    @FXML
    public void initialize() {
        Font font = Font.loadFont("file:resources/fonts/antiquity-print.ttf", 32);
        HBox titleBox = new HBox();
        Label loopLabel = new Label("Loop");
        loopLabel.setFont(font);
        loopLabel.setTextFill(Paint.valueOf("#FFFFFF"));
        Label maniaLabel = new Label("Mania");
        maniaLabel.setFont(font);
        maniaLabel.setTextFill(Paint.valueOf("#FF0000"));
        titleBox.getChildren().add(loopLabel);
        titleBox.getChildren().add(maniaLabel);
        box.getChildren().add(0, titleBox);
        box.setBackground(new Background((new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));
        font = Font.loadFont("file:resources/fonts/antiquity-print.ttf", 12);
        startGameButton.setFont(font);
        helpButton.setFont(font);
        exitButton.setFont(font);
    }

    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    private MenuSwitcher helpMenuSwitcher;
    private Stage stage;

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setHelpSwitcher(MenuSwitcher helpMenuSwitcher){
        this.helpMenuSwitcher = helpMenuSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
        if (switched == true) {
            stopMusic();
        }
        switched = true;
    }

    @FXML
    private void switchToHelp() throws IOException {
        helpMenuSwitcher.switchMenu();
    }
    @FXML
    private void exitGame() throws IOException {
        stage.close();
    }

    // Media Player to play main menu music
    public void playMusic() {

        mainMenuMusic = new Media((new File("resources/music/mainmenu.mp3")).toURI().toString());
        player = new MediaPlayer(mainMenuMusic);
        player.setOnEndOfMedia(new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
        });
        player.play();
        double volume = 0.2;
        player.setVolume(volume);
    }

    public void stopMusic() {

        player.stop();
    }

    public MediaPlayer getMediaPlayer() {

        return player;
    }

}
