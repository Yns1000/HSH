package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import metier.Favbien;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class categController implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    @FXML
    private VBox main;

    @FXML
    private BorderPane bpane;

    List<String> categories;



    private Initializable loadScene(String fxmlFile, Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        return loader.getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Rendre la hauteur de bpane dynamique
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();

        // Recuperer les categories en regardant chaque bien dans la colonne typebien

        categories = entityManager.createQuery("SELECT DISTINCT b.typeBien FROM Bien b", String.class).getResultList();

        //Retirer les vides ou null
        categories.removeIf(s -> s == null || s.isEmpty());

        System.out.println("categories = " + categories);

        /*
        Dans main pour chaque categorie on ajoute un vbox dedans on met un hbox  alligné top left dans lequel on place un label avec le nom de la categorie ensuite on ajoute un scrollpane dans le vbox avec une couleur fff et dans ce scrollpane on ajoute un hbox avec cardLayout comme id et on ajoute les cards dedans

        de cette maniere :

        <VBox prefHeight="241.0" prefWidth="1034.0">
               <children>
                  <HBox prefHeight="34.0" prefWidth="1034.0">
                     <children>
                        <Label fx:id="" alignment="TOP_LEFT" contentDisplay="CENTER" prefHeight="44.0" prefWidth="143.0" text="Récement ajoutés " textAlignment="CENTER">
                           <font>
                              <Font name="Lato Bold" size="15.0" />
                           </font>
                        </Label>
                     </children>
                  </HBox>
                  <ScrollPane prefHeight="200.0" prefWidth="200.0" style="-fx-background-color: #fff;" styleClass="transparent" stylesheets="@style.css">
                     <content>
                        <HBox fx:id="cardLayout" prefHeight="183.0" prefWidth="1352.0" spacing="15.0" style="-fx-background-color: #fff;" styleClass="transparent" stylesheets="@style.css" />
                     </content>
                  </ScrollPane>
               </children>
            </VBox>

            pour mettre les card on utilise la technique try {
            for (int i = 0; i < recentlyAddedBiens.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setDatas(recentlyAddedBiens.get(i));
                if (cardLayout != null) cardLayout.getChildren().add(cardBox);
            }
        } catch (Exception e) {
            handleException(e);
        }



         */

        for (String category : categories) {
            VBox vbox = new VBox();
            HBox hbox = new HBox();
            Label label = new Label(category);
            //Margin 20 sur label
            VBox.setMargin(label, new Insets(20, 20, 20, 20));
            label.setFont(new Font("Lato Bold", 15));
            label.setAlignment(Pos.TOP_LEFT);
            hbox.getChildren().add(label);
            vbox.getChildren().add(hbox);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPrefHeight(200);
            scrollPane.setPrefWidth(200);
            scrollPane.setStyle("-fx-background-color: #fff;");
            scrollPane.getStyleClass().add("transparent");
            scrollPane.getStylesheets().add("@style.css");
            HBox cardLayout = new HBox();
            cardLayout.setPrefHeight(183);
            cardLayout.setPrefWidth(1352);
            cardLayout.setSpacing(15);
            cardLayout.setStyle("-fx-background-color: #fff;");
            cardLayout.getStyleClass().add("transparent");
            cardLayout.getStylesheets().add("@style.css");
            scrollPane.setContent(cardLayout);
            vbox.getChildren().add(scrollPane);
            main.getChildren().add(vbox);

            List<Favbien> favBiens = entityManager.createQuery("SELECT f FROM Favbien f WHERE f.bien.typeBien = :category", Favbien.class)
                    .setParameter("category", category)
                    .getResultList();

            for (Favbien favBien : favBiens) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
                    HBox cardBox = loader.load();
                    CardController cardController = loader.getController();
                    cardController.setDatas(favBien.getBien());
                    cardLayout.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        bpane.prefHeightProperty().bind(main.heightProperty());


        // Recuperer l'utilisateur connecté
    }
}