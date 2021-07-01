package me.disktradev.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class CurrencyActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, GetCurrencyJsonData.OnDataAvailable {

    private static final String TAG = "CurrencyActivity";
    private static final String SHARED_PREFERENCE = "CurrenciesData";
    private static final int MODE = Context.MODE_PRIVATE;

    private String mSelectedCurrencyFrom;
    private String mSelectedCurrencyTo;

    Currency[] mCurrencies;

    Currency mUSD;
    Currency mEUR;
    Currency mJPY;
    Currency mSEK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        Button buttonConvert = findViewById(R.id.buttonConvert);
        Button buttonRefresh = findViewById(R.id.buttonRefresh);
        Button buttonInvert = findViewById(R.id.buttonInvert);
        Spinner spinFrom = findViewById(R.id.spinnerFrom);
        Spinner spinTo = findViewById(R.id.spinnerTo);

        // Spinners setting
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.currencies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFrom.setAdapter(adapter);
        spinTo.setAdapter(adapter);
        spinFrom.setOnItemSelectedListener(this);
        spinTo.setOnItemSelectedListener(this);

        EditText editTextBaseValue = findViewById(R.id.editTextBaseValue);
        editTextBaseValue.setText("1");

        // Currencies Setting
        mUSD = new Currency("USD", "United States dollar");
        mEUR = new Currency("EUR", "Euro");
        mJPY = new Currency("JPY", "Japanese yen");
        mSEK = new Currency("SEK", "Swedish krona");

        mCurrencies = new Currency[]{mUSD, mEUR, mJPY, mSEK};

        // Load the saved currencies values
        for (int i = 0; i < mCurrencies.length; i++) {
            String code = mCurrencies[i].getCode();
            mCurrencies[i].setUSDValue(Double.parseDouble(loadData(code + "USD")));
            mCurrencies[i].setEURValue(Double.parseDouble(loadData(code + "EUR")));
            mCurrencies[i].setJPYValue(Double.parseDouble(loadData(code + "JPY")));
            mCurrencies[i].setSEKValue(Double.parseDouble(loadData(code + "SEK")));
        }

        // Setting the Convert button
        View.OnClickListener convertListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertCurrency();
            }
        };

        buttonConvert.setOnClickListener(convertListener);

        // Setting the Refresh button
        View.OnClickListener refreshListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BuildConfig.API_KEY.length() == 0) {
                    Context context = getApplicationContext();
                    CharSequence text = "No API key provided.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                GetCurrencyJsonData getCurrencyJsonData = new GetCurrencyJsonData(CurrencyActivity.this, mSelectedCurrencyFrom);
                getCurrencyJsonData.execute();
            }
        };

        buttonRefresh.setOnClickListener(refreshListener);

        // Setting the Invert button
        View.OnClickListener invertListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = spinFrom.getSelectedItemPosition();
                int y = spinTo.getSelectedItemPosition();

                spinFrom.setSelection(y);
                spinTo.setSelection(x);
            }
        };

        buttonInvert.setOnClickListener(invertListener);

        // USD is the default
        mSelectedCurrencyFrom = "USD";
        mSelectedCurrencyTo = "USD";
        updateExchangeValueText();
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
            case R.id.mnuPercentage:
                startActivity(new Intent(this, PercentageActivity.class));
                break;
            case R.id.mnuTime:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerFrom) {
            mSelectedCurrencyFrom = parent.getItemAtPosition(position).toString().substring(0, 3);
            Log.d(TAG, "onItemSelected: mSelectedCurrencyFrom set to " + mSelectedCurrencyFrom);
        } else if (parent.getId() == R.id.spinnerTo) {
            mSelectedCurrencyTo = parent.getItemAtPosition(position).toString().substring(0, 3);
            Log.d(TAG, "onItemSelected: mSelectedCurrencyTo set to " + mSelectedCurrencyTo);
        }

        updateExchangeValueText();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: starts");
    }

    private void convertCurrency() {
        Log.d(TAG, "convertCurrency: starts");
        EditText editTextBaseValue = findViewById(R.id.editTextBaseValue);
        TextView currencyResult = findViewById(R.id.textViewCurrencyResult);

        double valueFrom = Double.parseDouble(editTextBaseValue.getText().toString());
        double valueTo;

        // TODO : Find a better way to do this
        // Setting the Start Currency
        int i = 0;  // USD by default
        switch (mSelectedCurrencyFrom) {
            case "EUR":
                i = 1;
                break;
            case "JPY":
                i = 2;
                break;
            case "SEK":
                break;
            default:
                i = 0;
                break;
        }

        // Setting the Target currency
        switch (mSelectedCurrencyTo) {
            case "USD":
                valueTo = mCurrencies[i].getUSDValue();
                break;
            case "EUR":
                valueTo = mCurrencies[i].getEURValue();
                break;
            case "JPY":
                valueTo = mCurrencies[i].getJPYValue();
                break;
            case "SEK":
                valueTo = mCurrencies[i].getSEKValue();
                break;
            default:
                valueTo = 0.0;
                break;
        }

        double result = valueFrom * valueTo;

        currencyResult.setText(Double.toString(result));

        updateExchangeValueText();

        Log.d(TAG, "convertCurrency: ends, result is " + result);
    }

    @Override
    public void onDataAvailable(HashMap<String, String> currencyData, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts updating values for " + mSelectedCurrencyFrom);
        Log.d(TAG, "onDataAvailable: status is " + status);

        for (int i = 0; i < mCurrencies.length; i++) {
            Log.d(TAG, "onDataAvailable: Currency code is " + mCurrencies[i].getCode());
            if (mCurrencies[i].getCode().equals(mSelectedCurrencyFrom)) {
                mCurrencies[i].setUSDValue(Double.parseDouble(currencyData.get("USD")));
                mCurrencies[i].setEURValue(Double.parseDouble(currencyData.get("EUR")));
                mCurrencies[i].setJPYValue(Double.parseDouble(currencyData.get("JPY")));
                mCurrencies[i].setSEKValue(Double.parseDouble(currencyData.get("SEK")));

                saveData(mSelectedCurrencyFrom + "USD", currencyData.get("USD"));
                saveData(mSelectedCurrencyFrom + "EUR", currencyData.get("EUR"));
                saveData(mSelectedCurrencyFrom + "JPY", currencyData.get("JPY"));
                saveData(mSelectedCurrencyFrom + "SEK", currencyData.get("SEK"));

                Log.d(TAG, "onDataAvailable: updating " + mSelectedCurrencyFrom);
            }
        }

        Context context = getApplicationContext();
        CharSequence text = mSelectedCurrencyFrom + " updated successfully";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();
    }

    private void updateExchangeValueText() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCE, MODE);
        String defaultValue = getResources().getString(R.string.DEFAULT);

        TextView exchangeValue = findViewById(R.id.textViewExchangeValue);
        String exchangeText = "1 " + mSelectedCurrencyFrom + " = " + sharedPref.getString(mSelectedCurrencyFrom + mSelectedCurrencyTo, defaultValue) + " " + mSelectedCurrencyTo;
        exchangeValue.setText(exchangeText);
    }

    private void saveData(String name, String value) {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCE, MODE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(name, value);

        String defaultValue = getResources().getString(R.string.DEFAULT);
        editor.apply();
        Log.d(TAG, "saveData: value saved for " + name + " = " + sharedPref.getString(name, defaultValue));
    }

    private String loadData(String name) {
        Log.d(TAG, "loadData: loading data for " + name);

        SharedPreferences sharedPref = this.getSharedPreferences(SHARED_PREFERENCE, MODE);

        String defaultValue = getResources().getString(R.string.DEFAULT);
        String result = sharedPref.getString(name, defaultValue);

        Log.d(TAG, "loadData: value is " + result);

        return result;
    }
}