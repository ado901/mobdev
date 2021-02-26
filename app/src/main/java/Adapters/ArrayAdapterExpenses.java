package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import Database.Expenses;
import it.unipr.mobdev.main.R;

import static Utility.Utility.intToStringDate;

public class ArrayAdapterExpenses extends ArrayAdapter<Expenses> {
    Context mContext;
    int layoutResourceId;
    Expenses data[] = null;

    public ArrayAdapterExpenses(Context mContext, int layoutResourceId, Expenses[] data) {
        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);


        }

        Expenses objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textView9);
        TextView textView2 = convertView.findViewById(R.id.textView8);
        TextView textView3 = convertView.findViewById(R.id.textView6);
        TextView textView4 = convertView.findViewById(R.id.textView7);

        textViewItem.setText(objectItem.getAmount().toString());
        textView2.setText(objectItem.getCurrency());
        textView3.setText(intToStringDate(objectItem.getDate()));
        textView4.setText(objectItem.getNote());

        return convertView;
    }
}
