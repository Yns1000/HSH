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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import metier.Bien;
import metier.Tier;
import metier.TypeTier;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML private Label welcomeText;
    @FXML private Button loginButton2, inscriptionButton, singUpButton, singInButton, addItem, search;
    @FXML private HBox loginButton, newItemsButton, famousItemsButton, categButtons, myitemsButton, favItemsButton, messagesButton, settingsButton, myAccountButton, cardLayout;
    @FXML private ChoiceBox<TypeTier> selectProfile;
    @FXML private TextField mailFieldSignup, nomFieldSignup, prenomFieldSignup, telFieldSignup, adresseFieldSignup, villeFieldSignup, codePostalFieldSignup, mailFieldLogin, villeAddr, surfaceBien, prixBien, nbPieceBien;
    @FXML private PasswordField passwordFieldSignup, passwordFieldLogin;
    @FXML private DatePicker naissanceFieldSignup;
    @FXML private ChoiceBox<String> typeBien, typeVente;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setupEntityManager();
        setupUIElements();
        if (UserSession.getInstance().isLoggedIn()) {
            handleUserLoggedIn();
        } else {
            if (addItem != null) addItem.setVisible(false);
        }

        loadChoices();
        setupSearchButton();
        setupAddItemButton();
        populateRecentlyAddedBiens();
        setupButtons();
    }

    private void setupEntityManager() {
        closeEntityManager();
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private void handleUserLoggedIn() {
        welcomeText.setText("Bienvenue " + UserSession.getInstance().getUsername());
        Tier user = entityManager.find(Tier.class, UserSession.getInstance().getId_user());
        if (addItem != null) {
            addItem.setVisible(user.getTypeTiers().getLibTypeTiers().equals("PROPRIETAIRE"));
        }
    }

    private void loadChoices() {
        if (typeVente != null) typeVente.getItems().addAll("Vente", "Location");
        if (typeBien != null) typeBien.getItems().addAll("Appartement", "Maison", "Terrain", "Local commercial", "Garage", "Autre");
    }

    private void setupSearchButton() {
        if (search != null) {
            search.setOnMouseClicked(event -> {
                setSearchParameters();
                try {
                    loadScene("toSearchAdvanced.fxml", search);
                } catch (IOException ex) {
                    handleException(ex);
                }
            });
        }
    }

    private void setSearchParameters() {
        UserSession.getInstance().setNbPieceToSearch(nbPieceBien.getText());
        UserSession.getInstance().setSurfaceToSearch(surfaceBien.getText());
        UserSession.getInstance().setVilleToSearch(villeAddr.getText());
        UserSession.getInstance().setPrixBienToSearch(prixBien.getText());
        UserSession.getInstance().setTypeBienToSearch(typeBien.getValue());
        UserSession.getInstance().setTypeVenteToSearch(typeVente.getValue());
    }

    private void setupAddItemButton() {
        if (addItem != null) {
            addItem.setOnMouseClicked(event -> {
                String sceneToLoad = UserSession.getInstance().isLoggedIn() ? "addItem.fxml" : "Connexion.fxml";
                try {
                    loadScene(sceneToLoad, addItem);
                } catch (IOException ex) {
                    handleException(ex);
                }
            });
        }
    }

    private void populateRecentlyAddedBiens() {
        List<Bien> recentlyAddedBiens = new ArrayList<>(recentlyAddedBiens());
        recentlyAddedBiens.forEach(bien -> addCard(bien));
    }

    private void addCard(Bien bien) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setDatas(bien);
            if (cardLayout != null) cardLayout.getChildren().add(cardBox);
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void setupButtons() {
        setOnClickLoadScene(loginButton, "Connexion.fxml");
        setOnClickLoadScene(loginButton2, "Connexion.fxml");
        setOnClickLoadScene(inscriptionButton, "Inscription.fxml");
        if (singUpButton != null) singUpButton.setOnAction(event -> createTierFromForm());
        if (singInButton != null) singInButton.setOnAction(event -> login());
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
        return entityManager.createQuery("SELECT b FROM Bien b ORDER BY b.id DESC", Bien.class).setMaxResults(5).getResultList();
    }

    private TextFormatter<Integer> createFormatter(int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return (newText.matches("[0-9]*") && newText.length() <= maxLength) ? change : null;
        };
        return new TextFormatter<>(new IntegerStringConverter(), null, filter);
    }

    public void createTierFromForm() {
        Tier newTier = createNewTierFromFormFields();
        persistEntity(newTier);
        try {
            loadScene("Connexion.fxml", singUpButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tier createNewTierFromFormFields() {
        Tier newTier = new Tier();
        newTier.setNomTiers(nomFieldSignup.getText());
        newTier.setPrenomTiers(prenomFieldSignup.getText());
        newTier.setTelTiers(telFieldSignup.getText());
        newTier.setMailTiers(mailFieldSignup.getText());
        newTier.setTypeTiers(selectProfile.getValue());
        newTier.setNaissanceTiers(naissanceFieldSignup.getValue());
        newTier.setAdresseTiers(adresseFieldSignup.getText());
        newTier.setVilleTiers(villeFieldSignup.getText());
        newTier.setCpTiers(codePostalFieldSignup.getText());
        newTier.setAdmin(0);
        newTier.setMdpTiers(BCrypt.hashpw(passwordFieldSignup.getText(), BCrypt.gensalt()));
        return newTier;
    }

    public void login() {
        try {
            Tier user = getUserByEmail(mailFieldLogin.getText());
            if (user != null && BCrypt.checkpw(passwordFieldLogin.getText(), user.getMdpTiers())) {
                successfulLogin(user);
            } else {
                failedLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            failedLogin();
        }
    }

    private Tier getUserByEmail(String email) {
        return entityManager.createQuery("SELECT t FROM Tier t WHERE t.mailTiers = :email", Tier.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    private void successfulLogin(Tier user) {
        UserSession.getInstance().setUsername(user.getNomTiers() + " " + user.getPrenomTiers());
        UserSession.getInstance().setId_user(user.getId());
        showBanner((Stage) singInButton.getScene().getWindow(), true);
        try {
            loadScene("hello-view.fxml", singInButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void failedLogin() {
        showBanner((Stage) singInButton.getScene().getWindow(), false);
        System.out.println("Login failed");
    }

    public void showBanner(Stage stage, boolean loginSuccess) {
        Label banner = new Label();
        banner.setText(loginSuccess ? "Connexion réussie" : "Login/Mot de passe incorrect");
        banner.setStyle(loginSuccess ? "-fx-background-color: #008000; -fx-text-fill: #ffffff; -fx-padding: 10px;" : "-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-padding: 10px;");
        StackPane root = new StackPane();
        StackPane.setAlignment(banner, javafx.geometry.Pos.BOTTOM_CENTER);
        root.getChildren().add(banner);
        banner.setTranslateY(stage.getHeight());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new KeyValue(banner.translateYProperty(), -banner.getHeight())),
                new KeyFrame(Duration.seconds(6), new KeyValue(banner.translateYProperty(), -banner.getHeight())),
                new KeyFrame(Duration.seconds(7), new KeyValue(banner.translateYProperty(), stage.getHeight()))
        );
        timeline.play();
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

    private void deleteUser(Integer id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tier tier = entityManager.find(Tier.class, id);
            entityManager.remove(tier);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private void setupUIElements() {
        // Setup ChoiceBoxes
        if (typeVente != null) {
            typeVente.getItems().addAll("Vente", "Location");
        }

        if (typeBien != null) {
            typeBien.getItems().addAll("Appartement", "Maison", "Terrain", "Local commercial", "Garage", "Autre");
        }

        // Setup form validators
        if (surfaceBien != null) {
            surfaceBien.setTextFormatter(createFormatter(5)); // Assuming max length is 5 for surface
        }

        if (prixBien != null) {
            prixBien.setTextFormatter(createFormatter(10)); // Assuming max length is 10 for price
        }

        if (nbPieceBien != null) {
            nbPieceBien.setTextFormatter(createFormatter(2)); // Assuming max length is 2 for number of pieces
        }

        // Set action for buttons
        if (singUpButton != null) {
            singUpButton.setOnAction(event -> createTierFromForm());
        }

        if (singInButton != null) {
            singInButton.setOnAction(event -> login());
        }
    }

}
