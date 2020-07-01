package mx.ssg.multas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import mx.com.netpay.sdk.IPage;
import mx.com.netpay.sdk.SmartApi;
import mx.com.netpay.sdk.SmartApiFactory;
import mx.com.netpay.sdk.models.BaseResponse;
import mx.com.netpay.sdk.models.Constants;
import mx.com.netpay.sdk.models.PrintRequest;
import mx.com.netpay.sdk.models.ReprintRequest;
import mx.com.netpay.sdk.models.SaleRequest;

public class NetPay extends AppCompatActivity {
    EditText txtMonto,txtFolio,txtOrderId;
    TextView lblRespuesta;
    Button btnVenta,btnImpresion,btnReimpresion;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pay);

        txtMonto = findViewById(R.id.txtMonto);
        txtFolio = findViewById(R.id.txtFolio);
        txtOrderId = findViewById(R.id.txtOrderId);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        btnVenta = findViewById(R.id.btnVenta);
        btnReimpresion = findViewById(R.id.btnReimpresion);
        btnImpresion = findViewById(R.id.btnImpresión);

        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount =Double.parseDouble(txtMonto.getText().toString());
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
                //Crear una página
                IPage page = smartApi.createPage();

                //Crear unidad que contiene texto y otros formatos
                IPage.ILine.IUnit unit1 = page.createUnit();
                unit1.setText("CONCEPTOS");
                unit1.setGravity(Gravity.START);

                //Se pueden agregar 2 o más unidades a una línea y se dividirá en columnas
                IPage.ILine.IUnit unit2 = page.createUnit();
                unit2.setText("MANEJAR EN ESTADO DE EBRIEDAD\n" + "DAR VUELTA EN LUGAR NO PERMITIDO");
                unit2.setGravity(Gravity.END);

                //Se crea una línea y se agregan sus unidades.
                page.addLine().
                        addUnit(unit1).
                        addUnit(unit2);

                //Se crea una nueva unidad
                IPage.ILine.IUnit unit3 = page.createUnit();
                unit3.setText("$3000.00");
                unit3.setGravity(Gravity.CENTER);

                //Se crea una nueva línea y se agrega la unidad pasada
                page.addLine().addUnit(unit3);

                //Se crea un request del tipo PrintRequest con el package name del app y la página creada
                PrintRequest printRequest = new PrintRequest(appId, page);

                smartApi.doTrans(printRequest);
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
}
