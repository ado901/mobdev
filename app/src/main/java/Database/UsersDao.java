package Database;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM users")
    List<Expenses> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    List<Expenses> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE email = :first and password = :second")
    List<Expenses> findByEmailPassword(String first, String second);

    @Insert
    void insertAll(Users... users);

    @Delete
    void delete(Users user);

}
