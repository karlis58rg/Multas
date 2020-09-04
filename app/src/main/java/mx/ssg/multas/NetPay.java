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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetPay extends AppCompatActivity {
    EditText txtMonto,txtFolio,txtOrderId;
    TextView lblRespuesta,lblRespuestaCadena;
    Button btnVenta,btnImpresion,btnReimpresion;
    String respuestaJson;
    String appId = "mx.ssg.multas";
    String orderId;
    Double amount;
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
    public int countResultado;
    public String nombre;
    public String estado;
    public String municipio;
    public String tipoLic;
    public String expedicion;
    public String vencimiento;
    public String observaciones;
    String cargarInfoUser;
    SharedPreferences share;
    public String nombreAgente;
    public String cargo;
    public String descInfracciones;
    public String umas;
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
        //amount = i1.getDoubleExtra("MONTO",0);
        amount = 1.0;
        folio = i1.getStringExtra("FOLIO");
        direccionInfraccion = i1.getStringExtra("DIRECCION");
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
        super.onActivityResult(requestCode, resultCode, data);
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
                                String[] textElements = resp.split(",");
                                List<String> qrlList = Arrays.asList(textElements);
                                countResultado = qrlList.size();
                                nombreAgente = textElements[0];
                                cargo = textElements[1];
                                System.out.println(nombreAgente+ "\n"+cargo);
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
                                String[] textElements = resp.split(",");
                                List<String> qrlList = Arrays.asList(textElements);
                                countResultado = qrlList.size();
                                if(countResultado > 3){
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
        unit1.setText(" IMOS \n INSTITUTO DE MOVILIDAD SUSTENTABLE \n GOBIERNO DE BAJA CALIFORNIA \n\n\n REGLAMENTO DE TRANSPORTE PUBLICO PARA EL MUNICIPIO DE TIJUANA. \n PUBLICADO EN EL PERIÓDICO OFICIAL NO. 23, DE FECHA 31 DE MAYO DE 2002, TOMO CIX. \n \n"+"FECHA EXPECIDIÓN: "+fecha+" \n"+"HORA: "+hora);
        unit1.setGravity(Gravity.CENTER);
        unit1.setTextStyle(1);
        page.addLine().addUnit(unit1);

        //Se pueden agregar 2 o más unidades a una línea y se dividirá en columnas
        IPage.ILine.IUnit unit2 = page.createUnit();
        unit2.setText("\n"+"Direcciòn: "+direccionInfraccion+"\n \n"+"No.INFRACCIÓN:"+folio+"\n \n \n" + "DATOS DEL INFRACTOR" + "\n \n" +"NOMBRE: " +nombre + "\n" + "ESTADO: "+estado+"\n"+"MUNICIPIO: "+municipio+"\n"+"TIPO LICENCIA: "+tipoLic+"\n"+"EXPEDICIÓN: "+expedicion+"\n"+"VENCIMIENTO: "+vencimiento+"\n"+"OBSERVACIONES: "+observaciones+"\n \n FIRMA ___________________"+"\n\n");
        unit2.setGravity(Gravity.CENTER);
        //Se crea una línea y se agregan sus unidades.
        page.addLine().addUnit(unit2);

        IPage.ILine.IUnit unit3 = page.createUnit();
        unit3.setText("DATOS DEL AGENTE"+"\n\n"+"NOMBRE: "+nombreAgente+"\n"+"CARGO: "+cargo+"\n\n"+"FIRMA ___________________"+"\n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit3.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit3);

        IPage.ILine.IUnit unit4 = page.createUnit();
        unit4.setText("INFRACCIONES"+"\n\n"+descInfracciones+"\n\n"+umas+"\n"+"VALOR DE LA UMA $86.00"+"\n"+total+","+totalComplemento+"\n\n"+"LUGARES DE PAGO"+"\n\n"+"PODRÁS ACUDIR A REALIZAR TU PAGO A LAS CAJAS DE IMOS EN EL MUNICIPIO DE LA INFRACCIÓN DESPUÉS DE 24 HORAS DEL LEVANTAMIENTO DE LA INFRACCIÓN, PODRÁS REALIZAR TU PAGO CON TARJETA DE CRÉDITO O DÉBITO Y EN EFECTIVO O IMOS PONE A TU SERVICIO EL PAGO EN LÍNEA A TRAVÉS DE LA PÁGINA HTTPS://IMOS.GOB.MX/SERVICIOENLINEA \n\n");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit4.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit4);

        IPage.ILine.IUnit unit5 = page.createUnit();
        unit5.setText("TERMINOS Y CONDICIONES INSTITUTO DE MOVILIDAD SUSTENTABLE");  //TRAERME LA DIRECCIÒN DE LA VISTA MAPA INFRACCIÓN. SU LUGAR ES ANTES DEL FOLIO
        unit5.setGravity(Gravity.CENTER);
        //Se crea una nueva línea y se agrega la unidad pasada
        page.addLine().addUnit(unit5);

       /* IPage.ILine.IUnit unit4 = page.createUnit();
        unit4.setText("TECAJETE 240, PITAHAYAS, PACHUCA DE SOTO");
        unit4.setGravity(Gravity.CENTER);*/
        //Se crea una nueva línea y se agrega la unidad pasada
        /*page.addLine().addUnit(unit4);*/
        /*
        *         page.addLine().addUnit(page.createUnit().apply {
            text = ""
            bitmap = logoBitmap()
            gravity = Gravity.CENTER
        })
    }*/

       /* IPage.ILine.IUnit unit4 = page.createUnit();
        unit4.setText(valorCadenaImpresion);
        unit4.getBitmap();
        unit4.setGravity(Gravity.END);
        page.addLine().
                addUnit(unit4);*/

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
