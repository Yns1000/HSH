package metier;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bien")
public class Bien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BIEN", nullable = false)
    private Integer id;

    @Column(name = "LIB_BIEN", nullable = false, length = 50)
    private String libBien;

    @Column(name = "ADR_NO_VOIE", length = 7)
    private String adrNoVoie;

    @Column(name = "NOM_VOIE", length = 100)
    private String nomVoie;

    @Column(name = "CODE_POSTAL", length = 5)
    private String codePostal;

    @Column(name = "COMMUNE", length = 100)
    private String commune;

    @Column(name = "COMPLEMENT_ADRESSE", length = 5)
    private String complementAdresse;

    @Column(name = "DATE_CREATION", nullable = false)
    private Instant dateCreation;

    @Column(name = "DATE_DERNIERE_MAJ", nullable = false)
    private Instant dateDerniereMaj;

    @Column(name = "SURFACE_HABITABLE", nullable = false, precision = 5, scale = 1)
    private BigDecimal surfaceHabitable;

    @Column(name = "NOMBRE_PIECES", nullable = false)
    private Integer nombrePieces;

    @Column(name = "ImageSrc", nullable = false)
    private String imageSrc;

    @Column(name = "prixBase", nullable = false)
    private Float prixBase;

    public Float getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(Float prixBase) {
        this.prixBase = prixBase;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibBien() {
        return libBien;
    }

    public void setLibBien(String libBien) {
        this.libBien = libBien;
    }

    public String getAdrNoVoie() {
        return adrNoVoie;
    }

    public void setAdrNoVoie(String adrNoVoie) {
        this.adrNoVoie = adrNoVoie;
    }

    public String getNomVoie() {
        return nomVoie;
    }

    public void setNomVoie(String nomVoie) {
        this.nomVoie = nomVoie;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateDerniereMaj() {
        return dateDerniereMaj;
    }

    public void setDateDerniereMaj(Instant dateDerniereMaj) {
        this.dateDerniereMaj = dateDerniereMaj;
    }

    public BigDecimal getSurfaceHabitable() {
        return surfaceHabitable;
    }

    public void setSurfaceHabitable(BigDecimal surfaceHabitable) {
        this.surfaceHabitable = surfaceHabitable;
    }

    public Integer getNombrePieces() {
        return nombrePieces;
    }

    public void setNombrePieces(Integer nombrePieces) {
        this.nombrePieces = nombrePieces;
    }

}