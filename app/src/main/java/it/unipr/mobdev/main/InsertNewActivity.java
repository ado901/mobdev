package it.unipr.mobdev.main;


import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Database.AppDatabase;
import Database.CategorieDao;
import Database.Expenses;
import Utility.DbInstance;

import static Utility.Utility.stringToIntDate;


public class InsertNewActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText edittext;
    private Integer data;
    private String currency;
    private String categoria;
    private String note;
    private Float importo;
    private ArrayAdapter<String> arrayAdapter;
    private Spinner spinner2;
    private EditText importotext;
    private EditText notetext;
    private Calendar myCalendar = Calendar.getInstance();
    private Toolbar toolbar;
    private DbInstance database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insert_new);

        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        database = DbInstance.getInstance();
        spinner = findViewById(R.id.spinner);
        edittext = findViewById(R.id.Birthday);
        spinner2 = findViewById(R.id.spinner2);
        importotext = findViewById(R.id.importo);
        notetext = findViewById(R.id.note);

        createSpinner();


        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InsertNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);

                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirm(View view) {
        String datastring = edittext.getText().toString();
        //2018/09/05
        try {

            if (importotext.getText().toString().equals("") || datastring.equals("")) {
                throw new NullPointerException();
            }


            data = stringToIntDate(datastring);
            categoria = spinner.getSelectedItem().toString();
            currency = spinner2.getSelectedItem().toString();
            importo = Float.valueOf(importotext.getText().toString());
            note = notetext.getText().toString();

            Expenses expense = new Expenses(importo, currency, categoria, data, note);
            database.insertAllExpenses(expense);
            Snackbar.make(findViewById(R.id.relativeLayout), "Dispositiva inserita",
                    Snackbar.LENGTH_SHORT)
                    .show();
            edittext.setText(null);
            importotext.setText(null);
            notetext.setText(null);

        } catch (NullPointerException e) {
            Toast.makeText(this, "Compila tutti i form", Toast.LENGTH_SHORT).show();

        }

    }


    private void createSpinner() {

        AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Database").allowMainThreadQueries().build();
        CategorieDao categorie = database.categorieDao();
        List<String> categories = categorie.getAllCategories();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.row, categories);


        spinner.setAdapter(arrayAdapter);


    }


    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }





}

