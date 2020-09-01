package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LicenciaConducir extends AppCompatActivity {
    ImageView btnMenuL,btnQrL,btnBuscarL,btnTarjetaL,btnInfraccionL;
    Button btnGuardarLC;
    public static EditText txtLicencia,txtNombre,txtApaterno,txtAmaterno,txtTipocalle,txtCalleLC,txtNumeroCalle,txtColoniaLC,txtCP,txtMunicipioLC,txtEstadoLC;
    public static EditText txtFechaExLC,txtFechaVenLC,txtTipoVigLC,txtTipoLic,txtRFCLC,txtHomoLC,txtGrupoSanguiLC,txtRequeriemientosEspLC,txtEmailLC,txtObservacionesLC;
    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    String Tag = "LICENCIA CONDUCIR";
    String licencia,nombre,apaterno,amaterno,Tipocalle,CalleLC,NumeroCalle,ColoniaLC,CP,MunicipioLC,EstadoLC,FechaExLC,respuestaJson;
    String FechaVenLC,TipoVigLC,TipoLic,RFCLC,HomoLC,GrupoSanguiLC,RequeriemientosEspLC,EmailLC,ObservacionesLC;
    private  DatePickerDialog.OnDateSetListener dateSetListener,date;
    Calendar calendar = Calendar.getInstance();
    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String codigoVerifi, cargarInfoRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licencia_conducir);
        cargarDatos();

        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        btnMenuL = findViewById(R.id.btnListL);
        btnQrL = findViewById(R.id.imgQrLicenciaConducir);
        btnBuscarL = findViewById(R.id.imgBuscarLicencia);
        btnTarjetaL = findViewById(R.id.imgTCLicencia);
        btnGuardarLC = findViewById(R.id.imgGuardarL);
        btnInfraccionL = findViewById(R.id.imgTerminalLC);

        txtLicencia = findViewById(R.id.txtNoLicencia);
        /////posicion de cursor///////////
        txtLicencia.setFocusableInTouchMode(true); txtLicencia.requestFocus();

        txtApaterno = findViewById(R.id.txtApaternoL);
        txtAmaterno = findViewById(R.id.txtAmaternoL);
        txtNombre = findViewById(R.id.txtNombreL);
        txtTipocalle = findViewById(R.id.txtTipocalle);
        txtCalleLC = findViewById(R.id.txtCalleLC);
        txtNumeroCalle = findViewById(R.id.txtNumeroCalle);
        txtColoniaLC = findViewById(R.id.txtColoniaLC);
        txtCP = findViewById(R.id.txtCP);
        txtMunicipioLC = findViewById(R.id.txtMunicipioLC);
        txtEstadoLC = findViewById(R.id.txtEstadoLC);
        txtFechaExLC = findViewById(R.id.txtFechaExLC);
        txtFechaVenLC = findViewById(R.id.txtFechaVenLC);
        txtTipoVigLC = findViewById(R.id.txtTipoVigLC);
        txtTipoLic = findViewById(R.id.txtTipoLic);
        txtRFCLC = findViewById(R.id.txtRFCLC);
        txtHomoLC = findViewById(R.id.txtHomoLC);
        txtGrupoSanguiLC = findViewById(R.id.txtGrupoSanguiLC);
        txtRequeriemientosEspLC = findViewById(R.id.txtRequeriemientosEspLC);
        txtEmailLC = findViewById(R.id.txtEmailLC);
        txtObservacionesLC = findViewById(R.id.txtObservacionesLC);

        btnReglamento = findViewById(R.id.lyInicioL);
        btnLugaresPago = findViewById(R.id.lyCategoriaL);
        btnContactos = findViewById(R.id.lyContactoL);
        btnTabulador = findViewById(R.id.lyFavoritosL);

        Intent i = getIntent();
        apaterno = i.getStringExtra("apaterno");
        amaterno = i.getStringExtra("amaterno");
        nombre = i.getStringExtra("nombre");
        Tipocalle = i.getStringExtra("Tipocalle");
        CalleLC = i.getStringExtra("CalleLC");
        NumeroCalle = i.getStringExtra("NumeroCalle");
        ColoniaLC = i.getStringExtra("ColoniaLC");
        CP = i.getStringExtra("CP");
        MunicipioLC = i.getStringExtra("MunicipioLC");
        EstadoLC = i.getStringExtra("EstadoLC");
        FechaExLC = i.getStringExtra("FechaExLC");
        FechaVenLC = i.getStringExtra("FechaVenLC");
        TipoVigLC = i.getStringExtra("TipoVigLC");
        TipoLic = i.getStringExtra("TipoLic");
        RFCLC = i.getStringExtra("RFCLC");
        HomoLC = i.getStringExtra("HomoLC");
        GrupoSanguiLC = i.getStringExtra("GrupoSanguiLC");
        RequeriemientosEspLC = i.getStringExtra("RequeriemientosEspLC");

        txtApaterno.setText(apaterno);
        txtAmaterno.setText(amaterno);
        txtNombre.setText(nombre);
        txtTipocalle.setText(Tipocalle);
        txtCalleLC.setText(CalleLC);
        txtNumeroCalle.setText(NumeroCalle);
        txtColoniaLC.setText(ColoniaLC);
        txtCP.setText(CP);
        txtMunicipioLC.setText(MunicipioLC);
        txtEstadoLC.setText(EstadoLC);
        txtFechaExLC.setText(FechaExLC);
        txtFechaVenLC.setText(FechaVenLC);
        txtTipoVigLC.setText(TipoVigLC);
        txtTipoLic.setText(TipoLic);
        txtRFCLC.setText(RFCLC);
        txtHomoLC.setText(HomoLC);
        txtGrupoSanguiLC.setText(GrupoSanguiLC);
        txtRequeriemientosEspLC.setText(RequeriemientosEspLC);

        btnMenuL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this,Reglamento.class);
                startActivity(i);
                finish();
            }
        });

        btnQrL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(LicenciaConducir.this).initiateScan();
            }
        });

        btnBuscarL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtLicencia.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL NÚMERO DE LICENCIA ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getUsuaioL();
                }

            }
        });

        btnTarjetaL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this,TarjetaCirculacion.class);
                startActivity(i);
                finish();
            }
        });

        btnGuardarLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtObservacionesLC.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR ALGÚN COMENTARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getExistRegistro();
                }
            }
        });
        btnInfraccionL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(LicenciaConducir.this, Infraccion.class);
                    startActivity(i);
                    finish();
            }
        });
        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this, Contactos.class);
                startActivity(i);
                finish();
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });
    }


    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaConducir?idExistente="+cargarInfoRandom)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    myResponse = myResponse.replace('"',' ');
                    myResponse = myResponse.trim();
                    String resp = myResponse;
                    String valorUser = "true";
                    if(resp.equals(valorUser)){
                        Looper.prepare(); // to be able to make toast
                        Toast.makeText(getApplicationContext(), "YA EXISTE UN REGISTRO CON ESTOS DATOS", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else{
                        //insertRegistroLicencia();
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    /********************************************************************************************************************/
    /******************GET A LA BD***********************************/
    public void getUsuaioL() {
        licencia = txtLicencia.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaConducir?noLicencia="+licencia)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    LicenciaConducir.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    String resObj = myResponse;
                                    resObj = resObj.replace("["," ");
                                    resObj = resObj.replace("]"," ");

                                    jObj = new JSONObject(""+resObj+"");
                                    apaterno = jObj.getString("paterno");
                                    amaterno = jObj.getString("materno");
                                    nombre = jObj.getString("nombre");
                                    Tipocalle = jObj.getString("tipoCalle");
                                    CalleLC = jObj.getString("calle");
                                    NumeroCalle = jObj.getString("numero");
                                    ColoniaLC = jObj.getString("colonia");
                                    CP = jObj.getString("cp");
                                    MunicipioLC = jObj.getString("municipio");
                                    EstadoLC = jObj.getString("estado");
                                    FechaExLC = jObj.getString("fechaExp");
                                    FechaVenLC = jObj.getString("fechaVenc");
                                    TipoVigLC = jObj.getString("tipoVigencia");
                                    TipoLic = jObj.getString("tipoLic");
                                    RFCLC = jObj.getString("rfc");
                                    HomoLC = jObj.getString("homo");
                                    GrupoSanguiLC = jObj.getString("grupoSanguineo");
                                    RequeriemientosEspLC = jObj.getString("requerimientosEspeciales");
                                    Log.i("HERE", ""+jObj);
                                }

                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }

        });
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
   /* private void insertRegistroLicencia() {
        cargarDatos();
        nombre = txtNombre.getText().toString().toUpperCase();
        apaterno = txtApaterno.getText().toString().toUpperCase();
        amaterno = txtAmaterno.getText().toString().toUpperCase();
        fNacimiento = txtFNacimiento.getText().toString();
        direccion = txtDireccion.getText().toString().toUpperCase();
        sangre = txtSangre.getText().toString();
        validez = txtValidez.getText().toString();
        licencia = txtLicencia.getText().toString();
        clase = txtClase.getText().toString().toUpperCase();
        observaciones = txtObservaciones.getText().toString().toUpperCase();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("NombreL", nombre)
                .add("ApellidoPL", apaterno)
                .add("ApellidoML", amaterno)
                .add("FechaNacimiento", fNacimiento)
                .add("Direccion", direccion)
                .add("Nacionalidad", resNacionalidad)
                .add("Sangre", sangre)
                .add("Validez", validez)
                .add("NoLicencia", licencia)
                .add("Clase", clase)
                .add("Observaciones", observaciones)
                //.add("Qr", ResultQR)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaConducir/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR SU REGISTRO", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();
                    LicenciaConducir.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
                            txtNombre.setText("");
                            txtApaterno.setText("");
                            txtAmaterno.setText("");
                            txtFNacimiento.setText("");
                            txtDireccion.setText("");
                            radioNacionalidad.clearCheck();
                            txtSangre.setText("");
                            txtValidez.setText("");
                            txtLicencia.setText("");
                            txtClase.setText("");
                            txtObservaciones.setText("");
                            lblResultScaner.setText("");
                        }
                    });
                }
            }
        });
    }*/

    public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(9000) * 99;
        codigoVerifi = String.valueOf(numberRandom);
        guardarRandom();
    }
    private void guardarRandom() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("RANDOM", codigoVerifi);
        editor.commit();
    }
    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarInfoRandom = share.getString("RANDOM", "");
    }
}
