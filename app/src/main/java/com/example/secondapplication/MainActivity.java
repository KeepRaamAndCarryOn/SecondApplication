package com.example.secondapplication;

import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button numericButton;
    Button operatorButton;

    TextView eqTextView;
    TextView eqTopView;


    String bottom_str;
    String top_str;
    String rhs_str;

    Double LHS;
    Double RHS;

    Op op;

    enum Op{
        NONE, ADD, MINUS, MULTIPLY, DIVIDE, EQUALS
    }



    ArrayMap<Integer, Op> op_table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eqTextView = (TextView) findViewById(R.id.eqTextBox);
        eqTopView = (TextView) findViewById(R.id.eqTop);

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
    }


    public void numericClick(View view){
        numericButton = (Button) view;
        bottom_str += numericButton.getText().toString();
        rhs_str += numericButton.getText().toString();
        updateDisplay();
    }

    public void operatorClick(View view){
        operatorButton = (Button) view;


        compute();


        top_str = String.valueOf(LHS);

        RHS = 0.0;
        rhs_str = "";
        bottom_str = operatorButton.getText().toString();

        op = op_table.get(view.getId());
        updateDisplay();
    }

    public void compute()
    {
        if (top_str.equals("")){
        LHS = 0.0;
        }

        if(rhs_str.equals(""))        {
            RHS = 0.0;
        }
        else{
            RHS = Double.valueOf(rhs_str);
        }


        //LHS = LHS+RHS;
        if (op != null)
        {
            switch(op){
                case ADD:
                    LHS = LHS + RHS;
                    break;

                case MULTIPLY:
                    LHS = (LHS.doubleValue() * RHS.doubleValue());
                    break;
            }
        }
    }

    public void equalClick(View view){

        compute();
        top_str = String.valueOf(LHS);

        RHS = 0.0;
        rhs_str = "";
        bottom_str = "";
        op = Op.ADD;
        updateDisplay();
    }

    public void updateDisplay(){
        eqTopView.setText(top_str);
        eqTextView.setText(bottom_str);

    }



}
