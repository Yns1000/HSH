package com.example.hsh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class editItemController implements Initializable {

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

    @FXML
    private VBox photos;

    //Liste de fichiers
    private final List<File> files = new ArrayList<>();

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        setupEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        // Get the id of the property to edit
        Integer idBien = UserSession.getInstance().getId_bien_to_edit();
        Bien bien = entityManager.find(Bien.class, idBien);

        // Set the form fields with the property data
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

        // ajouter des liens hypertexte avec un bouton supprimer pour chaque photos dans le pane photos

        photos.getChildren().clear();
        List<Photosbien> photosBiens = entityManager.createQuery("SELECT p FROM Photosbien p WHERE p.bien.id = :idBien", Photosbien.class)
                .setParameter("idBien", idBien)
                .getResultList();
        for (Photosbien photosBien : photosBiens) {
            //Ajouter un vbox puis dedans un lien hypertexte avec le nom du fichier qui envoie vers la photo sur le navigateur et un bouton supprimer
            Hyperlink link = new Hyperlink(photosBien.getRessource());
            Button delete = new Button("Supprimer");
            Pane vbox = new Pane();
            vbox.getChildren().addAll(link, delete);
            photos.getChildren().add(vbox);

            //Ajouter un listener sur le bouton supprimer
            delete.setOnAction(event -> {
                //Supprimer la photo de la base de données
                EntityTransaction transaction1 = entityManager.getTransaction();
                transaction1.begin();
                entityManager.remove(photosBien);
                transaction1.commit();
                //Supprimer le fichier
                try {
                    Files.deleteIfExists(Paths.get("src/main/resources", photosBien.getRessource()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Rafraichir la page
                try {
                    loadScene("editItem.fxml", delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            //Ajouter un listener sur le lien hypertexte
            link.setOnAction(event -> {
                //Ouvrir le fichier dans le navigateur
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "src/main/resources/" + photosBien.getRessource());
                    System.out.println("src/main/resources/" + photosBien.getRessource());
                    //Ouvrir le fichier dans le navigateur
                    File file = new File("src/main/resources/" + photosBien.getRessource());
                    Desktop.getDesktop().browse(file.toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }


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

    private void editBien(Integer idBien) {
        Bien bien = entityManager.find(Bien.class, idBien);
        bien.setLibBien(nameItem.getText());
        bien.setAdrNoVoie(numAddr.getText());
        bien.setLibVoie(typeAddr.getText());
        bien.setNomVoie(nomAddr.getText());
        bien.setComplementAdresse(cmptAddr.getText());
        bien.setCodePostal(pcAddr.getText());
        bien.setCommune(villeAddr.getText());
        bien.setSurfaceHabitable(BigDecimal.valueOf(Integer.parseInt(surfaceBien.getText())));
        bien.setNombrePieces(Integer.parseInt(nbPieceBien.getText()));
        bien.setPrixBase((float) Integer.parseInt(prixBien.getText()));
        bien.setReduction(reducBien.getValue());
        bien.setTypeBien(typeBien.getValue());
        bien.setTypeVente(typeVente.getValue());
        bien.setDateDerniereMaj(java.time.Instant.now());
        bien.setDescription(descItem.getText());
        persistEntity(bien);

        //if the list of files is not empty
        if (!files.isEmpty()) {
            //for each file
            for (File file : files) {
                //save the file in the resources/images folder
                //we start by copying the file to the resources/images folder
                try {
                    //generate a unique file name
                    String uniqueFileName = UUID.randomUUID() + "_" + file.getName();
                    Path dest = Paths.get("src/main/resources", uniqueFileName);

                    //check if the file already exists
                    while (Files.exists(dest)) {
                        //generate a new unique file name
                        uniqueFileName = UUID.randomUUID().toString() + "_" + file.getName();
                        dest = Paths.get("src/main/resources", uniqueFileName);
                    }

                    Files.createDirectories(dest.getParent());
                    Files.copy(file.toPath(), dest);

                    //save the file path in the Photosbien table with the id of the property and the file path
                    Photosbien photosbien = new Photosbien();
                    photosbien.setBien(bien);
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