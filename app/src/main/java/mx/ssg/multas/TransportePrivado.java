package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransportePrivado extends AppCompatActivity {
    ImageView btnList,btnBuscarPlaca;
    TextView txtNoPlacaTC;
    EditText txtSerieVp,txtDistribuidorVp,txtMarcaVp,txtVersionVp,txtClaseVp,txtTipoVp,txtModeloVp,txtCombustibleVp,txtCilindrosVp,txtColorVp;
    EditText txtUsoVp,txtProcedenciaVp,txtPuertasVp,txtNMotorVp,txtRepuveVp,txtFolioSCTVp,txtOficinaExpVp,txtPropietarioVp,txtRFCVp,txtDireccionVp;
    EditText txtColoniaVp,txtLocalidadVp,txtUltimaRevalidacionVp,txtEstatusVp,txtTelefonoVp,txtFechaVp,txtUrlVp,txtEmailVp,txtObservacionesVp;
    String noPlacaTC,respuestaJson;
    String SerieVp,DistribuidorVp,MarcaVp,VersionVp,ClaseVp,TipoVp,ModeloVp,CombustibleVp,CilindrosVp,ColorVp;
    String UsoVp,ProcedenciaVp,PuertasVp,NMotorVp,RepuveVp,FolioSCTVp,OficinaExpVp,PropietarioVp,RFCVp;
    String DireccionVp,ColoniaVp,LocalidadVp,UltimaRevalidacionVp,EstatusVp,TelefonoVp,FechaVp,UrlVp,EmailVp,ObservacionesVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_privado);

        btnList = findViewById(R.id.btnListVP);
        txtNoPlacaTC = findViewById(R.id.txtPlacaVp);
        btnBuscarPlaca = findViewById(R.id.imgBuscarNoPlacaVp);

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

        btnBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNoPlacaTC.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR ALGÚN COMENTARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getPlacaParticularBJ();
                }
            }
        });
    }

    /******************GET A BAJA CALIFORNIA***********************************/
    public void getPlacaParticularBJ() {
        noPlacaTC = txtNoPlacaTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/VehiculoParticular?noPlacaToken="+noPlacaTC)
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
                    TransportePrivado.this.runOnUiThread(new Runnable() {
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
                                    SerieVp = jObj.getString("serie");
                                    DistribuidorVp = jObj.getString("distribuidor");
                                    MarcaVp = jObj.getString("marca");
                                    VersionVp = jObj.getString("version");
                                    ClaseVp = jObj.getString("clase");
                                    TipoVp = jObj.getString("tipo");
                                    ModeloVp = jObj.getString("modelo");
                                    CombustibleVp = jObj.getString("combustible");
                                    CilindrosVp = jObj.getString("cilindros");
                                    ColorVp = jObj.getString("color");
                                    UsoVp = jObj.getString("uso");
                                    ProcedenciaVp = jObj.getString("procedencia");
                                    PuertasVp = jObj.getString("puertas");
                                    NMotorVp = jObj.getString("numMotor");
                                    RepuveVp = jObj.getString("repuve");
                                    FolioSCTVp = jObj.getString("folioSct");
                                    OficinaExpVp = jObj.getString("oficinaExpedidora");
                                    PropietarioVp = jObj.getString("propietario");
                                    RFCVp = jObj.getString("rfc");
                                    DireccionVp = jObj.getString("direccion");
                                    ColoniaVp = jObj.getString("colonia");
                                    LocalidadVp = jObj.getString("localidad");
                                    UltimaRevalidacionVp = jObj.getString("ultimaRevalidacion");
                                    EstatusVp = jObj.getString("estatus");
                                    TelefonoVp = jObj.getString("telefono");
                                    FechaVp = jObj.getString("fechaVencimiento");

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

}
