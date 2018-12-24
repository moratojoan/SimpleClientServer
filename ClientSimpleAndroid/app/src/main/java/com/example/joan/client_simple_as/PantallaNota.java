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

public class PantallaNota extends AppCompatActivity implements View.OnClickListener {

    private TextView Titol, Text;
    private EditText Titol_nou, Text_nou;
    private Button Ok, Ok2, Eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nota);

        Titol = findViewById(R.id.txt_pn_titolnota);
        Text = findViewById(R.id.txt_pn_textnota);
        Titol_nou = findViewById(R.id.txt_pn_titolnou);
        Ok = findViewById(R.id.btn_pn_ok);
        Text_nou = findViewById(R.id.txt_pn_noutext);
        Ok2 = findViewById(R.id.btn_pn_ok2);
        Eliminar = findViewById(R.id.btn_pn_eliminarnota);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            Titol.setText(extras.getString("titol_nota"));
            Text.setText(extras.getString("text_nota"));

            Titol_nou.setText(extras.getString("titol_nota"));
            Text_nou.setText(extras.getString("text_nota"));
        }

        Titol_nou.setVisibility(View.INVISIBLE);
        Ok.setVisibility(View.INVISIBLE);
        Text_nou.setVisibility(View.INVISIBLE);
        Ok2.setVisibility(View.INVISIBLE);

        //EDITAR
        Titol.setOnClickListener(this);
        Ok.setOnClickListener(this);
        Text.setOnClickListener(this);
        Ok2.setOnClickListener(this);
        Eliminar.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PantallaBlocNotes.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_pn_titolnota:
                Titol.setVisibility(View.INVISIBLE);
                Titol_nou.setVisibility(View.VISIBLE);
                Ok.setVisibility(View.VISIBLE);

                Eliminar.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_pn_ok:
                if (!Titol_nou.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url2 = "http://10.0.2.2:5000/modificar_titol_nota";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Titol modificat correctament")){
                                Titol.setVisibility(View.VISIBLE);
                                Titol_nou.setVisibility(View.INVISIBLE);
                                Ok.setVisibility(View.INVISIBLE);
                                Eliminar.setVisibility(View.VISIBLE);

                                Titol.setText(Titol_nou.getText().toString());
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
                            parr.put("nou_titol_nota", Titol_nou.getText().toString());
                            return parr;
                        }
                    };
                    queue.add(stringRequest);
                }else{
                    Toast.makeText(this,"Introdueix un Titol",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_pn_textnota:
                Text.setVisibility(View.INVISIBLE);
                Text_nou.setVisibility(View.VISIBLE);
                Ok2.setVisibility(View.VISIBLE);
                Eliminar.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_pn_ok2:
                if (!Text_nou.getText().toString().isEmpty()){
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url2 = "http://10.0.2.2:5000/modificar_text_nota";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Text modificat correctament")){
                                Text.setVisibility(View.VISIBLE);
                                Text_nou.setVisibility(View.INVISIBLE);
                                Ok2.setVisibility(View.INVISIBLE);
                                Eliminar.setVisibility(View.VISIBLE);

                                Text.setText(Text_nou.getText().toString());
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
                            parr.put("nou_text", Text_nou.getText().toString());
                            return parr;
                        }
                    };
                    queue.add(stringRequest);
                }else{
                    Toast.makeText(this,"Introdueix Text",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_pn_eliminarnota:
                RequestQueue queue = Volley.newRequestQueue(this);
                String url2 = "http://10.0.2.2:5000/eliminar_nota";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Nota eliminada amb èxit")){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PantallaBlocNotes.class);
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

                        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                        String usuari_sessio = preferences.getString("Usuari de la Sessió","");

                        parr.put("nom_usuari",usuari_sessio);
                        parr.put("titol_nota", Titol.getText().toString());
                        return parr;
                    }
                };
                queue.add(stringRequest);
                break;
        }
    }
}
