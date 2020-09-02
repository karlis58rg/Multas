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
            dataHelper.insertTabulador(1, "MANEJAR SIN LICENCIA", "51 / 76", "", 15);
            dataHelper.insertTabulador(2, "OBSTRUIR EL PASO AL PEATÓN", "94", "I", 3);
            dataHelper.insertTabulador(3, "MANEJAR EN ESTADO DE EBRIEDAD", "78", "I", 75);
            dataHelper.insertTabulador(4, "EXCESO DE VELOCIDAD", "166", "X", 20);
            dataHelper.insertTabulador(5, "IR A MAS DE 15 KMS. FRENTE A ESCUELA", "14", "I", 3);
            dataHelper.insertTabulador(6, "NEGARSE A PRESTAR LIC. Y TARJ. DE CIRCULACION", "67", "", 3);
            dataHelper.insertTabulador(7, "NO OBEDECER LAS INDICACIONES DE AGENTE ", "77", "IV", 3);
            dataHelper.insertTabulador(8, "RECURRIR A LA FUGA", "111", "VI", 20);
            dataHelper.insertTabulador(9, "FALTAS AL AGENTE", "62", "", 5);
            dataHelper.insertTabulador(10, "PRESTAR SERVS. PUB. SIN AUT Y/O CONCESION", "130", "I", 55);
            dataHelper.insertTabulador(11, "CIRCULAR FUERA DE RUTA ", "139 / 166", "139-XXI   166-III", 5);
            dataHelper.insertTabulador(12, "ESTACIONARSE MAL", "94", "", 3);
            dataHelper.insertTabulador(13, "ESTACIONARSE EN PARADEROS, CAJONES Y RAMPAS", "94", "XIII", 10);
            dataHelper.insertTabulador(14, "ESTACIONARSE EN ZONA PROHIBIDA", "94", "IV/VII", 5);
            dataHelper.insertTabulador(15, "TRANSP. PASAJE EN LUGAR DE CARGA ", "24", "I", 3);
            dataHelper.insertTabulador(16, "CORTAR CORTEJO FUNEBRE", "24", "IX", 3);
            dataHelper.insertTabulador(17, "ESTACIONARSE EN DOBLE FILA", "94", "II", 4);
            dataHelper.insertTabulador(18, "ABANDONAR LA UNIDAD EN VIA PUBLICA", "12", "XVI", 5);
            dataHelper.insertTabulador(19, "NO CEDER PASO A PEATON", "15", "II", 3);
            dataHelper.insertTabulador(20, "NO CEDER PASO A VEH. DE EMERG. EN FUNCIONES", "14", "III-D", 20);
            dataHelper.insertTabulador(21, "PASARSE ALTO DEL AGENTE", "14", "I", 4);
            dataHelper.insertTabulador(22, "PASARSE ALTO DEL SEMAFORO ", "14 / 75 ", "14-I   75-III-A", 20);
            dataHelper.insertTabulador(23, "ADELANTAR VEHICULO EN BOCACALLE", "78", "IV", 3);
            dataHelper.insertTabulador(24, "FALTA DE LUCES EN LOS FAROS DELANTEROS", "62 / 31", "31-I-D", 2);
            dataHelper.insertTabulador(25, "FALTA DE LUZ POSTERIOR ROJA", "62 / 31", "31-I-D", 2);
            dataHelper.insertTabulador(26, "FALTA DE LUCES LATERALES", "62 / 31", "31-I-D", 1);
            dataHelper.insertTabulador(27, "TRANSITAR CON LUZ ALTA EN LA CIUDAD ", "62", "", 2);
            dataHelper.insertTabulador(28, "FALTA DE RAZON SOCIAL", "170", "III", 5);
            dataHelper.insertTabulador(29, "CIRCULAR EN SENTIDO CONTRARIO", "24", "V", 5);
            dataHelper.insertTabulador(30, "DAR VUELTA U EN ZONA PROHIBIDA", "24", "XI", 5);
            dataHelper.insertTabulador(31, "NO PROTEGER CON LONA LA CARGA", "62", "", 15);
            dataHelper.insertTabulador(32, "TRANSITAR SIN PLACAS ", "33", "", 20);
            dataHelper.insertTabulador(33, "TRANSITAR CON PLACAS VENCIDAS", "33 / 62", "", 10);
            dataHelper.insertTabulador(34, "TRANSITAR CON PLACAS SOBREPUESTAS", "33 / 62", "", 20);
            dataHelper.insertTabulador(35, "FALTA DE TARJETA DE CIRCULACION", "33", "", 3);
            dataHelper.insertTabulador(36, "USO INDEBIDO DE PLACAS DEMOSTRATIVAS ", "37", "", 10);
            dataHelper.insertTabulador(37, "EXECESO DE PASAJE", "139", "XXIV", 5);
            dataHelper.insertTabulador(38, "CARECER DE ESPACIO PARA PERSONAS CON DISCAPACIDAD, EN VEH. DE SERV.PUB.DE PASAJE", " 62", "", 5);
            dataHelper.insertTabulador(39, "FALTA DE REVISTA EN LA UNIDAD", " 62 / 133", "", 3);
            dataHelper.insertTabulador(40, "SEGUIR VEHICULO DE EMERGIANCIA ", "62 / 140", "", 6);
            dataHelper.insertTabulador(41, "TRANSITAR A BAJA VELOCIDAD EN CARRIL RAPIDO", "78", "VIII/XI", 15);
            dataHelper.insertTabulador(42, "CIRCULAR CON PARABRISAS ESTRELADO,SIEMPRE Y CUANDO IMPIDA LA VISIBILIDAD O QUE SIGNIFIQUE RIESGO AL CONDUCTOR", "31", "IV", 2);
            dataHelper.insertTabulador(43, "FALTA DE BANDEROLA EN LA CARGA ", "62", "", 5);
            dataHelper.insertTabulador(44, "EFECTUAR FALSA SEÑAL DE DIRECCION ", "78", "VIII", 2);
            dataHelper.insertTabulador(45, "FALTA DE ESPEJO LATERAL", "170", "I", 1);
            dataHelper.insertTabulador(46, "ESTACIONARSE SOBRE GUARNICION", "94", "I", 3);
            dataHelper.insertTabulador(47, "TRANSITAR CON ESCAPE RUIDOSO", "124", "III", 3);
            dataHelper.insertTabulador(48, "NO REPORTAR CAMBIO DE COLORES ", "47", "", 3);
            dataHelper.insertTabulador(49, "NO REPORTAR CAMBIO DE PROPIETARIO", "47", "", 3);
            dataHelper.insertTabulador(50, "ESTACIONARSE EN SENTIDO CONTRARIO", "94", "IX", 5);
            dataHelper.insertTabulador(51, "FALTA DE CASCO PROTECTOR,AL CONDUCIR MOTOCICLETA", " 62/ 77", "77-XII", 2);
            dataHelper.insertTabulador(52, "USO INDEBIDO DE FAROS BUSCADORES ", "12 / 62", "12-VI", 5);
            dataHelper.insertTabulador(53, "EMPLEAR LUCES ROJAS AL FRENTE", "62 / 31", "31-I", 5);
            dataHelper.insertTabulador(54, "FALTA DE LUZ EN LAS PLACAS ", " 62", "", 1);
            dataHelper.insertTabulador(55, "FALTA DE LUZ ROJA AL FRENAR ", " 62", "", 6);
            dataHelper.insertTabulador(56, "FALTA DE LUZ DIRECCIONAL", "62", "", 1);
            dataHelper.insertTabulador(57, "FRENOS EN MALAS CONDICIONES", "31", "III", 2);
            dataHelper.insertTabulador(58, "USO INDEBIDO DE CLAXON", "78", "XV", 5);
            dataHelper.insertTabulador(59, "EMISION EXCESIVA DE HUMO", "124", "I", 5);
            dataHelper.insertTabulador(60, "FALTA DE ESPEJO RETROVISOR", "31", "V", 1);
            dataHelper.insertTabulador(61, "OBSTRUIR VISIBILIDAD EN CRISTAL TRASERO Y DELA", "62", "", 2);
            dataHelper.insertTabulador(62, "FALTA DE LIMPIADORES PARABRISAS", "31", "VI", 2);
            dataHelper.insertTabulador(63, "NO DAR AVISO DE CAMBIO DE DOMICILIO", "47", "", 2);
            dataHelper.insertTabulador(64, "NO DAR AVISO DE CAMBIO DE CARROCERIA", "47", "", 2);
            dataHelper.insertTabulador(65, "MALA COLOCACION DE PLACAS ", "42 / 62", "", 2);
            dataHelper.insertTabulador(66, "MALA COLOCACION DE CALCOMANIAS ", "62", "", 2);
            dataHelper.insertTabulador(67, "USAR TARJETA DE CIRCULACION DE OTRO VEHICULO", "33", "", 10);
            dataHelper.insertTabulador(68, "USAR CALCOMANIA DE OTRO VEHICULO", "33", "", 10);
            dataHelper.insertTabulador(69, "PERMITIR MANEJAR A PERS.INCAPAZ FISICO-MENTAL", "62", "", 10);
            dataHelper.insertTabulador(70, "ARROJAR OBJETOS O BASURA A LA VIA PUBLICA ", "129", "II", 10);
            dataHelper.insertTabulador(71, "LLEVAR OBJETOS QUE OBSTR. LA VISIBILIDAD", "62", "", 2);
            dataHelper.insertTabulador(72, "CARGAR COMBUSTIBLE CON EL MOTOR ENCENDIDO", "139", "V", 2);
            dataHelper.insertTabulador(73, "CARGAR COMBUSTIBLE CON PASAJE ABORDO", "139", "III", 3);
            dataHelper.insertTabulador(74, "TRANSPORTAR PERSONAS EN VEH. DE REMOLQUE POR CADA UNA", "62 / 78", "78-IX", 2);
            dataHelper.insertTabulador(75, "LLEVAR CARGA MAL SUJETA", "62 / 130", "", 5);
            dataHelper.insertTabulador(76, "OCULTAR PLACA CON CARGA", "46", "", 1);
            dataHelper.insertTabulador(77, "CONDUCIR EN MALAS CONDICIONES FISICO-MENTAL", "62", "", 10);
            dataHelper.insertTabulador(78, "TRANSPORTAR PASAJE EN ESTRIBO", "62", "", 5);
            dataHelper.insertTabulador(79, "REMOLCAR VEHICULO SIN AUTORIZACION", "78", "XII", 3);
            dataHelper.insertTabulador(80, "DAR VUELTA EN LUGAR NO PERMITIDO", "62", "", 2);
            dataHelper.insertTabulador(81, "PRESENTAR TARJETA DE CIRCULACION ILEGIBLE", "62", "", 1);
            dataHelper.insertTabulador(82, "TRANSITAR CON UNA PLACA ", "41 / 62", "", 5);
            dataHelper.insertTabulador(83, "TRANSITAR CON PLACAS ILEGIBLES", "62", "", 3);
            dataHelper.insertTabulador(84, "FALTA DE PERMISO TRANSP. MAQUINARIA PESADA", "62 / 130", "IV", 3);
            dataHelper.insertTabulador(85, "POR ALTERAR TARIFAS EN SERV. DE TRANSP.PUBLICO ", "62", "", 15);
            dataHelper.insertTabulador(86, "NO TRAER TARIFA EN LUGAR VISIBLE ", "129", "", 5);
            dataHelper.insertTabulador(87, "PERMITIR CONTROL DIRECC. A PERS. ACOMPAÑAN", "62", "", 6);
            dataHelper.insertTabulador(88, "LLEVAR BULTOS EN BRAZOS AL CONDUCIR", "24", "II", 6);
            dataHelper.insertTabulador(89, "LLEVAR PERSONAS EN BRAZOS AL CONDUCIR ", "24", "II", 10);
            dataHelper.insertTabulador(90, "PRODUCIR RUIDOS INNECESARIOS (BOCINAS,OTROS) ", "62 / 78", "78-VI", 5);
            dataHelper.insertTabulador(91, "NO GUARDAR DISTANCIA REGLAMENTARIA", "26", "II", 2);
            dataHelper.insertTabulador(92, "CAMBIAR INTEMPESTIVAMENTE DE CARRIL ", "103", "IV", 3);
            dataHelper.insertTabulador(93, "EFECTUAR RESERVA MAS DE LO PERMITIDO ", "24", "X", 2);
            dataHelper.insertTabulador(94, "PASAR SOBRE MANGUERA DE INCENDIO ", "62", "", 2);
            dataHelper.insertTabulador(95, "OBSTRUIR UNA INTERSECCION DE CRUCERO", "78", "XIV", 3);
            dataHelper.insertTabulador(96, "FRENAR BRUSCAMENTE SIN PRECAUCION", "62", "", 2);
            dataHelper.insertTabulador(97, "DAR VUELTA SIN PREVIA SEÑAL", "62", "", 2);
            dataHelper.insertTabulador(98, "ENTAMBLAR COMPETENCIA DE VELOCIDAD VIA PUB. ", "78", "VII", 50);
            dataHelper.insertTabulador(99, "ADELANTAR VEHICULO EN ZONA PEATONAL ", "78", "IV", 3);
            dataHelper.insertTabulador(100, "ADELANTAR HILERA DE VEHICULOS ", "78", "V", 3);
            dataHelper.insertTabulador(101, "IMPEDIR REBASAR AUMENTANDO LA VELOCIDAD ", "166", "X", 5);
            dataHelper.insertTabulador(102, "ADELANTAR VEHICULO POR ACOTAMIENTO ", "78", "V", 5);
            dataHelper.insertTabulador(103, "ESTACIONARSE SOBRE LA ACERA", "94", "I", 3);
            dataHelper.insertTabulador(104, "ESTACIONARSE FRENTE A COCHERA ", "94", "III", 6);
            dataHelper.insertTabulador(105, "ESTACINARSE FRENTE A HIDRANTE ", "94", "X", 3);
            dataHelper.insertTabulador(106, "ESTACIONAR FRENTE A BOMBEROS Y AMBULANCIAS", "94", "VI", 10);
            dataHelper.insertTabulador(107, "NO ATENDER DESCUENTOS A ESTUDIANTES Y PERSONAS DE LA TERCERA EDAD", "139 / 140 ", "140-I", 5);
            dataHelper.insertTabulador(108, "MANEJAR Y TRANSP. PASAJE DE AUTOBUS BAJO LA INFLUENCIA DE ESTUPEFACIENTE-EBRIO O DROGADO", "134 / 139", "139-III", 200);
            dataHelper.insertTabulador(109, "TRANSP. ANIMALES, BULTOS QUE MOLESTEN AL PASAJE ", "139", "XV", 5);
            dataHelper.insertTabulador(110, "TRANSP. MAS DE 2 CARGADORES EN CAM/CARGA ", "139", "XXIV", 1);
            dataHelper.insertTabulador(111, "TRANSP. MAS DE 2 PERS. EN CABINA VEH. DE CARGA", "139", "XXIV", 1);
            dataHelper.insertTabulador(112, "TRANSP. CARGA QUE NO SEA DEBIDAMENTE ABANDERADA", "130 / 139", "139-XXV", 2);
            dataHelper.insertTabulador(113, "PINTAR CARROCERIA CON COLORES DE SERVICIO OFICIAL Y DE EMERGENCIA ", "62 / 165 ", "", 2);
            dataHelper.insertTabulador(114, "ESTABLECER SITO EN LUGAR NO AUTORIZADO", "183 / 185", "", 5);
            dataHelper.insertTabulador(115, "DERRAPAR QUEMANDO LLANTA ", "78", "XV", 20);
            dataHelper.insertTabulador(116, "INTENTO DE SOBORNO POR AMBAS PARTES ", "10/77", "", 1);
            dataHelper.insertTabulador(117, "SUBIR  Y BAJAR PASAJE EN DOBLE FILA ", "139", "XXV", 3);
            dataHelper.insertTabulador(118, "SUBIR Y BAJAR PASAJE POR PUERTA IZQUIERDA ", "62 / 139", "", 3);
            dataHelper.insertTabulador(119, "REALIZAR MANIOBRA PARA INTRODUCIRSE A NEGOCIOS OBSTRUYENDO LA CIRCULACION ", "166", "", 10);
            dataHelper.insertTabulador(120, "REALIZAR MANIOBRAS DE CARGA SIN PERMISO ", "171", "", 3);
            dataHelper.insertTabulador(121, "EFECTUAR VUELTA A EXCESO DE VELOCIDD Y DERRAPAR INNECESARIAMENTE", "78", "XV", 20);
            dataHelper.insertTabulador(122, "ESTACIONAR CAMION PESADO EN PRIMER CUADRO", "94", "VII", 5);
            dataHelper.insertTabulador(123, "INGERIR BEBIDAS EMBRIAGANTES EN VEHICULO", "78", "I", 20);
            dataHelper.insertTabulador(124, "PASARSE ALTO DE DISCO (SEÑALAMIENTO)", "23", "", 10);
            dataHelper.insertTabulador(125, "NO CEDER PASO A VEH.EN PREFERENCIA ", "14", "III D", 5);
            dataHelper.insertTabulador(126, "NO USAR CINTURON DE SEGURIDAD ", "77", "I", 3);
            dataHelper.insertTabulador(127, "NO PRESERVARLA ESCENA DEL LUGAR DE LOS HECHOS", "111", "IV", 5);
            dataHelper.insertTabulador(128, "POR PRESTACION DE SERVICIO PUB. SIN AUTORIZACION PARA TRANSPORTAR CARGA Y MAT. CONSTRUCCION", "130", "I", 55);
            dataHelper.insertTabulador(129, "POR PINTAR INMUEBLES, PAREDES O TRANSPORTE PUBLICO CON GRAFFITI", "62", "", 100);
            dataHelper.insertTabulador(130, "OBSTRUIR PASO DE VEHICULO DE EMERGENCIA", "21", "VII", 10);
            dataHelper.insertTabulador(131, "POR MANEJAR VEHICULO MENOR DE EDAD FUERA DE HORARIO PERMITIDO ", "24 / 58", "24-XV", 20);
            dataHelper.insertTabulador(132, "PERSONA SORPRENDIDA EN COMPAÑIA DE MENOR DE EDAD INGIRIENDO BEBIDAS EMBRIAGANTES Y/O DROGADO, ALTAS HORAS ", "78 / 190 ", "78-XIX", 100);
        }
    }
}
