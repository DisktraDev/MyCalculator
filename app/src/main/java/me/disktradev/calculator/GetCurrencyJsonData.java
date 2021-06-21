package me.disktradev.calculator;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetCurrencyJsonData extends AsyncTask<String, Void, String> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetCurrencyJsonData";

    private final String baseUrl = "https://exchange-rates.abstractapi.com/v1/live/";
    private final String API_KEY = BuildConfig.API_KEY;
    private final String mBaseCurrency;
    private final OnDataAvailable mCallBack;

    private HashMap<String, String> mCurrencyData;


    interface OnDataAvailable {
        void onDataAvailable(HashMap<String, String> currencyData, DownloadStatus status);
    }

    public GetCurrencyJsonData(OnDataAvailable callBack, String baseCurrency) {
        this.mCallBack = callBack;
        this.mBaseCurrency = baseCurrency;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");

        if (mCallBack != null) {
            mCallBack.onDataAvailable(mCurrencyData, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts for " + mBaseCurrency);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(buildUri());

        Log.d(TAG, "doInBackground: ends");
        return null;
    }

    private String buildUri() {
        Log.d(TAG, "buildUri: starts");

        StringBuilder result = new StringBuilder();
        // TODO : There is a better way to do this
        result.append(baseUrl)
                .append("?api_key=" + API_KEY)
                .append("&base=" + mBaseCurrency)
                .append("&target=USD,EUR,JPY,SEK");

        Log.d(TAG, "buildUri: ends");
        return result.toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts");

        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: status is " + status);
            mCurrencyData = new HashMap<>();
            // JSON Parsing
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONObject values = jsonData.getJSONObject("exchange_rates");

                String valueUSD = values.getString( "USD");
                String valueEUR = values.getString( "EUR");
                String valueJPY = values.getString( "JPY");
                String valueSEK = values.getString("SEK");

                mCurrencyData.put("USD", valueUSD);
                mCurrencyData.put("EUR", valueEUR);
                mCurrencyData.put("JPY", valueJPY);
                mCurrencyData.put("SEK", valueSEK);

                Log.d(TAG, "onDownloadComplete: data collected are " + mCurrencyData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing JSON data: " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
