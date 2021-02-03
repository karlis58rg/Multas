package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import mx.ssg.multas.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class infraccionOffline extends AppCompatActivity {

    EditText txtDocReteInfra, txtObservacionesInfraccion;
    ImageView btnAgregarInfraccion, btnpasajeros,btnllantamal ,btnmecanico, btnvigenciatarjet,btnseguro,btnCarril,btndesensopa,btnplacasvigen ,btncinturon ,btnpolarizado ;
    String cargarInfoUsuario, cargarFolioInfra;
    String fecha, hora, documentoRetenido, claveInfraccion, respuestaJson,resGarantia, resGarantiaPlaca, resGarantiaVehiculo, resGarantiaTCirculacion, resGarantiaLconducir;
    int monto = 0;
    float salarioMinimo = 86.88f;
    float sumaSalarios = 0.0f;
    int restaSalarios = 0;
    int varSalarios = 0;

    TextView txtMontoInfraPagar, txtDescSalarios, txtSalarios;
    RadioGroup radioGarantia, radioGarantia1;
    CheckBox cheplaca,chevehiculo,chetcirculacion,chelconducir;
    Button btnGuardarInfraccion;
    int numberRandom;
    public String codigoVerifi, resClave, resSalarios;
    public float cargarInfoValor = 0.0f;
    public float montoApagar = 0.0f;
    float valor = 0.0f;
    String cadenaBorrar = "";
    String cadenaSalarioBorrar = "";
    public static String direccionTurno;
    public static String contrasenia;
    public String respLicencia;
    public String licenciaExist;
    public String tarjetaExist;

    ListView lv1;
    ArrayList<String> palabras;
    ArrayAdapter<String> adaptador1;

    private LinearLayout lyInicio;
    private LinearLayout lyCategoria;
    private LinearLayout lyContact;
    private LinearLayout lyFavoritos;
    private ImageView btnList;

    SharedPreferences share;
    SharedPreferences.Editor editor;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    Infraccion tm = new Infraccion();
    int runAudio = 0;
    Spinner spinCatTabulador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infraccion_offline);
        //Random();
        cargarDatos();
        generateRandomString(6);
        getExistRegistroLicencia();
        getExistRegistroTarjeta();

        lyInicio = findViewById(R.id.lyInicioInfraOffline);
        lyCategoria = findViewById(R.id.lyCategoriaInfraOffline);
        lyContact = findViewById(R.id.lyContactInfraOffline);
        lyFavoritos = findViewById(R.id.lyFavoritosInfraOffline);
        btnList = findViewById(R.id.btnListInfraOffline);

        //////checks/////////////
        cheplaca = findViewById(R.id.checkPlacaOffline);
        chevehiculo = findViewById(R.id.checkVehiculoOffline);
        chelconducir = findViewById(R.id.checkLConducirOffline);
        chetcirculacion = findViewById(R.id.checkTCirculacionOffline);

        txtDocReteInfra = findViewById(R.id.txtDocRetenidoOffline);
        txtObservacionesInfraccion = findViewById(R.id.txtObservacionesInfraccionOffline);
        btnAgregarInfraccion = findViewById(R.id.imgAgregarInfraccionOffline);
        btnGuardarInfraccion = findViewById(R.id.imgGuardarInfraccionOffline);

        btnpasajeros = findViewById(R.id.imgpasajerosOffline);
        btnllantamal = findViewById(R.id.imgllantamalOffline);
        btnmecanico = findViewById(R.id.imgmecanicoOffline);
        btnvigenciatarjet = findViewById(R.id.imgvigenciatarjetaOffline);
        btnseguro = findViewById(R.id.imgseguroOffline);
        btnCarril = findViewById(R.id.imgcarrilOffline);
        btndesensopa = findViewById(R.id.imgdesensopasajeOffline);
        btnplacasvigen = findViewById(R.id.imgplacasvigenciaOffline);
        btncinturon = findViewById(R.id.imgcinturonOffline);
        btnpolarizado = findViewById(R.id.imgpolarizadosOffline);

        txtMontoInfraPagar = findViewById(R.id.lblMontoInfraccionOffline);
        spinCatTabulador = findViewById(R.id.spinInfraccionOffline);
        ListTabulador();

        palabras = new ArrayList<String>();
        adaptador1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, palabras);
        lv1 = findViewById(R.id.ListaPOffline);
        lv1.setAdapter(adaptador1);
    }

    /******************metodo de checkbox*********************/
    public void garantia(){

        if(txtDocReteInfra.getText().toString().isEmpty()){
            resGarantia = "NO SE ESPECIFICO DOCUMENTO";
        }else{
            resGarantia = txtDocReteInfra.getText().toString();
        }
        if(cheplaca.isChecked() == true ){
            resGarantiaPlaca = "SE RETIENE PLACA";
        }else{
            resGarantiaPlaca = "NO SE RETIENE PLACA";
        }
        if (chetcirculacion.isChecked()==true){
            resGarantiaTCirculacion = "SE RETIENE T.CIRCULACION ";
        }else{
            resGarantiaTCirculacion = "NO SE RETIENE T. CIRCULACION";
        }
        if (chevehiculo.isChecked()==true){
            resGarantiaVehiculo = "SE RETIENE VEHÍCULO ";
        }else{
            resGarantiaVehiculo = "NO SE RETIENE VEHÍCULO";
        }
        if (chelconducir.isChecked()==true){
            resGarantiaLconducir = "SE RETIENE L. CONDUCIR ";
        }else{
            resGarantiaLconducir = "NO SE RETIENE L. CONDUCIR";
        }
    }



    /******************GET A LA BD***********************************/
    public void getExistRegistroLicencia() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaConducir?idExistente="+cargarFolioInfra)
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
                        infraccionOffline.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chelconducir.setChecked(true);
                            }
                        });
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    /******************GET A LA BD***********************************/
    public void getExistRegistroTarjeta() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?idExistente="+cargarFolioInfra)
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
                        infraccionOffline.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chetcirculacion.setChecked(true);
                            }
                        });
                    }
                    Log.i("HERE", resp);
                }
            }

        });
    }

    public void cargarDatos() {
        share = getApplicationContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarInfoValor = share.getFloat("ValorInfraccion", 0.0f);
        cargarInfoUsuario = share.getString("USER", "");
        cargarFolioInfra = share.getString("FOLIOINFRACCION", "");
    }

    private void eliminarDatos() {
        share = getApplicationContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.remove("ValorInfraccion").commit();
        editor.remove("FOLIOINFRACCION").commit();
    }

    private void guardarDatosInfra() {
        share = getApplicationContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.putFloat("ValorInfraccion", valor);
        editor.commit();
    }

    public String generateRandomString(int length) {
        String randomString = "";

        final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890".toCharArray();
        final Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString = randomString + chars[random.nextInt(chars.length)];
        }
        contrasenia = randomString;
        System.out.println(contrasenia);
        return randomString;
    }


    /******************** LLENAR EL COMBO *************************************/
    private void ListTabulador() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllTabulador();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, R.id.txt, list);
            spinCatTabulador.setAdapter(adapter);
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
            dataHelper.insertTabulador(66, "TRANSITAR CON LLANTAS EN MAL ESTADO ", "274", "1", 5);
            dataHelper.insertTabulador(67, "NO CONTAR CON REVISION MECANICA VIGENTE", "274", "2", 10);
            dataHelper.insertTabulador(68, "NO CONTAR CON POLIZA DE SEGURO VIGENTE", "274", "3", 10);
            dataHelper.insertTabulador(69, "NO PORTAR CINTURON DE SEGURIDAD", "274", "4", 5);
            dataHelper.insertTabulador(70, "TRAER VIDRIOS POLARIZADOS ", "274", "5", 10);
            dataHelper.insertTabulador(71, "NO CONTAR CON PLACAS VIGENTES", "274", "6", 10);
            dataHelper.insertTabulador(72, "EFECTUAR ASENSO Y DESENSO FUERA DE LUGAR AUTORIZADO ", "274", "7", 10);
            dataHelper.insertTabulador(73, "TRANSPORTAR MAYOR NUMERO DE PERSONAS DE LO APROBADO", "274", "8", 10);
            dataHelper.insertTabulador(74, "NO CONTAR CON LICENCIA CORRESPONDIENTE VIGENTE", "274", "9", 10);
            dataHelper.insertTabulador(75, "POR NO CIRCULAR POR CARRIL DERECHO", "274", "10", 10);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, R.id.txt, list);
            spinCatTabulador.setAdapter(adapter);
        }
    }
}
