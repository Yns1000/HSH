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
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class itemsToSearchAdvanced implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    @FXML
    private Label text;



    private void loadSearchBiens(){

        String nbPieceToSearch = UserSession.getInstance().getNbPieceToSearch();
        String surfaceToSearch = UserSession.getInstance().getSurfaceToSearch();
        String villeToSearch = UserSession.getInstance().getVilleToSearch();
        String prixBienToSearch = UserSession.getInstance().getPrixBienToSearch();
        String typeBienToSearch = UserSession.getInstance().getTypeBienToSearch();
        String typeVenteToSearch = UserSession.getInstance().getTypeVenteToSearch();

        String queryString = "SELECT b FROM Bien b WHERE 1=1";

        if (nbPieceToSearch != null && !nbPieceToSearch.isEmpty()) {
            queryString += " AND b.nombrePieces = :nbPiece";
        }
        if (surfaceToSearch != null && !surfaceToSearch.isEmpty()) {
            queryString += " AND b.surfaceHabitable = :surface";
        }
        if (villeToSearch != null && !villeToSearch.isEmpty()) {
            queryString += " AND b.commune = :ville";
        }
        if (prixBienToSearch != null && !prixBienToSearch.isEmpty()) {
            queryString += " AND b.prixBase = :prix";
        }
        if (typeBienToSearch != null && !typeBienToSearch.isEmpty()) {
            queryString += " AND b.typeBien = :typeBien";
        }
        if (typeVenteToSearch != null && !typeVenteToSearch.isEmpty()) {
            queryString += " AND b.typeVente = :typeVente";
        }

        Query query = entityManager.createQuery(queryString, Bien.class);

        if (nbPieceToSearch != null && !nbPieceToSearch.isEmpty()) {
            query.setParameter("nbPiece", Integer.parseInt(nbPieceToSearch));
        }
        if (surfaceToSearch != null && !surfaceToSearch.isEmpty()) {
            query.setParameter("surface", new BigDecimal(surfaceToSearch));
        }
        if (villeToSearch != null && !villeToSearch.isEmpty()) {
            query.setParameter("ville", villeToSearch);
        }
        if (prixBienToSearch != null && !prixBienToSearch.isEmpty()) {
            query.setParameter("prix", Float.parseFloat(prixBienToSearch));
        }
        if (typeBienToSearch != null && !typeBienToSearch.isEmpty()) {
            query.setParameter("typeBien", typeBienToSearch);
        }
        if (typeVenteToSearch != null && !typeVenteToSearch.isEmpty()) {
            query.setParameter("typeVente", typeVenteToSearch);
        }

        List<Bien> biens = query.getResultList();


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
            text.setText("Aucun résultat trouvé");
        }

        loadSearchBiens();
    }
}