package it.unipr.mobdev.main;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Database.Categorie;
import Utility.DbInstance;

public class OnItemClickListenerTableCategory implements AdapterView.OnItemClickListener {

    private DbInstance database;
    private static Integer task;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Context context = view.getContext();

        if (view.getId() == R.id.rowtext){
            if (task ==1){
                TextView textViewItem = (view.findViewById(R.id.rowtext));

                // get the clicked item name
                String listItemText = textViewItem.getText().toString();


                ((MainActivity) context).alertDialogCategories.cancel();
                ((MainActivity) context).showExpensesByCategories(listItemText);
            }
            if (task ==0){
                database= DbInstance.getInstance();

                TextView textView = view.findViewById(view.getId());
                List<Categorie> categories = database.findAllCategorieObjByCategory(textView.getText().toString());
                database.removecategorie(categories.get(0));
                Toast.makeText(view.getContext(), "Categoria cancellata", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).alertDialogCategories.cancel();
            }


        }






    }

    public static OnItemClickListenerTableCategory newInstance(Integer task1){
        task = task1;
       return new OnItemClickListenerTableCategory();

    }


}
