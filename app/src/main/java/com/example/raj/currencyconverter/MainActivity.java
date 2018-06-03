package com.example.raj.currencyconverter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText numberInput;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    Button buttonConvert;
    TextView textViewResult;
    double enteredCurrency;
    String toCurrency;
    Dialog dialog;
    AutoCompleteTextView suggestionBox;
    String[] currency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberInput = (EditText) findViewById(R.id.numberInput);
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        buttonConvert = (Button) findViewById(R.id.buttonConvert);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        suggestionBox = (AutoCompleteTextView) findViewById(R.id.suggestionBox);
        currency =  getResources().getStringArray(R.array.Currencyarray);


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter (this , android.R.layout.simple_list_item_1,currency);
        ArrayAdapter<CharSequence> adapterUSD = ArrayAdapter.createFromResource(this, R.array.CurrencyUSD, android.R.layout.simple_list_item_1);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerFrom.setAdapter(adapterUSD);
        suggestionBox.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numberInput.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "please enter amount", Toast.LENGTH_SHORT).show();
                } else {

                    enteredCurrency = Double.valueOf(numberInput.getText().toString());
                    String fromCurrency = String.valueOf(spinnerFrom.getSelectedItem());
                    toCurrency = String.valueOf(spinnerTo.getSelectedItem());


                    DownloadingTask downloadingTask = new DownloadingTask();
                    downloadingTask.execute("http://www.apilayer.net/api/live?access_key=0f4931e5f08a1d92aa90ad64e1abd305&format=1");
          spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {
              }
          });
                }
            }
        });
    }

    public class DownloadingTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                URL host = new URL(params[0]);
                urlConnection = (HttpURLConnection) host.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(in);
                bufferedReader = new BufferedReader(inputStreamReader);

                String data = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((data = bufferedReader.readLine()) != null) {
                    stringBuilder.append(data);
                }
                String finalData = stringBuilder.toString();
                JSONObject parentObject = new JSONObject(finalData);
                JSONObject jsonObject = parentObject.getJSONObject("quotes");


                if (spinnerTo.getSelectedItem().toString().equals("AED")){

                    double aed = jsonObject.getDouble("USDAED");

                   double aedAmount = aed * enteredCurrency;
                    return String.valueOf(aedAmount);


                }
                if (spinnerTo.getSelectedItem().toString().equals("AFN")){
                    Double afn = jsonObject.getDouble("USDAFN");
                    double afnAmount = afn * enteredCurrency;
                    return String.valueOf(afnAmount);


                }
                if (spinnerTo.getSelectedItem().toString().equals("AFN")){
                    Double afn = jsonObject.getDouble("USDAFN");
                    double afnAmount = afn * enteredCurrency;
                    return String.valueOf(afnAmount);
                }
                if (spinnerTo.getSelectedItem().toString().equals("INR")){
                    Double inr = jsonObject.getDouble("USDINR");
                     double inrAmount = inr * enteredCurrency;
                    return String.valueOf(inrAmount);
                }
                if (spinnerTo.getSelectedItem().toString().equals("ALL")){
                    Double all = jsonObject.getDouble("USDALL");
                    double allAmount = all * enteredCurrency;
                    return String.valueOf(allAmount);
                }
                if (spinnerTo.getSelectedItem().toString().equals("AMD")){
                    Double amd = jsonObject.getDouble("USDAMD");
                    double amdAmount = amd * enteredCurrency;
                    return String.valueOf(amdAmount);
                }
                if (spinnerTo.getSelectedItem().toString().equals("ANG")){
                    Double ang = jsonObject.getDouble("USDANG");
                    double angAmount = ang * enteredCurrency;
                    return String.valueOf(angAmount);
                }
                if (spinnerTo.getSelectedItem().toString().equals("AOA")){
                    Double aoa = jsonObject.getDouble("USDAOA");
                    double aoaAmount = aoa * enteredCurrency;
                    return String.valueOf(aoaAmount);
                }
              return String.valueOf(jsonObject);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(MainActivity.this);
            dialog.setTitle("please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            textViewResult.setText(result);
        }
    }

}
