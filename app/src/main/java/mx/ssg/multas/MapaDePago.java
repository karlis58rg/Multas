package mx.ssg.multas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapaDePago.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapaDePago#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapaDePago extends Fragment implements OnMapReadyCallback {
    private OnFragmentInteractionListener mListener;
    GoogleMap map;
    Button btn_address;
    Boolean actul_posicion = true;
    Marker marker = null;
    Double lat_origen, lon_origen;
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

    private LinearLayout lyInicio;
    private LinearLayout lyContacto;
    private LinearLayout lyFavoritos;
    private ImageView btnList;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    LugaresDePago tm =new LugaresDePago();
    int runAudio = 0;

    public MapaDePago() {
        // Required empty public constructor
    }

    public static MapaDePago newInstance(String param1, String param2) {
        MapaDePago fragment = new MapaDePago();
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
        final View view = inflater.inflate(R.layout.fragment_mapa_de_pago, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        lyInicio = view.findViewById(R.id.lyInicio1);
        lyContacto = view.findViewById(R.id.lyContacto1);
        lyFavoritos = view.findViewById(R.id.lyFavoritos1);
        btnList = view.findViewById(R.id.btnList1);

        lyInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewPDFController.class);
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

        //*************************** SE MUESTRA EL MAPA ****************************************//
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        tv_add = (TextView) getActivity().findViewById(R.id.tv_miadres);
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
