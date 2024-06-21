package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import metier.Tier;
import metier.TypeTier;

import java.io.IOException;
import java.util.List;

public class TypeTiersController {

    @FXML
    private Button saveType;

    @FXML
    private Button deleteType;

    @FXML
    private TextField nameType;

    @FXML
    private CheckBox visibleType;

    private EntityManager entityManager;

    public void initialize() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();

        //Préremplir les champs avec les informations du type de tiers
        TypeTier typeTier = entityManager.find(TypeTier.class, UserSession.getInstance().getId_type_tier_to_edit());
        nameType.setText(typeTier.getLibTypeTiers());
        visibleType.setSelected(typeTier.getVisible());

        saveType.setOnMouseClicked(event -> {
            editType(UserSession.getInstance().getId_type_tier_to_edit());
            // Rediriger vers la page de gestion des types de tiers
            try {
                loadScene("settings.fxml", saveType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteType.setOnMouseClicked(event -> {
            deleteType(UserSession.getInstance().getId_type_tier_to_edit());
            // Rediriger vers la page de gestion des types de tiers
            try {
                loadScene("settings.fxml", deleteType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }




    private void deleteType(Integer id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TypeTier typeTier = entityManager.find(TypeTier.class, id);
            entityManager.remove(typeTier);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void editType(Integer id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TypeTier typeTier = entityManager.find(TypeTier.class, id);
            typeTier.setLibTypeTiers(nameType.getText());
            typeTier.setVisible(visibleType.isSelected());
            entityManager.persist(typeTier);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
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

}