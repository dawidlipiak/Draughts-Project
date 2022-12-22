package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * HBox class for changing version of Draughts
 */
public class ButtonsHBox extends HBox{
    /**
     * Buttons in HBox for changing version of Draughts
     */
    ButtonsHBox() {
        //Create buttons changing draughts versions
        Label text = new Label(" Wersja:");
        Button buttonItalian = new Button("Włoska");
        Button buttonSpanish = new Button("Hiszpańska");
        Button buttonGerman = new Button("Niemiecka");
        this.getChildren().addAll(text,buttonItalian,buttonSpanish,buttonGerman);
        this.setSpacing(10.0);
    }

    // ? DO KONSULTACJI  ?
//    public HBox getVersionsHBox() {
//        return this;
//    }
}