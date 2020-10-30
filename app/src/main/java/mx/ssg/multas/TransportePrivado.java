package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class TransportePrivado extends AppCompatActivity {
    Button btnGuardar;

    ImageView btnList,btnVistaPrincipal,btnInfraccion;
    TextView txtNoPlacaTC;
    EditText txtSerieVp,txtDistribuidorVp,txtMarcaVp,txtVersionVp,txtClaseVp,txtTipoVp,txtModeloVp,txtCombustibleVp,txtCilindrosVp,txtColorVp;
    EditText txtUsoVp,txtProcedenciaVp,txtPuertasVp,txtNMotorVp,txtRepuveVp,txtFolioSCTVp,txtOficinaExpVp,txtPropietarioVp,txtRFCVp,txtDireccionVp;
    EditText txtColoniaVp,txtLocalidadVp,txtUltimaRevalidacionVp,txtEstatusVp,txtTelefonoVp,txtFechaVp,txtUrlVp,txtEmailVp,txtObservacionesVp;
    String noPlacaTC,respuestaJson;
    String SerieVp,DistribuidorVp,MarcaVp,VersionVp,ClaseVp,TipoVp,ModeloVp,CombustibleVp,CilindrosVp,ColorVp;
    String UsoVp,ProcedenciaVp,PuertasVp,NMotorVp,RepuveVp,FolioSCTVp,OficinaExpVp,PropietarioVp,RFCVp;
    String DireccionVp,ColoniaVp,LocalidadVp,UltimaRevalidacionVp,EstatusVp,TelefonoVp,FechaVp,EmailVp,ObservacionesVp;
    String email;

    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String cargarFolioInfra;
    private int dia,mes,año;

    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_privado);
        cargarFolio();

        txtNoPlacaTC = findViewById(R.id.txtPlacaVp);
        /////posicion de cursor///////////
        txtNoPlacaTC.setFocusableInTouchMode(true); txtNoPlacaTC.requestFocus();


        btnList = findViewById(R.id.btnListVP);
        txtNoPlacaTC = findViewById(R.id.txtPlacaVp);
        txtSerieVp = findViewById(R.id.txtSerieVp);
        txtDistribuidorVp = findViewById(R.id.txtDistribuidorVp);
        txtMarcaVp = findViewById(R.id.txtMarcaVp);
        txtVersionVp = findViewById(R.id.txtVersionVp);
        txtClaseVp = findViewById(R.id.txtClaseVp);
        txtTipoVp = findViewById(R.id.txtTipoVp);
        txtModeloVp = findViewById(R.id.txtModeloVp);
        txtCombustibleVp = findViewById(R.id.txtCombustibleVp);
        txtCilindrosVp = findViewById(R.id.txtCilindrosVp);
        txtColorVp = findViewById(R.id.txtColorVp);
        txtUsoVp = findViewById(R.id.txtUsoVp);
        txtProcedenciaVp = findViewById(R.id.txtProcedenciaVp);
        txtPuertasVp = findViewById(R.id.txtPuertasVp);
        txtNMotorVp = findViewById(R.id.txtNMotorVp);
        txtRepuveVp = findViewById(R.id.txtRepuveVp);
        txtFolioSCTVp = findViewById(R.id.txtFolioSCTVp);
        txtOficinaExpVp = findViewById(R.id.txtOficinaExpVp);
        txtPropietarioVp = findViewById(R.id.txtPropietarioVp);
        txtRFCVp = findViewById(R.id.txtRFCVp);
        txtDireccionVp = findViewById(R.id.txtDireccionVp);
        txtColoniaVp = findViewById(R.id.txtColoniaVp);
        txtLocalidadVp = findViewById(R.id.txtLocalidadVp);
        txtUltimaRevalidacionVp = findViewById(R.id.txtUltimaRevalidacionVp);
        txtEstatusVp = findViewById(R.id.txtEstatusVp);
        txtTelefonoVp = findViewById(R.id.txtTelefonoVp);
        txtFechaVp = findViewById(R.id.txtFechaVp);
        txtUrlVp = findViewById(R.id.txtUrlVp);
        txtEmailVp = findViewById(R.id.txtEmailVp);
        txtObservacionesVp = findViewById(R.id.txtObservacionesVp);

        btnGuardar = findViewById(R.id.imgGuardarVehiculoParticular);
        btnVistaPrincipal = findViewById(R.id.imgVistaPVP);
        btnInfraccion = findViewById(R.id.imgTerminalVP);

        btnReglamento = findViewById(R.id.lyInicio3);
        btnLugaresPago = findViewById(R.id.lyCategoria3);
        btnContactos = findViewById(R.id.lyContacto3);
        btnTabulador = findViewById(R.id.lyFavoritos3);

        Intent i = getIntent();
        noPlacaTC = i.getStringExtra("Placa");
        SerieVp = i.getStringExtra("SerieVp");
        DistribuidorVp = i.getStringExtra("DistribuidorVp");
        MarcaVp = i.getStringExtra("MarcaVp");
        VersionVp = i.getStringExtra("VersionVp");
        ClaseVp = i.getStringExtra("ClaseVp");
        TipoVp = i.getStringExtra("TipoVp");
        ModeloVp = i.getStringExtra("ModeloVp");
        CombustibleVp = i.getStringExtra("CombustibleVp");
        CilindrosVp = i.getStringExtra("CilindrosVp");
        ColorVp = i.getStringExtra("ColorVp");
        UsoVp = i.getStringExtra("UsoVp");
        ProcedenciaVp = i.getStringExtra("ProcedenciaVp");
        PuertasVp = i.getStringExtra("PuertasVp");
        NMotorVp = i.getStringExtra("NMotorVp");
        RepuveVp = i.getStringExtra("RepuveVp");
        FolioSCTVp = i.getStringExtra("FolioSCTVp");
        OficinaExpVp = i.getStringExtra("OficinaExpVp");
        PropietarioVp = i.getStringExtra("PropietarioVp");
        RFCVp = i.getStringExtra("RFCVp");
        DireccionVp = i.getStringExtra("DireccionVp");
        ColoniaVp = i.getStringExtra("ColoniaVp");
        LocalidadVp = i.getStringExtra("LocalidadVp");
        UltimaRevalidacionVp = i.getStringExtra("UltimaRevalidacionVp");
        EstatusVp = i.getStringExtra("EstatusVp");
        TelefonoVp = i.getStringExtra("TelefonoVp");
        FechaVp = i.getStringExtra("FechaVp");

        txtNoPlacaTC.setText(noPlacaTC);
        txtSerieVp.setText(SerieVp);
        txtDistribuidorVp.setText(DistribuidorVp);
        txtMarcaVp.setText(MarcaVp);
        txtVersionVp.setText(VersionVp);
        txtClaseVp.setText(ClaseVp);
        txtTipoVp.setText(TipoVp);
        txtModeloVp.setText(ModeloVp);
        txtCombustibleVp.setText(CombustibleVp);
        txtCilindrosVp.setText(CilindrosVp);
        txtColorVp.setText(ColorVp);
        txtUsoVp.setText(UsoVp);
        txtProcedenciaVp.setText(ProcedenciaVp);
        txtPuertasVp.setText(PuertasVp);
        txtNMotorVp.setText(NMotorVp);
        txtRepuveVp.setText(RepuveVp);
        txtFolioSCTVp.setText(FolioSCTVp);
        txtOficinaExpVp.setText(OficinaExpVp);
        txtPropietarioVp.setText(PropietarioVp);
        txtRFCVp.setText(RFCVp);
        txtDireccionVp.setText(DireccionVp);
        txtColoniaVp.setText(ColoniaVp);
        txtLocalidadVp.setText(LocalidadVp);
        txtUltimaRevalidacionVp.setText(UltimaRevalidacionVp);
        txtEstatusVp.setText(EstatusVp);
        txtTelefonoVp.setText(TelefonoVp);
        txtFechaVp.setText(FechaVp);

        btnVistaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this, TarjetasConductor.class);
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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///////validacion email//////

                if(validateEmailAddress( txtEmailVp.getText().toString())){
                    //Toast.makeText(getApplicationContext(), "EMAIL VALIDO", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(), "EMAIL INVALIDO.", Toast.LENGTH_SHORT).show();
                }
                getExistRegistro();
            }
        });

        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this, Contactos.class);
                startActivity(i);
                finish();
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportePrivado.this,Reglamento.class);
                startActivity(i);
                finish();
            }
        });
        ///////////// calendario para fechas en formulario///////////
        txtFechaVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                año = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransportePrivado.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaVp.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, dia, mes, año);
                datePickerDialog.show();
            }
        });




    }

    /////////validar email////////
    private boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Vehiculo?idExistente="+cargarFolioInfra)
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
        ObservacionesVp = txtObservacionesVp.getText().toString();
        EmailVp = txtEmailVp.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarFolioInfra)
                .add("Placa", noPlacaTC )
                .add("NoSerie", SerieVp)
                .add("Distribuidor", DistribuidorVp)
                .add("Marca", MarcaVp)
                .add("Version", VersionVp)
                .add("Clase", ClaseVp)
                .add("Tipo", TipoVp)
                .add("Combustible", CombustibleVp)
                .add("Cilindros", CilindrosVp)
                .add("Color", ColorVp)
                .add("Uso", UsoVp)
                .add("Procedencia",ProcedenciaVp)
                .add("Puertas", PuertasVp)
                .add("NoMotor", NMotorVp)
                .add("Repuve", RepuveVp)
                .add("FolioSCT", FolioSCTVp)
                .add("OficinaExoedidora", OficinaExpVp)
                .add("Propietario", PropietarioVp)
                .add("RFC", RFCVp)
                .add("Direccion", DireccionVp)
                .add("Colonia", ColoniaVp)
                .add("Localidad", LocalidadVp)
                .add("UltimaRevalidacion", UltimaRevalidacionVp)
                .add("Estatus", EstatusVp)
                .add("Telefono", TelefonoVp)
                .add("FechaVencimiento", FechaVp)
                .add("Uso", UsoVp)
                .add("Observaciones", ObservacionesVp)
                .add("Email", EmailVp)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Vehiculo/")
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
                    TransportePrivado.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
                            txtNoPlacaTC.setText("");
                            txtSerieVp.setText("");
                            txtDistribuidorVp.setText("");
                            txtMarcaVp.setText("");
                            txtVersionVp.setText("");
                            txtClaseVp.setText("");
                            txtTipoVp.setText("");
                            txtModeloVp.setText("");
                            txtCombustibleVp.setText("");
                            txtCilindrosVp.setText("");
                            txtColorVp.setText("");
                            txtUsoVp.setText("");
                            txtProcedenciaVp.setText("");
                            txtPuertasVp.setText("");
                            txtNMotorVp.setText("");
                            txtRepuveVp.setText("");
                            txtFolioSCTVp.setText("");
                            txtOficinaExpVp.setText("");
                            txtPropietarioVp.setText("");
                            txtRFCVp.setText("");
                            txtDireccionVp.setText("");
                            txtColoniaVp.setText("");
                            txtLocalidadVp.setText("");
                            txtUltimaRevalidacionVp.setText("");
                            txtEstatusVp.setText("");
                            txtTelefonoVp.setText("");
                            txtFechaVp.setText("");
                            txtEmailVp.setText("");
                            txtObservacionesVp.setText("");
                        }
                    });
                }
            }
        });
    }
    /******************GET A LA BD***********************************/
    public void getExistRegistroLicencia() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Licencia?idExistente="+cargarFolioInfra)
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
                        Intent i = new Intent(TransportePrivado.this, Infraccion.class);
                        startActivity(i);
                        finish();
                    }else{
                        Looper.prepare(); // to be able to make toast
                        Toast.makeText(getApplicationContext(), "LO SENTIMOS, LOS DATOS DE LA LICENCIA SON NECESARIOS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TransportePrivado.this, TarjetasConductor.class);
                        startActivity(i);
                        finish();
                        Looper.loop();
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    public void cargarFolio(){
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarFolioInfra = share.getString("FOLIOINFRACCION", "");
    }

}
