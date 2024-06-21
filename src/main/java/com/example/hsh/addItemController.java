package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import metier.Bien;
import metier.Photosbien;
import metier.Tier;
import metier.TypeTier;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.UnaryOperator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class addItemController implements Initializable {

    @FXML
    private Button btnChooseFile;

    @FXML
    private ChoiceBox<String> typeBien;

    @FXML
    private ChoiceBox<String> typeVente;

    @FXML
    private TextField nameItem;

    @FXML
    private TextField numAddr;

    @FXML
    private TextField typeAddr;

    @FXML
    private TextField nomAddr;

    @FXML
    private TextField cmptAddr;

    @FXML
    private TextField pcAddr;

    @FXML
    private TextField villeAddr;

    @FXML
    private TextField surfaceBien;

    @FXML
    private TextField nbPieceBien;

    @FXML
    private TextField prixBien;

    @FXML
    private Slider reducBien;

    @FXML
    private Button saveBien;

    @FXML
    private Button retour;

    @FXML
    private TextField descItem;

    //Liste de fichiers
    private final List<File> files = new ArrayList<>();

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        setupEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();


        btnChooseFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            fileChooser.setTitle("Choisir une image");
            File selectedFile = fileChooser.showOpenDialog(btnChooseFile.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println("Image selected: " + selectedFile.getName());
            }
            //Ajouter le fichier à la liste
            files.add(selectedFile);
        });

        if(typeBien != null) {
            typeBien.getItems().addAll("Appartement", "Maison", "Terrain", "Local commercial", "Garage", "Autre");
        }
        if (typeVente != null) {
            typeVente.getItems().addAll("Vente", "Location");
        }

        //Ajouter un listener sur le bouton saveBien
        saveBien.setOnAction(event -> {
            createBien();
            // Rediriger vers la page d'accueil
            try {
                loadScene("hello-view.fxml", saveBien);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Ajouter un listener sur le bouton retour
        retour.setOnAction(event -> {
            // Rediriger vers la page d'accueil
            try {
                loadScene("hello-view.fxml", retour);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });












    }

    private void setupEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
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



    public void createBien() {
        Bien newBien = createNewBienFromFormFields();
        persistEntity(newBien);

        //Si la liste de fichiers n'est pas vide
        if (!files.isEmpty()) {
            //Pour chaque fichier
            for (File file : files) {
                //Enregistrer le fichier dans le dossier resources/images
                //On commence par copier le fichier dans le dossier resources/images
                try {
                    //Générer un nom de fichier unique
                    String uniqueFileName = UUID.randomUUID() + "_" + file.getName();
                    Path dest = Paths.get("src/main/resources", uniqueFileName);

                    //Vérifier si le fichier existe déjà
                    while (Files.exists(dest)) {
                        //Générer un nouveau nom de fichier unique
                        uniqueFileName = UUID.randomUUID().toString() + "_" + file.getName();
                        dest = Paths.get("src/main/resources", uniqueFileName);
                    }

                    Files.createDirectories(dest.getParent());
                    Files.copy(file.toPath(), dest);

                    //Enregistrer le chemin du fichier dans la table Photosbien avec l'id du bien et le chemin du fichier
                    Photosbien photosbien = new Photosbien();
                    photosbien.setBien(newBien);
                    photosbien.setRessource(uniqueFileName);

                    // Persist the Photosbien entity
                    persistEntity(photosbien);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bien createNewBienFromFormFields() {
        Tier tier = entityManager.find(Tier.class, UserSession.getInstance().getId_user());
        Bien newBien = new Bien();
        newBien.setLibBien(nameItem.getText());
        newBien.setAdrNoVoie(numAddr.getText());
        newBien.setLibVoie(typeAddr.getText());
        newBien.setNomVoie(nomAddr.getText());
        newBien.setComplementAdresse(cmptAddr.getText());
        newBien.setCodePostal(pcAddr.getText());
        newBien.setCommune(villeAddr.getText());
        newBien.setSurfaceHabitable(BigDecimal.valueOf(Integer.parseInt(surfaceBien.getText())));
        newBien.setNombrePieces(Integer.parseInt(nbPieceBien.getText()));
        newBien.setPrixBase((float) Integer.parseInt(prixBien.getText()));
        newBien.setReduction(reducBien.getValue());
        newBien.setTypeBien(typeBien.getValue());
        newBien.setTypeVente(typeVente.getValue());
        newBien.setDateCreation(java.time.Instant.now());
        newBien.setDateDerniereMaj(java.time.Instant.now());
        newBien.setIdProprietaire(tier);
        newBien.setNote1(0);
        newBien.setNote2(0);
        newBien.setNote3(0);
        newBien.setNote4(0);
        newBien.setNote5(0);
        newBien.setDescription(descItem.getText());
        return newBien;
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