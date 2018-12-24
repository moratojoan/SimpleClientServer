package com.example.joan.client_simple_as;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText Usuari, Contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuari = findViewById(R.id.txt_usuari);
        Contrasenya = findViewById(R.id.txt_contrasenya);
        Button login = findViewById(R.id.btn_login);
        Button registar = findViewById(R.id.btn_registrar_se);

        login.setOnClickListener(this);
        registar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if (!Usuari.getText().toString().isEmpty() && !Contrasenya.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url2 = "http://10.0.2.2:5000/login_usuari";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            switch (response) {
                                case "Login realitzat correctament":
                                    SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor Obj_editor = preferences.edit();
                                    Obj_editor.putString("Estat Sessió", "Oberta");
                                    Obj_editor.putString("Usuari de la Sessió", Usuari.getText().toString());
                                    Obj_editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), PantallaBlocNotes.class);
                                    startActivity(intent);
                                    break;
                                case "Usuari i/o Contrasenya no vàlids":
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    break;
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
            case R.id.btn_registrar_se:
                Intent intent = new Intent(this, PantallaRegistrar.class);
                startActivity(intent);
                break;
        }
    }
}
