package me.disktradev.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

    private static final String TAG = "MainActivity";

    // Variable to hold the OPERAND1s and type of calculations
    private Double operand1 = null;
    private String pendingOperation = "=";

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND1 = "Operand1";

    // TODO : Keep track of calculations made bu the user, like the calculator in Ubuntu and Windows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result =  findViewById(R.id.result);
        newNumber =  findViewById(R.id.newNumber);
        displayOperation =  findViewById(R.id.operation);

        Button button0 =  findViewById(R.id.button0);
        Button button1 =  findViewById(R.id.button1);
        Button button2 =  findViewById(R.id.button2);
        Button button3 =  findViewById(R.id.button3);
        Button button4 =  findViewById(R.id.button4);
        Button button5 =  findViewById(R.id.button5);
        Button button6 =  findViewById(R.id.button6);
        Button button7 =  findViewById(R.id.button7);
        Button button8 =  findViewById(R.id.button8);
        Button button9 =  findViewById(R.id.button9);
        Button buttonDot =  findViewById(R.id.buttonDot);

        Button buttonEquals =  findViewById(R.id.buttonEquals);
        Button buttonDivide =  findViewById(R.id.buttonDivide);
        Button buttonMultiply =  findViewById(R.id.buttonMultiply);
        Button buttonMinus =  findViewById(R.id.buttonMinus);
        Button buttonPlus =  findViewById(R.id.buttonPlus);
        Button buttonNegative =  findViewById(R.id.buttonNegative);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                newNumber.append(b.getText().toString());
            }
        };

        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);

        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();

                // Prevent crash if the user enters nothing but a decimal point
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, op);
                } catch (NumberFormatException e) {
                    newNumber.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };

        buttonEquals.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double value = Double.valueOf(newNumber.getText().toString());
                    value *= -1;
                    newNumber.setText(value.toString());
                } catch (NumberFormatException e) {
                    newNumber.setText("");
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
        displayOperation.setText(pendingOperation);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION, pendingOperation);
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected: " + item.toString());

        switch (id) {
            case R.id.mnuPercentage:
                startActivity(new Intent(this, PercentageActivity.class));
                break;
            case R.id.mnuCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
            case R.id.mnuTime:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void performOperation(Double value, String operation) {
        if (null == operand1) {
            operand1 = value;
        } else {
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }

            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
                case "/":
                    if (value == 0) {
                        operand1 = 0.0;
                    } else {
                        operand1 /= value;
                    }
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
                case "+":
                    operand1 += value;
                    break;
                default:
                    break;
            }
        }

        result.setText(operand1.toString());
        newNumber.setText("");
    }
}