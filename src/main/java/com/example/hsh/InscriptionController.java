package com.example.hsh;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InscriptionController implements Initializable  {

    @FXML
    private Label inscriptionButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inscriptionButton.setOnMouseClicked(event -> {
            try {
                // Charger le fichier FXML 'B'
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Inscription.fxml"));
                Parent root = loader.load();

                // Obtenir le stage actuel à partir d'un composant de la scène actuelle
                Stage stage = (Stage) inscriptionButton.getScene().getWindow();

                // Définir et afficher la nouvelle scène
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }
}
