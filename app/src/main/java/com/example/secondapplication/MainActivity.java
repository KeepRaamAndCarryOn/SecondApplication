package com.example.secondapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Calculator Buttons
    Button numericButton;
    Button operatorButton;

    //Calculator display
    TextView eqTextView;   //bottom line
    TextView eqTopView;    //top line

    //Calculator display strings
    String bottom_str;
    String top_str;

    String rhs_str;  //buffer string to capture RHS (without operator)

    //Numeric values for calculator
    Double LHS;
    Double RHS;

    //Operations
    enum Op {  //enum of operations
        NONE, ADD, MINUS, MULTIPLY, DIVIDE, EQUALS
    }

    Op op;  //Holds the operation to perform upon next operator click or equals click
    ArrayMap<Integer, Op> op_table;  //Maps button id to operation

    //PaidSpent List views
    LinearLayout paidColumn;
    ScrollView paidScroll;

    //Entry selection
    public class Entry {
        Double amount;
        Act act;
    }
    TextView selectedView;   //Variable to hold clicked view (only valid in onClick method)
    int selectedEntryint;    //Index of Selected view
    Entry selectedEntry;     //Selected Entry

    //Handling entries
    enum Act {
        PAID, SPENT
    }
    ArrayMap<Integer, Act> act_table; //Maps button id to Paid/Spent action
    ArrayMap<Integer, Entry> paidSpentMap;  //Holds the list of entry based on index of view in PaidColumn

    //Log
    private static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eqTextView = (TextView) findViewById(R.id.eqTextBox);
        eqTopView = (TextView) findViewById(R.id.eqTop);

        paidColumn = findViewById(R.id.paidLinearLayout);
        paidScroll = findViewById(R.id.paidColumnView);

        op_table = new ArrayMap<>();
        op_table.put(R.id.buttonPlus, Op.ADD);
        op_table.put(R.id.buttonMinus, Op.MINUS);
        op_table.put(R.id.buttonEqual, Op.EQUALS);
        op_table.put(R.id.buttonMulti, Op.MULTIPLY);
        op_table.put(R.id.buttonDivide, Op.DIVIDE);

        act_table = new ArrayMap<>();
        act_table.put(R.id.paidButtonView, Act.PAID);
        act_table.put(R.id.spentButtonView, Act.SPENT);

        bottom_str = "";
        top_str = "";
        rhs_str = "";

        LHS = 0.0;
        RHS = 0.0;

        op = Op.NONE;

        selectedView = null;
        selectedEntryint = -1;
        selectedEntry = null;

        paidSpentMap = new ArrayMap<>();
        paidSpentMap.clear();

    }


    public void numericClick(View view) {
        numericButton = (Button) view;

        if (op.equals(Op.NONE)) {   //No Operation is selected yet
            if (rhs_str.equals("")) {  //No numbers pressed yet (fresh)
                top_str = "";          //When holding the value from old entry, overwrite
                LHS = 0.0;
            }
            top_str += numericButton.getText().toString();  //Continue adding digits
        } else {
            bottom_str += numericButton.getText().toString();  //add digits to bottom
        }

        rhs_str += numericButton.getText().toString();  //add digits for calculation (ignore operators)
        updateDisplay();
    }

    public void operatorClick(View view) {
        operatorButton = (Button) view;

        //compute based on previously pressed operation
        compute();

        top_str = String.valueOf(LHS);
        RHS = 0.0;
        rhs_str = "";
        bottom_str = operatorButton.getText().toString();

        //Store the newly pressed operation
        op = op_table.get(view.getId());
        updateDisplay();
    }

    public void clearClick(View v) {
        if (selectedEntry != null) {
            paidSpentMap.remove(selectedEntry);
            paidColumn.removeViewAt(selectedEntryint);
            selectedEntry = null;
            selectedEntryint = -1;
        }
        clearDisplay();
    }

    public void compute() {
        if (rhs_str.equals("")) {
            RHS = 0.0;
        } else {
            RHS = Double.valueOf(rhs_str);
        }

        if (op != null) {
            switch (op) {
                case NONE:
                case ADD:
                    LHS = LHS + RHS;
                    break;

                case DIVIDE:
                    if (RHS <= 0.0) {
                        LHS = 0.0;
                    } else {
                        LHS = LHS / RHS;
                    }
                    break;

                case MINUS:
                    LHS = LHS - RHS;
                    if (LHS < 0) {
                        LHS = 0.0;
                    }
                    break;

                case MULTIPLY:
                    LHS = (LHS.doubleValue() * RHS.doubleValue());
                    break;
            }
        }
    }

    public void equalClick(View view) {

        //compute based on previously pressed operation
        compute();
        top_str = String.valueOf(LHS);

        RHS = 0.0;
        rhs_str = "";
        bottom_str = "";
        op = Op.NONE;
        updateDisplay();
    }

    View.OnClickListener entryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //If there is a previously selected view, clear selection
            /*if (selectedEntryint >= 0) {
                Log.d(TAG, "entryonClick: More than zero, " + selectedEntryint);
                selectedView = (TextView) (paidColumn.getChildAt(selectedEntryint));
                selectedView.setTypeface(null, Typeface.NORMAL);
            }
            */
            //If same entry clicked, deselect
            if (selectedEntryint == paidColumn.indexOfChild(v)) {
                clearEntrySelection();
                clearDisplay();
            }//Select new entry
            else {
                clearEntrySelection();
                clearDisplay();
                selectedView = (TextView) v;
                selectedView.setTypeface(selectedView.getTypeface(), Typeface.BOLD);
                selectedEntryint = paidColumn.indexOfChild(v);
                Log.d(TAG, "entryonClick: selectedEntryint " + selectedEntryint);

                selectedEntry = paidSpentMap.get(selectedEntryint);

                //Load Entry onto display
                RHS = selectedEntry.amount;
                top_str = RHS.toString();
                rhs_str = RHS.toString();
                updateDisplay();
            }
        }
    };

    public void PaidSpentClick(View view) {
        if (top_str.equals(""))
            return;

        Act act = act_table.get(view.getId());

        compute();

        if (selectedEntry == null) {  //If no entry in selection, create new entry
            Entry newEntry = new Entry();
            newEntry.amount = LHS;
            newEntry.act = act;

            TextView newEntryView = createEntryView(newEntry.amount.toString(), act);
            paidColumn.addView(newEntryView);

            paidSpentMap.put(paidColumn.indexOfChild(newEntryView), newEntry);
            paidScroll.fullScroll(View.FOCUS_DOWN);
        } else {                     //If entry selected, overwrite entry
            selectedEntry.amount = LHS;
            updateEntryView(selectedEntry.amount.toString(), act);
        }
        clearDisplay();
        clearEntrySelection();
    }

    public void clearEntrySelection(){
        if(selectedEntryint != -1) {
            selectedView = (TextView) (paidColumn.getChildAt(selectedEntryint));
            selectedView.setTypeface(null, Typeface.NORMAL);
        }
        selectedEntry = null;
        selectedView = null;
        selectedEntryint = -1;

    }

    public TextView createEntryView(String s, Act a) {
        TextView ret;
        ret = new TextView(this);

        /*TextView
        android:id="@+id/textView2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:clickable="true"
        android:onClick="PaidSpentClick"
        android:text="TextView"
        android:textAlignment="center"
        tools:layout_editor_absoluteX="16dp"
        tools:layout_editor_absoluteY="16dp"*/

        ret.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,    //width
                LinearLayout.LayoutParams.WRAP_CONTENT));  //height

        ret.setClickable(true);
        ret.setOnClickListener(entryClickListener);
        ret.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ret.setTextSize(16);

        switch (a) {
            case PAID:
                ret.setText(String.format("+%s", s));
                ret.setTextColor(Color.BLUE);
                break;
            case SPENT:
                ret.setText(String.format("-%s", s));
                ret.setTextColor(Color.RED);
                break;
        }
        //ret.setText(s);

        return ret;
    }

    public void updateEntryView(String s, Act a) {

        TextView ret = (TextView) paidColumn.getChildAt(selectedEntryint);

        switch (a) {
            case PAID:
                ret.setText(String.format("+%s", s));
                ret.setTextColor(Color.BLUE);
                break;
            case SPENT:
                ret.setText(String.format("-%s", s));
                ret.setTextColor(Color.RED);
                break;
        }
        ret.setTypeface(null, Typeface.NORMAL);
    }

    public void clearDisplay() {
        LHS = 0.0;
        RHS = 0.0;
        top_str = "";
        bottom_str = "";
        rhs_str = "";
        op = Op.NONE;
        updateDisplay();
    }

    public void updateDisplay() {
        eqTopView.setText(top_str);
        eqTextView.setText(bottom_str);
    }
}
