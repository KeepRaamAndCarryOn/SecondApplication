package com.example.secondapplication;

import android.graphics.Typeface;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button numericButton;
    Button operatorButton;

    TextView eqTextView;
    TextView eqTopView;

    TextView selectedView;

    LinearLayout paidColumn;
    int selectedEntryint;

    String bottom_str;
    String top_str;
    String rhs_str;

    Double LHS;
    Double RHS;

    Op op;

    enum Op {
        NONE, ADD, MINUS, MULTIPLY, DIVIDE, EQUALS
    }


    ArrayMap<Integer, Op> op_table;
    private static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eqTextView = (TextView) findViewById(R.id.eqTextBox);
        eqTopView = (TextView) findViewById(R.id.eqTop);

        paidColumn = findViewById(R.id.paidLinearLayout);

        op_table = new ArrayMap<>();
        op_table.put(R.id.buttonPlus, Op.ADD);
        op_table.put(R.id.buttonMinus, Op.MINUS);
        op_table.put(R.id.buttonEquals, Op.EQUALS);
        op_table.put(R.id.buttonMulti, Op.MULTIPLY);

        bottom_str = "";
        top_str = "";
        rhs_str = "";

        LHS = 0.0;
        RHS = 0.0;

        op = Op.ADD;

        selectedView = null;
        selectedEntryint = -1;

    }


    public void numericClick(View view) {
        numericButton = (Button) view;
        bottom_str += numericButton.getText().toString();
        rhs_str += numericButton.getText().toString();
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

    public void compute() {
        if (top_str.equals("")) {
            LHS = 0.0;
        }

        if (rhs_str.equals("")) {
            RHS = 0.0;
        } else {
            RHS = Double.valueOf(rhs_str);
        }

        if (op != null) {
            switch (op) {
                case ADD:
                    LHS = LHS + RHS;
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
        op = Op.ADD;
        updateDisplay();
    }

    View.OnClickListener entryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            //If there is a previously selected view, clear selection
            if (selectedEntryint >= 0){
                Log.d(TAG, "entryonClick: More than zero, " + selectedEntryint);
                selectedView = (TextView) (paidColumn.getChildAt(selectedEntryint));
                selectedView.setTypeface(null,Typeface.NORMAL);
            }

            //Select new view
            selectedView = (TextView) v;
            selectedView.setTypeface(selectedView.getTypeface(),Typeface.BOLD);
            selectedEntryint = paidColumn.indexOfChild(v);
            Log.d(TAG, "entryonClick: selectedEntryint " + selectedEntryint);
        }
    };

    public class Entry {
        Double amount;
    }

    public void TestClick(View v){
        /*selectedView.setTypeface(selectedView.getTypeface(),Typeface.NORMAL);
        TextView test;
        if (selectedEntryint >= 0){
            test = (TextView) paidColumn.getChildAt(selectedEntryint);
            test.setTypeface(selectedView.getTypeface(),Typeface.ITALIC);
        }*/
    }
    public void PaidClick(View view) {
        if (top_str.equals(""))
            return;

        Entry newEntry = new Entry();
        newEntry.amount = LHS;

        TextView newEntryView = createEntryView(newEntry.amount.toString());
        paidColumn.addView(newEntryView);
        clearDisplay();
    }

    public TextView createEntryView(String s) {
        TextView ret;
        ret = new TextView(this);

        /*TextView
        android:id="@+id/textView2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:clickable="true"
        android:onClick="PaidClick"
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
        ret.setText(s);

        return ret;
    }

    public void clearDisplay() {
        LHS = 0.0;
        RHS = 0.0;
        top_str = "";
        bottom_str = "";
        rhs_str = "";
        op = Op.ADD;
        updateDisplay();
    }

    public void updateDisplay() {
        eqTopView.setText(top_str);
        eqTextView.setText(bottom_str);

    }


}
