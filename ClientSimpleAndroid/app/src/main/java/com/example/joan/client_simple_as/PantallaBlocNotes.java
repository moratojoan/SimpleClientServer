package com.example.joan.client_simple_as;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PantallaBlocNotes extends AppCompatActivity implements View.OnClickListener {

    private EditText Buscador;
    private ImageView btn_buscador;

    private ArrayList<Notes> llista_notes;
    private RecyclerView recyclerView_notes;

    private Button NovaNota, Perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_bloc_notes);

        Buscador = findViewById(R.id.txt_pbn_buscador);
        btn_buscador = findViewById(R.id.btn_pbn_buscador);
        btn_buscador.setOnClickListener(this);

        recyclerView_notes = findViewById(R.id.recyclerView_notes);
        recyclerView_notes.setLayoutManager(new LinearLayoutManager(this));

        llista_notes = new ArrayList<>();
        omplirllista_notes();

        AdaptadorNotes adapter = new AdaptadorNotes(llista_notes);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),String.valueOf(recyclerView_notes.getChildAdapterPosition(v)),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),PantallaNota.class);
                intent.putExtra("titol_nota",llista_notes.get(recyclerView_notes.getChildAdapterPosition(v)).getTitol_nota());
                intent.putExtra("text_nota",llista_notes.get(recyclerView_notes.getChildAdapterPosition(v)).getText_nota());
                startActivity(intent);
            }
        });
        recyclerView_notes.setAdapter(adapter);

        NovaNota = findViewById(R.id.btn_novanota);
        NovaNota.setOnClickListener(this);

        Perfil = findViewById(R.id.btn_pbn_perfil);
        Perfil.setOnClickListener(this);
    }

    private void omplirllista_notes() {
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio = preferences.getString("Usuari de la Sessió","");
        String url2 = "http://10.0.2.2:5000/obtenir_totes_notes_dic?nom_usuari="+usuari_sessio;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object;
                    for (int i=0; i<array.length();i++){
                        object = array.getJSONObject(i);
                        llista_notes.add(new Notes(object.getString("Titol"),object.getString("Text")));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString() ,Toast.LENGTH_SHORT).show();
                Log.d("Error: ", error.toString());
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pbn_buscador:
                RequestQueue queue = Volley.newRequestQueue(this);
                SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                String usuari_sessio = preferences.getString("Usuari de la Sessió","");
                String url2 = "http://10.0.2.2:5000/obtenir_nota_dic?nom_usuari="+usuari_sessio+"&titol_nota="+Buscador.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object;
                            object = array.getJSONObject(0);
                            Intent intent = new Intent(getApplicationContext(),PantallaNota.class);
                            intent.putExtra("titol_nota",object.getString("Titol"));
                            intent.putExtra("text_nota",object.getString("Text"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"No existeix cap nota amb aquest titol",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString() ,Toast.LENGTH_SHORT).show();
                        Log.d("Error: ", error.toString());
                    }
                });
                queue.add(stringRequest);
                break;
            case R.id.btn_novanota:
                Intent intent = new Intent(getApplicationContext(), PantallaNotaNova.class);
                startActivity(intent);
                break;
            case R.id.btn_pbn_perfil:
                Intent intent2 = new Intent(getApplicationContext(), PantallaPerfil.class);
                startActivity(intent2);
                break;
        }
    }
}
