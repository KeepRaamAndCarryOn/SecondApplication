package com.example.secondapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button numericButton;
    Button operatorButton;

    TextView eqTextView;
    TextView rhsTextView;
    TextView lhsTextView;


    String eqStr;
    String rhsStr;

    Integer LHS;
    Integer RHS;
    Integer res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eqTextView = (TextView) findViewById(R.id.eqTextBox);
        rhsTextView = (TextView) findViewById(R.id.rhsView);
        lhsTextView = (TextView) findViewById(R.id.lhsView);

        eqStr = "";
        rhsStr = "";
        LHS = null;
        RHS = null;


    }


    public void numericClick(View view){
        numericButton = (Button) view;

        eqStr += numericButton.getText().toString();
        rhsStr += numericButton.getText().toString();
        eqTextView.setText(eqStr);
        //rhsTextView.setText(rhsStr);
        updateDisplay();
    }

    public void operatorClick(View view){
        operatorButton = (Button) view;

        if(LHS == null)        {
            LHS = Integer.decode(rhsStr);
            eqStr += "+";
        }
        else {
            RHS = Integer.decode(rhsStr);
            res = LHS + RHS;
            LHS = res;

            eqStr = String.format("%s+", String.valueOf(res));
        }
        rhsStr = "";
        RHS = 0;
        updateDisplay();
    }

    public void updateDisplay()
    {
        eqTextView.setText(eqStr);
        rhsTextView.setText(rhsStr);
        if(LHS != null) {
            lhsTextView.setText(String.valueOf(LHS.intValue()));
        }
    }
}
