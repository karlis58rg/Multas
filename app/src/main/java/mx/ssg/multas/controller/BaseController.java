package mx.ssg.multas.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import mx.ssg.multas.R;

public class BaseController extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lyInicio;
    LinearLayout lyCategoria;
    LinearLayout lyContacto;
    LinearLayout lyFavoritos;
    LinearLayout contenedor;
    LinearLayout contenedorInicio;
    RelativeLayout lyBarAction;
    TextView tittleB;
    TextView subTitle;
    int layoutID;
    ImageView btnAtras;
    ImageView imgSubHeader;
    TextView txtSubHeader;
    public void onCreate(Bundle savedInstanceState, int recurso, String subHeader) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_view_pdfcontroller);

        contenedor = (LinearLayout) findViewById(R.id.content);

        this.layoutID = layoutID;

        lyInicio = (LinearLayout) findViewById(R.id.lyInicio);
        lyInicio.setOnClickListener(this);
        lyCategoria = (LinearLayout) findViewById(R.id.lyCategoria);
        lyCategoria.setOnClickListener(this);
        lyContacto = (LinearLayout) findViewById(R.id.lyContacto);
        lyContacto.setOnClickListener(this);
        lyFavoritos = (LinearLayout) findViewById(R.id.lyFavoritos);
        lyFavoritos.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
        if (recurso != 0){
            imgSubHeader.setBackgroundResource(recurso);
        }
    }

    public void configScreen() {

        if (layoutID == R.layout.activity_view_pdfcontroller || layoutID == R.layout.activity_view_pdfcontroller){
            lyBarAction.setVisibility(View.GONE);
            contenedor.setVisibility(View.GONE);
            contenedorInicio.setVisibility(View.VISIBLE);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(layoutID, contenedorInicio, true);
            //tittleMain.setText(title);
        }else {
            lyBarAction.setVisibility(View.VISIBLE);
            contenedorInicio.setVisibility(View.GONE);
            contenedor.setVisibility(View.VISIBLE);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(layoutID, contenedor, true);
        }

    }

    public void onCreate(Bundle savedInstanceState, String tittle, String subTittle, int layoutID,int recurso,String subHeader) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_view_pdfcontroller);

        contenedor = (LinearLayout) findViewById(R.id.content);

        this.layoutID = layoutID;

        lyInicio = (LinearLayout) findViewById(R.id.lyInicio);
        lyInicio.setOnClickListener(this);
        lyCategoria = (LinearLayout) findViewById(R.id.lyCategoria);
        lyCategoria.setOnClickListener(this);
        lyContacto = (LinearLayout) findViewById(R.id.lyContacto);
        lyContacto.setOnClickListener(this);
        lyFavoritos = (LinearLayout) findViewById(R.id.lyFavoritos);
        lyFavoritos.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
        if (recurso != 0){
            imgSubHeader.setBackgroundResource(recurso);
        }
        txtSubHeader.setText(subHeader);
        configScreen(layoutID,tittle,subTittle);
    }

    public void configScreen(final int layoutID,String titulo, String subTitulo) {

        if (layoutID == R.layout.activity_view_pdfcontroller || layoutID == R.layout.activity_view_pdfcontroller){
            lyBarAction.setVisibility(View.GONE);
            contenedor.setVisibility(View.GONE);
            contenedorInicio.setVisibility(View.VISIBLE);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(layoutID, contenedorInicio, true);
            //tittleMain.setText(title);
        }else {
            lyBarAction.setVisibility(View.VISIBLE);
            contenedorInicio.setVisibility(View.GONE);
            contenedor.setVisibility(View.VISIBLE);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(layoutID, contenedor, true);
            tittleB.setText(titulo);
            subTitle.setText(subTitulo);
        }

    }

    @Override
    public void onClick(View v) {

    }
}
