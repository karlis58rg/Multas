package mx.ssg.multas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class Reglamento extends AppCompatActivity {
    public String cargarInfoUsuario;
    /*********** SHARE PREFERENCE ************/
    SharedPreferences share;
    SharedPreferences.Editor editor;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //QUITA EL COLOR DE LOS ICONOS EN GRIS
        navigationView.setItemIconTintList(null);
        //ACCEDE AL LABEL DEL ENCABEZADO
        View headerView = navigationView.getHeaderView(0);
        TextView lblUserName =(TextView)headerView.findViewById(R.id.lblAvatarMenu);
        cargarDatos();
        lblUserName.setText(cargarInfoUsuario);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reglamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_session){
            eliminarDatos();
            Intent i = new Intent(Reglamento.this, LoginUser.class);
            startActivity(i);
            finish();
            //Toast.makeText(getApplicationContext(),"ADIOS",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cargarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        cargarInfoUsuario = share.getString("USER","");
    }

    private void eliminarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        editor = share.edit();
        editor.remove("STATUSUSER").commit();
    }
}
