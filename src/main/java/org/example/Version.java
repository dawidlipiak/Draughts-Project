package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.Strategy.GermanStrategy;
import org.example.Strategy.ItalianStrategy;
import org.example.Strategy.MovesStrategy;
import org.example.Strategy.SpanishStrategy;

/**
 * Class to choice the version of the game
 */
public class Version extends Stage{
    private final Stage gameStage;
    private String chosenVersion;
    private final Button buttonItalian;
    private final Button buttonSpanish;
    private final Button buttonGerman;
    private final Button buttonSolo;
    private final Button buttonDuo;
    private final Button exitButton;
    private MovesStrategy strategy;
    private boolean soloMode = true;

    /**
     * Contructor of a class allowing to choose version of the game
     * @param stage italian game stage
     */
    public Version(Stage stage){
        this.gameStage = stage;

        buttonItalian = new Button("Włoska");
        buttonSpanish = new Button("Hiszpańska");
        buttonGerman = new Button("Niemiecka");
        buttonSolo = new Button("Solo");
        buttonDuo = new Button("Duo");
        exitButton = new Button("Wyjście");

        // Making text in buttons bigger and set colors
        buttonItalian.setFont(new Font(40));
        buttonItalian.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");
        buttonGerman.setFont(new Font(40));
        buttonGerman.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");
        buttonSpanish.setFont(new Font(40));
        buttonSpanish.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");
        buttonSolo.setFont(new Font(25));
        buttonSolo.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");
        buttonDuo.setFont(new Font(25));
        buttonDuo.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");
        exitButton.setFont(new Font(20));
        exitButton.setStyle("-fx-background-color: #FFDAA7; -fx-border-color:brown; -fx-border-width: 2 2 2 2; -fx-background-insets: 0; ");

        setUp();
        choosingVersion();
    }

    /**
     * Function to show window
     */
    public void setUp(){
        Text text = new Text("Wybierz wersje gry");
        text.setFont(new Font(30));
        HBox modeButtons = new HBox(buttonSolo,buttonDuo);
        modeButtons.setSpacing(20);
        modeButtons.alignmentProperty().set(Pos.CENTER);
        VBox vbox = new VBox(text, buttonItalian, buttonGerman, buttonSpanish, modeButtons, exitButton);
        vbox.alignmentProperty().set(Pos.CENTER);
        vbox.setSpacing(12);
        vbox.setBackground(new Background(new BackgroundFill(Color.PERU,null, null)));
        Scene scene = new Scene(vbox, 280, 500);
        this.setScene(scene);
        this.setResizable(false);

    }
    private void choosingGameMode (){
        EventHandler<ActionEvent> buttonModeEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button button = (Button) event.getSource();
                if(button.getText().equals("Solo")){
                    soloMode = true;
                    System.out.println(soloMode);
                }
                if(button.getText().equals("Duo")){
                    soloMode = false;
                    System.out.println(soloMode);
                }

            }
        };
        buttonSolo.setOnAction(buttonModeEventHandler);
        buttonDuo.setOnAction(buttonModeEventHandler);
    }


    /**
     * Function to set version after clicking the button
     */
    public void choosingVersion() {
        choosingGameMode();
        EventHandler<ActionEvent> buttonEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button button = (Button) event.getSource();
                setVersion(button.getText());
            }
        };
        buttonItalian.setOnAction(buttonEventHandler);
        buttonGerman.setOnAction(buttonEventHandler);
        buttonSpanish.setOnAction(buttonEventHandler);
        exitButton.setOnAction(buttonEventHandler);
    }

    /**
     * Function to show stage with picked version and hiding previous window
     * @param version version of the game
     */
    public void setVersion(String version) {
        if (version.equals("Włoska")) {
            this.strategy = new ItalianStrategy(8);
        }
        if (version.equals("Niemiecka")) {
            this.strategy = new GermanStrategy(8);
        }
        if (version.equals("Hiszpańska")) {
            this.strategy = new SpanishStrategy(8);
        }
        if (version.equals("Wyjście")) {
            System.exit(0);
        }
        chosenVersion = version;
        gameStage.show();
        this.hide();
    }

    /**
     * Function to return chosen version of the game.
     * @return chosen version of the game
     */

    public String getChosenVersion(){
        return chosenVersion;
    }

    /**
     * Function to return which mode game will be played
     * @return if the mode is set to solo or duo
     */
    public boolean isSoloMode() {
        return soloMode;
    }

    /**
     * Method to get the strategy.
     * @return chosen strategy
     */
    public MovesStrategy getStrategy() {
        return this.strategy;
    }
}
