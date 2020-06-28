package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TarjetasConductor extends AppCompatActivity {
    private ImageView btnList,btnLicenciaC,btnTarjetaC;
    private LinearLayout btnReglamentoTC,btnLugaresPagoTC,btnContactoTC,btnTabuladorTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas_conductor);

        btnLicenciaC = findViewById(R.id.imgLicenciaConducir);
        btnTarjetaC = findViewById(R.id.imgTarjetaCirculacon);

        btnList = findViewById(R.id.btnListTarjetas);
        btnReglamentoTC = findViewById(R.id.lyInicio);
        btnLugaresPagoTC = findViewById(R.id.lyCategoria);
        btnContactoTC = findViewById(R.id.lyContacto);
        btnTabuladorTC = findViewById(R.id.lyFavoritos);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Reglamento.class);
                startActivity(i);
                finish();
            }
        });

        btnLicenciaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, LicenciaConducir.class);
                startActivity(i);
                finish();
            }
        });

        btnTarjetaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, TarjetaCirculacion.class);
                startActivity(i);
                finish();

            }
        });

        btnReglamentoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });

        btnLugaresPagoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, LugaresDePago.class);
                startActivity(i);
                finish();

            }
        });

        btnContactoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Contactos.class);
                startActivity(i);
                finish();

            }
        });

        btnTabuladorTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TarjetasConductor.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });
    }
}
