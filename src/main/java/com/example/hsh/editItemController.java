package com.example.hsh;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import metier.Bien;
import metier.Photosbien;
import metier.Tier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class editItemController implements Initializable {

    @FXML
    private Button btnChooseFile, saveBien, retour;

    @FXML
    private ChoiceBox<String> typeBien, typeVente;

    @FXML
    private TextField nameItem, numAddr, typeAddr, nomAddr, cmptAddr, pcAddr, villeAddr, surfaceBien, nbPieceBien, prixBien, descItem;

    @FXML
    private Slider reducBien;

    @FXML
    private VBox photos;

    private final List<File> files = new ArrayList<>();
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        setupEntityManager();
        Integer idBien = UserSession.getInstance().getId_bien_to_edit();
        Bien bien = entityManager.find(Bien.class, idBien);
        fillFormFields(bien);
        populatePhotos(idBien);
        setupFileChooser();
        setupChoiceBoxes();
        setupButtonActions(idBien);
    }

    private void setupEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private void fillFormFields(Bien bien) {
        nameItem.setText(bien.getLibBien());
        numAddr.setText(bien.getAdrNoVoie());
        typeAddr.setText(bien.getLibVoie());
        nomAddr.setText(bien.getNomVoie());
        cmptAddr.setText(bien.getComplementAdresse());
        pcAddr.setText(bien.getCodePostal());
        villeAddr.setText(bien.getCommune());
        surfaceBien.setText(bien.getSurfaceHabitable().toString());
        nbPieceBien.setText(String.valueOf(bien.getNombrePieces()));
        prixBien.setText(String.valueOf(bien.getPrixBase()));
        reducBien.setValue(bien.getReduction());
        typeBien.setValue(bien.getTypeBien());
        typeVente.setValue(bien.getTypeVente());
        descItem.setText(bien.getDescription());
    }

    private void populatePhotos(Integer idBien) {
        photos.getChildren().clear();
        List<Photosbien> photosBiens = entityManager.createQuery("SELECT p FROM Photosbien p WHERE p.bien.id = :idBien", Photosbien.class)
                .setParameter("idBien", idBien)
                .getResultList();

        for (Photosbien photosBien : photosBiens) {
            Hyperlink link = new Hyperlink(photosBien.getRessource());
            Button delete = new Button("Supprimer");
            Pane vbox = new Pane();
            vbox.getChildren().addAll(link, delete);
            photos.getChildren().add(vbox);

            delete.setOnAction(event -> deletePhoto(photosBien, delete));
            link.setOnAction(event -> openPhotoInBrowser(photosBien.getRessource()));
        }
    }

    private void deletePhoto(Photosbien photosBien, Button deleteButton) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(photosBien);
            transaction.commit();
            Files.deleteIfExists(Paths.get("src/main/resources", photosBien.getRessource()));
            reloadScene(deleteButton, "editItem.fxml");
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void openPhotoInBrowser(String ressource) {
        try {
            File file = new File("src/main/resources/" + ressource);
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void setupFileChooser() {
        btnChooseFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(btnChooseFile.getScene().getWindow());
            if (selectedFile != null) {
                files.add(selectedFile);
            }
        });
    }

    private void setupChoiceBoxes() {
        typeBien.getItems().addAll("Appartement", "Maison", "Terrain", "Local commercial", "Garage", "Autre");
        typeVente.getItems().addAll("Vente", "Location");
    }

    private void setupButtonActions(Integer idBien) {
        saveBien.setOnAction(event -> {
            editBien(idBien);
            reloadScene(saveBien, "hello-view.fxml");
        });

        retour.setOnAction(event -> reloadScene(retour, "hello-view.fxml"));
    }

    private void reloadScene(Node node, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void editBien(Integer idBien) {
        Bien bien = entityManager.find(Bien.class, idBien);
        updateBienFields(bien);
        persistEntity(bien);
        savePhotos(bien);
    }

    private void updateBienFields(Bien bien) {
        bien.setLibBien(nameItem.getText());
        bien.setAdrNoVoie(numAddr.getText());
        bien.setLibVoie(typeAddr.getText());
        bien.setNomVoie(nomAddr.getText());
        bien.setComplementAdresse(cmptAddr.getText());
        bien.setCodePostal(pcAddr.getText());
        bien.setCommune(villeAddr.getText());
        bien.setSurfaceHabitable(new BigDecimal(surfaceBien.getText()));
        bien.setNombrePieces(Integer.parseInt(nbPieceBien.getText()));
        bien.setPrixBase(Float.parseFloat(prixBien.getText()));
        bien.setReduction(reducBien.getValue());
        bien.setTypeBien(typeBien.getValue());
        bien.setTypeVente(typeVente.getValue());
        bien.setDateDerniereMaj(Instant.now());
        bien.setDescription(descItem.getText());
    }

    private void savePhotos(Bien bien) {
        if (!files.isEmpty()) {
            for (File file : files) {
                savePhotoFile(file, bien);
            }
        }
    }

    private void savePhotoFile(File file, Bien bien) {
        try {
            String uniqueFileName = UUID.randomUUID() + "_" + file.getName();
            Path dest = Paths.get("src/main/resources", uniqueFileName);
            Files.createDirectories(dest.getParent());
            Files.copy(file.toPath(), dest);
            Photosbien photosbien = new Photosbien();
            photosbien.setBien(bien);
            photosbien.setRessource(uniqueFileName);
            persistEntity(photosbien);
        } catch (IOException e) {
            handleException(e);
        }
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
            handleException(e);
        }
    }

    private void handleException(Exception ex) {
        ex.printStackTrace();
    }
}
