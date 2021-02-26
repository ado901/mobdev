package it.unipr.mobdev.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import Database.Expenses;
import Fragments.ExpensesFragment;
import Utility.DbInstance;

public class ShowAllRowsActivity extends AppCompatActivity {

    DbInstance database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_rows);

        database= DbInstance.getInstance();

        Toolbar toolbar =findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new ExpensesFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);

                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(String text, final Long identificativo, int task, final View view, final TableRow tableRow, final TableLayout tableLayout){

        if (task==0){
            new AlertDialog.Builder(ShowAllRowsActivity.this)
                    .setMessage(text)
                    .setTitle("Categorie")
                    .show();
        }
        else if (task==1){
            new AlertDialog.Builder(ShowAllRowsActivity.this)
                    .setMessage("Vuoi cancellare questa dispositiva?")
                    .setTitle("Elimina dispositiva")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Expenses expenses = database.getAllExpensesById(identificativo);
                            database.deleteAllExpenses(expenses);
                            tableLayout.removeView(tableRow);

                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .show();
        }

    }
}
