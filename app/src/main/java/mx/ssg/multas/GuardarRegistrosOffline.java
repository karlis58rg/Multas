package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.ssg.multas.SqLite.DataHelper;
import mx.ssg.multas.SqLite.TableLicenciaConducirOffline;
import mx.ssg.multas.SqLite.TableVehiculoOffline;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GuardarRegistrosOffline extends AppCompatActivity {

    TextView lblRespuesta;
    Button btnConsultarVehiculos,btnSubirInformacionVehiculos, btnConsultarLicencias,btnSubirInformacionLicencias,btnCheckWifi;
    /*VEHICULO*/
    String idInfraccion, placa, noSerie, marca, version, clase, tipo, modelo, combustible, cilindros, color, uso;
    String puertas, noMotor, propietario, direccion, colonia, localidad, estatus, observaciones, email;
    /*LICENCIA*/
    String idInfraccionLicencia, noLicencia, apellidoPL, apellidoML, nombreL, tipoCalle, calle, numero, coloniaLicencia, cp, municipio, estado, fechaExpedicion;
    String fechaVencimiento, tipoVigencia, tipoLecencia, rfc, homo, grupoSanguineo, requerimientosEspeciales, emailLicencia, observacionesLicencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_registros_offline);

        lblRespuesta= findViewById(R.id.lblRespuestaOffline);
        btnConsultarVehiculos = findViewById(R.id.btnConsultarInfoOffline);
        btnSubirInformacionVehiculos = findViewById(R.id.btnGuardarInfoOffline);
        btnConsultarLicencias = findViewById(R.id.btnConsultarInfoOfflineLicencias);
        btnSubirInformacionLicencias = findViewById(R.id.btnGuardarInfoOfflineLicencias);
        btnCheckWifi = findViewById(R.id.btnWifi);

        btnConsultarVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                ArrayList<String> list = dataHelperOffLine.getAllVehiculosOffline();
                if (list.size() > 0) {
                    System.out.println(dataHelperOffLine.getAllVehiculosOffline());
                    lblRespuesta.setText(""+dataHelperOffLine.getAllVehiculosOffline());
                }else{
                    Toast.makeText(getApplicationContext(), "NO TIENE REGISTROS A GUARDAR", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSubirInformacionVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                List<TableVehiculoOffline> list = dataHelperOffLine.getVehiculosAndInsertThem();
                for (int i = 0; i < list.size(); i++) {
                    TableVehiculoOffline infraccion = list.get(i);
                    idInfraccion = infraccion.IdInfraccion;
                    placa = infraccion.Placa;
                    noSerie = infraccion.NoSerie;
                    marca = infraccion.Marca;
                    version = infraccion.Version;
                    clase = infraccion.Clase;
                    tipo = infraccion.Tipo;
                    modelo = infraccion.Modelo;
                    combustible = infraccion.Combustible;
                    cilindros = infraccion.Cilindros;
                    color = infraccion.Color;
                    uso = infraccion.Uso;
                    puertas = infraccion.Puertas;
                    noMotor = infraccion.NoMotor;
                    propietario = infraccion.Propietario;
                    direccion = infraccion.Direccion;
                    colonia = infraccion.Colonia;
                    localidad = infraccion.Localidad;
                    estatus = infraccion.Estatus;
                    observaciones = infraccion.Observaciones;
                    email = infraccion.Email;
                    insertRegistroVehiculos();
                }
            }
        });

        btnConsultarLicencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                ArrayList<String> list = dataHelperOffLine.getAllLicenciaOffline();
                if (list.size() > 0) {
                    System.out.println(dataHelperOffLine.getAllLicenciaOffline());
                    lblRespuesta.setText(""+dataHelperOffLine.getAllLicenciaOffline());
                }else{
                    Toast.makeText(getApplicationContext(), "NO TIENE REGISTROS A GUARDAR", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSubirInformacionLicencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                List<TableLicenciaConducirOffline> list = dataHelperOffLine.getLicenciasAndInsertThem();
                for (int i = 0; i < list.size(); i++) {
                    TableLicenciaConducirOffline licenciaOff = list.get(i);
                    idInfraccionLicencia = licenciaOff.IdInfraccion;
                    noLicencia = licenciaOff.NoLicencia;
                    apellidoPL = licenciaOff.ApellidoPL;
                    apellidoML = licenciaOff.ApellidoML;
                    nombreL = licenciaOff.NombreL;
                    tipoCalle = licenciaOff.TipoCalle;
                    calle = licenciaOff.Calle;
                    numero = licenciaOff.Numero;
                    coloniaLicencia = licenciaOff.Colonia;
                    cp = licenciaOff.Cp;
                    municipio = licenciaOff.Municipio;
                    estado = licenciaOff.Estado;
                    fechaExpedicion = licenciaOff.FechaExpedicion;
                    fechaVencimiento = licenciaOff.FechaVencimiento;
                    tipoVigencia = licenciaOff.TipoVigencia;
                    tipoLecencia = licenciaOff.TipoLecencia;
                    rfc = licenciaOff.Rfc;
                    homo = licenciaOff.Homo;
                    grupoSanguineo = licenciaOff.GrupoSanguineo;
                    requerimientosEspeciales = licenciaOff.RequerimientosEspeciales;
                    emailLicencia = licenciaOff.Email;
                    observacionesLicencia = licenciaOff.Observaciones;
                    insertRegistroLicencias();
                }
            }
        });

        btnCheckWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                int downSpeed = nc.getLinkDownstreamBandwidthKbps();
                int upSpeed = nc.getLinkUpstreamBandwidthKbps();
                System.out.println("WIFI BAJADA"+" "+downSpeed);
                System.out.println("WIFI SUBIDA"+" "+upSpeed);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
                System.out.println("NIVEL WIFI"+" "+level);

            }
        });

    }
    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertRegistroVehiculos() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", idInfraccion)
                .add("Placa", placa)
                .add("NoSerie", noSerie)
                .add("Marca", marca)
                .add("Version", version)
                .add("Clase", clase)
                .add("Tipo", tipo)
                .add("Modelo", modelo)
                .add("Combustible", combustible)
                .add("Cilindros", cilindros)
                .add("Color", color)
                .add("Uso", uso)
                .add("Puertas", puertas)
                .add("NoMotor", noMotor)
                .add("Propietario", propietario)
                .add("Direccion", direccion)
                .add("Colonia", color)
                .add("Localidad", localidad)
                .add("Estatus", estatus)
                .add("Observaciones", observaciones)
                .add("Email", email)
                .build();
        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/VehiculosOffline/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR LOS REGISTROS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTENTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                Looper.loop(); //UN MOMENTO POR FAVOR, LOS REGISTROS SE ESTAN GUARDANDO
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Looper.prepare(); // to be able to make toast
                    Toast.makeText(getApplicationContext(), "LOS REGISRTROS SE HAN ENVIADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                    dataHelperOffLine.deleteAllVehiculosOffLine();
                    Looper.loop();
                    final String myResponse = response.body().toString();
                    System.out.println(myResponse);
                }
            }
        });
    }

    private void insertRegistroLicencias() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdInfraccion", idInfraccionLicencia)
                .add("NoLicencia", noLicencia)
                .add("ApellidoPL", apellidoPL)
                .add("ApellidoML", apellidoML)
                .add("NombreL", nombreL)
                .add("TipoCalle", tipoCalle)
                .add("Calle", calle)
                .add("Numero", numero)
                .add("Colonia", coloniaLicencia)
                .add("Cp", cp)
                .add("Municipio", municipio)
                .add("Estado", estado)
                .add("FechaExpedicion", fechaExpedicion)
                .add("FechaVencimiento", fechaVencimiento)
                .add("TipoVigencia", tipoVigencia)
                .add("TipoLecencia", tipoLecencia)
                .add("Rfc", rfc)
                .add("Homo", homo)
                .add("GrupoSanguineo", grupoSanguineo)
                .add("RequerimientosEspeciales", requerimientosEspeciales)
                .add("Email", emailLicencia)
                .add("Observaciones", observacionesLicencia)
                .build();
        Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/LicenciaOffline/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR LOS REGISTROS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTENTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                Looper.loop(); //UN MOMENTO POR FAVOR, LOS REGISTROS SE ESTAN GUARDANDO
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Looper.prepare(); // to be able to make toast
                    Toast.makeText(getApplicationContext(), "LOS REGISRTROS SE HAN ENVIADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    DataHelper dataHelperOffLine = new DataHelper(getApplicationContext());
                    dataHelperOffLine.deleteAllLicenciasOffLine();
                    Looper.loop();
                    final String myResponse = response.body().toString();
                    System.out.println(myResponse);
                }
            }
        });
    }
}
