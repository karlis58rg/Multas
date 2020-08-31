package mx.ssg.multas;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class VehiculoPublico extends Fragment {
    EditText txtPlacaTp,txtEconomico,txtEstatusActual,txtFechaExTp,txtFechaVigTp,txtPropietario,txtTenedorUsuario,txtOficinaExpedTp,txtDelegacionTp,txtNRepuveTp,txtMarcaTp,txtLineaTp,txtVersionTp;
    EditText txtClaseTipoTp,txtColorTp,txtModeloTp,txtPuertasTp,txtCilindrosTp,txtCombustibleTp,txtCapacidadTp,txtAgrupacionTp,txtNSerieTp,txtRegistroPropTp;
    EditText txtRutaSitioTp,txtPermisionarioTp,txtNMotorTp,txtUsoTp,txtObservacionesTp,txtFolioSCTTp,txtUrlTp,txtEmailTp,txtNotasTp;

    String Placa,Economico,EstatusActual,FechaExTp,FechaVigTp,Propietario,TenedorUsuario,OficinaExpedTp,DelegacionTp,NRepuveTp,MarcaTp,LineaTp,VersionTp,ClaseTipoTp,ColorTp,ModeloTp;
    String PuertasTp,CilindrosTp,CombustibleTp,CapacidadTp,AgrupacionTp,NSerieTp,RegistroPropTp,RutaSitioTp,PermisionarioTp,NMotorTp,UsoTp,ObservacionesTp;
    String FolioSCTTp,UrlTp,EmailTp,NotasTp,respuestaJson;

    ImageView btnBuscarPlaca;

    public VehiculoPublico() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vehiculo_publico, container, false);
        /************************************** ACCIONES DE LA VISTA **************************************/
        txtPlacaTp = root.findViewById(R.id.txtPlacaTp);
        txtEconomico = root.findViewById(R.id.txtEconomico);
        txtEstatusActual = root.findViewById(R.id.txtEstatusActual);
        txtFechaExTp = root.findViewById(R.id.txtFechaExTp);
        txtFechaVigTp = root.findViewById(R.id.txtFechaVigTp);
        txtPropietario = root.findViewById(R.id.txtPropietario);
        txtTenedorUsuario = root.findViewById(R.id.txtTenedorUsuario);
        txtOficinaExpedTp = root.findViewById(R.id.txtOficinaExpedTp);
        txtDelegacionTp = root.findViewById(R.id.txtDelegacionTp);
        txtNRepuveTp = root.findViewById(R.id.txtNRepuveTp);
        txtMarcaTp = root.findViewById(R.id.txtMarcaTp);
        txtLineaTp = root.findViewById(R.id.txtLineaTp);
        txtVersionTp = root.findViewById(R.id.txtVersionTp);
        txtClaseTipoTp = root.findViewById(R.id.txtClaseTipoTp);
        txtColorTp = root.findViewById(R.id.txtColorTp);
        txtModeloTp = root.findViewById(R.id.txtModeloTp);
        txtPuertasTp = root.findViewById(R.id.txtPuertasTp);
        txtCilindrosTp = root.findViewById(R.id.txtCilindrosTp);
        txtCombustibleTp = root.findViewById(R.id.txtCombustibleTp);
        txtCapacidadTp = root.findViewById(R.id.txtCapacidadTp);
        txtAgrupacionTp = root.findViewById(R.id.txtAgrupacionTp);
        txtNSerieTp = root.findViewById(R.id.txtNSerieTp);
        txtRegistroPropTp = root.findViewById(R.id.txtRegistroPropTp);
        txtRutaSitioTp = root.findViewById(R.id.txtRutaSitioTp);
        txtPermisionarioTp = root.findViewById(R.id.txtPermisionarioTp);
        txtNMotorTp = root.findViewById(R.id.txtNMotorTp);
        txtUsoTp = root.findViewById(R.id.txtUsoTp);
        txtObservacionesTp = root.findViewById(R.id.txtObservacionesTp);
        txtFolioSCTTp = root.findViewById(R.id.txtFolioSCTTp);
        txtUrlTp = root.findViewById(R.id.txtUrlTp);
        txtEmailTp = root.findViewById(R.id.txtEmailTp);
        txtNotasTp = root.findViewById(R.id.txtNotasTp);

        btnBuscarPlaca = root.findViewById(R.id.imgBuscarNoPlacaTP);

        btnBuscarPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPlacaTp.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"DEBE AGREGAR ALGÚN COMENTARIO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                    getPlacaTPBJ();
                }
            }
        });



        return root;
    }

    public interface OnFragmentInteractionListener {
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
                Toast.makeText(getContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    VehiculoPublico.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                String valorNull = " ";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getContext(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else {
                                    JSONObject jObj = null;
                                    String resObj = myResponse;
                                    resObj = resObj.replace("[", " ");
                                    resObj = resObj.replace("]", " ");

                                    jObj = new JSONObject("" + resObj + "");
                                    if (jObj.equals(valorNull)) {
                                        Toast.makeText(getContext(), "ESTA PLACA NO CONTIENE INFORMACIÓN", Toast.LENGTH_SHORT).show();
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
