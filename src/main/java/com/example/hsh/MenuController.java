package com.example.hsh;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private HBox loginButton;

    @FXML
    private HBox settingsButton;

    @FXML
    private Label stateConnexion;

    @FXML
    private HBox favItemsButton;

    @FXML
    private HBox myitemsButton;

    @FXML
    private HBox famousItemsButton;

    @FXML
    private HBox newItemsButton;

    @FXML
    private HBox myAccountButton;

    @FXML
    private HBox categButtons;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        //si on appuie sur le bouton favItemsButton
        if (favItemsButton != null) {
            favItemsButton.setOnMouseClicked(event -> {
                //si on est connecté

                if (UserSession.getInstance().isLoggedIn()) {
                    try {
                        loadScene("fav.fxml", favItemsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                } else {
                    try {
                        loadScene("Connexion.fxml", favItemsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                }

            });
        }

        //si on appuie sur le bouton newItemsButton
        if (newItemsButton != null) {
            newItemsButton.setOnMouseClicked(event -> {
                //si on est connecté
                try {
                    loadScene("newItems.fxml", newItemsButton);
                } catch (IOException ex) {
                    handleException(ex);
                }
            });
        }

        //si on appuie sur le bouton categButtons
        if (categButtons != null) {
            categButtons.setOnMouseClicked(event -> {
                //si on est connecté
                try {
                    loadScene("categ.fxml", categButtons);
                } catch (IOException ex) {
                    handleException(ex);
                }
            });
        }

        //si on appuie sur le bouton myAccountButton
        if (myAccountButton != null) {
            myAccountButton.setOnMouseClicked(event -> {
                //si on est connecté
                if (UserSession.getInstance().isLoggedIn()) {
                    try {
                        loadScene("viewProfile.fxml", myAccountButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                } else {
                    try {
                        loadScene("Connexion.fxml", myAccountButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                }
            });
        }

        //si on appuie sur le bouton settingsButton
        if (settingsButton != null) {
            settingsButton.setOnMouseClicked(event -> {
                //si on est connecté
                if (UserSession.getInstance().isLoggedIn()) {
                    try {
                        loadScene("settings.fxml", settingsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                } else {
                    try {
                        loadScene("Connexion.fxml", settingsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                }
            });
        }

        //si on appuie sur le bouton myitemsButton
        if (myitemsButton != null) {
            myitemsButton.setOnMouseClicked(event -> {
                //si on est connecté
                if (UserSession.getInstance().isLoggedIn()) {
                    try {
                        loadScene("myItems.fxml", myitemsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                } else {
                    try {
                        loadScene("Connexion.fxml", myitemsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                }
            });
        }

        //si on appuie sur le bouton famousItemsButton
        if (famousItemsButton != null) {
            famousItemsButton.setOnMouseClicked(event -> {
                //si on est connecté
                    try {
                        loadScene("popular.fxml", famousItemsButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }

            });
        }

        System.out.println("TEEEEEEEEE");

        if (UserSession.getInstance().isLoggedIn()) {
            stateConnexion.setText("Deconnexion");
        } else {
            stateConnexion.setText("Connexion");
        }

        if (loginButton != null) {
            loginButton.setOnMouseClicked(event -> {
                if (UserSession.getInstance().isLoggedIn()) {
                    UserSession.getInstance().clearSession();
                    try {
                        loadScene("hello-view.fxml", loginButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                } else {
                    try {
                        loadScene("Connexion.fxml", loginButton);
                    } catch (IOException ex) {
                        handleException(ex);
                    }
                }
            });
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

}