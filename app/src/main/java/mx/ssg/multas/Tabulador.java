package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Looper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tabulador extends AppCompatActivity {
    private LinearLayout btnReglamento,btnLugaresPago,btnTabulador;
    private ImageView btnList,btnbuscar;
    EditText txtBuscar;
    ListView listado;
    String buscador, respuestaJson;
    public CustomAdapter cl;
    private ArrayList<String> Clave;
    private ArrayList<String> Descripcion;
    private ArrayList<String> Articulos;
    private ArrayList<String> IdFraccion;
    private ArrayList<String> SalMinimos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabulador);


        listado = findViewById(R.id.Lista);
        btnReglamento = findViewById(R.id.lyInicioTab);
        btnLugaresPago = findViewById(R.id.lyCategoriaTab);
        btnTabulador = findViewById(R.id.lyFavoritosTab);
        btnList = findViewById(R.id.btnListTab);
        btnbuscar = findViewById(R.id.imgWebBuscar);
        txtBuscar = findViewById(R.id.txtBuscarTabulador);

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, ViewPDFController.class);
                startActivity(i);
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, LugaresDePago.class);
                startActivity(i);
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, Tabulador.class);
                startActivity(i);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tabulador.this, Reglamento.class);
                startActivity(i);
            }
        });
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTabulador();
            }
        });
    }

          public void getTabulador() {
                buscador = txtBuscar.getText().toString();
                buscador = buscador.trim();

                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url("http://187.174.102.142/AppTransito/api/Tabulador?varDesc=" + buscador)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();

                            Tabulador.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        respuestaJson = "null";
                                        if (myResponse.equals(respuestaJson)) {
                                            Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                        } else {

                                            JSONArray ja = null;

                                            try {
                                                ja = new JSONArray("" + myResponse + "");


                                            } catch (JSONException e) {
                                                e.printStackTrace();

                                            }

                                            Clave = new ArrayList<>();
                                            Descripcion = new ArrayList<>();
                                            Articulos = new ArrayList<>();
                                            IdFraccion = new ArrayList<>();
                                            SalMinimos = new ArrayList<>();

                                            for (int i = 0; i < ja.length(); i++) {

                                                try {

                                                    Clave.add(ja.getJSONObject(i).getString("Clave"));
                                                    Descripcion.add(ja.getJSONObject(i).getString("Descripcion"));
                                                    Articulos.add(ja.getJSONObject(i).getString("Articulos"));
                                                    IdFraccion.add(ja.getJSONObject(i).getString("IdFraccion"));
                                                    SalMinimos.add(ja.getJSONObject(i).getString("SalMinimos"));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }


                                            if (myResponse != null) {
                                                cl = new CustomAdapter(Tabulador.this, Clave, Descripcion, Articulos, IdFraccion, SalMinimos);
                                                cl.notifyDataSetChanged();
                                                listado.setAdapter(cl);
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Tabulador.this);
                                                builder.setMessage("error").setNegativeButton("PROCESAR DE NUEVO", null).create().show();
                                            }


                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                });
            }

}