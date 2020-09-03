package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TransporteGeneral extends AppCompatActivity {

    private LinearLayout btnReglamento,btnLugaresPago,btnContactos,btnTabulador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte_general);



        btnReglamento = findViewById(R.id.lyInicio4);
        btnLugaresPago = findViewById(R.id.lyCategoria4);
        btnContactos = findViewById(R.id.lyContacto4);
        btnTabulador = findViewById(R.id.lyFavoritos4);




        btnReglamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, ViewPDFController.class);
                startActivity(i);
                finish();
            }
        });
        btnLugaresPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, Contactos.class);
                startActivity(i);
                finish();
            }
        });
        btnTabulador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransporteGeneral.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });

    }
}
