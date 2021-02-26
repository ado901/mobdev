package it.unipr.mobdev.main;


import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Adapters.ArrayAdapterCategory;
import Adapters.ArrayAdapterExpenses;
import Database.AppDatabase;
import Database.Categorie;
import Database.Expenses;
import Utility.CSVUtils;
import Utility.DbInstance;

import static Utility.Utility.intToStringDate;
import static Utility.Utility.stringToIntDate;


public class MainActivity extends AppCompatActivity {
    public static final String CATEGORY = "category";
    public static final String CURRENCY = "currency";
    public static final String IMPORTO = "currency";

    private ImageButton button;
    private TextView resultText;

    AlertDialog alertDialogCategories;
    AlertDialog alertDialogExpenses;

    private static DbInstance database;
    private Calendar myCalendar = Calendar.getInstance();
    public static String TAG = "MainActivity";
    private static final int READ_REQUEST_CODE = 42;

    /**
     * SETTO L'EVENTO DEL CLICK SUL BOTTONE DELLA NUOVA CATEGORIA, VISUALIZZAZIONE DELLE CATEGORIE
     * E SETTING DELLA TOOLBAR
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "Database").allowMainThreadQueries().build();
        database = DbInstance.getInstanceByDB(db);
         if (db.categorieDao().getAllCategories().size() == 0)
         {
             database.insertAllCategorie(Categorie.populateData());
         }
        button = findViewById(R.id.newcategory);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showInputDialog();
            }




        });
        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.showAllRows:
                        showCategories();
                        break;
                }
            }
        };
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        findViewById(R.id.showAllRows).setOnClickListener(handler);
    }

    /**
     * setting del layout per il toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * gestione eventi click sul menu della toolbar, se si seleziona la freccia in alto si attiva la procedura di export delle dispositive
     * in db in un file csv
     * se si seleziona la freccia in basso si attiva la procedura di import personalizzato da un file csv in db
     * se si seleziona info si apre l'activity per visualizzare le info dell'app
     * se si seleziona visualizza tutte le dispositive si apre l'activity in cui si vedono tutte le dispositive in un fragment
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_export) {
            // get prompts.xml view
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Export");

            // Setting Dialog Message
            alertDialog.setMessage("Vuoi esportare i dati su un file csv? ");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    try {
                        if (isStoragePermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File f= new File(folder, "csv");
                            f.mkdir();

                            String csvFile = folder.toString()+ "/csv/expenses.csv";
                            FileWriter writer = new FileWriter(csvFile);



                            List<Expenses> expensesList = database.getAllExpenses();
                            for (int i = 0; i < expensesList.size(); i++) {
                                CSVUtils.writeLine(writer, Arrays.asList(expensesList.get(i).getCategory(),
                                        expensesList.get(i).getAmount().toString(), expensesList.get(i).getCurrency(), intToStringDate(expensesList.get(i).getDate()),
                                        expensesList.get(i).getNote()), ',');
                            }
                            writer.flush();
                            writer.close();
                            //Toast.makeText(MainActivity.this, "Generato file csv", Toast.LENGTH_SHORT).show();
                        }
                        } catch(IOException e){
                            e.printStackTrace();
                        }

                }

            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = alertDialog.create();
            alert.show();

            return true;
        }
        if (id == R.id.action_import){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Import");

            // Setting Dialog Message
            alertDialog.setMessage("Vuoi importare i dati da un csv? ");

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (isStoragePermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        performFileSearch();

                    }
                }

            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }

        if (id == R.id.info){
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }

        if (id== R.id.showall){

            if (database.getAllExpenses().size()==0){
                Toast.makeText(this, "NESSUNA DISPOSITIVA", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(this,ShowAllRowsActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.categoryremove){
            List<String> categorieList= database.getAllCategorieString();
            String[] categorieArray = categorieList.toArray(new String[categorieList.size()]);

            ArrayAdapterCategory adapter = new ArrayAdapterCategory(this, R.layout.row, categorieArray);
            final ListView listViewItems = new ListView(this);
            listViewItems.setAdapter(adapter);
            listViewItems.setOnItemClickListener(OnItemClickListenerTableCategory.newInstance(0));
            alertDialogCategories = new AlertDialog.Builder(MainActivity.this)
                    .setView(listViewItems)
                    .setTitle("Categorie")
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * al click sul bottone per inserire una nuova dispositiva viene aperta una nuova activity
     * @param view
     */
    public void callInsertNew(View view){
        Intent intent = new Intent(this,InsertNewActivity.class);
        startActivity(intent);

    }

    /**
     * al click sul pulsante per inserire una nuova categoria viene visualizzata questa finestra dove puoi inserirne una
     */

    public void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText =promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String categoria = editText.getText().toString();
                        List <String> list = database.findAllCategorieByCategoria(categoria);
                        if (list.size()!=0 || categoria.isEmpty()){
                            Toast.makeText(MainActivity.this, "CATEGORIA NON VALIDA", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Categorie row = new Categorie();
                            row.setCategorie(categoria);
                            database.insertAllCategorie(row);
                        }


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * al click sul pulsante per visualizzare tutte le dispositive tramite categoria
     * si apre una finestra iniziale dove si sceglie una categoria, come onclick è stato settato una classe esterna
     */
    public void showCategories(){

        List<String> categorieList= database.getAllCategorieString();
        String[] categorieArray = categorieList.toArray(new String[categorieList.size()]);

        ArrayAdapterCategory adapter = new ArrayAdapterCategory(this, R.layout.row, categorieArray);

        final ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(OnItemClickListenerTableCategory.newInstance(1));
        listViewItems.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public  boolean onLongClick(View v)
            {
                TextView textView = (TextView) listViewItems.getSelectedItem();
                listViewItems.removeView(textView);
                return true;

            }
        });

        alertDialogCategories = new AlertDialog.Builder(MainActivity.this)
                .setView(listViewItems)
                .setTitle("Categorie")
                .show();

    }

    /**
     * metodo per visualizzare una finestra con una tabella di dispositive. Chiamato da OnItemCLickListenerTableCategory
     * @param category
     */
    public void showExpensesByCategories(String category){


        List<Expenses> expensesList = database.getAllExpensesByCategory(category);
        if (expensesList.size()==0){
            Toast.makeText(this, "NESSUNA DISPOSITIVA", Toast.LENGTH_SHORT).show();
        }
        else{
            Expenses[] expensesArray = expensesList.toArray(new Expenses[expensesList.size()]);
            ArrayAdapterExpenses adapter = new ArrayAdapterExpenses(this, R.layout.tabledialog, expensesArray);

            ListView listViewItems = new ListView(this);
            listViewItems.setAdapter(adapter);

            alertDialogExpenses = new AlertDialog.Builder(MainActivity.this)
                    .setView(listViewItems)
                    .setTitle("Dispositive")
                    .show();
        }


    }

    /**
     * metodo chiamato dal bottone per visualizzare le dispositive per data
     * @param v
     */
    public void pickDate(View v){



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyyMMdd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                showExpensesByDate(Integer.parseInt(sdf.format(myCalendar.getTime())));




            }

        };
        new DatePickerDialog(MainActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * chiamato da pickDate(), visualizza le dispositive per la data selezionata
     * @param date
     */
    public void showExpensesByDate(Integer date){

        List<Expenses> expensesList = database.getAllExpensesByDate(date);
        if (expensesList.size()==0){
            Toast.makeText(this, "NESSUNA DISPOSITIVA", Toast.LENGTH_SHORT).show();
        }
        else{
            Expenses[] expensesArray = expensesList.toArray(new Expenses[expensesList.size()]);
            ArrayAdapterExpenses adapter = new ArrayAdapterExpenses(MainActivity.this, R.layout.tabledialog, expensesArray);

            ListView listViewItems = new ListView(MainActivity.this);
            listViewItems.setAdapter(adapter);

            alertDialogExpenses = new AlertDialog.Builder(MainActivity.this)
                    .setView(listViewItems)
                    .setTitle("Dispositive")
                    .show();
        }


    }


    /**
     * metodo che controlla se i permessi sono stati ottenuti, in caso negativo viene invocato il permesso
     * @param permission
     * @return
     */
    public  boolean isStoragePermissionGranted(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
                isStoragePermissionGranted(permission);
                return true;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    /**
     * chiamato dalla toolbar per importare un file, chiama un'activity presente di default
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(Intent.createChooser(intent, "Open CSV"), READ_REQUEST_CODE);
    }

    /**
     * metodo che viene invocato quando l'utente sceglie un file. Esegue la read del file e l'import
     * @param requestCode
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == MainActivity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());


                File file = new File(uri.getPath());
                InputStream inputStream = null;

                try {
                    inputStream = getContentResolver().openInputStream(uri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                List<Expenses> resultList = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream));

                try {
                    String csvLine;
                    while ((csvLine = reader.readLine()) != null) {
                        String[] row = csvLine.split(",");
                        Expenses expenses = new Expenses(null, null, null, null, null);
                        for (int i = 0; i < row.length; i++) {

                            switch (i) {
                                case 0:
                                    expenses.setCategory(row[i]);
                                    Log.d(TAG, row[i]);
                                    break;

                                case 1:
                                    expenses.setAmount(Float.parseFloat(row[i]));
                                    Log.d(TAG, row[i]);
                                    break;

                                case 2:
                                    expenses.setCurrency(row[i]);
                                    Log.d(TAG, row[i]);
                                    break;

                                case 3:
                                    expenses.setDate(stringToIntDate(row[i]));
                                    Log.d(TAG, row[i]);
                                    break;

                                case 4:
                                    expenses.setNote(row[i]);
                                    Log.d(TAG, row[i]);
                                    break;
                            }

                        }
                        resultList.add(expenses);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException("Error in reading CSV file: " + ex);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Error while closing input stream: " + e);
                    }
                }
                for (Expenses expenses : resultList) {
                    Log.d(TAG, "dispositiva con categoria " + expenses.getCategory());
                    database.insertAllExpenses(expenses);

                }
            }
        }
    }

}