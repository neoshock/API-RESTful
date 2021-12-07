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
        //Crea instancia para la cola de procesamiento del request con volley
        requestQueue = Volley.newRequestQueue(this);
        //Llamada a la API Con Retrofit
        retrofitRequest();
        //Llamada a la API Con Volley
        volleyRequest();
    }

    //Funcion para llamada con Retrofit
    private void retrofitRequest(){
        //Instanciamos retrofit para la llamada a la API
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://gorest.co.in/public/v1/").
                addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<Data> callData = service.getData();
        //Creamos la cola de procesamiento de la llamada
        callData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                data.data.forEach((e)->{
                    //Agreegamos los datos obtenidos a un string con formato
                    resultRetrofit += String.format("name: '%1$s' \n email: '%2$s' \n " +
                                    "gender: '%3$s' \n status: '%4$s' \n\n",
                        e.getName(),e.getEmail(),e.getGender(), e.getStatus());
                });
                //Llamando al text view para agregar los datos formateados
                TextView text = (TextView) findViewById(R.id.result);
                text.setText(resultRetrofit);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                //Mensaje de erro en caso de que no se muestre
                Toast.makeText(MainActivity.this, "Hubo un error al recibir los datos",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void volleyRequest(){
        Gson gson = new Gson();
        String url = "https://gorest.co.in/public/v1/users";
        //Metodo para realizar la peticion a la URL usando GET
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Data data = gson.fromJson(response,Data.class);
                data.data.forEach((e)->{
                    //Agreegamos los datos obtenidos a un string con formato
                    resultVolley += String.format("name: '%1$s' \n email: '%2$s' \n " +
                                    "gender: '%3$s' \n status: '%4$s' \n\n",
                            e.getName(),e.getEmail(),e.getGender(), e.getStatus());
                });
                //Llamando al text view para agregar los datos formateados
                TextView textView = (TextView) findViewById(R.id.resultVolley);
                textView.setText(resultVolley);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de erro en caso de que no se muestre
                Toast.makeText(MainActivity.this, "Hubo un error al recibir los datos",
                        Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
}