package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssg.multas.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TarjetasConductor extends AppCompatActivity {
    ImageView btnList,btnBuscarSerie,btnBuscarPlaca,qrSerie,qrPlaca;
    EditText txtNoSerie,txtPlaca;
    LinearLayout btnReglamentoTC,btnLugaresPagoTC,btnContactoTC,btnTabuladorTC;
    String resultadoQrSerie,serie,resultadoQrPlaca,placa,resultadoQr,respuestaJson;
    String jObjResp,respJsonVehiculos;
    String nombre,apaterno,amaterno,Tipocalle,CalleLC,NumeroCalle,ColoniaLC,CP,MunicipioLC,EstadoLC,FechaExLC;
    String FechaVenLC,TipoVigLC,TipoLic,RFCLC,HomoLC,GrupoSanguiLC,RequeriemientosEspLC,EmailLC,ObservacionesLC;
    String Economico,EstatusActual,FechaExTp,FechaVigTp,Propietario,TenedorUsuario,OficinaExpedTp,DelegacionTp,NRepuveTp,MarcaTp,LineaTp,VersionTp,ClaseTipoTp,ColorTp,ModeloTp;
    String PuertasTp,CilindrosTp,CombustibleTp,CapacidadTp,AgrupacionTp,NSerieTp,RegistroPropTp,RutaSitioTp,PermisionarioTp,NMotorTp,UsoTp,ObservacionesTp;
    String FolioSCTTp,UrlTp,EmailTp,NotasTp;
    String SerieVp,DistribuidorVp,MarcaVp,VersionVp,ClaseVp,TipoVp,ModeloVp,CombustibleVp,CilindrosVp,ColorVp;
    String UsoVp,ProcedenciaVp,PuertasVp,NMotorVp,RepuveVp,FolioSCTVp,OficinaExpVp,PropietarioVp,RFCVp;
    String DireccionVp,ColoniaVp,LocalidadVp,UltimaRevalidacionVp,EstatusVp,TelefonoVp,FechaVp,UrlVp,EmailVp,ObservacionesVp;
    String Tag = "TarjetasConductor";
    int bandera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas_conductor);
        ListTabulador();

        txtNoSerie = findViewById(R.id.txtNoSerieCIV);
        txtPlaca = findViewById(R.id.txtPlacaCIV);
        btnBuscarSerie = findViewById(R.id.imgBuscarNoSerieCIV);
        btnBuscarPlaca = findViewById(R.id.imgBuscarPlacaCIV);
        qrSerie = findViewById(R.id.imgBuscarNoSerieXqrCIV);
        qrPlaca =findViewById(R.id.imgBuscarPlacaXqrCIV);


        btnList = findViewById(R.id.btnListTarjetas);
        btnReglamentoTC = findViewById(R.id.lyInicio);
        btnLugaresPagoTC = findViewById(R.id.lyCategoria);
        btnContactoTC = findViewById(R.id.lyContacto);
        btnTabuladorTC = findViewById(R.id.lyFavoritos);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Reglamento.class);
                startActivity(i);
                finish();
            }
        });

        btnBuscarSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNoSerie.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL No. DE SERIE ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    bandera = 1;
                    serie = txtNoSerie.getText().toString();
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS",Toast.LENGTH_SHORT).show();
                    getUsuaioLicencia();
                }
            }
        });

        btnBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPlaca.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL No. DE SERIE ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    bandera = 2;
                    placa = txtPlaca.getText().toString();
                    Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS",Toast.LENGTH_SHORT).show();
                    getUsuaioPlaca();
                }
            }
        });

        qrSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(TarjetasConductor.this).initiateScan();
            }
        });

        qrPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(TarjetasConductor.this).initiateScan();
            }
        });

        btnReglamentoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });

        btnLugaresPagoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, LugaresDePago.class);
                startActivity(i);
                finish();

            }
        });

        btnContactoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Contactos.class);
                startActivity(i);
                finish();

            }
        });

        btnTabuladorTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null)
            if(result.getContents() != null){
                    resultadoQrSerie = result.getContents();
                    Log.i(Tag, resultadoQrSerie);
                    String[] textElements = resultadoQrSerie.split(",");
                    serie = textElements[4];
                    Log.i(Tag, serie);
                    txtNoSerie.setText(serie);
            }
    }

    /********************************************************************************************************************/
    /******************GET A LA BD***********************************/
    public void getUsuaioLicencia() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaConducir?noLicencia="+serie)
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
                    TarjetasConductor.this.runOnUiThread(new Runnable() {
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
                                    jObjResp = " ";
                                    if(jObj.equals(jObjResp)){
                                        Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                    }else{
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

                                        Intent i = new Intent(TarjetasConductor.this,LicenciaConducir.class);
                                        i.putExtra("serie",serie);
                                        i.putExtra("apaterno",apaterno);
                                        i.putExtra("amaterno",amaterno);
                                        i.putExtra("nombre",nombre);
                                        i.putExtra("Tipocalle",Tipocalle);
                                        i.putExtra("CalleLC",CalleLC);
                                        i.putExtra("NumeroCalle",NumeroCalle);
                                        i.putExtra("ColoniaLC",ColoniaLC);
                                        i.putExtra("CP",CP);
                                        i.putExtra("MunicipioLC",MunicipioLC);
                                        i.putExtra("EstadoLC",EstadoLC);
                                        i.putExtra("FechaExLC",FechaExLC);
                                        i.putExtra("FechaVenLC",FechaVenLC);
                                        i.putExtra("TipoVigLC",TipoVigLC);
                                        i.putExtra("TipoLic",TipoLic);
                                        i.putExtra("RFCLC",RFCLC);
                                        i.putExtra("HomoLC",HomoLC);
                                        i.putExtra("GrupoSanguiLC",GrupoSanguiLC);
                                        i.putExtra("RequeriemientosEspLC",RequeriemientosEspLC);
                                        startActivity(i);
                                        finish();
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

    public void getUsuaioPlaca() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/ConcentradoVehiculos?noPlacaToken="+placa)
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
                    TarjetasConductor.this.runOnUiThread(new Runnable() {
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
                                    jObjResp = " ";
                                    if(jObj.equals(jObjResp)){
                                        Toast.makeText(getApplicationContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Economico = jObj.getString("economico");
                                        SerieVp = jObj.getString("serie");
                                        if(Economico != null){
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

                                            Intent i = new Intent(TarjetasConductor.this,TransportePublico.class);
                                            i.putExtra("EstatusActual",EstatusActual);
                                            i.putExtra("FechaExTp",FechaExTp);
                                            i.putExtra("FechaVigTp",FechaVigTp);
                                            i.putExtra("Propietario",Propietario);
                                            i.putExtra("TenedorUsuario",TenedorUsuario);
                                            i.putExtra("OficinaExpedTp",OficinaExpedTp);
                                            i.putExtra("DelegacionTp",DelegacionTp);
                                            i.putExtra("NRepuveTp",NRepuveTp);
                                            i.putExtra("MarcaTp",MarcaTp);
                                            i.putExtra("LineaTp",LineaTp);
                                            i.putExtra("VersionTp",VersionTp);
                                            i.putExtra("ClaseTipoTp",ClaseTipoTp);
                                            i.putExtra("ColorTp",ColorTp);
                                            i.putExtra("ModeloTp",ModeloTp);
                                            i.putExtra("PuertasTp",PuertasTp);
                                            i.putExtra("CilindrosTp",CilindrosTp);
                                            i.putExtra("CombustibleTp",CombustibleTp);
                                            i.putExtra("CapacidadTp",CapacidadTp);
                                            i.putExtra("AgrupacionTp",AgrupacionTp);
                                            i.putExtra("NSerieTp",NSerieTp);
                                            i.putExtra("RegistroPropTp",RegistroPropTp);
                                            i.putExtra("RutaSitioTp",RutaSitioTp);
                                            i.putExtra("PermisionarioTp",PermisionarioTp);
                                            i.putExtra("NMotorTp",NMotorTp);
                                            i.putExtra("UsoTp",UsoTp);
                                            i.putExtra("ObservacionesTp",ObservacionesTp);
                                            i.putExtra("FolioSCTTp",FolioSCTTp);
                                            startActivity(i);
                                            finish();
                                        }else if(SerieVp != null){
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

                                            Intent i = new Intent(TarjetasConductor.this,TransportePrivado.class);
                                            i.putExtra("DistribuidorVp",DistribuidorVp);
                                            i.putExtra("MarcaVp",MarcaVp);
                                            i.putExtra("VersionVp",VersionVp);
                                            i.putExtra("ClaseVp",ClaseVp);
                                            i.putExtra("TipoVp",TipoVp);
                                            i.putExtra("ModeloVp",ModeloVp);
                                            i.putExtra("CombustibleVp",CombustibleVp);
                                            i.putExtra("CilindrosVp",CilindrosVp);
                                            i.putExtra("ColorVp",ColorVp);
                                            i.putExtra("UsoVp",UsoVp);
                                            i.putExtra("ProcedenciaVp",ProcedenciaVp);
                                            i.putExtra("PuertasVp",PuertasVp);
                                            i.putExtra("NMotorVp",NMotorVp);
                                            i.putExtra("RepuveVp",RepuveVp);
                                            i.putExtra("FolioSCTVp",FolioSCTVp);
                                            i.putExtra("OficinaExpVp",OficinaExpVp);
                                            i.putExtra("PropietarioVp",PropietarioVp);
                                            i.putExtra("RFCVp",RFCVp);
                                            i.putExtra("DireccionVp",DireccionVp);
                                            i.putExtra("ColoniaVp",ColoniaVp);
                                            i.putExtra("LocalidadVp",LocalidadVp);
                                            i.putExtra("UltimaRevalidacionVp",UltimaRevalidacionVp);
                                            i.putExtra("EstatusVp",EstatusVp);
                                            i.putExtra("TelefonoVp",TelefonoVp);
                                            i.putExtra("FechaVp",FechaVp);
                                            startActivity(i);
                                        }else{
                                            Intent i = new Intent(TarjetasConductor.this,TransporteGeneral.class);
                                            startActivity(i);
                                        }
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

    private void ListTabulador() {
        DataHelper dataHelper = new DataHelper(this);
        ArrayList<String> list = dataHelper.getAllTabulador();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN");
        } else {
            dataHelper.insertTabulador(1, "POR FALTA DE LUZ BLANCA EN LA PLACA", "263", "1", 3);
            dataHelper.insertTabulador(2, "FALTA DE LUZ DE REVERSA", "263", "2", 3);
            dataHelper.insertTabulador(3, "POR ODOMETRO EN MAL ESTADO", "263", "3", 3);
            dataHelper.insertTabulador(4, "POR FALTA DE TIMBRE PARA QUE EL PASAJE ANUNCIE EL DESCENSO", "263", "4", 3);
            dataHelper.insertTabulador(5, "POR FALTA DE ASEO EN EL INTERIOR Y EXTERIOR DE LA UNIDAD", "263", "5", 3);
            dataHelper.insertTabulador(6, "POR FALTA DE ASEO DEL OPERADOR", "263", "6", 3);
            dataHelper.insertTabulador(7, "POR NO PORTAR EL UNIFORME AUTORIZADO ", "263", "7", 3);
            dataHelper.insertTabulador(8, "POR NO DESINFECTAR LAS UNIDADES", "263", "8", 3);
            dataHelper.insertTabulador(9, "POR ESTAR LA PORTEZUELA EN MAL ESTADO", "263", "9", 3);
            dataHelper.insertTabulador(10, "POR NO PORTAR EN LUGAR VISIBLE LAS TARIFAS", "263", "10", 3);
            dataHelper.insertTabulador(11, "POR FALTA DE ESPEJOS RETROVISORES INTERIORES Y LATERALES ", "264", "1", 8);
            dataHelper.insertTabulador(12, "POR DESCORTESIA CON EL PUBLICO USUARIO", "264", "2", 8);
            dataHelper.insertTabulador(13, "POR PERMITIR QUE VIAJEN PERSONAS EN ESTADO DE EBRIEDAD O BAJO EL INFLUJO DE ESTUPEFACIENTES", "264", "3", 8);
            dataHelper.insertTabulador(14, "POR LLEVAR ADORNOS QUE IMPIDAN PARCIALMENTE LA VISIBILIDAD DEL CONDUCTOR", "264", "4", 8);
            dataHelper.insertTabulador(15, "POR FALTA DE LUCES EN EL TABLERO", "264", "5", 8);
            dataHelper.insertTabulador(16, "POR FALTA DE REFLEJANTES POSTERIORES Y LATERALES", "264", "6", 8);
            dataHelper.insertTabulador(17, "POR FALTA DE LUCES INTERMITENTES DELANTERAS Y TRASERAS", "264", "7", 8);
            dataHelper.insertTabulador(18, "POR FALTA DE LUCES INVERTIDAS", "264", "8", 8);
            dataHelper.insertTabulador(19, "POR CIRCULAR CON LUCES ALINEADAS EN FORMA INCORRECTA", "264", "9", 8);
            dataHelper.insertTabulador(20, "POR FALTA DE LUCES DELANTERAS Y TRASERAS", "265", "1", 15);
            dataHelper.insertTabulador(21, "POR FALTA DE LUZ DE FRENO", "265", "2", 15);
            dataHelper.insertTabulador(22, "POR FALTA DE LUZ EN DIRECCIONALES ", "265 ", "3", 15);
            dataHelper.insertTabulador(23, "POR FALTA DE LUCES DEMARCADORAS FRONTALES Y POSTERIORES", "265", "4", 15);
            dataHelper.insertTabulador(24, "POR FALTA DE LUZ INTERIOR", "265", "5", 15);
            dataHelper.insertTabulador(25, "POR FRENOS EN MAL ESTADO EN REMOLQUES", "265", "6", 15);
            dataHelper.insertTabulador(26, "POR CONTAR CON SILENCIADOR EN MAL ESTADO", "265", "7", 15);
            dataHelper.insertTabulador(27, "TRAER EL SILENCIADOR MODIFICADO ", "265", "8", 15);
            dataHelper.insertTabulador(28, "NO PONER EN FUNCIONAMIENTO DISPOSITIVOS", "265", "9", 15);
            dataHelper.insertTabulador(29, "POR NO RESPETAR EL REGLAMENTO DE TRANSITO Y CONTROL VEHICULAR", "266", "1", 25);
            dataHelper.insertTabulador(30, "POR FALTA DE TARJETA DE CIRCULACION", "266", "2", 25);
            dataHelper.insertTabulador(31, "POR HACER REPARACIONES MECANICAS CON PASAJEEROS A BORDO DE LA UNIDAD", "266", "3", 25);
            dataHelper.insertTabulador(32, "POR MODIFICAR LA CARROCERIA ", "266", "4", 25);
            dataHelper.insertTabulador(33, "POR FALTA DE FRENO DE ESTACIONAMIENTO", "266", "5", 25);
            dataHelper.insertTabulador(34, "POR CIRCULAR CON CARGA QUE NO PERMITA LA VISIBILIDAD DE LAS PLACAS", "266", "6", 25);
            dataHelper.insertTabulador(35, "POR ALTERACION DE HORARIOS", "266", "7", 25);
            dataHelper.insertTabulador(36, " POR ABASTECER COMBUSTIBLE CON PASAJE A BORDO", "266", "8", 25);
            dataHelper.insertTabulador(37, "POR FALTA DE LUCES EN CARGA SOBRESALIENTE", "266", "9", 25);
            dataHelper.insertTabulador(38, "POR CIRCULAR CON FRENOS EN MAL ESTADO", " 266", "10", 25);
            dataHelper.insertTabulador(39, "POR CIRCULAR CON EXCESO DE CARGA FUERA DEL LIMITE ESTIPULADO POR LA NORMATIVIDAD VIGENTE", " 266", "11", 25);
            dataHelper.insertTabulador(40, "POR CIRCULAR SIN PLACAS ", "267", "1", 5);
            dataHelper.insertTabulador(41, "SUBIR O BAJAR PASAJES EN LUGARES QUE NO OFRECEN SEGURIDAD", "267", "2", 5);
            dataHelper.insertTabulador(42, "POR CIRCULAR CON MAS PASAJE DE LO ESTIPULADO EN LA TARJETA DE CIRCULACION", "267", "3", 5);
            dataHelper.insertTabulador(43, "POR CIRCULAR CON LAS PUERTAS ABIERTAS", "267", "4", 5);
            dataHelper.insertTabulador(44, "POR IMPEDIR DE CUALQUIER MODO EL ACCESO A LAS UNIDADES DE TRANSPORTE PUBLICO O NEGARL EL TRASLADO A PERSONAS CON DISCAPACIDAD, INCLUYENDO LAS QUE SE DESPLACEN CON LA AHYUDA DE APARATOS ORTOPEDICOS O DE APOYO, ASI COMO PERROS GUIA, SIEMPRE Y CUANDO EL CANINO CUENTE CON SU IDENTIFICACION VISIBLE, CON LOS ELEMENTOS DE SUJECION CORRESPONDIENTE Y CON SUS RESPECTIVAS MEDIDAS DE SANIDAD Y DE HIGIENE BASICAS", "267", "5", 5);
            dataHelper.insertTabulador(45, "A QUIEN MANEJE SIN LICENCIA", "268", "1", 12);
            dataHelper.insertTabulador(46, "POR CIRCULAR EN CARRILES NO CORRESPONDIENTES", "268", "2", 12);
            dataHelper.insertTabulador(47, "POR REALIZAR COBROS ADICIONALES A LOS USUARIOS EN MENCION", "268", "3", 12);
            dataHelper.insertTabulador(48, "POR CARGAR GASOLINA Y/O REALIZAR REPARACIONES MECANICAS CON PASAJE A BORDO DE LA UNIDAD NO TRATANDOSE DE EMERGENCIA, EN ESTE ULTIMO  CASO EN LA VIA PUBLICA", "268", "4", 12);
            dataHelper.insertTabulador(49, "POR SUBIR Y BAJAR PASAJE EN LUGARES QUE NO OFRECEN SEGURIDAD", "268", "5", 15);
            dataHelper.insertTabulador(50, "POR ESTACIONARSE EN LUGARES PROHIBIDOS", "268", "6", 12);
            dataHelper.insertTabulador(51, "POR CIRCULAR CON FFRENOS EN MAL ESTADO", " 268", "7", 12);
            dataHelper.insertTabulador(52, "AL QUE INSTALE SITIOS, LANZADERAS O TERMINALES SIN EL PERMISO O LA AUTORIZACION CORRESPONDIENTE", "268", "8", 12);
            dataHelper.insertTabulador(53, "ALTERACION DE CUALQUIERA DE LAS TARIFAS VIGENTES Y LA OMISION DE ENTREGAR EL BOLETO DE VIAJE CORRESPONDIENTE", "268", "9", 125);
            dataHelper.insertTabulador(54, "POR PRESTAR EL SERVICIO PUBLICO DE TRANSPORTE CON PLACAS Y REGISTRO DE OTROS ESTADOS DE LA REPUBLICA O EXTRANJERAS ", " 269", "1", 75);
            dataHelper.insertTabulador(55, "POR DUPLICAR UNIDADES DEL SERVICIO DE TRANSPORTE PUBLICO, ENTENDIENDOSE POR ELLO QUIEN UTILICE UN NUMERO ECONOMICO YA EXISTENTE Y REGISTRADO A NOMBRE DE UN PERMISIONARIO. O BIEN LA DULPLICIDAD DE UN NUMERO ECONOMICO DE UN MISMO CONCESIONARIO", " 269", "2", 450);
            dataHelper.insertTabulador(56, "POR PRESTAR SERVICIO PUBLICO SIN PERMISO O CONCESION CORRESPONDIENTE; O A QUIEN PRESTE EL SERVICIO UTILIZANDO COLORES Y TRAZOS DISTINTOS DEL TRANSPORTE PUBLICO AMPARANDOSE EN DOCUMENTOS QUE NO HAYAN SIDO EXPEDIDOS POR LA AUTORIDAD COMPETENTE; O A QUIEN PRESTE EL SERVICIO PUBLICO EN UNA MODALIDAD DIFERENTE A LA AUTORIZADA", "269", "3", 600);
            dataHelper.insertTabulador(57, "POR TRANSPORTAR MATERIALES INFLAMABLES, EXPLOSIVOS O PELIGROSOS, COMPRENDIDOS DENTRO DE ESTOS ULTIMOS, DE MANERA NO LIMITATIVA, LOS CORROSIVOS, REACTIVOS, TOXICOS, INFECCIOSOS O BIOLOGICOS INFECCIOSOS, SIN LA AUTORIZACION DE LA AUTORIDAD COMPETENTE", "269", "4", 75);
            dataHelper.insertTabulador(58, "POR PRESTAR EL SERVICIO PUBLICO DE TRANSPORTE UNIDADES CON CAPACIDAD MAYOR A LO ESTABLECIDO PARA LA MODALIDAD AUTORIZADA; O MODIFICAR LA CAPACIDAD DE ORIGEN DEL VEHICULO CON LA FINALIDAD DE ADMITIR UN MAYOR NUMERO DE PASAJEROS", "269", "5", 75);
            dataHelper.insertTabulador(59, "POR CONDUCIR BAJO LOS INFLUJOS DEL ALCOHOL O CUALQUIER SUSTANCIA TOXICA O ESTUPEFACIENTE, PERDIENDO EN EL ULTTIMO CASO EL DERECHO DE CONDUCIR VEHICULOS DESTINADOS AL TRANSPORTE PUBLICO", "269", "6", 300);
            dataHelper.insertTabulador(60, "AL OPERADOR DE VEHICULO DE TRANSPORTE PUBLICO DE PASAJEROS QUE TRASMITA O REPRODUZCA MATERIAL DISCOGRAFICO QUE PROMUEVA LA CULTURA DE VIOLENCIA O HAGA APOLOGIA AL DELITO", "271", "1", 10);
            dataHelper.insertTabulador(61, "A QUIEN INFRINJA LAS DISPOSICIONES DE HORARIO ESTABLECIDO EN ESTE REGLAMENTO O EN EL PERMISO ESPECIAL PARA LA PRESTACION DEL SERVICIO", "273", "1", 37);
            dataHelper.insertTabulador(62, "A QUIEN INFRINJA LAS DISPOSICIONES SOBRE EL ITINERARIO O RECORRIDO ESTABLECIDO EN ELPERMISO ESPECIAL PARA LA PRESTACION DEL SERVICIO", "273", "2", 37);
            dataHelper.insertTabulador(63, "A QUIEN PRESTE EL SERVICIO DE TRANSPORTE DE CARGA SIN LA ESCOLTA O LA UNIDAD DE APOYO REQUERIDA Y ESTABLECIDA EN EL PERMISO ESPECIAL PARA LA PRESTACION DEL SERVICIO", "273", "3", 40);
            dataHelper.insertTabulador(64, "A LOS VEHICULOS DE CARGA QUE EFEXCTUEN TRASBORDO DE MERCANCIA EN LA VIA PUBLICA", "273", "4", 55);
            dataHelper.insertTabulador(65, "A QUIEN PRESTE EL SERVICIO DE TRNSPORTE DE CARGA SIN EL PERMISO PARA LA PRESTACION DEL SERVICIO, DEBIENDOSE ORDENAR LA DETENCION DEL VEHICULO ", "273", "5", 80);

        }
    }
}
