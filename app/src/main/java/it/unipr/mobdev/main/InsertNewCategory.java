package it.unipr.mobdev.main;

import android.arch.persistence.room.Room;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import Database.AppDatabase;
import Database.Categorie;
import Database.CategorieDao;

public class InsertNewCategory extends AppCompatActivity {

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_category);
    }

    public void onPressed(View view)
    {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "Database").allowMainThreadQueries().build();
        CategorieDao categorie=db.categorieDao();
        EditText categorytext = findViewById(R.id.editText);
        String categoria = categorytext.getText().toString();
        Categorie row = new Categorie();
        row.setCategorie(categoria);
        categorie.insertAllCategories(row);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Categoria inserita").setMessage("Categoria inserita con successo")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
