package com.example.apirestfull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String resultRetrofit = "";
    String resultVolley = "";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        retrofitRequest();
        volleyRequest();
    }

    private void retrofitRequest(){
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://gorest.co.in/public/v1/").
                addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<Data> callData = service.getData();
        callData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                data.data.forEach((e)->{
                    resultRetrofit += String.format("name: '%1$s' \n email: '%2$s' \n gender: '%3$s' \n status: '%4$s' \n\n",
                        e.getName(),e.getEmail(),e.getGender(), e.getStatus());
                });
                TextView text = (TextView) findViewById(R.id.result);
                text.setText(resultRetrofit);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Hubo un error al recibir los datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void volleyRequest(){
        Gson gson = new Gson();
        String url = "https://gorest.co.in/public/v1/users";
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Data data = gson.fromJson(response,Data.class);
                data.data.forEach((e)->{
                    resultVolley += String.format("name: '%1$s' \n email: '%2$s' \n gender: '%3$s' \n status: '%4$s' \n\n",
                            e.getName(),e.getEmail(),e.getGender(), e.getStatus());
                });
                TextView textView = (TextView) findViewById(R.id.resultVolley);
                textView.setText(resultVolley);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}