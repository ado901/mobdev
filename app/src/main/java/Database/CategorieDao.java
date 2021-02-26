package Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

@Dao
public interface CategorieDao {
    @Query("SELECT * FROM categorie")
    List<Categorie> getAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT categorie FROM categorie")
    List<String> getAllCategories();

    @Query("select categorie from categorie where categorie = :categorie")
    List<String> getAllCategoriesByCategorie(String categorie);
    @Query("select *  from categorie where categorie = :categorie")
    List<Categorie> getAllCategorieObjByCategorie(String categorie);

    @Insert
    void insertAllCategories(Categorie... categorie);

    @Delete
    void delete(Categorie categorie);


}
