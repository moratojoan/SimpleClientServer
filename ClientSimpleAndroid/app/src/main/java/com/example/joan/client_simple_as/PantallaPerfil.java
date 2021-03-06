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

public class PantallaPerfil extends AppCompatActivity implements View.OnClickListener {

    private TextView Usuari;
    private EditText Contrasenya;

    private Button EliminarUsuari, Acceptar, Cancelar, EliminarNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil);

        Usuari = findViewById(R.id.txt_p_usuari);
        Contrasenya = findViewById(R.id.txt_p_contrasenya);
        EliminarUsuari = findViewById(R.id.btn_p_eliminarusuari);
        Acceptar = findViewById(R.id.btn_p_acceptar);
        Cancelar = findViewById(R.id.btn_p_cancelar);
        EliminarNotes = findViewById(R.id.btn_p_eliminarnotes);

        EliminarUsuari.setOnClickListener(this);
        Acceptar.setOnClickListener(this);
        Cancelar.setOnClickListener(this);
        EliminarNotes.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio = preferences.getString("Usuari de la Sessió","");
        Usuari.setText(usuari_sessio);

        Contrasenya.setVisibility(View.INVISIBLE);
        Acceptar.setVisibility(View.INVISIBLE);
        Cancelar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_p_eliminarusuari:
                EliminarUsuari.setVisibility(View.INVISIBLE);
                Contrasenya.setVisibility(View.VISIBLE);
                Acceptar.setVisibility(View.VISIBLE);
                Cancelar.setVisibility(View.VISIBLE);
                EliminarNotes.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_p_cancelar:
                EliminarUsuari.setVisibility(View.VISIBLE);
                Contrasenya.setVisibility(View.INVISIBLE);
                Acceptar.setVisibility(View.INVISIBLE);
                Cancelar.setVisibility(View.INVISIBLE);
                EliminarNotes.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_p_acceptar:
                if(!Contrasenya.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url2 = "http://10.0.2.2:5000/eliminar_usuari";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Usuari eliminat correctament")){
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }else {
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                Contrasenya.setText("");
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
                }else{
                    Toast.makeText(getApplicationContext(),"Escriu la contrasenya", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_p_eliminarnotes:
                RequestQueue queue = Volley.newRequestQueue(this);
                String url2 = "http://10.0.2.2:5000/eliminar_totes_notes";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Notes eliminades amb èxit")){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
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
                        return parr;
                    }
                };
                queue.add(stringRequest);
                break;
        }
    }
}
