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

public class LicenciaConducirOffline extends AppCompatActivity {

    ImageView btnMenuLCOffline,btnTarjetaLCOffline,btnInfraccionLCOffline;
    Button btnGuardarLCOffline;
    EditText txtLicenciaOffline,txtNombreOffline,txtApaternoOffline,txtAmaternoOffline,txtTipocalleOffline,txtCalleLCOffline,txtNumeroCalleOffline,txtColoniaLCOffline,txtCPOffline,txtMunicipioLCOffline,txtEstadoLCOffline;
    EditText txtFechaExLCOffline,txtFechaVenLCOffline,txtTipoVigLCOffline,txtTipoLicOffline,txtRFCLCOffline,txtHomoLCOffline,txtGrupoSanguiLCOffline,txtRequeriemientosEspLCOffline,txtEmailLCOffline,txtObservacionesLCOffline;
    LinearLayout btnReglamentoOffline,btnLugaresPagoOffline,btnContactosOffline,btnTabuladorOffline;
    String IdInfraccion = "", NoLicencia = "", ApellidoPL = "", ApellidoML = "", NombreL = "", TipoCalle = "", Calle = "", Numero = "", Colonia = "", Cp = "", Municipio = "", Estado = "", FechaExpedicion = "";
    String FechaVencimiento = "", TipoVigencia = "", TipoLecencia = "", Rfc = "", Homo = "", GrupoSanguineo = "", RequerimientosEspeciales = "", Email = "", Observaciones = "";
    String codigoVerifi, cargarInfoRandom;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    int numberRandom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licencia_conducir_offline);
        cargarDatos();
        if(cargarInfoRandom.isEmpty()){
            Random();
        }else {
            System.out.println(cargarInfoRandom);
        }

        btnMenuLCOffline = findViewById(R.id.btnListLOffLine);
        btnTarjetaLCOffline = findViewById(R.id.imgTCLicenciaOffLine);
        btnInfraccionLCOffline = findViewById(R.id.imgTerminalLCOffLine);
        btnGuardarLCOffline = findViewById(R.id.imgGuardarLCOffLine);

        txtLicenciaOffline = findViewById(R.id.txtNoLicenciaOffLine);
        /////posicion de cursor///////////
        txtLicenciaOffline.setFocusableInTouchMode(true); txtLicenciaOffline.requestFocus();

        txtApaternoOffline = findViewById(R.id.txtApaternoLOffLine);
        txtAmaternoOffline = findViewById(R.id.txtAmaternoLOffLine);
        txtNombreOffline = findViewById(R.id.txtNombreLOffLine);
        txtTipocalleOffline = findViewById(R.id.txtTipocalleOffLine);
        txtCalleLCOffline = findViewById(R.id.txtCalleLCOffLine);
        txtNumeroCalleOffline = findViewById(R.id.txtNumeroCalleOffLine);
        txtColoniaLCOffline = findViewById(R.id.txtColoniaLCOffLine);
        txtCPOffline = findViewById(R.id.txtCPOffLine);
        txtMunicipioLCOffline = findViewById(R.id.txtMunicipioLCOffLine);
        txtEstadoLCOffline = findViewById(R.id.txtEstadoLCOffLine);
        txtFechaExLCOffline = findViewById(R.id.txtFechaExLCOffLine);
        txtFechaVenLCOffline = findViewById(R.id.txtFechaVenLCOffLine);
        txtTipoVigLCOffline = findViewById(R.id.txtTipoVigLCOffLine);
        txtTipoLicOffline = findViewById(R.id.txtTipoLicOffLine);
        txtRFCLCOffline = findViewById(R.id.txtRFCLCOffLine);
        txtHomoLCOffline = findViewById(R.id.txtHomoLCOffLine);
        txtGrupoSanguiLCOffline = findViewById(R.id.txtGrupoSanguiLCOffLine);
        txtRequeriemientosEspLCOffline = findViewById(R.id.txtRequeriemientosEspLCOffLine);
        txtEmailLCOffline = findViewById(R.id.txtEmailLCOffLine);
        txtObservacionesLCOffline = findViewById(R.id.txtObservacionesLCOffLine);

        btnReglamentoOffline = findViewById(R.id.lyInicioL6);
        btnLugaresPagoOffline = findViewById(R.id.lyCategoriaL6);
        btnContactosOffline = findViewById(R.id.lyContactoL6);
        btnTabuladorOffline = findViewById(R.id.lyFavoritosL6);

        btnGuardarLCOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR VARIOS SEGUNDOS ",Toast.LENGTH_SHORT).show();
                insertLicenciaOffline();
            }
        });
    }

    /******************** INSERTA LOS REGISTROS FUERA DE LINEA *************************************/
    public void insertLicenciaOffline() {
        cargarDatos();
        NoLicencia = txtLicenciaOffline.getText().toString();
        ApellidoPL = txtApaternoOffline.getText().toString();
        ApellidoML = txtAmaternoOffline.getText().toString();
        NombreL = txtNombreOffline.getText().toString();
        TipoCalle = txtTipocalleOffline.getText().toString();
        Calle = txtCalleLCOffline.getText().toString();
        Numero = txtNumeroCalleOffline.getText().toString();
        Colonia = txtColoniaLCOffline.getText().toString();
        Cp = txtCPOffline.getText().toString();
        Municipio = txtMunicipioLCOffline.getText().toString();
        Estado = txtEstadoLCOffline.getText().toString();
        FechaExpedicion = txtFechaExLCOffline.getText().toString();
        FechaVencimiento = txtFechaVenLCOffline.getText().toString();
        TipoVigencia = txtTipoVigLCOffline.getText().toString();
        TipoLecencia = txtTipoLicOffline.getText().toString();
        Rfc = txtRFCLCOffline.getText().toString();
        Homo = txtHomoLCOffline.getText().toString();
        GrupoSanguineo = txtGrupoSanguiLCOffline.getText().toString();
        RequerimientosEspeciales = txtRequeriemientosEspLCOffline.getText().toString();
        Email = txtEmailLCOffline.getText().toString();
        Observaciones = txtObservacionesLCOffline.getText().toString();
        DataHelper dataHelperOffLine = new DataHelper(getApplication());
        dataHelperOffLine.insertLicenciaConducirOffline(""+cargarInfoRandom,""+NoLicencia,""+ApellidoPL,""+ApellidoML,""+NombreL,""+TipoCalle,""+Calle,""+Numero,""+Colonia,""+Cp,""+Municipio, ""+Estado, ""+FechaExpedicion, ""+FechaVencimiento, ""+TipoVigencia, ""+TipoLecencia, ""+Rfc, ""+Homo, ""+GrupoSanguineo,""+RequerimientosEspeciales, ""+Email, ""+Observaciones);
        System.out.println(dataHelperOffLine.getAllLicenciaOffline());
        ArrayList<String> list = dataHelperOffLine.getAllLicenciaOffline();
        if (list.size() > 0) {
            System.out.println("REGISTRO GUARDADO CON EXITO");
            eliminarDatos();
            Toast.makeText(getApplicationContext(),"REGISTRO GUARDADO CON EXITO",Toast.LENGTH_SHORT).show();
            txtLicenciaOffline.setText("");
            txtApaternoOffline.setText("");
            txtAmaternoOffline.setText("");
            txtNombreOffline.setText("");
            txtTipocalleOffline.setText("");
            txtCalleLCOffline.setText("");
            txtNumeroCalleOffline.setText("");
            txtColoniaLCOffline.setText("");
            txtCPOffline.setText("");
            txtMunicipioLCOffline.setText("");
            txtEstadoLCOffline.setText("");
            txtFechaExLCOffline.setText("");
            txtFechaVenLCOffline.setText("");
            txtTipoVigLCOffline.setText("");
            txtTipoLicOffline.setText("");
            txtRFCLCOffline.setText("");
            txtHomoLCOffline.setText("");
            txtGrupoSanguiLCOffline.setText("");
            txtRequeriemientosEspLCOffline.setText("");
            txtEmailLCOffline.setText("");
            txtObservacionesLCOffline.setText("");
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

    private void eliminarDatos() {
        share = getApplication().getSharedPreferences("main", Context.MODE_PRIVATE);
        editor = share.edit();
        editor.remove("RANDOM").commit();
    }

}
