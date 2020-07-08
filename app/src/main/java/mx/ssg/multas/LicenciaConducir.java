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
    EditText txtLicencia,txtNombre,txtApaterno,txtAmaterno,txtFNacimiento,txtDireccion,txtSangre,txtValidez,txtClase,txtObservaciones;
    RadioGroup radioNacionalidad;
    RadioButton rMexicano,rExtranjero;
    TextView lblResultScaner;
    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    String ResultQR = "SIN QR";
    String Tag = "LICENCIA CONDUCIR";
    String licencia,nombre,apaterno,amaterno,fNacimiento,direccion,sangre,validez,clase,nacionalidad,observaciones,respuestaJson;
    String resNacionalidad = "";
    String resObservaciones = "";
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
        txtLicencia.setFocusableInTouchMode(true); txtLicencia.requestFocus();

        txtNombre = findViewById(R.id.txtNombreL);
        txtApaterno = findViewById(R.id.txtApaternoL);
        txtAmaterno = findViewById(R.id.txtAmaternoL);
        txtFNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtSangre = findViewById(R.id.txtTipoSangre);
        txtValidez = findViewById(R.id.txtValidez);
        txtClase = findViewById(R.id.txtClase);
        txtObservaciones = findViewById(R.id.txtObservacionesLC);
        radioNacionalidad = findViewById(R.id.radioNacionalidadL);

        lblResultScaner = findViewById(R.id.linkQrL);

        btnReglamento = findViewById(R.id.lyInicioL);
        btnLugaresPago = findViewById(R.id.lyCategoriaL);
        btnContactos = findViewById(R.id.lyContactoL);
        btnTabulador = findViewById(R.id.lyFavoritosL);


        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                txtFNacimiento.setText(+dayOfMonth+ "-" +month+ "-" +year);



            }
        };
        date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                txtValidez.setText(+dayOfMonth+ "-" +month+ "-" +year);


            }};

       txtFNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog=new DatePickerDialog(LicenciaConducir.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,day,month,year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        txtValidez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog=new DatePickerDialog(LicenciaConducir.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        date,day,month,year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

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
                if(txtObservaciones.getText().toString().isEmpty()){
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

        radioNacionalidad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMexicanaL) {
                    resNacionalidad = "Mexicana";
                } else if (checkedId == R.id.radioExtrangeraL) {
                    resNacionalidad = "Extranjera";
                }
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
                        insertRegistroLicencia();
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
                                    jObj = new JSONObject(""+myResponse+"");
                                    nombre = jObj.getString("NombreL");
                                    apaterno = jObj.getString("ApellidoPL");
                                    amaterno = jObj.getString("ApellidoML");
                                    fNacimiento = jObj.getString("FechaNacimiento");
                                    direccion = jObj.getString("Direccion");
                                    sangre = jObj.getString("Sangre");
                                    validez = jObj.getString("Validez");
                                    clase = jObj.getString("Clase");
                                    txtNombre.setText(nombre);
                                    txtApaterno.setText(apaterno);
                                    txtAmaterno.setText(amaterno);
                                    txtFNacimiento.setText(fNacimiento);
                                    txtDireccion.setText(direccion);
                                    txtSangre.setText(sangre);
                                    txtValidez.setText(validez);
                                    txtLicencia.setText(licencia);
                                    txtClase.setText(clase);
                                    resNacionalidad = jObj.getString("Nacionalidad");
                                    resObservaciones = jObj.getString("Observaciones");
                                    System.out.println(resNacionalidad);
                                    System.out.println(resObservaciones);
                                    nacionalidad = "Mexicana";
                                    observaciones = "null";
                                    if(resNacionalidad.equals(nacionalidad)){
                                        rMexicano = (RadioButton)radioNacionalidad.getChildAt(0);
                                        rMexicano.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidad.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroLicencia() {
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
