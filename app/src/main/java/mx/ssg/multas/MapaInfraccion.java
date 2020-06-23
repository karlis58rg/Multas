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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

    EditText txtDocReteInfra,txtClaveInfra;
    ImageView btnAgregarInfraccion;
    String cargarInfoUsuario;
    String fecha,hora,documentoRetenido,claveInfraccion,respuestaJson,resDescripcionClave,resSalarios,resGarantia,resGarantia1;
    int monto = 0;
    int salarioMinimo = 120;
    int sumaSalarios = 0;
    int valor = 0;
    int cargarInfoValor = 0;
    int varSalarios = 0;
    TextView txtMontoInfraPagar,txtDescSalarios,txtSalarios;
    RadioGroup radioGarantia,radioGarantia1;
    Button btnGuardarInfraccion;
    int numberRandom;
    public String codigoVerifi;

    private LinearLayout lyInicio;
    private LinearLayout lyCategoria;
    private LinearLayout lyContacto;
    private LinearLayout lyFavoritos;
    private ImageView btnList;

    SharedPreferences share;
    SharedPreferences.Editor editor;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    Infraccion tm =new Infraccion();
    int runAudio = 0;

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
        lyInicio = view.findViewById(R.id.lyInicioInfra);
        lyCategoria = view.findViewById(R.id.lyCategoriaInfra);
        lyContacto = view.findViewById(R.id.lyContactoInfra);
        lyFavoritos = view.findViewById(R.id.lyFavoritosInfra);
        btnList = view.findViewById(R.id.btnListInfra);

        radioGarantia = view.findViewById(R.id.radioGarantia);
        radioGarantia1 = view.findViewById(R.id.radioGarantia1);

        txtDocReteInfra = view.findViewById(R.id.txtDocRetenido);
        txtClaveInfra = view.findViewById(R.id.txtClaveInfraccion);
        btnAgregarInfraccion = view.findViewById(R.id.imgAgregarInfraccion);
        btnGuardarInfraccion = view.findViewById(R.id.imgGuardarInfraccion);

        txtDescSalarios = view.findViewById(R.id.txtDescInfraccion);
        txtSalarios = view.findViewById(R.id.txtSalariosInfraccion);
        txtMontoInfraPagar = view.findViewById(R.id.lblMontoInfraccion);

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

        lyContacto.setOnClickListener(new View.OnClickListener() {
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
                if(txtClaveInfra.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"EL NÚMERO DE TARJETA ES NECESARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getClaveInfra();
                }
            }
        });

        btnGuardarInfraccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDocReteInfra.toString().isEmpty()){
                    Toast.makeText(getContext(),"DEBE AGREGAR UNA OBSERVACIÓN",Toast.LENGTH_SHORT).show();
                }else{
                    Random();
                    Toast.makeText(getContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    insertRegistroInfraccion();
                    eliminarDatos();
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


        //*************************** SE MUESTRA EL MAPA ****************************************//
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapInfraccion);
        mapFragment.getMapAsync(this);

        return view;
    }

    /******************GET A LA BD***********************************/
    public void getClaveInfra() {
        cargarDatos();
        claveInfraccion = txtClaveInfra.getText().toString();
        claveInfraccion = claveInfraccion.trim();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Tabulador?clave="+claveInfraccion)
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
                    final String myResponse = response.body().string();
                    MapaInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    resDescripcionClave = jObj.getString("Descripcion");
                                    resSalarios = jObj.getString("SalMinimos");
                                    txtDescSalarios.setText(resDescripcionClave);
                                    txtSalarios.setText(resSalarios);
                                    Log.i("HERE", ""+jObj);
                                    if(cargarInfoValor != 0){
                                        monto = Integer.parseInt(resSalarios);
                                        sumaSalarios = monto*salarioMinimo;
                                        valor = sumaSalarios+cargarInfoValor; //360
                                        guardarDatosInfra();
                                    }else{
                                        monto = Integer.parseInt(resSalarios);
                                        sumaSalarios = monto*salarioMinimo;
                                        valor = sumaSalarios; //360
                                        guardarDatosInfra();
                                    }
                                    txtClaveInfra.setText("");
                                    txtMontoInfraPagar.setText("$"+valor+" "+"MXN");
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroInfraccion() {
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
                .add("IdInfraccion", codigoVerifi)
                .add("Usuario", cargarInfoUsuario)
                .add("Latitud", lat_origen.toString())
                .add("Longitud", lon_origen.toString())
                .add("Fecha", fecha)
                .add("Hora", hora)
                .add("Garantia", resGarantia+" "+resGarantia1)
                .add("SalariosMinimos", resSalarios)
                .add("Condonacion", "0")
                .add("Pago", String.valueOf(valor))
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
                            alertaGPS();
                            Toast.makeText(getContext(), "DATO AGREGADO", Toast.LENGTH_SHORT).show();
                            radioGarantia.clearCheck();
                            radioGarantia1.clearCheck();
                            txtClaveInfra.setText("");
                            txtDescSalarios.setText("");
                            txtSalarios.setText("");
                            txtDocReteInfra.setText("");
                            txtMontoInfraPagar.setText("$ MXN");

                        }
                    });
                }

            }
        });
    }

    public void cargarDatos(){
        share = getActivity().getSharedPreferences("main",Context.MODE_PRIVATE);
        cargarInfoValor = share.getInt("ValorInfraccion",0);
        cargarInfoUsuario = share.getString("USER","");
    }

    private void eliminarDatos(){
        share = getActivity().getSharedPreferences("main",Context.MODE_PRIVATE);
        editor = share.edit();
        editor.remove("ValorInfraccion").commit();
    }

    private void guardarDatosInfra(){
        share = getActivity().getSharedPreferences("main",Context.MODE_PRIVATE);
        editor = share.edit();
        editor.putInt("ValorInfraccion",valor);
        editor.commit();
    }

    private void alertaGPS(){
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
    public void Random()
    {
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        codigoVerifi = String.valueOf(numberRandom);
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
                actul_posicion=true;
                return false;
            }
        });
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                aux_loc = location;

                if(actul_posicion) {
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

    public String mi_ubi(LatLng au){
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
            if(municipio != null){
                municipio = DirCalle.getLocality();
            }else{
                municipio = "SIN INFORMACIÓN";
            }

        } catch (IOException e){

            e.printStackTrace();
        }

        return address;
    }

    public void iniciar(Location location){
        tv_add.setText("");

        if(marker != null){
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

}
