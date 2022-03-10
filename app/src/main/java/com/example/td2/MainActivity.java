package com.example.td2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    protected float rate = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button converbutton = findViewById(R.id.conversionButton);
        converbutton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.geoplugin.net/json.gp?base_currency=USD";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CURRENCY", response.toString());

                        try {
                            Double rateFromJson = response.getDouble("geoplugin_currencyConverter");
                            rate = 1/rateFromJson.floatValue();
                            Log.d("CURRENCY", String.format("Rate : %f", rate));
                            TextView rateAffLabel = findViewById(R.id.rateLabel);
                            rateAffLabel.setText(String.format("%.2f",rate));
                            converbutton.setEnabled(true);
                        } catch (JSONException e) {
                            Log.d("CURRENCY", "Error, geoplugin_currencyConverter introuvable");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.d("CURRENCY", "Error !");
                       Log.d("CURRENCY", error.toString());
                    }
                });

        queue.add(jsonObjectRequest);

        Button convertButton = findViewById(R.id.conversionButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.userInput);
                String stringInput = input.getText().toString();

                float floatInput = Float.parseFloat(stringInput);
                float result = floatInput * rate;

                TextView resultLabel = findViewById(R.id.conversionResult);
                resultLabel.setText(String.format("%.2f", result) );

            }
        });
    }
}