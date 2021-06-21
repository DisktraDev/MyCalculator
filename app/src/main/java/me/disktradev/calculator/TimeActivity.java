package me.disktradev.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TimeActivity extends AppCompatActivity {

    private static final String TAG = "TimeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
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
            case R.id.mnuCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}