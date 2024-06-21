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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import metier.Bien;
import metier.Favbien;
import metier.Photosbien;
import metier.Tier;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    private static final String LINK_HEART_EMPTY = "https://w7.pngwing.com/pngs/501/448/png-transparent-favourite-chart-favorites-heart-rating-top-web-icon.png";
    private static final String LINK_HEART_FULL = "https://w7.pngwing.com/pngs/275/802/png-transparent-black-heart-illustration-heart-computer-icons-heart-shaped-silhouette-love-heart-black.png";

    @FXML
    private ImageView imageView;

    @FXML
    private Label prixDuBien;

    @FXML
    private Label titreDuBien;

    @FXML
    private HBox vote_rate;

    @FXML
    private Pane fav;

    @FXML
    private Pane settings;

    @FXML
    private Button viewItem;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEntityManager();

        if (viewItem != null) {
            viewItem.setOnMouseClicked(event -> {
                Integer id_bien = Integer.parseInt(viewItem.getParent().getId());
                UserSession.getInstance().setId_bien_to_view(id_bien);
                try {
                    loadScene("viewItem.fxml", viewItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        fav.setOnMouseClicked(event -> {
            Integer id_bien = Integer.parseInt(vote_rate.getParent().getId());
            toggleFavoriteStatus(id_bien);
        });

        settings.setOnMouseClicked(event -> {
            Integer id_bien = Integer.parseInt(vote_rate.getParent().getId());
            UserSession.getInstance().setId_bien_to_edit(id_bien);
            try {
                loadScene("editItem.fxml", settings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setVoteRate(Bien bien) {
        vote_rate.getParent().setId(bien.getId().toString());
        int totalVotes = getTotalVotes(bien);
        int averageRating = totalVotes > 0 ? getTotalPoints(bien) / totalVotes : 0;

        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView();
            star.setFitHeight(15);
            star.setFitWidth(15);
            star.setImage(new Image(i <= averageRating ? "https://cdn-icons-png.flaticon.com/512/60/60962.png" : "https://static.vecteezy.com/system/resources/previews/009/992/275/original/star-icon-sign-symbol-design-free-png.png"));
            vote_rate.getChildren().add(star);
        }
    }

    public void setDatas(Bien bien) {
        Photosbien photo = entityManager.createQuery("SELECT p FROM Photosbien p WHERE p.bien.id = :id_bien", Photosbien.class)
                .setParameter("id_bien", bien.getId())
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (photo != null && photo.getRessource() != null) {
            imageView.setImage(new Image(photo.getRessource()));
        } else {
            imageView.setImage(new Image("https://edito.seloger.com/sites/default/files/styles/306x198/public/images/web/2024-03/Appartement-Paris-%C3%89t%C3%A9.jpg?h=b63d1a1c&itok=gZ_qznVD"));
        }

        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

        titreDuBien.setText(bien.getLibBien());
        prixDuBien.setText(bien.getPrixBase() + " €");
        setVoteRate(bien);

        ImageView favImage = new ImageView();
        favImage.setFitHeight(20);
        favImage.setFitWidth(20);

        settings.setVisible(UserSession.getInstance().isLoggedIn() && UserSession.getInstance().getId_user().equals(bien.getIdProprietaire().getId()));

        if (UserSession.getInstance().isLoggedIn()) {
            Integer id_bien = bien.getId();
            Integer id_user = UserSession.getInstance().getId_user();
            boolean isFavorited = isFavorited(id_bien, id_user);
            favImage.setImage(new Image(isFavorited ? LINK_HEART_FULL : LINK_HEART_EMPTY));
            favImage.getStyleClass().add(isFavorited ? "full" : "empty");
        } else {
            favImage.setImage(new Image(LINK_HEART_EMPTY));
        }

        fav.getChildren().add(favImage);
        viewItem.getParent().setId(bien.getId().toString());
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

    private void setupEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private boolean isFavorited(Integer id_bien, Integer id_user) {
        Long count = entityManager.createQuery("SELECT COUNT(f) FROM Favbien f WHERE f.tier.id = :id_user AND f.bien.id = :id_bien", Long.class)
                .setParameter("id_user", id_user)
                .setParameter("id_bien", id_bien)
                .getSingleResult();
        return count > 0;
    }

    private void toggleFavoriteStatus(Integer id_bien) {
        if (UserSession.getInstance().isLoggedIn()) {
            Integer id_user = UserSession.getInstance().getId_user();
            boolean isFavorited = isFavorited(id_bien, id_user);
            handleFavoriting(id_bien, isFavorited);
            updateFavoriteIcon(isFavorited);
        }
    }

    private void handleFavoriting(Integer id_bien, boolean isFavorited) {
        Integer id_user = UserSession.getInstance().getId_user();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (isFavorited) {
                entityManager.createQuery("DELETE FROM Favbien f WHERE f.tier.id = :id_user AND f.bien.id = :id_bien")
                        .setParameter("id_user", id_user)
                        .setParameter("id_bien", id_bien)
                        .executeUpdate();
            } else {
                Bien bien = entityManager.find(Bien.class, id_bien);
                Tier tier = entityManager.find(Tier.class, id_user);
                Favbien favbien = new Favbien();
                favbien.setTier(tier);
                favbien.setBien(bien);
                persistEntity(favbien);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void persistEntity(Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private int getTotalVotes(Bien bien) {
        return (bien.getNote1() != null ? bien.getNote1() : 0) +
                (bien.getNote2() != null ? bien.getNote2() : 0) +
                (bien.getNote3() != null ? bien.getNote3() : 0) +
                (bien.getNote4() != null ? bien.getNote4() : 0) +
                (bien.getNote5() != null ? bien.getNote5() : 0);
    }

    private int getTotalPoints(Bien bien) {
        return (bien.getNote1() != null ? bien.getNote1() : 0) +
                (bien.getNote2() != null ? bien.getNote2() * 2 : 0) +
                (bien.getNote3() != null ? bien.getNote3() * 3 : 0) +
                (bien.getNote4() != null ? bien.getNote4() * 4 : 0) +
                (bien.getNote5() != null ? bien.getNote5() * 5 : 0);
    }

    private void updateFavoriteIcon(boolean isFavorited) {
        fav.getChildren().clear();
        ImageView favImage = new ImageView();
        favImage.setFitHeight(20);
        favImage.setFitWidth(20);
        favImage.setImage(new Image(isFavorited ? LINK_HEART_EMPTY : LINK_HEART_FULL));
        favImage.getStyleClass().add(isFavorited ? "empty" : "full");
        fav.getChildren().add(favImage);
    }
}
