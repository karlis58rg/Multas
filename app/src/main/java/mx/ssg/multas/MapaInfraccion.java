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

    EditText txtDocReteInfra;
    ImageView btnAgregarInfraccion, btnAlcohol, btnVelocimetro, btnSemaforo, btnVuelta, btnDobleFila, btnCasco, btnCinturon, btnEstacionarse;
    String cargarInfoUsuario, cargarInfoRandom;
    String fecha, hora, documentoRetenido, claveInfraccion, respuestaJson, resGarantia, resGarantia1;
    int monto = 0;
    int salarioMinimo = 120;
    int sumaSalarios = 0;
    int restaSalarios = 0;
    int varSalarios = 0;

    TextView txtMontoInfraPagar, txtDescSalarios, txtSalarios;
    RadioGroup radioGarantia, radioGarantia1;
    Button btnGuardarInfraccion;
    int numberRandom;
    public String codigoVerifi, resClave, resSalarios;
    public int cargarInfoValor = 0;
    public Double montoApagar;
    int valor = 0;
    String cadenaBorrar = "";
    String cadenaSalarioBorrar = "";

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
        Random();
        lyInicio = view.findViewById(R.id.lyInicioInfra);
        lyCategoria = view.findViewById(R.id.lyCategoriaInfra);
        lyContact = view.findViewById(R.id.lyContactInfra);
        lyFavoritos = view.findViewById(R.id.lyFavoritosInfra);
        btnList = view.findViewById(R.id.btnListInfra);

        radioGarantia = view.findViewById(R.id.radioGarantia);
        radioGarantia1 = view.findViewById(R.id.radioGarantia1);

        txtDocReteInfra = view.findViewById(R.id.txtDocRetenido);
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

        radioGarantia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioPlaca) {
                    resGarantia = "Placa";
                } else if (checkedId == R.id.radioTCirculacion) {
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
                        cadenaBorrar = cadenaBorrar.replaceAll("[0-9]", "");
                        cadenaBorrar = cadenaBorrar.trim();
                        cadenaSalarioBorrar = cadenaSalarioBorrar.replaceAll("[a-zA-Z]","");
                        cadenaSalarioBorrar = cadenaSalarioBorrar.trim();
                        System.out.println(cadenaBorrar);
                        System.out.println(cadenaSalarioBorrar);
                        deleteInfraccionTemp();  // FALTA CONSUMIR EL SERVICIO Y ELIMINAR EL SALDO DEL TOTAL.
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
                claveInfraccion = "MANEJAR EN ESTADO DE EBRIEDAD";
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
            }
        });
        btnVelocimetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "EXCESO DE VELOCIDAD";
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
            }
        });
        btnSemaforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "PASARSE ALTO DEL SEMAFORO";
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
            }
        });
        btnVuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "DAR VUELTA EN “U” EN ZONA PROHIBIDA";
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
            }
        });
        btnDobleFila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "ESTACIONARSE EN DOBLE FILA";
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

            }
        });
        btnCasco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "FALTA DE CASCO PROTECTOR, AL CONDUCIRMOTOCICLETA";
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
            }
        });
        btnCinturon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "NO USAR CINTURON DE SEGURIDAD";
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

            }
        });
        btnEstacionarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claveInfraccion = "ESTACIONARSE EN ZONA PROHIBIDA";
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroInfraccion() {
        cargarDatos();
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
                .add("Latitud", "20.0087036") //lat_origen.toString()
                .add("Longitud", "-98.8208197") //
                .add("Fecha", fecha)
                .add("Hora", hora)
                .add("Garantia", resGarantia + " " + resGarantia1)
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
                    MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "DATO AGREGADO", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), NetPay.class);
                            montoApagar = Double.valueOf(cargarInfoValor);
                            i.putExtra("MONTO", montoApagar);
                            i.putExtra("FOLIO", cargarInfoRandom);
                            eliminarDatos();
                            startActivity(i);
                        }
                    });
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

    private void guardarRandom() {
        share = getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.putString("RANDOM", codigoVerifi);
        editor.commit();
    }


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
    public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(9000) * 99;
        codigoVerifi = String.valueOf(numberRandom);
        guardarRandom();
    }

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

        /*map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //scroll.requestDisallowInterceptTouchEvent(true);
                Toast.makeText(getActivity(), "Se moverá el marcador", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng neww = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                mi_ubi(neww);
            }
        });*/
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
            String direccion1 = DirCalle.getAddressLine(0);
            String municipio = DirCalle.getLocality();
            String estado = DirCalle.getAdminArea();
            if (municipio != null) {
                municipio = DirCalle.getLocality();
            } else {
                municipio = "SIN INFORMACIÓN";
            }

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

    /******************** LLENAR EL COMBO *************************************/
    private void ListTabulador() {
        DataHelper dataHelper = new DataHelper(getActivity());
        ArrayList<String> list = dataHelper.getAllTabulador();
        if (list.toString().length() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spinCatTabulador.setAdapter(adapter);
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
            dataHelper.insertTabulador(30, "DAR VUELTA 'U' EN ZONA PROHIBIDA", "24", "XI", 5);
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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spinCatTabulador.setAdapter(adapter);
        }
    }
}
