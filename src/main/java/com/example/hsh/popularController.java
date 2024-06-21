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
import metier.Tier;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class popularController implements Initializable {

    private EntityManager entityManager;

    @FXML
    private VBox mainCard;

    private List< Bien > biens;

    private Integer noteBien(Bien bien){
        Integer note1 = bien.getNote1();
        Integer note2 = bien.getNote2();
        Integer note3 = bien.getNote3();
        Integer note4 = bien.getNote4();
        Integer note5 = bien.getNote5();

        //Note 1 represente le nombre de 1 etoile
        //Note 2 represente le nombre de 2 etoiles
        //Note 3 represente le nombre de 3 etoiles
        //Note 4 represente le nombre de 4 etoiles
        //Note 5 represente le nombre de 5 etoiles

        //Calcul de la note moyenne
        if(note1 + note2 + note3 + note4 + note5 == 0)
            return 0;
        else
            return (note1 + 2*note2 + 3*note3 + 4*note4 + 5*note5) / (note1 + note2 + note3 + note4 + note5);
    }

    private void loadBiensPopular(){
        // Recuperer les biens les plus populaires
        biens = entityManager.createQuery("SELECT b FROM Bien b", Bien.class)
                .getResultList();

        biens.sort((b1, b2) -> noteBien(b2) - noteBien(b1));

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

        loadBiensPopular();
    }
}