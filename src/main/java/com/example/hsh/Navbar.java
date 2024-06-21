package com.example.hsh;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Navbar implements Initializable {

    @FXML
    private Label nameProfile;

    @FXML
    private HBox goToProfile;

    @FXML
    private ImageView Accueil;

    @FXML
    private TextField searchField;

    @FXML
    private Label searchButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchButton.setOnMouseClicked(event -> {
            //On récupère le texte de la barre de recherche
            String search = searchField.getText();

            //On stocke le texte de la barre de recherche dans la session
            UserSession.getInstance().setToSearch(search);

            //On charge la page itemsToSearch.fxml
            try {
                loadScene("toSearch.fxml", searchButton);
            } catch (IOException ex) {
                handleException(ex);
            }

            //
        });

        //si je suis pas connecté
        if (UserSession.getInstance().isLoggedIn()) {
            nameProfile.setText(UserSession.getInstance().getUsername());
            setOnClickLoadScene(goToProfile, "viewProfile.fxml");
            // Logique lorsque l'utilisateur est connecté
        } else {
            // Logique lorsque l'utilisateur n'est pas connecté
            setOnClickLoadScene(goToProfile, "Connexion.fxml");
            nameProfile.setText("Se connecter");
        }

        setOnClickLoadScene(Accueil, "hello-view.fxml");
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
}