package mx.ssg.multas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import mx.ssg.multas.SqLite.DataHelper;

public class VehiculosOffline extends AppCompatActivity {

    ImageView btnVistaPOff,btnInfraccionOff,btnListTCOff;
    Button btnGuardarOff;

    EditText txtNoPlacaVGOff,txtSerieVGOff,txtMarcaVGOff,txtVersionVGOff,txtClaseVGOff,txtTipoVGOff,txtModeloVGOff,txtCombustibleVGOff,txtCilindrosVGOff,txtColorVGOff,txtUsoVGOff;
    EditText txtPuertasVGOff,txtNMotorVGOff,txtPropietarioVGOff,txtDireccionVGOff,txtColoniaVGOff,txtLocalidadVGOff,txtEstatusVGOff,txtEmailVGOff,txtObservacionesVGOff;

    String PlacaOff = " ",SerieVGOff = " ",MarcaVGOff = " ",VersionVGOff = " ",ClaseVGOff = " ",TipoVGOff = " ",ModeloVGOff = " ",CombustibleVGOff = " ",CilindrosVGOff = " ",ColorVGOff = " ";
    String UsoVGOff = " ",PuertasVGOff = " ",NMotorVGOff = " ",PropietarioVGOff = " ";
    String DireccionVGOff = " ",ColoniaVGOff = " ",LocalidadVGOff = " ",EstatusVGOff = " ",EmailVGOff = " ",ObservacionesVGOff = " ";

    private LinearLayout btnReglamentoOff,btnLugaresPagoOff,btnContactosOff,btnTabuladorOff;

    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    public String codigoVerifi, cargarInfoRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos_offline);
        cargarDatos();
        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        txtNoPlacaVGOff = findViewById(R.id.txtPlacaVGOff);
        txtSerieVGOff = findViewById(R.id.txtNSerieVGOff);
        txtMarcaVGOff = findViewById(R.id.txtMarcaVGOff);
        txtVersionVGOff = findViewById(R.id.txtVersionVGOff);
        txtClaseVGOff = findViewById(R.id.txtClaseVGOff);
        txtTipoVGOff = findViewById(R.id.txtTipoVGOff);
        txtModeloVGOff = findViewById(R.id.txtModeloVGOff);
        txtCombustibleVGOff = findViewById(R.id.txtCombustibleVGOff);
        txtCilindrosVGOff = findViewById(R.id.txtCilindrosVGOff);
        txtColorVGOff = findViewById(R.id.txtColorVGOff);
        txtUsoVGOff = findViewById(R.id.txtUsoVGOff);
        txtPuertasVGOff = findViewById(R.id.txtPuertasVGOff);
        txtNMotorVGOff = findViewById(R.id.txtNMotorVGOff);
        txtPropietarioVGOff = findViewById(R.id.txtPropietarioVGOff);
        txtDireccionVGOff = findViewById(R.id.txtDireccionVGOff);
        txtColoniaVGOff = findViewById(R.id.txtColoniaVGOff);
        txtLocalidadVGOff = findViewById(R.id.txtLocalidadVGOff);
        txtEstatusVGOff = findViewById(R.id.txtEstatusVGOff);
        txtEmailVGOff = findViewById(R.id.txtEmailVGOff);
        txtObservacionesVGOff = findViewById(R.id.txtObservacionesVGOff);

        btnVistaPOff = findViewById(R.id.imgVistaPVOff);
        btnGuardarOff = findViewById(R.id.imgGuardarVehiculoOff);
        btnInfraccionOff = findViewById(R.id.imgTerminalVOff);
        btnListTCOff= findViewById(R.id.btnListTCOff);


        btnReglamentoOff = findViewById(R.id.lyInicio5);
        btnLugaresPagoOff = findViewById(R.id.lyCategoria5);
        btnContactosOff = findViewById(R.id.lyContacto5);
        btnTabuladorOff = findViewById(R.id.lyFavoritos5);

        btnGuardarOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR VARIOS SEGUNDOS ",Toast.LENGTH_SHORT).show();
                insertVehiOffline();
            }
        });
    }

    /******************** INSERTA LOS REGISTROS FUERA DE LINEA *************************************/
    public void insertVehiOffline() {
        cargarDatos();
        PlacaOff = txtNoPlacaVGOff.getText().toString();
        SerieVGOff = txtSerieVGOff.getText().toString();
        MarcaVGOff = txtMarcaVGOff.getText().toString();
        VersionVGOff = txtVersionVGOff.getText().toString();
        ClaseVGOff = txtClaseVGOff.getText().toString();
        TipoVGOff = txtTipoVGOff.getText().toString();
        ModeloVGOff = txtModeloVGOff.getText().toString();
        CombustibleVGOff = txtCombustibleVGOff.getText().toString();
        CilindrosVGOff = txtCilindrosVGOff.getText().toString();
        ColorVGOff = txtColorVGOff.getText().toString();
        UsoVGOff = txtUsoVGOff.getText().toString();
        PuertasVGOff = txtPuertasVGOff.getText().toString();
        NMotorVGOff = txtNMotorVGOff.getText().toString();
        PropietarioVGOff = txtPropietarioVGOff.getText().toString();
        DireccionVGOff = txtDireccionVGOff.getText().toString();
        ColoniaVGOff = txtColoniaVGOff.getText().toString();
        LocalidadVGOff = txtLocalidadVGOff.getText().toString();
        EstatusVGOff = txtEstatusVGOff.getText().toString();
        ObservacionesVGOff = txtObservacionesVGOff.getText().toString();
        EmailVGOff = txtEmailVGOff.getText().toString();
        DataHelper dataHelperOffLine = new DataHelper(getApplication());
        dataHelperOffLine.insertVehiculosOffline(""+cargarInfoRandom,""+PlacaOff,""+SerieVGOff,""+MarcaVGOff,""+VersionVGOff,""+ClaseVGOff,""+TipoVGOff,""+ModeloVGOff,""+CombustibleVGOff,""+CilindrosVGOff, ""+ColorVGOff, ""+UsoVGOff, ""+PuertasVGOff, ""+NMotorVGOff, ""+PropietarioVGOff, ""+DireccionVGOff, ""+ColoniaVGOff, ""+LocalidadVGOff,""+EstatusVGOff, ""+ObservacionesVGOff, ""+EmailVGOff);
        System.out.println(dataHelperOffLine.getAllVehiculosOffline());
        ArrayList<String> list = dataHelperOffLine.getAllVehiculosOffline();
        if (list.size() > 0) {
            System.out.println("REGISTRO GUARDADO CON EXITO");
            eliminarDatos();
            Toast.makeText(getApplicationContext(),"REGISTRO GUARDADO CON EXITO",Toast.LENGTH_SHORT).show();
            txtNoPlacaVGOff.setText("");
            txtSerieVGOff.setText("");
            txtMarcaVGOff.setText("");
            txtVersionVGOff.setText("");
            txtClaseVGOff.setText("");
            txtTipoVGOff.setText("");
            txtModeloVGOff.setText("");
            txtCombustibleVGOff.setText("");
            txtCilindrosVGOff.setText("");
            txtColorVGOff.setText("");
            txtUsoVGOff.setText("");
            txtPuertasVGOff.setText("");
            txtNMotorVGOff.setText("");
            txtPropietarioVGOff.setText("");
            txtDireccionVGOff.setText("");
            txtColoniaVGOff.setText("");
            txtLocalidadVGOff.setText("");
            txtEstatusVGOff.setText("");
            txtEmailVGOff.setText("");
            txtObservacionesVGOff.setText("");
        }
    }

    public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(90000) * 99;
        codigoVerifi = String.valueOf(numberRandom);
        System.out.println(codigoVerifi);
        guardarRandom();
    }
    private void guardarRandom() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("RANDOM","20"+codigoVerifi);
        editor.commit();
    }
    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarInfoRandom = share.getString("RANDOM", "");
    }
    public void eliminarDatos() {
        share = getApplication().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.remove("RANDOM").commit();
    }
}
