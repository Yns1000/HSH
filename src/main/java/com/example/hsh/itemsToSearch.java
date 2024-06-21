package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.Bien;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class itemsToSearch implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    @FXML
    private Label text;



    private void loadSearchBiens(){

        String toSearch = UserSession.getInstance().getToSearch();
        Float toSearchFloat = null;

        try {
            toSearchFloat = Float.parseFloat(toSearch);
        } catch (NumberFormatException e) {
            // toSearch is not a number, ignore it for the b.prixBase condition
        }

        String queryString = "SELECT b FROM Bien b WHERE b.libBien LIKE :toSearch OR b.description LIKE :toSearch OR b.libVoie LIKE :toSearch OR b.commune LIKE :toSearch OR b.codePostal LIKE :toSearch OR b.typeBien LIKE :toSearch OR b.complementAdresse LIKE :toSearch OR b.typeVente LIKE :toSearch";
        if (toSearchFloat != null) {
            queryString += " OR b.prixBase = :toSearchFloat";
        }

        Query query = entityManager.createQuery(queryString, Bien.class);
        query.setParameter("toSearch", "%" + toSearch + "%");
        if (toSearchFloat != null) {
            query.setParameter("toSearchFloat", toSearchFloat);
        }

        List< Bien > biens = query.getResultList();


        //Creation des cards pour chaque bien
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
        mainCard.getChildren().add(grid);

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

        if(UserSession.getInstance().getToSearch() != null){
            text.setText("Résultats de la recherche pour : " + UserSession.getInstance().getToSearch());
        }else{
            text.setText("BUG");
        }

        loadSearchBiens();
    }
}