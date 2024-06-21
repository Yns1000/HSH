package com.example.hsh;

import jakarta.persistence.criteria.CriteriaBuilder;

public class UserSession {
    private static UserSession instance;

    private String username; // ou tout autre détail de l'utilisateur que vous souhaitez stocker

    private Integer id_user;

    private Integer id_tier_to_edit;

    private Integer id_type_tier_to_edit;

    private Integer id_bien_to_view;

    private Integer id_bien_to_edit;

    private String toSearch;

    private Integer id_tier_to_view;

    public String getTypeBienToSearch() {
        return typeBienToSearch;
    }

    public void setTypeBienToSearch(String typeBienToSearch) {
        this.typeBienToSearch = typeBienToSearch;
    }

    public String getTypeVenteToSearch() {
        return typeVenteToSearch;
    }

    public void setTypeVenteToSearch(String typeVenteToSearch) {
        this.typeVenteToSearch = typeVenteToSearch;
    }

    public String getVilleToSearch() {
        return villeToSearch;
    }

    public void setVilleToSearch(String villeToSearch) {
        this.villeToSearch = villeToSearch;
    }

    public String getSurfaceToSearch() {
        return surfaceToSearch;
    }

    public void setSurfaceToSearch(String surfaceToSearch) {
        this.surfaceToSearch = surfaceToSearch;
    }

    public String getPrixBienToSearch() {
        return prixBienToSearch;
    }

    public void setPrixBienToSearch(String prixBienToSearch) {
        this.prixBienToSearch = prixBienToSearch;
    }

    public String getNbPieceToSearch() {
        return nbPieceToSearch;
    }

    public void setNbPieceToSearch(String nbPieceToSearch) {
        this.nbPieceToSearch = nbPieceToSearch;
    }

    private String nbPieceToSearch;

    private String prixBienToSearch;

    private String surfaceToSearch;

    private String villeToSearch;

    private String typeVenteToSearch;

    private String typeBienToSearch;


    private UserSession() {
        // Constructor privé pour empêcher l'instantiation
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void clearSession() {
        username = null;
        id_user = null;
    }

    public boolean isLoggedIn() {
        return username != null;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_tier_to_edit() {
        return id_tier_to_edit;
    }

    public void setId_tier_to_edit(Integer id_tier_to_edit) {
        this.id_tier_to_edit = id_tier_to_edit;
    }

    public Integer getId_type_tier_to_edit() {
        return id_type_tier_to_edit;
    }

    public void setId_type_tier_to_edit(Integer id_type_tier_to_edit) {
        this.id_type_tier_to_edit = id_type_tier_to_edit;
    }

    public Integer getId_bien_to_view() {
        return id_bien_to_view;
    }

    public void setId_bien_to_view(Integer id_bien_to_view) {
        this.id_bien_to_view = id_bien_to_view;
    }

    public Integer getId_bien_to_edit() {
        return id_bien_to_edit;
    }

    public void setId_bien_to_edit(Integer id_bien_to_edit) {
        this.id_bien_to_edit = id_bien_to_edit;
    }

    public String getToSearch() {
        return toSearch;
    }

    public void setToSearch(String toSearch) {
        this.toSearch = toSearch;
    }

    public Integer getId_tier_to_view() {
        return id_tier_to_view;
    }

    public void setId_tier_to_view(Integer id_tier_to_view) {
        this.id_tier_to_view = id_tier_to_view;
    }
}

