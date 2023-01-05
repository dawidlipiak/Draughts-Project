package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class VersionChoice {
    private final Stage stage, stageItalian, stageGerman, stageSpanish;
    private String chosenVersion;
    private final Button buttonItalian;
    private final Button buttonSpanish;
    private final Button buttonGerman;
    private final Button exitButton;

    /**
     * Contructor of a class allowing to choose version of the game
     * @param stage1 italian game stage
     * @param stage2 spanish game stage
     * @param stage3 german game stage
     */
    public VersionChoice(Stage stage1, Stage stage2, Stage stage3){
        stage = new Stage();
        this.stageItalian = stage1;
        this.stageSpanish = stage2;
        this.stageGerman = stage3;

        buttonItalian = new Button("Włoska");
        buttonSpanish = new Button("Hiszpańska");
        buttonGerman = new Button("Niemiecka");
        exitButton = new Button("Wyjście");

        // Making text in buttons bigger
        buttonItalian.setFont(new Font(40));
        buttonGerman.setFont(new Font(40));
        buttonSpanish.setFont(new Font(40));
        exitButton.setFont(new Font(20));
    }

    /**
     * Function that shows window
     */
    public void show(){
        Text text = new Text("Wybierz wersje gry");
        text.setFont(new Font(30));
        VBox vbox = new VBox(text, buttonItalian, buttonGerman, buttonSpanish, exitButton);
        vbox.alignmentProperty().set(Pos.CENTER);
        vbox.setSpacing(12);
        Scene scene = new Scene(vbox, 280, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Function setting version after clicking the button
     */
    public void choosingVersion() {
        EventHandler<ActionEvent> buttonEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button button = (Button) event.getSource();
                if (button.getText().equals("Włoska")) {
                    setVersion("Włoska", stageItalian);
                }
                if (button.getText().equals("Niemiecka")) {
                    setVersion("Niemiecka", stageGerman);
                }
                if (button.getText().equals("Hiszpańska")) {
                    setVersion("Hiszpańska", stageSpanish);
                }
                if (button.getText().equals("Wyjście")) {
                    System.exit(0);
                }
            }
        };
        buttonItalian.setOnAction(buttonEventHandler);
        buttonGerman.setOnAction(buttonEventHandler);
        buttonSpanish.setOnAction(buttonEventHandler);
        exitButton.setOnAction(buttonEventHandler);
    }

    /**
     * Function showing stage with picked version and hiding previous window
     * @param version
     * @param selectedStage
     */
    public void setVersion(String version, Stage selectedStage) {
        selectedStage.show();
        stage.hide();
        chosenVersion = version;
    }

    /**
     * Funtion returning chosen version of the game
     * @return chosen version of the game
     */
    public String getChosenVersion(){
        return chosenVersion;
    }
}
