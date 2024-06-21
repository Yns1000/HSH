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
import javafx.stage.Stage;
import metier.Tier;
import metier.TypeTier;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditProfileAdminController implements Initializable {

    @FXML
    private ChoiceBox<TypeTier> selectProfile;

    @FXML
    private TextField mailFieldSignup;

    @FXML
    private PasswordField passwordFieldSignup;

    @FXML
    private TextField nomFieldSignup;

    @FXML
    private TextField prenomFieldSignup;

    @FXML
    private TextField telFieldSignup;

    @FXML
    private DatePicker naissanceFieldSignup;

    @FXML
    private TextField adresseFieldSignup;

    @FXML
    private TextField villeFieldSignup;

    @FXML
    private TextField codePostalFieldSignup;

    @FXML
    private Button saveProfile;

    //Connexion

    @FXML
    private TextField mailFieldLogin;

    @FXML
    private PasswordField passwordFieldLogin;

    @FXML
    private CheckBox isAdmin;

    @FXML
    private Button deleteProfile;

    @FXML
    private Label nameProfileToEdit;

    private EntityManager entityManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();

        List<TypeTier> typeTiers = entityManager.createQuery("SELECT t FROM TypeTier t", TypeTier.class).getResultList();


        //Recuperer les informations de l'utilisateur connecté et les mettre dans les champs
        Integer userId = UserSession.getInstance().getId_tier_to_edit();
        Tier tier = entityManager.find(Tier.class, userId);

        if (this.nameProfileToEdit != null) nameProfileToEdit.setText("Modifier le profile de : " + tier.getNomTiers() + " " + tier.getPrenomTiers() );

        if (this.mailFieldSignup != null) mailFieldSignup.setText(tier.getMailTiers());
        if (this.nomFieldSignup != null) nomFieldSignup.setText(tier.getNomTiers());
        if (this.prenomFieldSignup != null) prenomFieldSignup.setText(tier.getPrenomTiers());
        if (this.telFieldSignup != null) telFieldSignup.setText(tier.getTelTiers());
        if (this.naissanceFieldSignup != null) naissanceFieldSignup.setValue(tier.getNaissanceTiers());
        if (this.adresseFieldSignup != null) adresseFieldSignup.setText(tier.getAdresseTiers());
        if (this.villeFieldSignup != null) villeFieldSignup.setText(tier.getVilleTiers());
        if (this.codePostalFieldSignup != null) codePostalFieldSignup.setText(tier.getCpTiers());
        if (this.selectProfile != null) selectProfile.setValue(tier.getTypeTiers());
        if (this.isAdmin != null) isAdmin.setSelected(tier.getAdmin() == 1);
        if (this.passwordFieldSignup != null) passwordFieldSignup.setText(tier.getMdpTiers());

// Remplir la liste déroulante

        if (selectProfile != null) selectProfile.getItems().addAll(typeTiers);
        if (selectProfile != null) selectProfile.setValue(tier.getTypeTiers());


        if (deleteProfile != null) deleteProfile.setOnMouseClicked(event -> {
            deleteUser(userId);
            // Rediriger vers la page de connexion
            try {
                loadScene("settings.fxml", deleteProfile);
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

        if (saveProfile != null) saveProfile.setOnMouseClicked(event -> {
            updateUser(userId);
            // Rediriger vers la page d'accueil
            try {
                loadScene("settings.fxml", saveProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUser(Integer id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tier tier = entityManager.find(Tier.class, id);
            tier.setMailTiers(mailFieldSignup.getText());
            tier.setNomTiers(nomFieldSignup.getText());
            tier.setPrenomTiers(prenomFieldSignup.getText());
            tier.setTelTiers(telFieldSignup.getText());
            tier.setNaissanceTiers(naissanceFieldSignup.getValue());
            tier.setAdresseTiers(adresseFieldSignup.getText());
            tier.setVilleTiers(villeFieldSignup.getText());
            tier.setCpTiers(codePostalFieldSignup.getText());
            tier.setTypeTiers(selectProfile.getValue());
            tier.setAdmin(isAdmin.isSelected() ? 1 : 0);
            String hashedPassword = BCrypt.hashpw(passwordFieldSignup.getText(), BCrypt.gensalt());
            tier.setMdpTiers(hashedPassword);

            entityManager.persist(tier);
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