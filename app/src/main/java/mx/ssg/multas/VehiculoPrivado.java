package mx.ssg.multas;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehiculoPrivado extends Fragment {
    ImageView btnList;

    public VehiculoPrivado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vehiculo_privado, container, false);
        /************************************** ACCIONES DE LA VISTA **************************************/
        btnList = root.findViewById(R.id.btnListVP);






        return  root;
    }

    public interface OnFragmentInteractionListener {
    }

    /******************GET A BAJA CALIFORNIA***********************************/
    public void getPlacaBJ() {
        noPlacaTC = txtNoPlacaTC.getText().toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/TarjetaCirculacion?noPlacaToken="+noPlacaTC)
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
                    VehiculoPrivado.this.getActivity().runOnUiThread(new Runnable() {
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
                                    noSerieTC = jObj.getString("serie");
                                    marca = jObj.getString("marca");
                                    subMarca = jObj.getString("version");
                                    modelo = jObj.getString("modelo");
                                    noMotorTC = jObj.getString("numMotor");
                                    nombreTC = jObj.getString("propietario");
                                    txtNoSerieTC.setText(noSerieTC);
                                    txtMarca.setText(marca);
                                    txtSubmarca.setText(subMarca);
                                    txtModelo.setText(modelo);
                                    txtNoMotorTC.setText(noMotorTC);
                                    txtNombreTC.setText(nombreTC);

                                   /* noTarjetaTC = jObj.getString("NoTarjeta");
                                    noSerieTC = jObj.getString("NoSerie");
                                    nombreTC = jObj.getString("NombreP");
                                    aPaternoTC = jObj.getString("ApellidoPP");
                                    aMaternoTC = jObj.getString("ApellidoMP");
                                    nombreR = jObj.getString("NombreU");
                                    aPaternoR = jObj.getString("ApellidoPU");
                                    aMaternoR = jObj.getString("ApellidoMU");
                                    noMotorTC = jObj.getString("NoMotor");
                                    marca = jObj.getString("Marca");
                                    subMarca = jObj.getString("SubMarca");
                                    modelo = jObj.getString("Modelo");
                                    color = jObj.getString("Color");
                                    municipio = jObj.getString("Municipio");
                                    localidad = jObj.getString("Localidad");
                                    resOrigen = jObj.getString("Origen");
                                    resServicio = jObj.getString("TipoServicio");
                                    resObservaciones = jObj.getString("Observaciones");
                                    txtNoTarjetaTC.setText(noTarjetaTC);
                                    txtApaternoTC.setText(aPaternoTC);
                                    txtAmaternoTC.setText(aMaternoTC);
                                    txtNombreR.setText(nombreR);
                                    txtApaternoR.setText(aPaternoR);
                                    txtAmaternoR.setText(aMaternoR);
                                    txtColor.setText(color);
                                    txtMunicipio.setText(municipio);
                                    txtLocalidad.setText(localidad);
                                    origen = "Nacional";
                                    servicio = "Privado";
                                    observaciones = "null";
                                    if(resOrigen.equals(origen)){
                                        rNacional = (RadioButton)radioNacionalidadTC.getChildAt(0);
                                        rNacional.setChecked(true);
                                    }else {
                                        rExtranjero = (RadioButton)radioNacionalidadTC.getChildAt(1);
                                        rExtranjero.setChecked(true);
                                    }*/
                                    /*if(resServicio.equals(servicio)){
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Privado"));
                                    }else{
                                        spinTipoServicio.setSelection(((ArrayAdapter<String>)spinTipoServicio.getAdapter()).getPosition("Publico"));
                                    }*/
                                    /*if(resObservaciones.equals(observaciones)){
                                        txtObservaciones.setText("SIN OBSERVACIONES");
                                    }else{
                                        resObservaciones = jObj.getString("Observaciones");
                                        txtObservaciones.setText(resObservaciones);
                                    }*/
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
