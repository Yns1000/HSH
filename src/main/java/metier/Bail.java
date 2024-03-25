package metier;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bail")
public class Bail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BAIL", nullable = false)
    private Integer id;

    @Column(name = "ID_TIERS_AGENT", nullable = false)
    private Integer idTiersAgent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ID_BIEN", nullable = false)
    private Bien idBien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_LOCATAIRE", nullable = false)
    private Tier idLocataire;

    @Column(name = "DATE_EFFET_BAIL", nullable = false)
    private LocalDate dateEffetBail;

    @Column(name = "DUREE_BAIL", nullable = false)
    private Integer dureeBail;

    @Column(name = "MOIS_RECONDUCTION_BAIL", nullable = false)
    private Integer moisReconductionBail;

    @Column(name = "DUREE_CONGE_DEPART", nullable = false)
    private Integer dureeCongeDepart;

    @Column(name = "LOYER_INITIAL", nullable = false, precision = 8, scale = 2)
    private BigDecimal loyerInitial;

    @Column(name = "MONTANT_DERNIER_LOYER", precision = 8, scale = 2)
    private BigDecimal montantDernierLoyer;

    @Column(name = "DATE_REVISION_ANNUELLE")
    private LocalDate dateRevisionAnnuelle;

    @Column(name = "COMMENTAIRE_CALCUL", length = 100)
    private String commentaireCalcul;

    @Column(name = "IRL_BASE", length = 20)
    private String irlBase;

    @Column(name = "VALEUR_IRL_BASE", precision = 5, scale = 2)
    private BigDecimal valeurIrlBase;

    @Column(name = "PROVISION_CHARGES", nullable = false, precision = 5, scale = 2)
    private BigDecimal provisionCharges;

    @Column(name = "OBJET_CHARGES", length = 100)
    private String objetCharges;

    @Column(name = "JOUR_ECHEANCE", nullable = false)
    private Integer jourEcheance;

    @Column(name = "MODE_REGLEMENT", length = 5)
    private String modeReglement;

    @Column(name = "MONTANT_DEPOT_GARANTIE", nullable = false, precision = 7, scale = 2)
    private BigDecimal montantDepotGarantie;

    @Column(name = "DATE_RECEPTION_PREAVIS_FIN")
    private LocalDate dateReceptionPreavisFin;

    @Column(name = "DATE_FIN_BAIL")
    private LocalDate dateFinBail;

    @Column(name = "VALIDE", nullable = false)
    private Integer valide;

    @Column(name = "ETAT_BAIL", length = 10)
    private String etatBail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTiersAgent() {
        return idTiersAgent;
    }

    public void setIdTiersAgent(Integer idTiersAgent) {
        this.idTiersAgent = idTiersAgent;
    }

    public Bien getIdBien() {
        return idBien;
    }

    public void setIdBien(Bien idBien) {
        this.idBien = idBien;
    }

    public Tier getIdLocataire() {
        return idLocataire;
    }

    public void setIdLocataire(Tier idLocataire) {
        this.idLocataire = idLocataire;
    }

    public LocalDate getDateEffetBail() {
        return dateEffetBail;
    }

    public void setDateEffetBail(LocalDate dateEffetBail) {
        this.dateEffetBail = dateEffetBail;
    }

    public Integer getDureeBail() {
        return dureeBail;
    }

    public void setDureeBail(Integer dureeBail) {
        this.dureeBail = dureeBail;
    }

    public Integer getMoisReconductionBail() {
        return moisReconductionBail;
    }

    public void setMoisReconductionBail(Integer moisReconductionBail) {
        this.moisReconductionBail = moisReconductionBail;
    }

    public Integer getDureeCongeDepart() {
        return dureeCongeDepart;
    }

    public void setDureeCongeDepart(Integer dureeCongeDepart) {
        this.dureeCongeDepart = dureeCongeDepart;
    }

    public BigDecimal getLoyerInitial() {
        return loyerInitial;
    }

    public void setLoyerInitial(BigDecimal loyerInitial) {
        this.loyerInitial = loyerInitial;
    }

    public BigDecimal getMontantDernierLoyer() {
        return montantDernierLoyer;
    }

    public void setMontantDernierLoyer(BigDecimal montantDernierLoyer) {
        this.montantDernierLoyer = montantDernierLoyer;
    }

    public LocalDate getDateRevisionAnnuelle() {
        return dateRevisionAnnuelle;
    }

    public void setDateRevisionAnnuelle(LocalDate dateRevisionAnnuelle) {
        this.dateRevisionAnnuelle = dateRevisionAnnuelle;
    }

    public String getCommentaireCalcul() {
        return commentaireCalcul;
    }

    public void setCommentaireCalcul(String commentaireCalcul) {
        this.commentaireCalcul = commentaireCalcul;
    }

    public String getIrlBase() {
        return irlBase;
    }

    public void setIrlBase(String irlBase) {
        this.irlBase = irlBase;
    }

    public BigDecimal getValeurIrlBase() {
        return valeurIrlBase;
    }

    public void setValeurIrlBase(BigDecimal valeurIrlBase) {
        this.valeurIrlBase = valeurIrlBase;
    }

    public BigDecimal getProvisionCharges() {
        return provisionCharges;
    }

    public void setProvisionCharges(BigDecimal provisionCharges) {
        this.provisionCharges = provisionCharges;
    }

    public String getObjetCharges() {
        return objetCharges;
    }

    public void setObjetCharges(String objetCharges) {
        this.objetCharges = objetCharges;
    }

    public Integer getJourEcheance() {
        return jourEcheance;
    }

    public void setJourEcheance(Integer jourEcheance) {
        this.jourEcheance = jourEcheance;
    }

    public String getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(String modeReglement) {
        this.modeReglement = modeReglement;
    }

    public BigDecimal getMontantDepotGarantie() {
        return montantDepotGarantie;
    }

    public void setMontantDepotGarantie(BigDecimal montantDepotGarantie) {
        this.montantDepotGarantie = montantDepotGarantie;
    }

    public LocalDate getDateReceptionPreavisFin() {
        return dateReceptionPreavisFin;
    }

    public void setDateReceptionPreavisFin(LocalDate dateReceptionPreavisFin) {
        this.dateReceptionPreavisFin = dateReceptionPreavisFin;
    }

    public LocalDate getDateFinBail() {
        return dateFinBail;
    }

    public void setDateFinBail(LocalDate dateFinBail) {
        this.dateFinBail = dateFinBail;
    }

    public Integer getValide() {
        return valide;
    }

    public void setValide(Integer valide) {
        this.valide = valide;
    }

    public String getEtatBail() {
        return etatBail;
    }

    public void setEtatBail(String etatBail) {
        this.etatBail = etatBail;
    }

}