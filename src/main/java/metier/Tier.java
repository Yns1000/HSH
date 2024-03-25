package metier;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tiers")
public class Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIERS", nullable = false)
    private Integer id;

    @Column(name = "NOM_TIERS", nullable = false, length = 100)
    private String nomTiers;

    @Column(name = "PRENOM_TIERS", length = 100)
    private String prenomTiers;

    @Column(name = "TEL_TIERS", nullable = false, length = 10)
    private String telTiers;

    @Column(name = "MAIL_TIERS", length = 100)
    private String mailTiers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TYPE_TIERS", nullable = false)
    private TypeTier typeTiers;

    @Column(name = "NAISSANCE_TIERS")
    private LocalDate naissanceTiers;

    @Column(name = "ADRESSE_TIERS", length = 100)
    private String adresseTiers;

    @Column(name = "CP_TIERS", length = 5)
    private String cpTiers;

    @Column(name = "VILLE_TIERS", length = 100)
    private String villeTiers;

    @Column(name = "PIECE_IDENTITE_TIERS", length = 1000)
    private String pieceIdentiteTiers;

    @Column(name = "RIB_TIERS", length = 1000)
    private String ribTiers;

    @Column(name = "NUMERO_SS_TIERS", length = 15)
    private String numeroSsTiers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public String getTelTiers() {
        return telTiers;
    }

    public void setTelTiers(String telTiers) {
        this.telTiers = telTiers;
    }

    public String getMailTiers() {
        return mailTiers;
    }

    public void setMailTiers(String mailTiers) {
        this.mailTiers = mailTiers;
    }

    public TypeTier getTypeTiers() {
        return typeTiers;
    }

    public void setTypeTiers(TypeTier typeTiers) {
        this.typeTiers = typeTiers;
    }

    public LocalDate getNaissanceTiers() {
        return naissanceTiers;
    }

    public void setNaissanceTiers(LocalDate naissanceTiers) {
        this.naissanceTiers = naissanceTiers;
    }

    public String getAdresseTiers() {
        return adresseTiers;
    }

    public void setAdresseTiers(String adresseTiers) {
        this.adresseTiers = adresseTiers;
    }

    public String getCpTiers() {
        return cpTiers;
    }

    public void setCpTiers(String cpTiers) {
        this.cpTiers = cpTiers;
    }

    public String getVilleTiers() {
        return villeTiers;
    }

    public void setVilleTiers(String villeTiers) {
        this.villeTiers = villeTiers;
    }

    public String getPieceIdentiteTiers() {
        return pieceIdentiteTiers;
    }

    public void setPieceIdentiteTiers(String pieceIdentiteTiers) {
        this.pieceIdentiteTiers = pieceIdentiteTiers;
    }

    public String getRibTiers() {
        return ribTiers;
    }

    public void setRibTiers(String ribTiers) {
        this.ribTiers = ribTiers;
    }

    public String getNumeroSsTiers() {
        return numeroSsTiers;
    }

    public void setNumeroSsTiers(String numeroSsTiers) {
        this.numeroSsTiers = numeroSsTiers;
    }

}