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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import metier.Bien;
import metier.Tier;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class viewProprioController implements Initializable {

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Label nomComplet;

    @FXML
    private Label mail;

    @FXML
    private Label codePostal;

    @FXML
    private Label ville;

    @FXML
    private Label numTelephone;

    @FXML
    private ImageView image;


    @FXML
    private Button retour;



    //Index de la photo actuellement affichée
    private int index = 0;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        setupEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();


        Integer id_tier = UserSession.getInstance().getId_tier_to_view();

        Tier User = entityManager.find(Tier.class, id_tier);

        nom.setText(User.getNomTiers());
        prenom.setText(User.getPrenomTiers());
        mail.setText(User.getMailTiers());
        codePostal.setText(User.getCpTiers());
        ville.setText(User.getVilleTiers());
        numTelephone.setText(User.getTelTiers());
        nomComplet.setText(User.getNomTiers() + " " + User.getPrenomTiers());
        if(User.getPhoto() != null && !User.getPhoto().isEmpty()){
            image.setImage(new Image(User.getPhoto()));
        }




        //Ajouter un listener sur le bouton retour
        retour.setOnAction(event -> {
            // Rediriger vers la page d'accueil
            try {
                loadScene("hello-view.fxml", retour);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Ajouter un listener sur le bouton modifier













    }

    private void setupEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }


    private void setOnClickLoadScene(Node node, String fxmlFile) {
        if (node != null) {
            node.setOnMouseClicked(event -> {
                try {
                    loadScene(fxmlFile, node);
                } catch (IOException ex) {
                    handleException(ex);
                }
            });
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

    private void handleException(Exception ex) {
        ex.printStackTrace();
    }

    private List<Bien> recentlyAddedBiens() {
        List<Bien> ls = entityManager.createQuery("SELECT b FROM Bien b ORDER BY b.dateCreation DESC", Bien.class).setMaxResults(5).getResultList();
        return ls;

    }

    private TextFormatter<Integer> createFormatter(int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };

        return new TextFormatter<>(new IntegerStringConverter(), null, filter);
    }





    private void persistEntity(Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }



}