package com.example.joan.client_simple_as;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PantallaNotaNova extends AppCompatActivity implements View.OnClickListener {

    private EditText Titol, Text;
    private Button Guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nota_nova);

        Titol = findViewById(R.id.txt_pnn_titolnota);
        Text = findViewById(R.id.txt_pnn_textnota);
        Guardar = findViewById(R.id.btn_pnn_guardar);
        Guardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pnn_guardar:
                if(!Titol.getText().toString().isEmpty() && !Text.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url2 = "http://10.0.2.2:5000/afegir_nota";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("La nota s'ha afegit correctament")){
//                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
//                                Titol.setText("");
//                                Text.setText("");
//                                Intent intent = new Intent(getApplicationContext(), PantallaBlocNotes.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), PantallaBlocNotes.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString() ,Toast.LENGTH_SHORT).show();
                            Log.d("Error: ", error.toString());
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> parr = new HashMap<String, String>();

                            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                            String usuari_sessio = preferences.getString("Usuari de la Sessió","");

                            parr.put("nom_usuari",usuari_sessio);
                            parr.put("titol_nota", Titol.getText().toString());
                            parr.put("text",Text.getText().toString());
                            return parr;
                        }
                    };
                    queue.add(stringRequest);
                }else{
                    Toast.makeText(this,"Omple tots els camps!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
