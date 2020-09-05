package mx.ssg.multas;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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

public class TransporteGeneral extends AppCompatActivity {

    ImageView btnVistaP,btnInfraccion,btnListTC;
    Button btnGuardar;
    EditText txtNoPlacaVG,txtSerieVG,txtMarcaVG,txtVersionVG,txtClaseVG,txtTipoVG,txtModeloVG,txtCombustibleVG,txtCilindrosVG,txtColorVG,txtUsoVG;
    EditText txtPuertasVG,txtNMotorVG,txtPropietarioVG,txtRFCVp,txtDireccionVG,txtColoniaVG,txtLocalidadVG,txtEstatusVG,txtEmailVG,txtObservacionesVG;

    String Placa = " ",SerieVG = " ",MarcaVG = " ",VersionVG = " ",ClaseVG = " ",TipoVG = " ",ModeloVG = " ",CombustibleVG = " ",CilindrosVG = " ",ColorVG = " ";
    String UsoVG = " ",PuertasVG = " ",NMotorVG = " ",PropietarioVG = " ";
    String DireccionVG = " ",ColoniaVG = " ",LocalidadVG = " ",EstatusVG = " ",EmailVG = " ",ObservacionesVG = " ";

    String email;

    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String codigoVerifi, cargarInfoRandom;

    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_general);
        cargarDatos();

        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        txtNoPlacaVG = findViewById(R.id.txtPlacaVG);
        /////posicion de cursor///////////
        txtNoPlacaVG.setFocusableInTouchMode(true); txtNoPlacaVG.requestFocus();

        txtNoPlacaVG = findViewById(R.id.txtPlacaVG);
        txtSerieVG = findViewById(R.id.txtNSerieVG);
        txtMarcaVG = findViewById(R.id.txtMarcaVG);
        txtVersionVG = findViewById(R.id.txtVersionVG);
        txtClaseVG = findViewById(R.id.txtClaseVG);
        txtTipoVG = findViewById(R.id.txtTipoVG);
        txtModeloVG = findViewById(R.id.txtModeloVG);
        txtCombustibleVG = findViewById(R.id.txtCombustibleVG);
        txtCilindrosVG = findViewById(R.id.txtCilindrosVG);
        txtColorVG = findViewById(R.id.txtColorVG);
        txtUsoVG = findViewById(R.id.txtUsoVG);
        txtPuertasVG = findViewById(R.id.txtPuertasVG);
        txtNMotorVG = findViewById(R.id.txtNMotorVG);
        txtPropietarioVG = findViewById(R.id.txtPropietarioVG);
        txtDireccionVG = findViewById(R.id.txtDireccionVG);
        txtColoniaVG = findViewById(R.id.txtColoniaVG);
        txtLocalidadVG = findViewById(R.id.txtLocalidadVG);
        txtEstatusVG = findViewById(R.id.txtEstatusVG);
        txtEmailVG = findViewById(R.id.txtEmailVG);
        txtObservacionesVG = findViewById(R.id.txtObservacionesVG);

        btnVistaP = findViewById(R.id.imgVistaPV);
        btnGuardar = findViewById(R.id.imgGuardarVehiculo);
        btnInfraccion = findViewById(R.id.imgTerminalV);
        btnListTC= findViewById(R.id.btnListTC);


        btnReglamento = findViewById(R.id.lyInicio4);
        btnLugaresPago = findViewById(R.id.lyCategoria4);
        btnContactos = findViewById(R.id.lyContacto4);
        btnTabulador = findViewById(R.id.lyFavoritos4);




        btnListTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this,Reglamento.class);
                startActivity(i);
            }
        });

        btnVistaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, TarjetasConductor.class);
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

                if(validateEmailAddress( txtEmailVG.getText().toString())){
                    Toast.makeText(getApplicationContext(), "EMAIL VALIDO", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "EMAIL INVALIDO.", Toast.LENGTH_SHORT).show();}
                getExistRegistro();
            }
        });


        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, Contactos.class);
                startActivity(i);
                finish();
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, Tabulador.class);
                startActivity(i);
                finish();
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



    /******************GET A LA BD***********************************/
    public void getExistRegistro() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/ConcentradoVehiculos?idExistente="+cargarInfoRandom)
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
        Placa = txtNoPlacaVG.getText().toString();
        SerieVG = txtSerieVG.getText().toString();
        MarcaVG = txtMarcaVG.getText().toString();
        VersionVG = txtVersionVG.getText().toString();
        ClaseVG = txtClaseVG.getText().toString();
        TipoVG = txtTipoVG.getText().toString();
        ModeloVG = txtModeloVG.getText().toString();
        CombustibleVG = txtCombustibleVG.getText().toString();
        CilindrosVG = txtCilindrosVG.getText().toString();
        ColorVG = txtColorVG.getText().toString();
        UsoVG = txtUsoVG.getText().toString();
        PuertasVG = txtPuertasVG.getText().toString();
        NMotorVG = txtNMotorVG.getText().toString();
        PropietarioVG = txtPropietarioVG.getText().toString();
        DireccionVG = txtDireccionVG.getText().toString();
        ColoniaVG = txtColoniaVG.getText().toString();
        LocalidadVG = txtLocalidadVG.getText().toString();
        EstatusVG = txtEstatusVG.getText().toString();
        ObservacionesVG = txtObservacionesVG.getText().toString();
        EmailVG = txtEmailVG.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("Placa", Placa )
                .add("NoSerie", SerieVG)
                .add("Marca", MarcaVG)
                .add("Version", VersionVG)
                .add("Clase", ClaseVG)
                .add("Tipo", TipoVG)
                .add("Modelo", ModeloVG)
                .add("Combustible", CombustibleVG)
                .add("Cilindros", CilindrosVG)
                .add("Color", ColorVG)
                .add("Uso", UsoVG)
                .add("Puertas", PuertasVG)
                .add("NoMotor", NMotorVG)
                .add("Propietario", PropietarioVG)
                .add("Direccion", DireccionVG)
                .add("Colonia", ColoniaVG)
                .add("Localidad", LocalidadVG)
                .add("Estatus", EstatusVG)
                .add("Observaciones", ObservacionesVG)
                .add("Email", EmailVG)
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/ConcentradoVehiculos")
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
                    TransporteGeneral.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
                            txtNoPlacaVG.setText("");
                            txtSerieVG.setText("");
                            txtMarcaVG.setText("");
                            txtVersionVG.setText("");
                            txtClaseVG.setText("");
                            txtTipoVG.setText("");
                            txtModeloVG.setText("");
                            txtCombustibleVG.setText("");
                            txtCilindrosVG.setText("");
                            txtColorVG.setText("");
                            txtUsoVG.setText("");
                            txtPuertasVG.setText("");
                            txtNMotorVG.setText("");
                            txtPropietarioVG.setText("");
                            txtDireccionVG.setText("");
                            txtColoniaVG.setText("");
                            txtLocalidadVG.setText("");
                            txtEstatusVG.setText("");
                            txtEmailVG.setText("");
                            txtObservacionesVG.setText("");
                        }
                    });
                }
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
                        Intent i = new Intent(TransporteGeneral.this, Infraccion.class);
                        startActivity(i);
                        finish();
                    }else{
                        Looper.prepare(); // to be able to make toast
                        Toast.makeText(getApplicationContext(), "LO SENTIMOS, LOS DATOS DE LA LICENCIA SON NECESARIOS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TransporteGeneral.this, TarjetasConductor.class);
                        startActivity(i);
                        finish();
                        Looper.loop();
                    }
                    Log.i("HERE", resp);
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
