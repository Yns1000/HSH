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


    @Column(name = "prixBase", nullable = false)
    private Float prixBase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PROPRIETAIRE", nullable = false)
    private Tier idProprietaire;

    @Column(name = "Note_5", nullable = false)
    private Integer note5;

    @Column(name = "Note_1", nullable = false)
    private Integer note1;

    @Column(name = "Note_2", nullable = false)
    private Integer note2;

    @Column(name = "Note_3", nullable = false)
    private Integer note3;

    @Column(name = "Note_4", nullable = false)
    private Integer note4;

    @Column(name = "LIB_VOIE", nullable = false, length = 200)
    private String libVoie;

    @Column(name = "TYPE_BIEN", nullable = false, length = 250)
    private String typeBien;

    @Column(name = "REDUCTION", nullable = false)
    private Double reduction;

    @Column(name = "TypeVente", nullable = false, length = 100)
    private String typeVente;

    @Column(name = "Description", nullable = false, length = 2000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(String typeVente) {
        this.typeVente = typeVente;
    }

    public Double getReduction() {
        return reduction;
    }

    public void setReduction(Double reduction) {
        this.reduction = reduction;
    }

    public String getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(String typeBien) {
        this.typeBien = typeBien;
    }

    public String getLibVoie() {
        return libVoie;
    }

    public void setLibVoie(String libVoie) {
        this.libVoie = libVoie;
    }

    public Integer getNote4() {
        return note4;
    }

    public void setNote4(Integer note4) {
        this.note4 = note4;
    }

    public Integer getNote3() {
        return note3;
    }

    public void setNote3(Integer note3) {
        this.note3 = note3;
    }

    public Integer getNote2() {
        return note2;
    }

    public void setNote2(Integer note2) {
        this.note2 = note2;
    }

    public Integer getNote1() {
        return note1;
    }

    public void setNote1(Integer note1) {
        this.note1 = note1;
    }

    public Integer getNote5() {
        return note5;
    }

    public void setNote5(Integer note5) {
        this.note5 = note5;
    }

    public Tier getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(Tier idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public Float getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(Float prixBase) {
        this.prixBase = prixBase;
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