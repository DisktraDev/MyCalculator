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
import android.widget.RadioGroup;
import android.widget.TextView;

public class PercentageActivity extends AppCompatActivity {

    private static final String TAG = "PercentageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage);

        Button resultButton = findViewById(R.id.btnPercentResult);
        TextView result = findViewById(R.id.tvResult);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        EditText editTextFirstValue = findViewById(R.id.editTextFirstValue);
        EditText editTextSecondValue = findViewById(R.id.editTextSecondValue);

        radioGroup.check(R.id.rbPercentFromValue);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButton = radioGroup.getCheckedRadioButtonId();

                double firstValue = Double.parseDouble(editTextFirstValue.getText().toString());
                double secondValue = Double.parseDouble(editTextSecondValue.getText().toString());
                double resultValue = 0.0;

                String resultText;
                boolean isResultPercent = true;

                switch (selectedRadioButton) {
                    case R.id.rbPercentFromValue:
                        Log.d(TAG, "onClick: x% sur un total de y");
                        resultValue = getPercentFromValue(firstValue, secondValue);
                        isResultPercent = false;
                        break;
                    case R.id.rbValueFromPercent:
                        Log.d(TAG, "onClick: quel pourcentage de x sur un total de y");
                        resultValue = getValueFromPercent(firstValue, secondValue);
                        break;
                    case R.id.rbRise:
                        Log.d(TAG, "onClick: x + y%");
                        resultValue = getRise(firstValue, secondValue);
                        isResultPercent = false;
                        break;
                    case R.id.rbReduce:
                        Log.d(TAG, "onClick: x - y%");
                        resultValue = getReduce(firstValue, secondValue);
                        isResultPercent = false;
                        break;
                    case R.id.rbVariation:
                        Log.d(TAG, "onClick: quel pourcentage entre x et y");
                        resultValue = getVariation(firstValue, secondValue);
                        break;
                }

                resultText = String.format("%.2f", resultValue);

                if (isResultPercent) {
                    resultText += " %";
                }

                result.setText(resultText);
            }
        };

        resultButton.setOnClickListener(listener);
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
            case R.id.mnuCalcutator:
                startActivity(new Intent(this, MainActivity.class));
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

    private double getPercentFromValue(double percent, double value) {
        return value * (percent / 100);
    }

    private double getValueFromPercent(double value, double percent) {
        return (value / percent) * 100;
    }

    private double getRise(double total, double percent) {
        return total + getPercentFromValue(percent, total);
    }

    private double getReduce(double total, double percent) {
        return total - getPercentFromValue(percent, total);
    }

    private double getVariation(double first, double second) {
        return ((second - first)/first) * 100;
    }
}