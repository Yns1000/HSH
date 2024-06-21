package metier;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "type_tiers")
public class TypeTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_TIERS", nullable = false)
    private Integer id;

    @Column(name = "LIB_TYPE_TIERS", length = 100)
    private String libTypeTiers;

    @ColumnDefault("0")
    @Column(name = "visible", nullable = false)
    private Boolean visible = false;

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibTypeTiers() {
        return libTypeTiers;
    }

    public void setLibTypeTiers(String libTypeTiers) {
        this.libTypeTiers = libTypeTiers;
    }

    @Override
    public String toString() {

        return getLibTypeTiers();

    }

}