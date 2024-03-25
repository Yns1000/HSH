package com.example.hsh;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import metier.Bien;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private HBox loginButton;


    @FXML
    private HBox cardLayout;
    private List<Bien> recentlyAddedBiens;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        loginButton.setOnMouseClicked(event -> {
            try {
                // Charger le fichier FXML 'B'
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
                Parent root = loader.load();

                // Obtenir la stage actuelle à partir d'un composant de la scène actuelle
                Stage stage = (Stage) loginButton.getScene().getWindow();

                // Définir et afficher la nouvelle scène
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        recentlyAddedBiens = new ArrayList<>(recentlyAddedBiens());
        System.out.println(recentlyAddedBiens.size());
        try {
            for (int i = 0; i < recentlyAddedBiens.size(); i++) {
                System.out.println("controller init");
                System.out.println(recentlyAddedBiens.get(i).getLibBien());
                System.out.println(recentlyAddedBiens.get(i).getImageSrc());
                System.out.println(recentlyAddedBiens.get(i).getPrixBase());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setDatas(recentlyAddedBiens.get(i));
                System.out.println(recentlyAddedBiens.get(i).getLibBien());
                cardLayout.getChildren().add(cardBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Bien> recentlyAddedBiens() {
        List<Bien> ls = new ArrayList<>();

        Bien b1 = new Bien();
        b1.setLibBien("Appartement cool");
        b1.setPrixBase(100000F);
        b1.setImageSrc("https://via.placeholder.com/150");

        Bien b2 = new Bien();
        b2.setLibBien("Maison cool");
        b2.setPrixBase(200000F);
        b2.setImageSrc("https://via.placeholder.com/150");

        Bien b3 = new Bien();
        b3.setLibBien("Villa cool");
        b3.setPrixBase(300000F);
        b3.setImageSrc("https://via.placeholder.com/150");


        Bien b4 = new Bien();
        b4.setLibBien("Studio cool");
        b4.setPrixBase(400000F);
        b4.setImageSrc("@../../../img/logo_name.png"); //TODO REPARE LES LIENS BORDEL

        ls.add(b1);
        ls.add(b2);
        ls.add(b3);
        ls.add(b4);

        return ls;
    }
}