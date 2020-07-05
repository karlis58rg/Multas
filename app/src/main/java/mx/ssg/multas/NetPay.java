package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.com.netpay.sdk.IPage;
import mx.com.netpay.sdk.SmartApi;
import mx.com.netpay.sdk.SmartApiFactory;
import mx.com.netpay.sdk.models.BaseResponse;
import mx.com.netpay.sdk.models.Constants;
import mx.com.netpay.sdk.models.PrintRequest;
import mx.com.netpay.sdk.models.ReprintRequest;
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
    SharedPreferences share;
    SharedPreferences.Editor editor;
    public String valorCadenaImpresion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pay);

        txtMonto = findViewById(R.id.txtMonto);
        txtFolio = findViewById(R.id.txtFolio);
        txtOrderId = findViewById(R.id.txtOrderId);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        lblRespuestaCadena = findViewById(R.id.lblRespuestaCadena);
        btnVenta = findViewById(R.id.btnVenta);
        btnReimpresion = findViewById(R.id.btnReimpresion);
        btnImpresion = findViewById(R.id.btnImpresión);

        /*********** PARAMETROS PROVINIENTES DE LA INFRACCION **********/
        Intent i1 = getIntent();
        amount = i1.getDoubleExtra("MONTO",0);
        folio = i1.getStringExtra("FOLIO");
        String monto = Double.toString(amount);
        txtMonto.setText(monto);
        txtFolio.setText(folio);

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

        btnReimpresion.setOnClickListener(new View.OnClickListener() {
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
        });

        btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                .url("http://187.174.102.142/AppTransito/api/TempInfraccion?infraccion="+folio)
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
                            //myResponse = myResponse.replace('"',' ');
                            //myResponse = myResponse.trim();
                            resp = myResponse;
                            String valorRfc = "NO SE ENCONTRO INFORMACION";
                            if(resp.equals(valorRfc)){
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS " + resp, Toast.LENGTH_SHORT).show();

                            }else{
                                cadenaImpresion = resp;
                                lblRespuestaCadena.setText(cadenaImpresion);
                                //cadenaImpresion = cadenaImpresion.replace('\n','\n');
                                System.out.println(cadenaImpresion);
                                cadenaInfraccion();
                            }
                            Log.i("HERE", resp);
                        }
                    });

                }
            }

        });
    }

    private void cadenaInfraccion(){
        valorCadenaImpresion = lblRespuestaCadena.getText().toString();
        //Crear una página
        IPage page = smartApi.createPage();

        String cadena1 = cadenaImpresion;
        String cadena = cadena1;
        System.out.println(cadena);

        //Crear unidad que contiene texto y otros formatos
        IPage.ILine.IUnit unit1 = page.createUnit();
        unit1.setText("INFRACCION");
        unit1.setGravity(Gravity.START);

        //Se pueden agregar 2 o más unidades a una línea y se dividirá en columnas
        IPage.ILine.IUnit unit2 = page.createUnit();
        unit2.setText(valorCadenaImpresion);
        unit2.setGravity(Gravity.END);

        //Se crea una línea y se agregan sus unidades.
        page.addLine().
                addUnit(unit1).
                addUnit(unit2);

        //Se crea una nueva unidad
        //IPage.ILine.IUnit unit3 = page.createUnit();
        //unit3.setText("Costo: $3000.00");
        //unit3.setGravity(Gravity.CENTER);

        //Se crea una nueva línea y se agrega la unidad pasada
        //page.addLine().addUnit(unit3);
        /*
        *         page.addLine().addUnit(page.createUnit().apply {
            text = ""
            bitmap = logoBitmap()
            gravity = Gravity.CENTER
        })
    }*/

        IPage.ILine.IUnit unit4 = page.createUnit();
        unit4.setText(valorCadenaImpresion);
        unit4.getBitmap();
        unit4.setGravity(Gravity.END);
        page.addLine().
                addUnit(unit4);






        //Se crea un request del tipo PrintRequest con el package name del app y la página creada
        PrintRequest printRequest = new PrintRequest(appId, page);
        smartApi.doTrans(printRequest);
    }
}
