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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import metier.Bien;
import metier.Photosbien;
import metier.Tier;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ItemController implements Initializable {

    @FXML
    private Label nomBien;

    @FXML
    private Label name;

    @FXML
    private Hyperlink proprio;

    @FXML
    private Label numVoie;

    @FXML
    private Label typeVoie;

    @FXML
    private Label nomVoie;

    @FXML
    private Label nomVoie2;

    @FXML
    private Label codePostal;

    @FXML
    private Label ville;

    @FXML
    private Label dateCreation;

    @FXML
    private Label derniereMaj;

    @FXML
    private Label surfaceHab;

    @FXML
    private Label nbPiece;

    @FXML
    private Label typeBien;

    @FXML
    private Label typeContrat;

    @FXML
    private HBox avis;

    @FXML
    private ImageView image;

    @FXML
    private ImageView gauche;

    @FXML
    private ImageView droite;

    @FXML
    private Label desc;

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


        Integer id_bien = UserSession.getInstance().getId_bien_to_view();

        Bien bien = entityManager.find(Bien.class, id_bien);
        Tier proprietaire = entityManager.createQuery("SELECT t FROM Tier t WHERE t.id = :idTier", Tier.class)
                .setParameter("idTier", bien.getIdProprietaire().getId())
                .getSingleResult();


        nomBien.setText(bien.getLibBien());
        name.setText(bien.getLibBien());
        proprio.setText(proprietaire.getNomTiers() + " " + proprietaire.getPrenomTiers());
        numVoie.setText(bien.getAdrNoVoie());
        typeVoie.setText(bien.getLibVoie());
        nomVoie.setText(bien.getNomVoie());
        nomVoie2.setText(bien.getComplementAdresse());
        codePostal.setText(bien.getCodePostal());
        ville.setText(bien.getCommune());
        dateCreation.setText(bien.getDateCreation().toString());
        derniereMaj.setText(bien.getDateDerniereMaj().toString());
        surfaceHab.setText(bien.getSurfaceHabitable().toString());
        nbPiece.setText(String.valueOf(bien.getNombrePieces()));
        typeBien.setText(bien.getTypeBien());
        typeContrat.setText(bien.getTypeVente());
        desc.setText(bien.getDescription());

        if (bien.getNote1() == null) {
            bien.setNote1(0);
        }
        if (bien.getNote2() == null) {
            bien.setNote2(0);
        }
        if (bien.getNote3() == null) {
            bien.setNote3(0);
        }
        if (bien.getNote4() == null) {
            bien.setNote4(0);
        }
        if (bien.getNote5() == null) {
            bien.setNote5(0);
        }
        int totalVotes = bien.getNote1() + bien.getNote2() + bien.getNote3() + bien.getNote4() + bien.getNote5();
        int totalPoints = bien.getNote1() + bien.getNote2() * 2 + bien.getNote3() * 3 + bien.getNote4() * 4 + bien.getNote5() * 5;
        int averageRating = totalVotes > 0 ? totalPoints / totalVotes : 0;

        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView();
            star.setFitHeight(15);
            star.setFitWidth(15);
            if (i <= averageRating) {
                System.out.println("i = " + i);
                star.setImage(new Image("https://cdn-icons-png.flaticon.com/512/60/60962.png"));
            } else {
                star.setImage(new Image("https://static.vecteezy.com/system/resources/previews/009/992/275/original/star-icon-sign-symbol-design-free-png.png"));
            }
            avis.getChildren().add(star);
        }



        //Cherchez toutes les photos dans la table Photosbien qui ont l'id du bien
        List<Photosbien> photosbiens = entityManager.createQuery("SELECT p FROM Photosbien p WHERE p.bien.id = :idBien", Photosbien.class)
                .setParameter("idBien", id_bien)
                .getResultList();

        //si une seule photo on cache les boutons droite et gauche
        if (photosbiens.size() == 1) {
            gauche.setVisible(false);
            droite.setVisible(false);
        }

        //Si la liste de photos n'est pas vide
        if (!photosbiens.isEmpty()) {
            //Afficher la première photo
            image.setImage(new javafx.scene.image.Image(photosbiens.get(index).getRessource()));
            //Ajouter un listener sur le bouton gauche
            gauche.setOnMouseClicked(event -> {
                //Si l'index est supérieur à 0
                if (index > 0) {
                    //Décrémenter l'index
                    index--;
                    //Afficher la photo correspondante
                    image.setImage(new javafx.scene.image.Image(photosbiens.get(index).getRessource()));
                }
            });

            //Ajouter un listener sur le bouton droit
            droite.setOnMouseClicked(event -> {
                //Si l'index est inférieur à la taille de la liste de photos - 1
                if (index < photosbiens.size() - 1) {
                    //Incrémenter l'index
                    index++;
                    //Afficher la photo correspondante
                    image.setImage(new javafx.scene.image.Image(photosbiens.get(index).getRessource()));
                }
            });
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

        //Si on clique sur le nom du propriétaire
        proprio.setOnAction(event -> {
            //On stocke l'id du propriétaire dans la session
            UserSession.getInstance().setId_tier_to_view(proprietaire.getId());
            //On charge la page viewProfile.fxml
            try {
                loadScene("viewProprio.fxml", proprio);
            } catch (IOException ex) {
                handleException(ex);
            }
        });












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