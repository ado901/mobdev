package Utility;

import java.util.List;

import Database.AppDatabase;
import Database.Categorie;
import Database.CategorieDao;
import Database.Expenses;
import Database.ExpensesDao;

public class DbInstance {
    private static DbInstance instance = null;
    private AppDatabase db;
    private ExpensesDao expensesDao;
    private CategorieDao categorieDao;

    private DbInstance(AppDatabase db) {
        this.db = db;
        this.expensesDao = db.expensesDao();
        this.categorieDao = db.categorieDao();
    }

    public static DbInstance getInstanceByDB(AppDatabase db) {
        if (instance == null) {
            instance = new DbInstance(db);
        }
        return instance;

    }

    public static DbInstance getInstance() {
        return instance;
    }

    public void insertAllExpenses(Expenses... expenses) {
        expensesDao.insertAll(expenses);

    }

    public void insertAllCategorie(Categorie... categorie) {
        categorieDao.insertAllCategories(categorie);
    }

    public List<Expenses> getAllExpenses() {
        return expensesDao.getAll();
    }

    public List<Categorie> getAllCategorie() {
        return categorieDao.getAll();
    }

    public List<Expenses> getAllExpensesByCategory(String category) {
        return expensesDao.findByCategory(category);
    }

    public List<Expenses> getAllExpensesByDate(Integer date) {
        return expensesDao.findByDate(date);

    }

    public Expenses getAllExpensesById(Long id) {
        return expensesDao.findById(id);
    }

    public List<String> getAllCategorieString() {
        return categorieDao.getAllCategories();
    }

    public void deleteAllExpenses(Expenses expenses) {
        expensesDao.delete(expenses);
    }
    public List<String> findAllCategorieByCategoria(String categorie){
        return categorieDao.getAllCategoriesByCategorie(categorie);
    }
    public List<Categorie> findAllCategorieObjByCategory(String categorie){
        return categorieDao.getAllCategorieObjByCategorie(categorie);
    }
    public void removecategorie(Categorie categoria){
        categorieDao.delete(categoria);
    }

}
