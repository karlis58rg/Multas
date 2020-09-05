package mx.ssg.multas;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import mx.ssg.multas.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaInfraccion extends Fragment implements OnMapReadyCallback {
    private MapaInfraccion.OnFragmentInteractionListener mListener;
    GoogleMap map;
    Button btn_address;
    Boolean actul_posicion = true;
    Marker marker = null;
    public Double lat_origen, lon_origen;
    String myCity = "";
    TextView tv_add;
    Button btn_reg;
    ImageButton detenerAudio;
    LatLng aux;
    Location aux_loc;
    AlertDialog alert = null;
    int contador1;
    Chronometer tiempo;
    Boolean correr = false;
    long detenerse;

    EditText txtDocReteInfra, txtObservacionesInfraccion;
    ImageView btnAgregarInfraccion, btnAlcohol, btnVelocimetro, btnSemaforo, btnVuelta, btnDobleFila, btnCasco, btnCinturon, btnEstacionarse;
    String cargarInfoUsuario, cargarInfoRandom;
    String fecha, hora, documentoRetenido, claveInfraccion, respuestaJson,resGarantia, resGarantiaPlaca, resGarantiaVehiculo, resGarantiaTCirculacion, resGarantiaLconducir;
    int monto = 0;
    int salarioMinimo = 86;
    int sumaSalarios = 0;
    int restaSalarios = 0;
    int varSalarios = 0;

    TextView txtMontoInfraPagar, txtDescSalarios, txtSalarios;
    RadioGroup radioGarantia, radioGarantia1;
    CheckBox cheplaca,chevehiculo,chetcirculacion,chelconducir;
    Button btnGuardarInfraccion;
    int numberRandom;
    public String codigoVerifi, resClave, resSalarios;
    public int cargarInfoValor = 0;
    public Double montoApagar;
    int valor = 0;
    String cadenaBorrar = "";
    String cadenaSalarioBorrar = "";
    public static String direccion,municipio,estado;
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

    public MapaInfraccion() {
        // Required empty public constructor
    }

    public static MapaInfraccion newInstance(String param1, String param2) {
        MapaInfraccion fragment = new MapaInfraccion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_mapa_infraccion, container, false);
        //************************************** ACCIONES DE LA VISTA **************************************//
        //Random();
        cargarDatos();
        generateRandomString(6);
        getExistRegistroLicencia();
        getExistRegistroTarjeta();

        lyInicio = view.findViewById(R.id.lyInicioInfra);
        lyCategoria = view.findViewById(R.id.lyCategoriaInfra);
        lyContact = view.findViewById(R.id.lyContactInfra);
        lyFavoritos = view.findViewById(R.id.lyFavoritosInfra);
        btnList = view.findViewById(R.id.btnListInfra);

        //////checks/////////////
        cheplaca= view.findViewById(R.id.checkPlaca);
        chevehiculo= view.findViewById(R.id.checkVehiculo);
        chelconducir= view.findViewById(R.id.checkLConducir);
        chetcirculacion= view.findViewById(R.id.checkTCirculacion);

       /*radioGarantia = view.findViewById(R.id.radioGarantia);
        radioGarantia1 = view.findViewById(R.id.radioGarantia1);*/

        txtDocReteInfra = view.findViewById(R.id.txtDocRetenido);
        txtObservacionesInfraccion = view.findViewById(R.id.txtObservacionesInfraccion);
        btnAgregarInfraccion = view.findViewById(R.id.imgAgregarInfraccion);
        btnGuardarInfraccion = view.findViewById(R.id.imgGuardarInfraccion);

        btnAlcohol = view.findViewById(R.id.imgAlcohol);
        btnVelocimetro = view.findViewById(R.id.imgVelocimetro);
        btnSemaforo = view.findViewById(R.id.imgSemaforo);
        btnVuelta = view.findViewById(R.id.imgVuelta);
        btnDobleFila = view.findViewById(R.id.imgDobleFila);
        btnCasco = view.findViewById(R.id.imgCasco);
        btnCinturon = view.findViewById(R.id.imgCinturon);
        btnEstacionarse = view.findViewById(R.id.imgEstacionarse);

        txtMontoInfraPagar = view.findViewById(R.id.lblMontoInfraccion);
        spinCatTabulador = view.findViewById(R.id.spinInfraccion);
        ListTabulador();

        palabras = new ArrayList<String>();
        adaptador1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, palabras);
        lv1 = view.findViewById(R.id.ListaP);
        lv1.setAdapter(adaptador1);


        lyInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewPDFController.class);
                startActivity(i);
            }
        });

        lyCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LugaresDePago.class);
                startActivity(i);
            }
        });

        lyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Contactos.class);
                startActivity(i);
            }
        });

        lyFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Tabulador.class);
                startActivity(i);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Reglamento.class);
                startActivity(i);
            }
        });

        btnAgregarInfraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClaveInfra();
            }
        });

        btnGuardarInfraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDocReteInfra.toString().isEmpty()) {
                    Toast.makeText(getContext(), "DEBE AGREGAR UNA OBSERVACIÓN", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                    insertRegistroInfraccion();
                }
            }
        });

      /* cheplaca.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.checkPlaca) {
                    resGarantia = "Placa";
                } else if (checkedId == R.id.checkTCirculacion) {
                    resGarantia = "T.Circulacion";
                }
            }
        });

        radioGarantia1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioVehiculo) {
                    resGarantia1 = "Vehículo";
                } else if (checkedId == R.id.radioLConducir) {
                    resGarantia1 = "L.Conducir";
                }
            }
        });
*/
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion = i;
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("IMPORTANTE");
                dialogo1.setMessage("¿DESEA ELIMINAR ESTE DATO?");
                dialogo1.setCancelable(false);
                cadenaBorrar = palabras.get(posicion);
                cadenaSalarioBorrar = palabras.get(posicion);
                System.out.println(cadenaBorrar);
                System.out.println(cadenaSalarioBorrar);
                dialogo1.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cadenaBorrar = cadenaBorrar.replaceAll("[0-9]", ""); // QUITAR LAS "" EN EL SQL Y EN EL SQLITE
                        cadenaBorrar = cadenaBorrar.trim();
                        cadenaSalarioBorrar = cadenaSalarioBorrar.replaceAll("[a-zA-Z]","");
                        cadenaSalarioBorrar = cadenaSalarioBorrar.trim();
                        System.out.println(cadenaBorrar);
                        System.out.println(cadenaSalarioBorrar);
                        deleteInfraccionTemp();
                        palabras.remove(posicion);
                        adaptador1.notifyDataSetChanged();
                    }
                });
                dialogo1.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
                return false;
            }
        });

        btnAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos();
                if (claveInfraccion != "POR FALTA DE LUZ BLANCA EN LA PLACA"){
                    claveInfraccion = "POR FALTA DE LUZ BLANCA EN LA PLACA";
                    resClave = "3";
                    resSalarios = "75";
                    /***LLENADO DE LA TABLA***/
                    palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                    adaptador1.notifyDataSetChanged();
                    /**************************/
                    inserInfraccionTemp();
                    if (cargarInfoValor != 0) {
                        monto = Integer.parseInt(resSalarios);
                        sumaSalarios = monto * salarioMinimo;
                        valor = sumaSalarios + cargarInfoValor; //360
                        guardarDatosInfra();
                    } else {
                        monto = Integer.parseInt(resSalarios);
                        sumaSalarios = monto * salarioMinimo;
                        valor = sumaSalarios; //360
                        guardarDatosInfra();
                    }
                    txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
                }else{

                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnVelocimetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (claveInfraccion != "FALTA DE LUZ DE REVERSA") {
                    claveInfraccion = "FALTA DE LUZ DE REVERSA";
                    resClave = "4";
                    resSalarios = "20";
                    /***LLENADO DE LA TABLA***/
                    palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                    adaptador1.notifyDataSetChanged();
                    /**************************/
                    inserInfraccionTemp();
                    if (cargarInfoValor != 0) {
                        monto = Integer.parseInt(resSalarios);
                        sumaSalarios = monto * salarioMinimo;
                        valor = sumaSalarios + cargarInfoValor; //360
                        guardarDatosInfra();
                    } else {
                        monto = Integer.parseInt(resSalarios);
                        sumaSalarios = monto * salarioMinimo;
                        valor = sumaSalarios; //360
                        guardarDatosInfra();
                    }
                    txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
                } else {
                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnSemaforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion != "POR ODOMETRO EN MAL ESTADO"){
                claveInfraccion = "POR ODOMETRO EN MAL ESTADO";
                resClave = "22";
                resSalarios = "20";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
            }else{
                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnVuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion != "POR FALTA DE TIMBRE PARA QUE EL PASAJE ANUNCIE EL DESCENSO"){
                claveInfraccion ="POR FALTA DE TIMBRE PARA QUE EL PASAJE ANUNCIE EL DESCENSO";
                resClave = "30";
                resSalarios = "5";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
            }else{
                Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                  }
            }
        });
        btnDobleFila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion !="POR FALTA DE ASEO EN EL INTERIOR Y EXTERIOR DE LA UNIDAD"){
                claveInfraccion = "POR FALTA DE ASEO EN EL INTERIOR Y EXTERIOR DE LA UNIDAD";
                resClave = "17";
                resSalarios = "4";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR

            }else{
                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCasco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion!="POR FALTA DE ASEO DEL OPERADOR"){
                claveInfraccion = "POR FALTA DE ASEO DEL OPERADOR";
                resClave = "51";
                resSalarios = "2";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
            }else{
                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCinturon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion !="POR NO PORTAR EL UNIFORME AUTORIZADO "){
                claveInfraccion = "POR NO PORTAR EL UNIFORME AUTORIZADO ";
                resClave = "128";
                resSalarios = "3";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR

            }else{
                Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
            }
           }
        });
        btnEstacionarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claveInfraccion !="POR NO DESINFECTAR LAS UNIDADES"){
                claveInfraccion = "POR NO DESINFECTAR LAS UNIDADES";
                resClave = "14";
                resSalarios = "5";
                /***LLENADO DE LA TABLA***/
                palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                adaptador1.notifyDataSetChanged();
                /**************************/
                inserInfraccionTemp();
                if (cargarInfoValor != 0) {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios + cargarInfoValor; //360
                    guardarDatosInfra();
                } else {
                    monto = Integer.parseInt(resSalarios);
                    sumaSalarios = monto * salarioMinimo;
                    valor = sumaSalarios; //360
                    guardarDatosInfra();
                }
                txtMontoInfraPagar.setText("$" + valor + " " + "MXN"); //TE TRAES LOS SALARIOS DEL COMBO Y SE LOS QUITAS AL VALOR
            }else{
                    Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                }
            }
        });

        //*************************** SE MUESTRA EL MAPA ****************************************//
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapInfraccion);
        mapFragment.getMapAsync(this);

        return view;
    }
    /******************GET A LA BD***********************************/
    public void getClaveInfra() {
        cargarDatos();
        final String Claveinfraccion2 = claveInfraccion;
        claveInfraccion = (String) spinCatTabulador.getSelectedItem();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Tabulador?desc=" + claveInfraccion)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject jObj = null;
                                    jObj = new JSONObject("" + myResponse + "");
                                    resClave = jObj.getString("Clave");
                                    resSalarios = jObj.getString("SalMinimos");
                                    if(claveInfraccion!=Claveinfraccion2){
                                    /***LLENADO DE LA TABLA***/
                                    palabras.add(claveInfraccion + "  " + "  " + resSalarios);
                                    adaptador1.notifyDataSetChanged();
                                    /*************************/
                                    Log.i("HERE", "" + jObj);
                                    /**************************/
                                    inserInfraccionTemp();
                                    if (cargarInfoValor != 0) {
                                        monto = Integer.parseInt(resSalarios);
                                        sumaSalarios = monto * salarioMinimo;
                                        valor = sumaSalarios + cargarInfoValor; //360
                                        guardarDatosInfra();
                                    } else {
                                        monto = Integer.parseInt(resSalarios);
                                        sumaSalarios = monto * salarioMinimo;
                                        valor = sumaSalarios; //360
                                        guardarDatosInfra();
                                    }
                                    spinCatTabulador.clearFocus();
                                    txtMontoInfraPagar.setText("$" + valor + " " + "MXN");
                                }else{
                                        Toast.makeText(getContext(), "EL ELEMENTO YA SE ENCUENTRA AGREGADO", Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void inserInfraccionTemp() {
        cargarDatos();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("Clave", resClave)
                .add("Descripcion", claveInfraccion)
                .add("SalMinimos", resSalarios)
                .build();
        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TempInfraccion/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();
                    MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                            System.out.println(cargarInfoRandom);
                        }
                    });
                }
            }
        });
    }

    //***************** ELIMINA A LA BD MEDIANTE EL WS **************************//
    private void deleteInfraccionTemp() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TempInfraccion?infraccionDelete="+cargarInfoRandom+"&descripcionDelete="+cadenaBorrar)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();
                    MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            monto = Integer.parseInt(cadenaSalarioBorrar); //3
                            sumaSalarios = monto * salarioMinimo; //120*3 = 360
                            valor = cargarInfoValor - sumaSalarios; //360
                            guardarDatosInfra();
                            System.out.println("EL DATO SE ELIMINO CORRECTAMENTE");
                            System.out.println(valor);
                            txtMontoInfraPagar.setText("$" + valor + " " + "MXN");
                        }
                    });
                }

            }
        });
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroInfraccion() {
        cargarDatos();
        garantia();
        //*************** FECHA **********************//
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);
        //*************** HORA **********************//
        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        hora = timeFormat.format(time);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", cargarInfoRandom)
                .add("Usuario", cargarInfoUsuario)
                .add("Latitud", lat_origen.toString())
                .add("Longitud", lon_origen.toString())
                .add("Direccion", direccionTurno)
                .add("Contrasena", contrasenia) //
                .add("Fecha", fecha)
                .add("Hora", hora)
                .add("Garantia",resGarantia+ " " +resGarantiaPlaca+ " " +resGarantiaTCirculacion+ " " +resGarantiaVehiculo+ " " +resGarantiaLconducir )
                //.add("SalariosMinimos", resSalarios) //CHECAR LA SUMA DE LOS SALARIOS
                .add("Condonacion", "0")
                .add("Pago", String.valueOf(cargarInfoValor))
                .add("StatusPago", "1")
                .build();

        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Infracciones/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();
                    Intent i = new Intent(getActivity(), NetPay.class);
                    montoApagar = Double.valueOf(cargarInfoValor);
                    i.putExtra("MONTO", montoApagar);
                    i.putExtra("FOLIO", cargarInfoRandom);
                    i.putExtra("DIRECCION", direccionTurno);
                    i.putExtra("CONTRASENIA", contrasenia);
                    eliminarDatos();
                    startActivity(i);
                }
            }
        });
    }

    /******************GET A LA BD***********************************/
    public void getExistRegistroLicencia() {
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
                Toast.makeText(getContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
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
                        MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
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
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?idExistente="+cargarInfoRandom)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
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
                        MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
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
        share = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarInfoValor = share.getInt("ValorInfraccion", 0);
        cargarInfoUsuario = share.getString("USER", "");
        cargarInfoRandom = share.getString("RANDOM", "");
    }

    private void eliminarDatos() {
        share = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.remove("ValorInfraccion").commit();
        editor.remove("RANDOM").commit();
    }

    private void guardarDatosInfra() {
        share = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.putInt("ValorInfraccion", valor);
        editor.commit();
    }

    /*private void guardarRandom() {
        share = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.putString("RANDOM", codigoVerifi);
        editor.commit();
    }*/


    private void alertaGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("UN MOMENTO, IMPRIMIENDO INFRACCIÓN")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        alert = builder.create();
        alert.show();
    }

    //********************* GENERA EL NÚMERO ALEATORIO PARA EL ID*****************************//
    /*public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(9000) * 99;
        codigoVerifi = String.valueOf(numberRandom);
        guardarRandom();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapaInfraccion.OnFragmentInteractionListener) {
            mListener = (MapaInfraccion.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng punto1 = new LatLng(20.1229876, -98.7383968);
        map.addMarker(new MarkerOptions().position(punto1).title("Módulo de infracciones Plaza Juarez"));
        map.addMarker(new MarkerOptions().position(punto1).snippet("Horario: "));
        map.moveCamera(CameraUpdateFactory.newLatLng(punto1));

        LatLng punto2 = new LatLng(20.0641948, -98.7949564);
        map.addMarker(new MarkerOptions().position(punto2).title("MÓDULO DE INFRACCIONES PITAHAYAS"));
        map.moveCamera(CameraUpdateFactory.newLatLng(punto2));

        final String[] permisos = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        map = googleMap;
        tv_add = (TextView) getActivity().findViewById(R.id.tv_miadresInfraccion);
        map.setMyLocationEnabled(true);

        //activar el boton " ubicación" de mapa y regrese la dirección actual/////
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                actul_posicion = true;
                return false;
            }
        });
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                aux_loc = location;

                if (actul_posicion) {
                    iniciar(location);
                }
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //scroll.requestDisallowInterceptTouchEvent(true);
                Toast.makeText(getActivity(), "SE MOVERÁ EL MARCADOR", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng neww = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                mi_ubi(neww);
            }
        });
    }

    public String mi_ubi(LatLng au) {
        String address = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(au.latitude, au.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            address = "Dirección: \n" + address;
            tv_add.setText(address);
            Address DirCalle = addresses.get(0);
            direccion = DirCalle.getAddressLine(0);
            municipio = DirCalle.getLocality();
            estado = DirCalle.getAdminArea();
            if (municipio != null) {
                municipio = DirCalle.getLocality();
            } else {
                municipio = "SIN INFORMACIÓN";
            }
            direccionTurno = direccion +" " +municipio +" "+estado;
            Log.i("HERE", "dir" + direccion + "mun"+ municipio + "est"+ estado);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return address;
    }

    public void iniciar(Location location) {
        tv_add.setText("");

        if (marker != null) {
            marker.remove();
        }

        lat_origen = location.getLatitude();
        lon_origen = location.getLongitude();
        actul_posicion = false;

        LatLng mi_posicion = new LatLng(lat_origen, lon_origen);

        marker = map.addMarker(new MarkerOptions().position(mi_posicion).title("USTED SE ENCUENTRA AQUÍ").draggable(true));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat_origen, lon_origen)).zoom(14).bearing(15).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        aux = new LatLng(lat_origen, lon_origen);

        mi_ubi(aux);
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
        DataHelper dataHelper = new DataHelper(getActivity());
        ArrayList<String> list = dataHelper.getAllTabulador();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spinCatTabulador.setAdapter(adapter);
        }
    }
}
