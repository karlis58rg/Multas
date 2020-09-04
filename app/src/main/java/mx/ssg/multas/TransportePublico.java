package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransportePublico extends AppCompatActivity {
    EditText txtPlacaTp,txtEconomico,txtEstatusActual,txtFechaExTp,txtFechaVigTp,txtPropietario,txtTenedorUsuario,txtOficinaExpedTp,txtDelegacionTp,txtNRepuveTp,txtMarcaTp,txtLineaTp,txtVersionTp;
    EditText txtClaseTipoTp,txtColorTp,txtModeloTp,txtPuertasTp,txtCilindrosTp,txtCombustibleTp,txtCapacidadTp,txtAgrupacionTp,txtNSerieTp,txtRegistroPropTp;
    EditText txtRutaSitioTp,txtPermisionarioTp,txtNMotorTp,txtUsoTp,txtObservacionesTp,txtFolioSCTTp,txtUrlTp,txtEmailTp,txtNotasTp;

    String Placa,Economico,EstatusActual,FechaExTp,FechaVigTp,Propietario,TenedorUsuario,OficinaExpedTp,DelegacionTp,NRepuveTp,MarcaTp,LineaTp,VersionTp,ClaseTipoTp,ColorTp,ModeloTp;
    String PuertasTp,CilindrosTp,CombustibleTp,CapacidadTp,AgrupacionTp,NSerieTp,RegistroPropTp,RutaSitioTp,PermisionarioTp,NMotorTp,UsoTp,ObservacionesTp;
    String FolioSCTTp,UrlTp,EmailTp,NotasTp,respuestaJson;
    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;
    private int dia,mes,año,dia1,mes1,año1;
    ImageView btnBuscarPlaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_publico);

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

        btnReglamento = findViewById(R.id.lyInicio2);
        btnLugaresPago = findViewById(R.id.lyCategoria2);
        btnContactos = findViewById(R.id.lyContacto2);
        btnTabulador = findViewById(R.id.lyFavoritos2);

        btnBuscarPlaca = findViewById(R.id.imgBuscarNoPlacaTP);


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

        btnBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPlacaTp.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR ALGÚN COMENTARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getPlacaTPBJ();
                }
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

    /******************GET A BAJA CALIFORNIA***********************************/
    public void getPlacaTPBJ() {
        Placa = txtPlacaTp.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TransportePublico?noPlacaToken="+Placa)
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
                    TransportePublico.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                String valorNull = " ";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else {
                                    JSONObject jObj = null;
                                    String resObj = myResponse;
                                    resObj = resObj.replace("[", " ");
                                    resObj = resObj.replace("]", " ");

                                    jObj = new JSONObject("" + resObj + "");
                                    if (jObj.equals(valorNull)) {
                                        Toast.makeText(getApplicationContext(), "ESTA PLACA NO CONTIENE INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Economico = jObj.getString("economico");
                                        EstatusActual = jObj.getString("estatusActual");
                                        FechaExTp = jObj.getString("fechaExpedicion");
                                        FechaVigTp = jObj.getString("fechaVigencia");
                                        Propietario = jObj.getString("propietario");
                                        TenedorUsuario = jObj.getString("tenedorUsuario");
                                        OficinaExpedTp = jObj.getString("oficinaExpedidora");
                                        DelegacionTp = jObj.getString("delegacion");
                                        NRepuveTp = jObj.getString("numeroRepuve");
                                        MarcaTp = jObj.getString("marca");
                                        LineaTp = jObj.getString("linea");
                                        VersionTp = jObj.getString("version");
                                        ClaseTipoTp = jObj.getString("claseTipo");
                                        ColorTp = jObj.getString("color");
                                        ModeloTp = jObj.getString("modelo");
                                        PuertasTp = jObj.getString("puertas");
                                        CilindrosTp = jObj.getString("cilindros");
                                        CombustibleTp = jObj.getString("combustible");
                                        CapacidadTp = jObj.getString("capacidad");
                                        AgrupacionTp = jObj.getString("agrupacion");
                                        NSerieTp = jObj.getString("serie");
                                        RegistroPropTp = jObj.getString("registroPropiedad");
                                        RutaSitioTp = jObj.getString("rutaSitio");
                                        PermisionarioTp = jObj.getString("permisionario");
                                        NMotorTp = jObj.getString("numMotor");
                                        UsoTp = jObj.getString("uso");
                                        ObservacionesTp = jObj.getString("observaciones");
                                        FolioSCTTp = jObj.getString("folioSCT");

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

                                        Log.i("HERE", "" + jObj);
                                    }
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
}
