package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class TransportePublico extends AppCompatActivity {
    EditText txtPlacaTp,txtEconomico,txtEstatusActual,txtFechaExTp,txtFechaVigTp,txtPropietario,txtTenedorUsuario,txtOficinaExpedTp,txtDelegacionTp,txtNRepuveTp,txtMarcaTp,txtLineaTp,txtVersionTp;
    EditText txtClaseTipoTp,txtColorTp,txtModeloTp,txtPuertasTp,txtCilindrosTp,txtCombustibleTp,txtCapacidadTp,txtAgrupacionTp,txtNSerieTp,txtRegistroPropTp;
    EditText txtRutaSitioTp,txtPermisionarioTp,txtNMotorTp,txtUsoTp,txtObservacionesTp,txtFolioSCTTp,txtUrlTp,txtEmailTp,txtNotasTp;

    String Placa,Economico,EstatusActual,FechaExTp,FechaVigTp,Propietario,TenedorUsuario,OficinaExpedTp,DelegacionTp,NRepuveTp,MarcaTp,LineaTp,VersionTp,ClaseTipoTp,ColorTp,ModeloTp;
    String PuertasTp,CilindrosTp,CombustibleTp,CapacidadTp,AgrupacionTp,NSerieTp,RegistroPropTp,RutaSitioTp,PermisionarioTp,NMotorTp,UsoTp,ObservacionesTp;
    String FolioSCTTp,EmailTp = " ",NotasTp = " ";

    Button btnGuardar;
    ImageView btnVistaPrincipal,btnInfraccion,btnMenuTpu;

    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String codigoVerifi, cargarInfoRandom;

    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    private int dia,mes,año,dia1,mes1,año1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_publico);
        cargarDatos();

        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        ////boton menu //////
        btnMenuTpu = findViewById(R.id.btnListTpu);



        txtPlacaTp = findViewById(R.id.txtPlacaTp);
        /////posicion de cursor///////////
        txtPlacaTp.setFocusableInTouchMode(true); txtPlacaTp.requestFocus();

        txtPlacaTp = findViewById(R.id.txtPlacaTp);
        txtEconomico = findViewById(R.id.txtEconomico);
        txtEstatusActual = findViewById(R.id.txtEstatusActual);
        txtFechaExTp = findViewById(R.id.txtFechaExTp);
        txtFechaVigTp = findViewById(R.id.txtFechaVigTp);
        txtPropietario = findViewById(R.id.txtPropietario);
        txtTenedorUsuario = findViewById(R.id.txtTenedorUsuario);
        txtOficinaExpedTp = findViewById(R.id.txtOficinaExpedTp);
        txtDelegacionTp = findViewById(R.id.txtDelegacionTp);
        txtNRepuveTp = findViewById(R.id.txtNRepuveTp);
        txtMarcaTp = findViewById(R.id.txtMarcaTp);
        txtLineaTp = findViewById(R.id.txtLineaTp);
        txtVersionTp = findViewById(R.id.txtVersionTp);
        txtClaseTipoTp = findViewById(R.id.txtClaseTipoTp);
        txtColorTp = findViewById(R.id.txtColorTp);
        txtModeloTp = findViewById(R.id.txtModeloTp);
        txtPuertasTp = findViewById(R.id.txtPuertasTp);
        txtCilindrosTp = findViewById(R.id.txtCilindrosTp);
        txtCombustibleTp = findViewById(R.id.txtCombustibleTp);
        txtCapacidadTp = findViewById(R.id.txtCapacidadTp);
        txtAgrupacionTp = findViewById(R.id.txtAgrupacionTp);
        txtNSerieTp = findViewById(R.id.txtNSerieTp);
        txtRegistroPropTp = findViewById(R.id.txtRegistroPropTp);
        txtRutaSitioTp = findViewById(R.id.txtRutaSitioTp);
        txtPermisionarioTp = findViewById(R.id.txtPermisionarioTp);
        txtNMotorTp = findViewById(R.id.txtNMotorTp);
        txtUsoTp = findViewById(R.id.txtUsoTp);
        txtObservacionesTp = findViewById(R.id.txtObservacionesTp);
        txtFolioSCTTp = findViewById(R.id.txtFolioSCTTp);
        txtUrlTp = findViewById(R.id.txtUrlTp);
        txtEmailTp = findViewById(R.id.txtEmailTp);
        txtNotasTp = findViewById(R.id.txtNotasTp);

        btnGuardar = findViewById(R.id.imgGuardarTransportePublico);
        btnVistaPrincipal = findViewById(R.id.imgVistaPTP);
        btnInfraccion = findViewById(R.id.imgTerminalTP);

        btnReglamento = findViewById(R.id.lyInicio2);
        btnLugaresPago = findViewById(R.id.lyCategoria2);
        btnContactos = findViewById(R.id.lyContacto2);
        btnTabulador = findViewById(R.id.lyFavoritos2);

        Intent i = getIntent();
        Placa = i.getStringExtra("Placa");
        Economico = i.getStringExtra("Economico");
        EstatusActual = i.getStringExtra("EstatusActual");
        FechaExTp = i.getStringExtra("FechaExTp");
        FechaVigTp = i.getStringExtra("FechaVigTp");
        Propietario = i.getStringExtra("Propietario");
        TenedorUsuario = i.getStringExtra("TenedorUsuario");
        OficinaExpedTp = i.getStringExtra("OficinaExpedTp");
        DelegacionTp = i.getStringExtra("DelegacionTp");
        NRepuveTp = i.getStringExtra("NRepuveTp");
        MarcaTp = i.getStringExtra("MarcaTp");
        LineaTp = i.getStringExtra("LineaTp");
        VersionTp = i.getStringExtra("VersionTp");
        ClaseTipoTp = i.getStringExtra("ClaseTipoTp");
        ColorTp = i.getStringExtra("ColorTp");
        ModeloTp = i.getStringExtra("ModeloTp");
        PuertasTp = i.getStringExtra("PuertasTp");
        CilindrosTp = i.getStringExtra("CilindrosTp");
        CombustibleTp = i.getStringExtra("CombustibleTp");
        CapacidadTp = i.getStringExtra("CapacidadTp");
        AgrupacionTp = i.getStringExtra("AgrupacionTp");
        NSerieTp = i.getStringExtra("NSerieTp");
        RegistroPropTp = i.getStringExtra("RegistroPropTp");
        RutaSitioTp = i.getStringExtra("RutaSitioTp");
        PermisionarioTp = i.getStringExtra("PermisionarioTp");
        NMotorTp = i.getStringExtra("NMotorTp");
        UsoTp = i.getStringExtra("UsoTp");
        ObservacionesTp = i.getStringExtra("ObservacionesTp");
        FolioSCTTp = i.getStringExtra("FolioSCTTp");

        txtPlacaTp.setText(Placa);
        txtEconomico.setText(Economico);
        txtEstatusActual.setText(EstatusActual);
        txtFechaExTp.setText(FechaExTp);
        txtFechaVigTp.setText(FechaVigTp);
        txtPropietario.setText(Propietario);
        txtTenedorUsuario.setText(TenedorUsuario);
        txtOficinaExpedTp.setText(OficinaExpedTp);
        txtDelegacionTp.setText(DelegacionTp);
        txtNRepuveTp.setText(NRepuveTp);
        txtMarcaTp.setText(MarcaTp);
        txtLineaTp.setText(LineaTp);
        txtVersionTp.setText(VersionTp);
        txtClaseTipoTp.setText(ClaseTipoTp);
        txtColorTp.setText(ColorTp);
        txtModeloTp.setText(ModeloTp);
        txtPuertasTp.setText(PuertasTp);
        txtCilindrosTp.setText(CilindrosTp);
        txtCombustibleTp.setText(CombustibleTp);
        txtCapacidadTp.setText(CapacidadTp);
        txtAgrupacionTp.setText(AgrupacionTp);
        txtNSerieTp.setText(NSerieTp);
        txtRegistroPropTp.setText(RegistroPropTp);
        txtRutaSitioTp.setText(RutaSitioTp);
        txtPermisionarioTp.setText(PermisionarioTp);
        txtNMotorTp.setText(NMotorTp);
        txtUsoTp.setText(UsoTp);
        txtObservacionesTp.setText(ObservacionesTp);
        txtFolioSCTTp.setText(FolioSCTTp);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////validacion email//////

                if(validateEmailAddress( txtEmailTp.getText().toString())){
                    //Toast.makeText(getApplicationContext(), "EMAIL VALIDO", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "EMAIL INVALIDO.", Toast.LENGTH_SHORT).show();}
                getExistRegistro();
            }
        });

        btnMenuTpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this,Reglamento.class);
                startActivity(i);
                finish();
            }
        });

        btnVistaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this, TarjetasConductor.class);
                startActivity(i);
                finish();
            }
        });

        btnInfraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExistRegistroLicencia();
            }
        });

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this, Contactos.class);
                startActivity(i);
                finish();
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePublico.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });

        ///////////// calendario para fechas en formulario///////////
        txtFechaExTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                año = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransportePublico.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaExTp.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, dia, mes, año);
                datePickerDialog.show();
            }
        });

        txtFechaVigTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia1 = c.get(Calendar.DAY_OF_MONTH);
                mes1 = c.get(Calendar.MONTH);
                año1 = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransportePublico.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaVigTp.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, dia1, mes1, año1);
                datePickerDialog.show();
            }
        });

    }


    /******************GET A LA BD***********************************/
    public void getExistRegistroLicencia() {
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
                        Intent i = new Intent(TransportePublico.this, Infraccion.class);
                        startActivity(i);
                        finish();
                    }else{
                        Looper.prepare(); // to be able to make toast
                        Toast.makeText(getApplicationContext(), "LO SENTIMOS, LOS DATOS DE LA LICENCIA SON NECESARIOS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TransportePublico.this, TarjetasConductor.class);
                        startActivity(i);
                        finish();
                        Looper.loop();
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Transporte?idExistente="+cargarInfoRandom)
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
                        insertRegistro();
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistro() {
        cargarDatos();
        NotasTp = txtNotasTp.getText().toString();
        EmailTp = txtEmailTp.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("Placa", Placa )
                .add("Economico", Economico)
                .add("EstatusActual", EstatusActual)
                .add("FechaExpedicion", FechaExTp)
                .add("FechaVigencia", FechaVigTp)
                .add("OficinaExoedidora", OficinaExpedTp)
                .add("Propietario", Propietario)
                .add("TenedorUsuario", TenedorUsuario)
                .add("Delegacion", DelegacionTp)
                .add("NumeroRepuve", NRepuveTp)
                .add("Marca", MarcaTp)
                .add("Linea",LineaTp)
                .add("Version", VersionTp)
                .add("ClaseTipo", ClaseTipoTp)
                .add("Color", ColorTp)
                .add("Modelo", ModeloTp)
                .add("Puertas", PuertasTp)
                .add("Cilindros", CilindrosTp)
                .add("Combustible", CombustibleTp)
                .add("Capacidad", CapacidadTp)
                .add("Agrupacion", AgrupacionTp)
                .add("NoSerie", NSerieTp)
                .add("RegistroPropiedad", RegistroPropTp)
                .add("RutaSitio", RutaSitioTp)
                .add("Permisionario", PermisionarioTp)
                .add("NoMotor", NMotorTp)
                .add("Uso", UsoTp)
                .add("Observaciones", ObservacionesTp)
                .add("FolioSCT", FolioSCTTp)
                .add("Email", EmailTp)
                .add("Notas", NotasTp)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Transporte/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();
                    TransportePublico.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
                            txtPlacaTp.setText("");
                            txtEconomico.setText("");
                            txtEstatusActual.setText("");
                            txtFechaExTp.setText("");
                            txtFechaVigTp.setText("");
                            txtPropietario.setText("");
                            txtTenedorUsuario.setText("");
                            txtOficinaExpedTp.setText("");
                            txtDelegacionTp.setText("");
                            txtNRepuveTp.setText("");
                            txtMarcaTp.setText("");
                            txtLineaTp.setText("");
                            txtVersionTp.setText("");
                            txtClaseTipoTp.setText("");
                            txtColorTp.setText("");
                            txtModeloTp.setText("");
                            txtPuertasTp.setText("");
                            txtCilindrosTp.setText("");
                            txtCombustibleTp.setText("");
                            txtCapacidadTp.setText("");
                            txtAgrupacionTp.setText("");
                            txtNSerieTp.setText("");
                            txtRegistroPropTp.setText("");
                            txtRutaSitioTp.setText("");
                            txtPermisionarioTp.setText("");
                            txtNMotorTp.setText("");
                            txtUsoTp.setText("");
                            txtObservacionesTp.setText("");
                            txtFolioSCTTp.setText("");
                            txtEmailTp.setText("");
                            txtNotasTp.setText("");
                        }
                    });
                }
            }
        });
    }

    private boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
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
