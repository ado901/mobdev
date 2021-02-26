package Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(version = 1, entities = {Expenses.class, Categorie.class}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;

    public static AppDatabase getAppDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Database")
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }

    abstract public ExpensesDao expensesDao();

    abstract public CategorieDao categorieDao();

}