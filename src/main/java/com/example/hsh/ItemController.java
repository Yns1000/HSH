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
import javafx.scene.layout.VBox;
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
    private TextArea desc;

    @FXML
    private Button retour;

    @FXML
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private VBox avis_user;


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


        initializeStars(bien);

        avis_user.setVisible(UserSession.getInstance().isLoggedIn());










    }

    private void setupEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private void initializeStars(Bien bien) {
        Image starEmpty = new Image("https://w7.pngwing.com/pngs/501/448/png-transparent-favourite-chart-favorites-heart-rating-top-web-icon.png");
        Image starFull = new Image("https://w7.pngwing.com/pngs/275/802/png-transparent-black-heart-illustration-heart-computer-icons-heart-shaped-silhouette-love-heart-black.png");

        star1.setImage(starEmpty);
        star2.setImage(starEmpty);
        star3.setImage(starEmpty);
        star4.setImage(starEmpty);
        star5.setImage(starEmpty);

        star1.setOnMouseClicked(event -> updateRating(bien, 1));
        star2.setOnMouseClicked(event -> updateRating(bien, 2));
        star3.setOnMouseClicked(event -> updateRating(bien, 3));
        star4.setOnMouseClicked(event -> updateRating(bien, 4));
        star5.setOnMouseClicked(event -> updateRating(bien, 5));

        // Calculate the average rating
        int totalVotes = bien.getNote1() + bien.getNote2() + bien.getNote3() + bien.getNote4() + bien.getNote5();
        int totalPoints = bien.getNote1() + bien.getNote2() * 2 + bien.getNote3() * 3 + bien.getNote4() * 4 + bien.getNote5() * 5;
        int averageRating = totalVotes > 0 ? totalPoints / totalVotes : 0;

        setStarImages(averageRating, starFull, starEmpty);
    }

    private void setStarImages(int rating, Image starFull, Image starEmpty) {
        star1.setImage(rating >= 1 ? starFull : starEmpty);
        star2.setImage(rating >= 2 ? starFull : starEmpty);
        star3.setImage(rating >= 3 ? starFull : starEmpty);
        star4.setImage(rating >= 4 ? starFull : starEmpty);
        star5.setImage(rating >= 5 ? starFull : starEmpty);
    }

    private void updateRating(Bien bien, int rating) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Update the notes based on the rating
            switch (rating) {
                case 1:
                    bien.setNote1(bien.getNote1() + 1);
                    break;
                case 2:
                    bien.setNote2(bien.getNote2() + 1);
                    break;
                case 3:
                    bien.setNote3(bien.getNote3() + 1);
                    break;
                case 4:
                    bien.setNote4(bien.getNote4() + 1);
                    break;
                case 5:
                    bien.setNote5(bien.getNote5() + 1);
                    break;
            }

            entityManager.merge(bien);
            transaction.commit();

            // Recalculate the average rating
            int totalVotes = bien.getNote1() + bien.getNote2() + bien.getNote3() + bien.getNote4() + bien.getNote5();
            int totalPoints = bien.getNote1() + bien.getNote2() * 2 + bien.getNote3() * 3 + bien.getNote4() * 4 + bien.getNote5() * 5;
            int averageRating = totalVotes > 0 ? totalPoints / totalVotes : 0;

            setStarImages(averageRating, new Image("https://w7.pngwing.com/pngs/275/802/png-transparent-black-heart-illustration-heart-computer-icons-heart-shaped-silhouette-love-heart-black.png"), new Image("https://w7.pngwing.com/pngs/501/448/png-transparent-favourite-chart-favorites-heart-rating-top-web-icon.png"));
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
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