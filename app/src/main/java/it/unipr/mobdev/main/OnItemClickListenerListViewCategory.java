package it.unipr.mobdev.main;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class OnItemClickListenerListViewCategory implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Context context = view.getContext();

        TextView textViewItem = (view.findViewById(R.id.rowtext));

        // get the clicked item name
        String listItemText = textViewItem.getText().toString();


        ((MainActivity) context).alertDialogCategories.cancel();
        ((MainActivity) context).showExpensesByCategories(listItemText);

    }


}
