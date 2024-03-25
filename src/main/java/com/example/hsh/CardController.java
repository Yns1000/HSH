package com.example.hsh;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import metier.Bien;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private Label prixDuBien;

    @FXML
    private Label titreDuBien;

    public void setDatas(Bien bien) {
        //Image image = new Image(bien.getImageSrc());
        Image image = new Image("https://edito.seloger.com/sites/default/files/styles/306x198/public/images/web/2024-03/Appartement-Paris-%C3%89t%C3%A9.jpg?h=b63d1a1c&itok=gZ_qznVD");
        imageView.setImage(image);

        // Création et application du clip arrondi
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        //rendre arrondis tout les coins de l'image
        clip.setArcWidth(40); // Ajustez selon les besoins
        clip.setArcHeight(40); // Ajustez selon les besoins

        imageView.setClip(clip);

        titreDuBien.setText(bien.getLibBien());
        prixDuBien.setText(bien.getPrixBase() + " €");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //imageView.setImage(new Image("..\\..\\..\\img\\App_sample.jpg"));

        // Création et application du clip arrondi
        //Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        //clip.setArcWidth(40); // Ajustez selon les besoins
        //clip.setArcHeight(40); // Ajustez selon les besoins
        //imageView.setClip(clip);


    }
}
