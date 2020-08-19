package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TarjetaCirculacion extends AppCompatActivity {
    LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    ImageView btnQrScan,btnLicencia,btnBuscarTarjeta,btnBuscarSerie,btnBuscarPlaca,btnMenuTC,btnTerminalTC;
    TextView lblResultScaner;
    String ResultQR,servicio,origen,observaciones,resOrigen,resObservaciones,resServicio,respuestaJson;
    String Tag = "TarjetaCirculación";
    EditText txtNoTarjetaTC,txtNoSerieTC,txtNoPlacaTC;
    EditText txtNombreTC,txtApaternoTC,txtAmaternoTC,txtNombreR,txtApaternoR,txtAmaternoR;
    EditText txtNoMotorTC,txtMarca,txtSubmarca,txtModelo,txtColor,txtMunicipio,txtLocalidad,txtObservaciones;
    String noTarjetaTC,noSerieTC,noPlacaTC;
    String nombreTC,aPaternoTC,aMaternoTC,nombreR,aPaternoR,aMaternoR;
    String noMotorTC,marca,subMarca,modelo,color,municipio,localidad;
    Spinner spinTipoServicio;
    RadioGroup radioNacionalidadTC;
    RadioButton rNacional,rExtranjero;
    Button btnGuardarTC;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String codigoVerifi, cargarInfoRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta_circulacion);
        cargarDatos();
        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        btnMenuTC = findViewById(R.id.btnListTC);
        btnBuscarTarjeta = findViewById(R.id.imgBuscarNoTarjeta);
        btnBuscarSerie = findViewById(R.id.imgBuscarNoserie);
        btnBuscarPlaca = findViewById(R.id.imgBuscarNoPlaca);
        btnQrScan = findViewById(R.id.imgQrTarjetaCirculacion);
        lblResultScaner = findViewById(R.id.linkQrTC);
        btnLicencia = findViewById(R.id.imgLicenciaTC);
        btnGuardarTC = findViewById(R.id.btnGuardarTC);
        btnTerminalTC = findViewById(R.id.imgTerminalTC);

        txtNoTarjetaTC = findViewById(R.id.txtNoTarjeta);
        txtNoTarjetaTC.setFocusableInTouchMode(true); txtNoTarjetaTC.requestFocus();

        txtNoSerieTC = findViewById(R.id.txtNoSerie);
        txtNoPlacaTC = findViewById(R.id.txtPlaca);


        txtNombreTC = findViewById(R.id.txtNombreTC);
        txtApaternoTC = findViewById(R.id.txtApaternoTC);
        txtAmaternoTC = findViewById(R.id.txtAmaternoTC);
        txtNombreR = findViewById(R.id.txtNombreR);
        txtApaternoR = findViewById(R.id.txtApaternoR);
        txtAmaternoR = findViewById(R.id.txtAmaternoR);

        txtNoMotorTC = findViewById(R.id.txtNoMotor);
        txtMarca = findViewById(R.id.txtMarca);
        txtSubmarca = findViewById(R.id.txtSubMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtColor = findViewById(R.id.txtColor);
        txtMunicipio = findViewById(R.id.txtMunicipio);
        txtLocalidad = findViewById(R.id.txtLocalidad);
        radioNacionalidadTC = findViewById(R.id.radioNacionalidad);
        txtObservaciones = findViewById(R.id.txtObservacionesTC);

        btnReglamento = findViewById(R.id.lyInicioTC);
        btnLugaresPago = findViewById(R.id.lyCategoriaTC);
        btnContactos = findViewById(R.id.lyContactoTC);
        btnTabulador = findViewById(R.id.lyFavoritosTC);

        btnMenuTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this,Reglamento.class);
                startActivity(i);
            }
        });

        btnBuscarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNoTarjetaTC.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL NÚMERO DE TARJETA ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getNoTarjeta();
                }
            }
        });

        btnBuscarSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNoSerieTC.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL NÚMERO DE SERIE ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getNoSerie();
                }
            }
        });

        btnBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNoPlacaTC.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"LA PLACA ES NECESARIA",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getPlaca();
                }
            }
        });

        btnGuardarTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtObservaciones.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR ALGÚN COMENTARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getExistRegistro();
                }
            }
        });

        btnQrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(TarjetaCirculacion.this).initiateScan();
            }
        });

        btnLicencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, LicenciaConducir.class);
                startActivity(i);
            }
        });

        radioNacionalidadTC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMexicanaL) {
                    resOrigen = "Nacional";
                } else if (checkedId == R.id.radioExtrangeraL) {
                    resOrigen = "Extranjero";
                }

            }
        });

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, ViewPDFController.class);
                startActivity(i);
            }
        });

        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, LugaresDePago.class);
                startActivity(i);
            }
        });

        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, Contactos.class);
                startActivity(i);
            }
        });

        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, Tabulador.class);
                startActivity(i);
            }
        });

        btnTerminalTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetaCirculacion.this, Infraccion.class);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null)
            if(result.getContents() != null){
                lblResultScaner.setText(result.getContents());
                ResultQR = lblResultScaner.toString();
                Toast.makeText(this, "QR CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                Log.i(Tag, ResultQR);
            }else{
                ResultQR = "QR SIN INFORMACIÓN";
                Toast.makeText(this, ResultQR, Toast.LENGTH_SHORT).show();
            }
    }

    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?idExistente="+cargarInfoRandom)
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
                        insertRegistroTarjeta();
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    /********************************************************************************************************************/
    /******************GET A LA BD***********************************/
    public void getNoTarjeta() {
        noTarjetaTC = txtNoTarjetaTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?noTarjeta="+noTarjetaTC)
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
                    TarjetaCirculacion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    noSerieTC = jObj.getString("NoSerie");
                                    noPlacaTC = jObj.getString("Placa");
                                    nombreTC = jObj.getString("NombreP");
                                    aPaternoTC = jObj.getString("ApellidoPP");
                                    aMaternoTC = jObj.getString("ApellidoMP");
                                    nombreR = jObj.getString("NombreU");
                                    aPaternoR = jObj.getString("ApellidoPU");
                                    aMaternoR = jObj.getString("ApellidoMU");
                                    noMotorTC = jObj.getString("NoMotor");
                                    marca = jObj.getString("Marca");
                                    subMarca = jObj.getString("SubMarca");
                                    modelo = jObj.getString("Modelo");
                                    color = jObj.getString("Color");
                                    municipio = jObj.getString("Municipio");
                                    localidad = jObj.getString("Localidad");
                                    resOrigen = jObj.getString("Origen");
                                    resServicio = jObj.getString("TipoServicio");
                                    resObservaciones = jObj.getString("Observaciones");
                                    txtNoSerieTC.setText(noSerieTC);
                                    txtNoPlacaTC.setText(noPlacaTC);
                                    txtNombreTC.setText(nombreTC);
                                    txtApaternoTC.setText(aPaternoTC);
                                    txtAmaternoTC.setText(aMaternoTC);
                                    txtNombreR.setText(nombreR);
                                    txtApaternoR.setText(aPaternoR);
                                    txtAmaternoR.setText(aMaternoR);
                                    txtNoMotorTC.setText(noMotorTC);
                                    txtMarca.setText(marca);
                                    txtSubmarca.setText(subMarca);
                                    txtModelo.setText(modelo);
                                    txtColor.setText(color);
                                    txtMunicipio.setText(municipio);
                                    txtLocalidad.setText(localidad);
                                    origen = "Nacional";
                                    servicio = "Privado";
                                    observaciones = "null";
                                    if(resOrigen.equals(origen)){
                                        rNacional = (RadioButton)radioNacionalidadTC.getChildAt(0);
                                        rNacional.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidadTC.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }
                                    /*if(resServicio.equals(servicio)){
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Privado"));
                                    }else{
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Publico"));
                                    }*/
                                    if(resObservaciones.equals(observaciones)){
                                        txtObservaciones.setText("SIN OBSERVACIONES");
                                    }else{
                                        resObservaciones = jObj.getString("Observaciones");
                                        txtObservaciones.setText(resObservaciones);
                                    }
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

    public void getNoSerie() {
        noSerieTC = txtNoSerieTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?noSerie="+noSerieTC)
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
                    TarjetaCirculacion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    noTarjetaTC = jObj.getString("NoTarjeta");
                                    noPlacaTC = jObj.getString("Placa");
                                    nombreTC = jObj.getString("NombreP");
                                    aPaternoTC = jObj.getString("ApellidoPP");
                                    aMaternoTC = jObj.getString("ApellidoMP");
                                    nombreR = jObj.getString("NombreU");
                                    aPaternoR = jObj.getString("ApellidoPU");
                                    aMaternoR = jObj.getString("ApellidoMU");
                                    noMotorTC = jObj.getString("NoMotor");
                                    marca = jObj.getString("Marca");
                                    subMarca = jObj.getString("SubMarca");
                                    modelo = jObj.getString("Modelo");
                                    color = jObj.getString("Color");
                                    municipio = jObj.getString("Municipio");
                                    localidad = jObj.getString("Localidad");
                                    resOrigen = jObj.getString("Origen");
                                    resServicio = jObj.getString("TipoServicio");
                                    resObservaciones = jObj.getString("Observaciones");
                                    txtNoTarjetaTC.setText(noTarjetaTC);
                                    txtNoPlacaTC.setText(noPlacaTC);
                                    txtNombreTC.setText(nombreTC);
                                    txtApaternoTC.setText(aPaternoTC);
                                    txtAmaternoTC.setText(aMaternoTC);
                                    txtNombreR.setText(nombreR);
                                    txtApaternoR.setText(aPaternoR);
                                    txtAmaternoR.setText(aMaternoR);
                                    txtNoMotorTC.setText(noMotorTC);
                                    txtMarca.setText(marca);
                                    txtSubmarca.setText(subMarca);
                                    txtModelo.setText(modelo);
                                    txtColor.setText(color);
                                    txtMunicipio.setText(municipio);
                                    txtLocalidad.setText(localidad);
                                    origen = "Nacional";
                                    servicio = "Privado";
                                    observaciones = "null";
                                    if(resOrigen.equals(origen)){
                                        rNacional = (RadioButton)radioNacionalidadTC.getChildAt(0);
                                        rNacional.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidadTC.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }
                                    /*if(resServicio.equals(servicio)){
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Privado"));
                                    }else{
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Publico"));
                                    }*/
                                    if(resObservaciones.equals(observaciones)){
                                        txtObservaciones.setText("SIN OBSERVACIONES");
                                    }else{
                                        resObservaciones = jObj.getString("Observaciones");
                                        txtObservaciones.setText(resObservaciones);
                                    }
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

    public void getPlaca() {
        noPlacaTC = txtNoPlacaTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?noPlaca="+noPlacaTC)
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
                    TarjetaCirculacion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    noTarjetaTC = jObj.getString("NoTarjeta");
                                    noSerieTC = jObj.getString("NoSerie");
                                    nombreTC = jObj.getString("NombreP");
                                    aPaternoTC = jObj.getString("ApellidoPP");
                                    aMaternoTC = jObj.getString("ApellidoMP");
                                    nombreR = jObj.getString("NombreU");
                                    aPaternoR = jObj.getString("ApellidoPU");
                                    aMaternoR = jObj.getString("ApellidoMU");
                                    noMotorTC = jObj.getString("NoMotor");
                                    marca = jObj.getString("Marca");
                                    subMarca = jObj.getString("SubMarca");
                                    modelo = jObj.getString("Modelo");
                                    color = jObj.getString("Color");
                                    municipio = jObj.getString("Municipio");
                                    localidad = jObj.getString("Localidad");
                                    resOrigen = jObj.getString("Origen");
                                    resServicio = jObj.getString("TipoServicio");
                                    resObservaciones = jObj.getString("Observaciones");
                                    txtNoTarjetaTC.setText(noTarjetaTC);
                                    txtNoSerieTC.setText(noSerieTC);
                                    txtNombreTC.setText(nombreTC);
                                    txtApaternoTC.setText(aPaternoTC);
                                    txtAmaternoTC.setText(aMaternoTC);
                                    txtNombreR.setText(nombreR);
                                    txtApaternoR.setText(aPaternoR);
                                    txtAmaternoR.setText(aMaternoR);
                                    txtNoMotorTC.setText(noMotorTC);
                                    txtMarca.setText(marca);
                                    txtSubmarca.setText(subMarca);
                                    txtModelo.setText(modelo);
                                    txtColor.setText(color);
                                    txtMunicipio.setText(municipio);
                                    txtLocalidad.setText(localidad);
                                    origen = "Nacional";
                                    servicio = "Privado";
                                    observaciones = "null";
                                    if(resOrigen.equals(origen)){
                                        rNacional = (RadioButton)radioNacionalidadTC.getChildAt(0);
                                        rNacional.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidadTC.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }
                                    /*if(resServicio.equals(servicio)){
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Privado"));
                                    }else{
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Publico"));
                                    }*/
                                    if(resObservaciones.equals(observaciones)){
                                        txtObservaciones.setText("SIN OBSERVACIONES");
                                    }else{
                                        resObservaciones = jObj.getString("Observaciones");
                                        txtObservaciones.setText(resObservaciones);
                                    }
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

    /******************GET A BAJA CALIFORNIA***********************************/
    public void getPlacaBJ() {
        noPlacaTC = txtNoPlacaTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?noPlacaToken="+noPlacaTC)
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
                    TarjetaCirculacion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                   /* noTarjetaTC = jObj.getString("NoTarjeta");
                                    noSerieTC = jObj.getString("NoSerie");
                                    nombreTC = jObj.getString("NombreP");
                                    aPaternoTC = jObj.getString("ApellidoPP");
                                    aMaternoTC = jObj.getString("ApellidoMP");
                                    nombreR = jObj.getString("NombreU");
                                    aPaternoR = jObj.getString("ApellidoPU");
                                    aMaternoR = jObj.getString("ApellidoMU");
                                    noMotorTC = jObj.getString("NoMotor");
                                    marca = jObj.getString("Marca");
                                    subMarca = jObj.getString("SubMarca");
                                    modelo = jObj.getString("Modelo");
                                    color = jObj.getString("Color");
                                    municipio = jObj.getString("Municipio");
                                    localidad = jObj.getString("Localidad");
                                    resOrigen = jObj.getString("Origen");
                                    resServicio = jObj.getString("TipoServicio");
                                    resObservaciones = jObj.getString("Observaciones");
                                    txtNoTarjetaTC.setText(noTarjetaTC);
                                    txtNoSerieTC.setText(noSerieTC);
                                    txtNombreTC.setText(nombreTC);
                                    txtApaternoTC.setText(aPaternoTC);
                                    txtAmaternoTC.setText(aMaternoTC);
                                    txtNombreR.setText(nombreR);
                                    txtApaternoR.setText(aPaternoR);
                                    txtAmaternoR.setText(aMaternoR);
                                    txtNoMotorTC.setText(noMotorTC);
                                    txtMarca.setText(marca);
                                    txtSubmarca.setText(subMarca);
                                    txtModelo.setText(modelo);
                                    txtColor.setText(color);
                                    txtMunicipio.setText(municipio);
                                    txtLocalidad.setText(localidad);
                                    origen = "Nacional";
                                    servicio = "Privado";
                                    observaciones = "null";
                                    if(resOrigen.equals(origen)){
                                        rNacional = (RadioButton)radioNacionalidadTC.getChildAt(0);
                                        rNacional.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidadTC.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }*/
                                    /*if(resServicio.equals(servicio)){
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Privado"));
                                    }else{
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Publico"));
                                    }*/
                                    /*if(resObservaciones.equals(observaciones)){
                                        txtObservaciones.setText("SIN OBSERVACIONES");
                                    }else{
                                        resObservaciones = jObj.getString("Observaciones");
                                        txtObservaciones.setText(resObservaciones);
                                    }*/
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
    private void insertRegistroTarjeta() {

        noTarjetaTC = txtNoTarjetaTC.getText().toString().toUpperCase();
        noSerieTC = txtNoSerieTC.getText().toString().toUpperCase();
        noPlacaTC = txtNoPlacaTC.getText().toString().toUpperCase();
        nombreTC = txtNombreTC.getText().toString().toUpperCase();
        aPaternoTC = txtApaternoTC.getText().toString().toUpperCase();
        aMaternoTC = txtAmaternoTC.getText().toString().toUpperCase();
        nombreR = txtNombreR.getText().toString().toUpperCase();
        aPaternoR = txtApaternoR.getText().toString().toUpperCase();
        aMaternoR = txtAmaternoR.getText().toString().toUpperCase();
        noMotorTC = txtNoMotorTC.getText().toString().toUpperCase();
        marca = txtMarca.getText().toString().toUpperCase();
        subMarca = txtSubmarca.getText().toString().toUpperCase();
        modelo = txtModelo.getText().toString().toUpperCase();
        color = txtColor.getText().toString().toUpperCase();
        municipio = txtMunicipio.getText().toString().toUpperCase();
        localidad = txtLocalidad.getText().toString().toUpperCase();
        //servicio = (String) spinTipoServicio.getSelectedItem();
        observaciones = txtObservaciones.getText().toString().toUpperCase();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("NoTarjeta", noTarjetaTC)
                .add("NoSerie", noSerieTC)
                .add("Placa", noPlacaTC)
                .add("NombreP", nombreTC)
                .add("ApellidoPP", aPaternoTC)
                .add("ApellidoMP", aMaternoTC)
                .add("NombreU", nombreR)
                .add("ApellidoPU", aPaternoR)
                .add("ApellidoMU", aMaternoR)
                .add("NoMotor", noMotorTC)
                .add("Marca", marca)
                .add("SubMarca", subMarca)
                .add("Modelo", modelo)
                .add("Color", color)
                .add("Municipio", municipio)
                .add("Localidad", localidad)
                .add("Origen", resOrigen)
                //.add("TipoServicio", servicio)
                .add("Observaciones", observaciones)
                //.add("Qr", ResultQR)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion/")
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
                    TarjetaCirculacion.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
                            txtNoTarjetaTC.setText("");
                            txtNoSerieTC.setText("");
                            txtNoPlacaTC.setText("");
                            txtNombreTC.setText("");
                            txtApaternoTC.setText("");
                            txtAmaternoTC.setText("");
                            txtNombreR.setText("");
                            txtApaternoR.setText("");
                            txtAmaternoR.setText("");
                            txtNoMotorTC.setText("");
                            txtMarca.setText("");
                            txtSubmarca.setText("");
                            txtModelo.setText("");
                            txtColor.setText("");
                            txtMunicipio.setText("");
                            txtLocalidad.setText("");
                            radioNacionalidadTC.clearCheck();
                            //spinTipoServicio.setAdapter(null);
                            txtObservaciones.setText("");
                            lblResultScaner.setText("");
                        }
                    });
                }

            }
        });
    }

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
