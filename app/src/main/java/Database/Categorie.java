package Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"categorie"},
        unique = true)})
public class Categorie {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "categorie")
    private String categorie;

    public Categorie(String categorie) {
        this.categorie = categorie;
    }

    public Categorie() {
    }

    public static Categorie[] populateData() {
        return new Categorie[]{
                new Categorie("Sport"),
                new Categorie("Tasse"),
                new Categorie("Hobby"),
                new Categorie("Musica"),
                new Categorie("Elettronica"),
                new Categorie("Bollette"),
                new Categorie("Multe")
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
