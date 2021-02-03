package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mx.com.netpay.sdk.IPage;
import mx.com.netpay.sdk.SmartApi;
import mx.com.netpay.sdk.SmartApiFactory;
import mx.com.netpay.sdk.models.BaseResponse;
import mx.com.netpay.sdk.models.Constants;
import mx.com.netpay.sdk.models.PrintRequest;
import mx.com.netpay.sdk.models.SaleRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetPay extends AppCompatActivity {
    EditText txtMonto,txtFolio,txtOrderId;
    TextView lblRespuesta,lblRespuestaCadena;
    Button btnVenta,btnImpresion,btnReimpresion;
    String respuestaJson;
    String appId = "mx.ssg.multas";
    String orderId;
    Float amount = 0.0f;
    Double tip;
    int msi;
    int waiter;
    int settlementId;
    String settlementDate;
    String table;
    String folio;
    Boolean checkIn = false;
    Boolean tableId = false;
    String additionalData;
    String traceability;
    String exchangeRateUsd;
    String pendingAmount;
    BaseResponse response;
    private SmartApi smartApi = SmartApiFactory.INSTANCE.createSmartApi(this);
    public String cadenaImpresion;
    public String resp;
    public String valorCadenaImpresion = "";
    public String imgString;
    String respCadena;
    String fecha,hora;
    private ArrayList<String> resDescripcion;
    private ArrayList<String> resSalario;
    public String direccionInfraccion;
    public String contrasenia;
    public int countResultado;
    public String nombre = " ";
    public String estado = " ";
    public String municipio = " ";
    public String tipoLic = " ";
    public String expedicion = " ";
    public String vencimiento = " ";
    public String observaciones = "SIN OBSERVACIONES";
    String cargarInfoUser;
    SharedPreferences share;
    public String nombreAgente;
    public String noEmpleado;
    public String descInfracciones = " ";
    public String umas = " ";
    public String total,totalComplemento = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pay);

        cargarDatos();

        txtMonto = findViewById(R.id.txtMonto);
        txtMonto.setFocusableInTouchMode(true); txtMonto.requestFocus();
        txtFolio = findViewById(R.id.txtFolio);
        //txtOrderId = findViewById(R.id.txtOrderId);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        //lblRespuestaCadena = findViewById(R.id.lblRespuestaCadena);
        btnVenta = findViewById(R.id.btnVenta);
        //btnReimpresion = findViewById(R.id.btnReimpresion);
        btnImpresion = findViewById(R.id.btnImpresion);

        /*********** PARAMETROS PROVINIENTES DE LA INFRACCION **********/
        Intent i1 = getIntent();
        amount = i1.getFloatExtra("MONTO",0.0f);
        //amount = 1.0;
        folio = i1.getStringExtra("FOLIO");
        direccionInfraccion = i1.getStringExtra("DIRECCION");
        contrasenia = i1.getStringExtra("CONTRASENIA");
        String monto = Double.toString(amount);
        txtMonto.setText(monto);
        txtFolio.setText(folio);
        txtMonto.setEnabled(false);
        txtFolio.setEnabled(false);

        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleRequest sale = new SaleRequest(appId,amount,tip,msi,waiter,settlementId,settlementDate,table,folio,checkIn,tableId,additionalData,traceability,exchangeRateUsd,pendingAmount);
                try {
                    smartApi.doTrans(sale);
                }catch(Exception e){
                    lblRespuesta.setText(e.getMessage());
                    System.out.println(e);
                }
            }
        });

       /* btnReimpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = txtOrderId.getText().toString();
                ReprintRequest sale = new ReprintRequest(appId,orderId);
                try {
                    smartApi.doTrans(sale);
                }catch (Exception e){
                    lblRespuesta.setText(e.getMessage());
                    System.out.println(e);
                }
            }
        });*/

        btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cadenaInfraccion();
                Toast.makeText(getApplicationContext(),"UN MOMENTO POR FAVOR",Toast.LENGTH_SHORT).show();
                getCadenaImpresion();
                getSendEmail();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Gson gson = new Gson();
        if(data != null){
            if(requestCode == Constants.SALE_REQUEST){
                response = smartApi.onResult(requestCode,resultCode,data);
                lblRespuesta.setText(gson.toJson(response));
            }else if(requestCode == Constants.CANCEL_REQUEST){
                response = smartApi.onResult(requestCode,resultCode,data);
                lblRespuesta.setText(gson.toJson(response));
            }else if(requestCode == Constants.REPRINT_REQUEST){
                response = smartApi.onResult(requestCode,resultCode,data);
                lblRespuesta.setText(gson.toJson(response));
            }else if(requestCode == Constants.PRINT_REQUEST){
                response = smartApi.onResult(requestCode,resultCode,data);
                lblRespuesta.setText(gson.toJson(response));
            }else {
                lblRespuesta.setText("OPCION NO DISPONIBLE");
            }
        }
        putPagoStatus();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /******************GET A LA BD***********************************/
    public void putPagoStatus() {
        cargarDatos();
        int statusPago = 2;
        final OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("status", "2")
                .build();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Infracciones?folioUpdate="+folio+"&status="+statusPago)
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"ERROR AL ENVIAR LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    String resp = myResponse;
                    Log.i("HERE", resp);
                }
            }

        });
    }

    /******************GET A LA BD***********************************/
    public void getSendEmail() {
        cargarDatos();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Email?idExistente="+folio)
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
                    String resp = myResponse;
                    Log.i("HERE", resp);
                }
            }

        });
    }


    /******************GET A LA BD***********************************/
    private void getCadenaImpresion() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/DatosLicencia?idInfraccionLicencia="+folio)
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
                    NetPay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resp = myResponse;
                            String valorRfc = "SIN INFORMACION";
                            if(resp.equals(valorRfc)){
                                Toast.makeText(getApplicationContext(), "NO SE ENCONTRO INFORMACIÓN ", Toast.LENGTH_SHORT).show();
                            }else{
                                resp = resp.replace('"',' ');
                                resp = resp.trim();
                                /*********SEPARADO POR COMAS**************/
                                String[] textElements = resp.split(",");
                                List<String> qrlList = Arrays.asList(textElements);
                                countResultado = qrlList.size();
                                nombre = textElements[1];
                                estado = textElements[2];
                                municipio = textElements[3];
                                tipoLic = textElements[4];
                                expedicion = textElements[5];
                                vencimiento = textElements[6];
                                observaciones = textElements[7];
                                //cadenaImpresion = resp;
                                //cadenaImpresion = cadenaImpresion.replace('\n','\n');
                                System.out.println(nombre+ "\n"+estado);
                                getAgente();
                            }
                            Log.i("HERE", resp);
                        }
                    });

                }
            }

        });
    }

    /******************GET A LA BD***********************************/
    private void getAgente() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Agente?usuario="+cargarInfoUser)
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
                    NetPay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resp = myResponse;
                            String valorRfc = "SIN INFORMACION";
                            if(resp.equals(valorRfc)){
                                Toast.makeText(getApplicationContext(), "NO SE ENCONTRO INFORMACIÓN ", Toast.LENGTH_SHORT).show();
                            }else{
                                resp = resp.replace('"',' ');
                                resp = resp.trim();
                                /*********SEPARADO POR COMAS**************/
                                String[] textElements = resp.split(",");
                                List<String> qrlList = Arrays.asList(textElements);
                                countResultado = qrlList.size();
                                nombreAgente = textElements[0];
                                noEmpleado = textElements[1];
                                System.out.println(nombreAgente+ "\n"+noEmpleado);
                                getCadenaLicencia();
                            }
                            Log.i("HERE", resp);
                        }
                    });

                }
            }

        });
    }


    /**************************************************************************************************************************************/
    /******************GET A LA BD***********************************/
    private void getCadenaLicencia() {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://187.174.102.142/AppTransito/api/Infracciones?infraccion="+folio)
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
                    NetPay.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resp = myResponse;
                            String valorRfc = "SIN INFORMACION";
                            if(resp.equals(valorRfc)){
                                Toast.makeText(getApplicationContext(), "NO SE ENCONTRO INFORMACIÓN ", Toast.LENGTH_SHORT).show();
                            }else{
                                resp = resp.replace('"',' ');
                                resp = resp.trim();
                                String[] textElements = resp.split(",");
                                List<String> qrlList = Arrays.asList(textElements);
                                countResultado = qrlList.size();
                                if(countResultado > 3){
                                    /***********SEPARADO POR COMAS**************/
                                    descInfracciones = textElements[0];
                                    umas = textElements[1];
                                    total = textElements[2];
                                    totalComplemento = textElements[3];
                                    cadenaInfraccion();
                                }else{
                                    descInfracciones = textElements[0];
                                    umas = textElements[1];
                                    total = textElements[2];
                                    System.out.println(descInfracciones);
                                    cadenaInfraccion();
                                }

                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }

        });
    }



    private void cadenaInfraccion(){
        //valorCadenaImpresion = txtFolio.getText().toString();
        //Crear una página
        //*************** FECHA **********************//
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);

        //*************** HORA **********************//
        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        hora = timeFormat.format(time);


        IPage page = smartApi.createPage();


        //Crear unidad que contiene texto y otros formatos
        IPage.ILine.IUnit unit1 = page.createUnit();
        unit1.setText(" IMOS \n INSTITUTO DE MOVILIDAD SUSTENTABLE \n Del Gobierno del Estado de Baja California \n\n\n Reglamento de Transporte Publico para El Municipio De Tijuana, BC, aplicado de manera supletoria tal y como lo establece el Décimo Séptimo Transitorio de la Ley de Movilidad\n" +
                "Sustentable y de Transporte del Estado de Baja California\n \n \n"+"Fecha de Emisión: "+fecha+" \n"+"Hora: "+hora);
        unit1.setGravity(Gravity.CENTER);
        unit1.setTextStyle(1);
        page.addLine().addUnit(unit1);

        //Se pueden agregar 2 o más unidades a una línea y se dividirá en columnas
        IPage.ILine.IUnit unit2 = page.createUnit();
        unit2.setText("\n"+"Direcciòn: "+direccionInfraccion+"\n \n"+"No.Infracción: "+folio+"\n"+"Contraseña Web: "+contrasenia+"\n \n \n" + "Datos del Infractor" + "\n");
        unit2.setGravity(Gravity.CENTER);
        //Se crea una línea y se agregan sus unidades.
        page.addLine().addUnit(unit2);

        //Se pueden agregar 2 o más unidades a una línea y se dividirá en columnas
        IPage.ILine.IUnit unit3 = page.createUnit();
        unit3.setText("Nombre: " +nombre + "\n" + "Estado: "+estado+"\n"+"Municipio: "+municipio+"\n"+"Tipo de licencia: "+tipoLic+"\n"+"Expedición: "+expedicion+"\n"+"Vencimiento: "+vencimiento+"\n"+"Observaciones: "+observaciones+"\n\n");
        unit3.setGravity(Gravity.LEFT);
        //Se crea una línea y se agregan sus unidades.
        page.addLine().addUnit(unit3);

        IPage.ILine.IUnit unit4 = page.createUnit();
        unit4.setText("Firma del Infractor \n\n\n ____________________ \n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit4.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit4);

        IPage.ILine.IUnit unit5 = page.createUnit();
        unit5.setText("Datos del Inspector de Movilidad"+"\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit5.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit5);

        IPage.ILine.IUnit unit6 = page.createUnit();
        unit6.setText("El que suscribe la presente Boleta de Infracción el  C. "+nombreAgente+", Inspector de movilidad, del Instituto de Movilidad Sustentable del Estado de Baja California, facultades conferidas mediante nombramiento expedido por el Director General del Instituto en fecha "+fecha+" quien se identificó con el infractor con credencial institucional expedida y otorgada por el Instituto, con número de empleado "+noEmpleado+" la cual cuenta con nombre y fotografía del suscrito. \n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit6.setGravity(Gravity.LEFT);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit6);

        IPage.ILine.IUnit unit7 = page.createUnit();
        unit7.setText("Firma del Inspector \n\n\n ____________________ \n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit7.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit7);

        IPage.ILine.IUnit unit8 = page.createUnit();
        unit8.setText("CONDUCTA:"+"\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit8.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit8);

        IPage.ILine.IUnit unit9 = page.createUnit();
        unit9.setText(descInfracciones+"\n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit9.setGravity(Gravity.LEFT);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit9);

        IPage.ILine.IUnit unit10 = page.createUnit();
        unit10.setText(umas+"\n\n"+"VALOR DE LA UMA ACTUAL $86.88"+"\n\n"+total+","+totalComplemento+"\n\n"+"Lugares de Pago\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit10.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit10);

        IPage.ILine.IUnit unit11 = page.createUnit();
        unit11.setText("Podrás realizar pago de la infracción en las instalaciones del instituto en los horarios de atención o a través del portal oficial: www.imos.gob.mx/inspeccion \n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit11.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit11);

        IPage.ILine.IUnit unit12 = page.createUnit();
        unit12.setText("Métodos de Pago: \n\n El infractor reconoce la falta cometida y acepta hacer el pago inmediato de la sanción en el dispositivo móvil a cargo del Inspector de Movilidad mediante pago electrónico con tarjeta de crédito o debito \n\n\n Firma de Conformidad \n\n\n____________________ \n\n\n No se puede hacer pago en efectivo por este medio móvil" );  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit12.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit12);

        //Se crea un request del tipo PrintRequest con el package name del app y la página creada
        PrintRequest printRequest = new PrintRequest(appId, page);
        smartApi.doTrans(printRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NetPay.this,Reglamento.class);
        startActivity(i);
    }

    private Bitmap logoBitmap(){
        Bitmap bitLogo = BitmapFactory.decodeResource(getResources(),R.drawable.logo_ticket);
        return bitLogo;
        //val myDrawable = getDrawable(R.drawable.logo_ticket)
        //return (myDrawable as BitmapDrawable).bitmap
    }


    private Bitmap logo() {
        Bitmap bitLogo = BitmapFactory.decodeResource(getResources(),R.drawable.logo_ticket);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitLogo.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] imgBytes = baos.toByteArray();
        imgString = android.util.Base64.encodeToString(imgBytes, android.util.Base64.NO_WRAP);
        //cadena = imgString;

        imgBytes = android.util.Base64.decode(imgString, android.util.Base64.NO_WRAP);
        Bitmap decoded = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        System.out.print("IMAGEN" + decoded);
        return decoded;
    }

    public void cargarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        cargarInfoUser = share.getString("USER","");
    }

}
