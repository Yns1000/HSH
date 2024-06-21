package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import metier.Favbien;
import metier.Tier;
import metier.TypeTier;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class favController implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    private void loadFavBiens(Integer idUser){
        // Recuperer les biens favoris de l'utilisateur connecté
        List<Favbien> favBiens = entityManager.createQuery("SELECT f FROM Favbien f WHERE f.tier.id = :idUser", Favbien.class)
                .setParameter("idUser", idUser)
                .getResultList();

        System.out.println("favBiens = " + favBiens);

        // Creer une card du format de card.fxml pour chaque bien favori et on ajoute la card au mainCard
        int row = 0;
        int column = 0;
        GridPane grid = new GridPane();
        for (Favbien favBien : favBiens) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
                HBox cardBox = loader.load();
                CardController cardController = loader.getController();
                cardController.setDatas(favBien.getBien());
                grid.add(cardBox, column, row);
                GridPane.setMargin(cardBox, new Insets(0, 10, 10, 0)); // Ajoute une marge à droite de 10px
                column++;
                if (column > 1) { // Change this line
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mainCard != null){
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
        loadFavBiens(UserSession.getInstance().getId_user());
    }
}