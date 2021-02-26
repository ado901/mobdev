package Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ExpensesDao {
    @Query("SELECT * FROM expenses")
    List<Expenses> getAll();

    @Query("SELECT * FROM expenses WHERE id IN (:userIds)")
    List<Expenses> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM expenses WHERE category = :first")
    List<Expenses> findByCategory(String first);

    @Query("Select * from expenses where date = :date")
    List<Expenses> findByDate(Integer date);

    @Query("Select * from expenses where id =:id")
    Expenses findById(Long id);

    @Insert
    void insertAll(Expenses... users);

    @Delete
    void delete(Expenses user);
}