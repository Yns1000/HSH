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
import metier.Tier;
import metier.TypeTier;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class AdminSettingsController {

    @FXML
    private Button saveNewTypeTiers;

    @FXML
    private TextField newTypeTiers;

    @FXML
    private TableView<Tier> clientsTableau;

    @FXML
    private TableView<TypeTier> typeTiersTable;

    private EntityManager entityManager;

    public void initialize() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        loadClients();

        saveNewTypeTiers.setOnAction(event -> {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                TypeTier typeTier = new TypeTier();
                typeTier.setLibTypeTiers(newTypeTiers.getText());
                typeTier.setVisible(true);
                entityManager.persist(typeTier);
                transaction.commit();
                typeTiersTable.getItems().add(typeTier);
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        });

        // code d'initialisation ici...
    }



    private void loadClients(){
        // 1. Récupérez les données de la base de données en utilisant Hibernate.
        List<Tier> tiers = entityManager.createQuery("SELECT t FROM Tier t", Tier.class).getResultList();

        System.out.println(tiers);

        // 2. Créez des objets TableColumn pour chaque colonne de votre tableau.
        TableColumn<Tier, String> nomCol = new TableColumn<>("Nom");
        TableColumn<Tier, String> prenomCol = new TableColumn<>("Prénom");
        TableColumn<Tier, String> mailCol = new TableColumn<>("Email");

        // Alligner centre les colonnes
        nomCol.setStyle("-fx-alignment: CENTER;");
        prenomCol.setStyle("-fx-alignment: CENTER;");
        mailCol.setStyle("-fx-alignment: CENTER;");

        // 3. Définissez la valeur de chaque colonne en utilisant la méthode setCellValueFactory.
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomTiers"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenomTiers"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mailTiers"));

        // 4. Ajoutez les objets TableColumn à votre tableau.
        clientsTableau.getColumns().add(nomCol);
        clientsTableau.getColumns().add(prenomCol);
        clientsTableau.getColumns().add(mailCol);

        // 5. Ajoutez les données récupérées à votre tableau.
        clientsTableau.getItems().addAll(tiers);


        // Ajouter deux colonnes supplémentaires pour les boutons de suppression et de modification
        TableColumn<Tier, Void> deleteCol = new TableColumn<>("Supprimer");
        TableColumn<Tier, Void> updateCol = new TableColumn<>("Modifier");

        // Alligner centre les colonnes
        deleteCol.setStyle("-fx-alignment: CENTER;");
        updateCol.setStyle("-fx-alignment: CENTER;");

        // Définir un cellFactory personnalisé pour les colonnes de boutons
        Callback<TableColumn<Tier, Void>, TableCell<Tier, Void>> cellFactoryForDelete = new Callback<>() {
            @Override
            public TableCell<Tier, Void> call(final TableColumn<Tier, Void> param) {
                return new TableCell<>() {

                    // Créer le bouton
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        // Définir l'action du bouton
                        deleteBtn.setOnAction((event) -> {
                            Tier tier = getTableView().getItems().get(getIndex());

                            //Supprimer l'objet 'tier'
                            deleteUser(tier.getId());
                            // Retirer l'objet 'tier' de la liste
                            getTableView().getItems().remove(tier);


                            // Ajoutez ici le code pour supprimer l'objet 'tier'
                            System.out.println("Supprimer " + tier.getNomTiers());
                        });

                        // Définir la classe du bouton
                        deleteBtn.setStyle("-fx-background-color: #fff; -fx-text-fill: red;-fx-border-style: solid;-fx-border-color: red;-fx-border-width: 1;");
                    }

                    // Cette méthode est appelée pour mettre à jour l'item de la cellule
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Si la cellule est vide, nous n'affichons rien
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Sinon, nous affichons le bouton
                            setGraphic(deleteBtn);
                        }
                    }
                };
            }
        };

        Callback<TableColumn<Tier, Void>, TableCell<Tier, Void>> cellFactoryForUpdate = new Callback<>() {
            @Override
            public TableCell<Tier, Void> call(final TableColumn<Tier, Void> param) {
                return new TableCell<>() {

                    // Créer le bouton
                    private final Button updateBtn = new Button("Modifier");

                    {
                        // Définir l'action du bouton
                        updateBtn.setOnAction((event) -> {
                            Tier tier = getTableView().getItems().get(getIndex());

                            // Enregistrer l'ID de l'objet 'tier' à modifier
                            UserSession.getInstance().setId_tier_to_edit(tier.getId());
                            // Rediriger l'utilisateur vers la page de modification
                            try {
                                loadScene("setting_profile_admin.fxml", updateBtn);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            // Ajoutez ici le code pour modifier l'objet 'tier'
                            System.out.println("Modifier " + tier.getNomTiers());
                        });

                        // Définir la classe du bouton
                        updateBtn.setStyle("-fx-background-color: #fff; -fx-text-fill: #3f3f98;-fx-border-style: solid;-fx-border-color: #3f3f98;-fx-border-width: 1;");
                    }

                    // Cette méthode est appelée pour mettre à jour l'item de la cellule
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Si la cellule est vide, nous n'affichons rien
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Sinon, nous affichons le bouton
                            setGraphic(updateBtn);
                        }
                    }
                };
            }
        };

        // Utiliser le cellFactory pour les colonnes de boutons
        deleteCol.setCellFactory(cellFactoryForDelete);
        updateCol.setCellFactory(cellFactoryForUpdate);

        // Ajouter les colonnes de boutons au tableau
        clientsTableau.getColumns().add(deleteCol);
        clientsTableau.getColumns().add(updateCol);

        List<TypeTier> typeTiers = entityManager.createQuery("SELECT t FROM TypeTier t", TypeTier.class).getResultList();

        // 2. Créez des objets TableColumn pour chaque colonne de votre tableau.
        TableColumn<TypeTier, String> typeCol = new TableColumn<>("Type");
        TableColumn<TypeTier, String> visibleCol = new TableColumn<>("Visible");

        // Alligner centre les colonnes
        typeCol.setStyle("-fx-alignment: CENTER;");
        visibleCol.setStyle("-fx-alignment: CENTER;");

        // 3. Définissez la valeur de chaque colonne en utilisant la méthode setCellValueFactory.
        typeCol.setCellValueFactory(new PropertyValueFactory<>("libTypeTiers"));
        visibleCol.setCellValueFactory(new PropertyValueFactory<>("visible"));

        // 4. Ajoutez les objets TableColumn à votre tableau.
        typeTiersTable.getColumns().add(typeCol);
        typeTiersTable.getColumns().add(visibleCol);

        // 5. Ajoutez les données récupérées à votre tableau.
        typeTiersTable.getItems().addAll(typeTiers);

        // Ajouter deux colonnes supplémentaires pour les boutons de suppression et de modification
        TableColumn<TypeTier, Void> deleteTypeCol = new TableColumn<>("Supprimer");
        TableColumn<TypeTier, Void> updateTypeCol = new TableColumn<>("Modifier");

        // Alligner centre les colonnes
        deleteTypeCol.setStyle("-fx-alignment: CENTER;");
        updateTypeCol.setStyle("-fx-alignment: CENTER;");

        // Définir un cellFactory personnalisé pour les colonnes de boutons
        Callback<TableColumn<TypeTier, Void>, TableCell<TypeTier, Void>> cellFactoryForDeleteType = new Callback<>() {
            @Override
            public TableCell<TypeTier, Void> call(final TableColumn<TypeTier, Void> param) {
                return new TableCell<>() {

                    // Créer le bouton
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        // Définir l'action du bouton
                        deleteBtn.setOnAction((event) -> {
                            TypeTier typeTier = getTableView().getItems().get(getIndex());

                            //Supprimer l'objet 'typeTier'
                            deleteType(typeTier.getId());
                            // Retirer l'objet 'typeTier' de la liste
                            getTableView().getItems().remove(typeTier);

                            // Ajoutez ici le code pour supprimer l'objet 'typeTier'
                            System.out.println("Supprimer " + typeTier.getLibTypeTiers());
                        });

                        // Définir la classe du bouton
                        deleteBtn.setStyle("-fx-background-color: #fff; -fx-text-fill: red;-fx-border-style: solid;-fx-border-color: red;-fx-border-width: 1;");


                    }

                    // Cette méthode est appelée pour mettre à jour l'item de la cellule
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Si la cellule est vide, nous n'affichons rien
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Sinon, nous affichons le bouton
                            setGraphic(deleteBtn);
                        }
                    }
                };
            }
        };

        Callback<TableColumn<TypeTier, Void>, TableCell<TypeTier, Void>> cellFactoryForUpdateType = new Callback<>() {
            @Override
            public TableCell<TypeTier, Void> call(final TableColumn<TypeTier, Void> param) {
                return new TableCell<>() {

                    // Créer le bouton
                    private final Button updateBtn = new Button("Modifier");

                    {
                        // Définir l'action du bouton
                        updateBtn.setOnAction((event) -> {
                            TypeTier typeTier = getTableView().getItems().get(getIndex());

                            // Enregistrer l'ID de l'objet 'typeTier' à modifier
                            UserSession.getInstance().setId_type_tier_to_edit(typeTier.getId());
                            // Rediriger l'utilisateur vers la page de modification
                            try {
                                loadScene("type_tiers_edit.fxml", updateBtn);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            // Ajoutez ici le code pour modifier l'objet 'typeTier'
                            System.out.println("Modifier " + typeTier.getLibTypeTiers());
                        });

                        // Définir la classe du bouton
                        updateBtn.setStyle("-fx-background-color: #fff; -fx-text-fill: #3f3f98;-fx-border-style: solid;-fx-border-color: #3f3f98;-fx-border-width: 1;");
                    }

                    // Cette méthode est appelée pour mettre à jour l'item de la cellule
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Si la cellule est vide, nous n'affichons rien
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Sinon, nous affichons le bouton
                            setGraphic(updateBtn);
                        }
                    }
                };
            }
        };

        // Utiliser le cellFactory pour les colonnes de boutons
        deleteTypeCol.setCellFactory(cellFactoryForDeleteType);
        updateTypeCol.setCellFactory(cellFactoryForUpdateType);

        // Ajouter les colonnes de boutons au tableau
        typeTiersTable.getColumns().add(deleteTypeCol);
        typeTiersTable.getColumns().add(updateTypeCol);


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

    private void deleteUser(Integer id){
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