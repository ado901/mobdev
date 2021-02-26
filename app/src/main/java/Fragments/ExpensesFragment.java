package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Database.Expenses;
import Utility.DbInstance;
import it.unipr.mobdev.main.R;
import it.unipr.mobdev.main.ShowAllRowsActivity;

import static Utility.Utility.intToStringDate;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExpensesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private DbInstance database;
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 5;
    private OnListFragmentInteractionListener mListener;




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpensesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExpensesFragment newInstance(int columnCount) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses_list, container, false);
        database = DbInstance.getInstance();
        showTable(view);

        return view;
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Expenses item);
    }
    public void showTable(final View view){
        List<Expenses> expensesList = database.getAllExpenses();
        int i = 1;
        TableLayout tableLayout = view.findViewById(R.id.list);
        for (Expenses expenses: expensesList){
            final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
            tr.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public  boolean onLongClick(View v)
                {
                    TextView id = (TextView)tr.getChildAt(0);
                    Long identificativo = Long.parseLong(id.getText().toString());
                    Expenses expenses =database.getAllExpensesById(identificativo);
                    String text;

                    if((text=expenses.getNote())==null){
                        Toast.makeText(getActivity().getApplicationContext(), "Nessuna nota presente", Toast.LENGTH_SHORT).show();

                    }else{
                        ((ShowAllRowsActivity)getActivity()).showDialog(text, null, 0, view, tr, (TableLayout)tr.getParent());

                    }
                    return true;
                }
            });
            tr.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view){

                    TextView textview;

                    textview= (TextView)tr.getChildAt(0);


                    ((ShowAllRowsActivity)getActivity()).showDialog(null, Long.parseLong(textview.getText().toString()), 1,view, tr, (TableLayout)tr.getParent() );

                }
            });

            TextView id= tr.findViewById(R.id.textView28);
            TextView  number = tr.findViewById(R.id.textView21);
            TextView categoria = tr.findViewById(R.id.textView27);
            TextView importo = tr.findViewById(R.id.textView22);
            TextView  currency = tr.findViewById(R.id.textView23);
            TextView data = tr.findViewById(R.id.textView24);


            number.setText(String.valueOf(i));
            categoria.setText(expenses.getCategory());
            importo.setText(expenses.getAmount().toString());
            currency.setText(expenses.getCurrency());
            data.setText(intToStringDate(expenses.getDate()));
            id.setText(expenses.getId().toString());
            i++;
            tableLayout.addView(tr);
        }



    }

}
