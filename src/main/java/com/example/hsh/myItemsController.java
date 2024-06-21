package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.Bien;
import metier.Favbien;
import metier.Tier;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class myItemsController implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    private void loadBiensProprio(Integer idUser){
        // Recuperer les biens favoris dont l'utilisateur est proprietaire

        Tier proprio = entityManager.find(Tier.class, idUser);

        List< Bien > biens = entityManager.createQuery("SELECT b FROM Bien b WHERE b.idProprietaire = :proprio", Bien.class)
                .setParameter("proprio", proprio)
                .getResultList();

        int row = 0;
        int column = 0;
        GridPane grid = new GridPane();
        for (Bien bien : biens) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
                HBox cardBox = loader.load();
                CardController cardController = loader.getController();
                cardController.setDatas(bien);
                grid.add(cardBox, column, row);
                GridPane.setMargin(cardBox, new Insets(0, 10, 10, 0)); // Ajoute une marge à droite de 10px
                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mainCard != null) {
            mainCard.getChildren().add(grid);
        }
    }


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
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();

        // Recuperer l'utilisateur connecté
        loadBiensProprio(UserSession.getInstance().getId_user());
    }
}