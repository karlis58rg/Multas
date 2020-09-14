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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LicenciaConducir extends AppCompatActivity {
    ImageView btnMenuL,btnTarjetaL,btnInfraccionL;
    Button btnGuardarLC;
    public static EditText txtLicencia,txtNombre,txtApaterno,txtAmaterno,txtTipocalle,txtCalleLC,txtNumeroCalle,txtColoniaLC,txtCP,txtMunicipioLC,txtEstadoLC;
    public static EditText txtFechaExLC,txtFechaVenLC,txtTipoVigLC,txtTipoLic,txtRFCLC,txtHomoLC,txtGrupoSanguiLC,txtRequeriemientosEspLC,txtEmailLC,txtObservacionesLC;
    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    String Tag = "LICENCIA CONDUCIR";
    String licencia = " " ,nombre = " " ,apaterno = " " ,amaterno = " " ,Tipocalle = " " ,CalleLC = " " ,NumeroCalle = " " ,ColoniaLC = " " ,CP,MunicipioLC = " " ,EstadoLC = " " ,FechaExLC = " " ,respuestaJson;
    String FechaVenLC = " " ,TipoVigLC = " " ,TipoLic = " " ,RFCLC = " " ,HomoLC = " " ,GrupoSanguiLC = " " ,RequeriemientosEspLC = " " ,EmailLC = " " ,ObservacionesLC = " ";
    private  DatePickerDialog.OnDateSetListener dateSetListener,date;
    Calendar calendar = Calendar.getInstance();
    SharedPreferences share;
    SharedPreferences.Editor editor;
    private int dia,mes,año,dia1,mes1,año1;
    String email;
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
        btnTarjetaL = findViewById(R.id.imgTCLicencia);
        btnGuardarLC = findViewById(R.id.imgGuardarLC);
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
        licencia = i.getStringExtra("serie");
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

        txtLicencia.setText(licencia);
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

        btnTarjetaL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LicenciaConducir.this,TarjetasConductor.class);
                startActivity(i);
                finish();
            }
        });

        btnGuardarLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                ///////validacion email//////

                if(validateEmailAddress( txtEmailLC.getText().toString())){
                    //Toast.makeText(getApplicationContext(), "EMAIL VALIDO.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "EMAIL INVALIDO.", Toast.LENGTH_SHORT).show();
                }
                getExistRegistro();
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


   ///////////// calendario para fechas en formulario///////////
        txtFechaExLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                año = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LicenciaConducir.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaExLC.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, dia, mes, año);
                datePickerDialog.show();
            }
        });

        txtFechaVenLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia1 = c.get(Calendar.DAY_OF_MONTH);
                mes1 = c.get(Calendar.MONTH);
                año1 = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LicenciaConducir.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaVenLC.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, dia1, mes1, año1);
                datePickerDialog.show();
            }
        });



    }
    ///////validacion email//////
    private boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();


}


    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Licencia?idExistente="+cargarInfoRandom)
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroLicencia() {
        cargarDatos();
        licencia = txtLicencia.getText().toString();
        apaterno = txtApaterno.getText().toString();
        amaterno = txtAmaterno.getText().toString();
        nombre = txtNombre.getText().toString();
        Tipocalle = txtTipocalle.getText().toString();
        CalleLC = txtCalleLC.getText().toString();
        NumeroCalle = txtNumeroCalle.getText().toString();
        ColoniaLC = txtColoniaLC.getText().toString();
        CP = txtCP.getText().toString();
        MunicipioLC = txtMunicipioLC.getText().toString();
        EstadoLC = txtEstadoLC.getText().toString();
        FechaExLC = txtFechaExLC.getText().toString();
        FechaVenLC = txtFechaVenLC.getText().toString();
        TipoVigLC = txtTipoVigLC.getText().toString();
        TipoLic = txtTipoLic.getText().toString();
        RFCLC = txtRFCLC.getText().toString();
        HomoLC = txtHomoLC.getText().toString();
        GrupoSanguiLC = txtGrupoSanguiLC.getText().toString();
        RequeriemientosEspLC = txtRequeriemientosEspLC.getText().toString();
        EmailLC = txtEmailLC.getText().toString();
        if(txtObservacionesLC.getText().toString().isEmpty()){
            ObservacionesLC = "NINGUNA";
        }else{
            ObservacionesLC = txtObservacionesLC.getText().toString();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("NoLicencia", licencia )
                .add("ApellidoPL", apaterno)
                .add("ApellidoML", amaterno)
                .add("NombreL", nombre)
                .add("TipoCalle", Tipocalle)
                .add("Calle", CalleLC)
                .add("Numero", NumeroCalle)
                .add("Colonia", ColoniaLC)
                .add("Cp", CP)
                .add("Municipio", MunicipioLC)
                .add("Estado", EstadoLC)
                .add("FechaExpedicion",FechaExLC)
                .add("FechaVencimiento", FechaVenLC)
                .add("TipoVigencia", TipoVigLC)
                .add("TipoLecencia", TipoLic)
                .add("Rfc", RFCLC)
                .add("Homo", HomoLC)
                .add("GrupoSanguineo", GrupoSanguiLC)
                .add("RequerimientosEspeciales", RequeriemientosEspLC)
                .add("Email", EmailLC)
                .add("Observaciones", ObservacionesLC)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Licencia/")
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
                            txtLicencia.setText("");
                            txtApaterno.setText("");
                            txtAmaterno.setText("");
                            txtNombre.setText("");
                            txtTipocalle.setText("");
                            txtCalleLC.setText("");
                            txtNumeroCalle.setText("");
                            txtColoniaLC.setText("");
                            txtCP.setText("");
                            txtMunicipioLC.setText("");
                            txtEstadoLC.setText("");
                            txtFechaExLC.setText("");
                            txtFechaVenLC.setText("");
                            txtTipoVigLC.setText("");
                            txtTipoLic.setText("");
                            txtRFCLC.setText("");
                            txtHomoLC.setText("");
                            txtGrupoSanguiLC.setText("");
                            txtRequeriemientosEspLC.setText("");
                            txtEmailLC.setText("");
                            txtObservacionesLC.setText("");
                        }
                    });
                }
            }
        });
    }

    public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(90000) * 99;
        codigoVerifi = String.valueOf(numberRandom);
        System.out.println(codigoVerifi);
        guardarRandom();
    }

    private void guardarRandom() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("RANDOM","20"+codigoVerifi);
        editor.commit();
    }
    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarInfoRandom = share.getString("RANDOM", "");
    }
}
