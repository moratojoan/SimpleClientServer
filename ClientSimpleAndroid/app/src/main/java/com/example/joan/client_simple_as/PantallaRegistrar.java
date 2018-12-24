package com.example.joan.client_simple_as;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PantallaRegistrar extends AppCompatActivity implements View.OnClickListener{

    private EditText Usuari, Email, Contrasenya;
    private Button Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registrar);

        Usuari = findViewById(R.id.txt_pr_usuari);
        Email = findViewById(R.id.txt_pr_email);
        Contrasenya = findViewById(R.id.txt_pr_contrasenya);
        Registrar = findViewById(R.id.btn_pr_registrar_se);

        Registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pr_registrar_se:
                if (!Usuari.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() && !Contrasenya.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://localhost:5000/afegir_usuari";
                    String url2 = "http://10.0.2.2:5000/afegir_usuari";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("Ja existeix un usuari amb aquest nom")){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
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

                            parr.put("nom_usuari",Usuari.getText().toString());
                            parr.put("email", Email.getText().toString());
                            parr.put("contrasenya",Contrasenya.getText().toString());
                            return parr;
                        }
                    };
                    queue.add(stringRequest);
                    break;
                }else{
                    Toast.makeText(this,"Introdueix tots els camps",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
