package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mx.com.netpay.sdk.SmartApi;
import mx.com.netpay.sdk.SmartApiFactory;
import mx.com.netpay.sdk.models.ReprintRequest;

public class NetPay extends AppCompatActivity {
    EditText txtMonto,txtFolio,txtOrderId;
    TextView lblRespuesta;
    Button btnImpresion;
    String appId = "mx.hgo.reglamento";
    String print;
    private SmartApi smartApi = SmartApiFactory.INSTANCE.createSmartApi(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pay);

        txtMonto = findViewById(R.id.txtMonto);
        txtFolio = findViewById(R.id.txtFolio);
        txtOrderId = findViewById(R.id.txtOrderId);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        btnImpresion = findViewById(R.id.btnImpresión);

        btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print = txtOrderId.getText().toString();
                ReprintRequest sale = new ReprintRequest(appId,print);
                try {
                    smartApi.doTrans(sale);
                }catch (Exception e){
                    System.out.println(e);
                }

            }
        });
    }
}
