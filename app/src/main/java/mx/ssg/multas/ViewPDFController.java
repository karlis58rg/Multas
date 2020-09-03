package mx.ssg.multas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import mx.ssg.multas.controller.BaseController;
import mx.ssg.multas.utils.KeyboardUtils;

public class ViewPDFController extends BaseController implements View.OnClickListener {

    final String viewerPath = "file:///android_asset/pdfjs/web/viewer.html?file=";
    final String pdfPath = "file:///android_asset/reglamento_tijuana.pdf";
    final String zoomPageWidth = "#zoom=page-width";

    public static WebView mWebView;
    private EditText mEditText;
    private DrawerLayout drawerLayout;

    private LinearLayout lyInicio;
    private LinearLayout lyCategoria;
    private LinearLayout lyContacto;
    private LinearLayout lyFavoritos;
    private LinearLayout lyMenu;
    private LinearLayout lySearch;
    LinearLayout lySearch2;
    ImageView btnBuscar;
    ImageView btnCancelar;
    private ImageView btnList;
    LinearLayout contentWebView;

    private static boolean banderaDetalle;
    private static String banderaPDFActivity;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdfcontroller);

        mWebView = findViewById(R.id.web_view_example);
        mEditText = findViewById(R.id.search_bar_example);
        mEditText.setFocusableInTouchMode(true); mEditText.requestFocus();

        lyInicio = findViewById(R.id.lyInicio);
        lyInicio.setOnClickListener(this);
        lyCategoria = findViewById(R.id.lyCategoria);
        lyCategoria.setOnClickListener(this);
        lyContacto = findViewById(R.id.lyContacto);
        lyContacto.setOnClickListener(this);
        lyFavoritos = findViewById(R.id.lyFavoritos);
        lyFavoritos.setOnClickListener(this);
        lyMenu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        lySearch = findViewById(R.id.lySearch);
        lySearch2 = findViewById(R.id.lySearch2);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
        btnList = findViewById(R.id.btnList);
        btnList.setOnClickListener(this);
        contentWebView = findViewById(R.id.contentWebView);

        ImageButton next = findViewById(R.id.next_result);
        ImageButton previous = findViewById(R.id.previous_result);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        mEditText.clearFocus();
        mEditText.setFocusableInTouchMode(false);
        mEditText.setFocusable(false);
        lyMenu.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
        lay.weight = (float) 0.88;
        drawerLayout.setLayoutParams(lay);
        initEditxt();

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findBarHelper(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mEditText.setFocusableInTouchMode(true);
                mEditText.setFocusable(true);
                lyMenu.setVisibility(View.GONE);
                LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
                lay.weight = (float) 0.99;
                drawerLayout.setLayoutParams(lay);
                initEditxt();
                return false;
            }
        });

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    lyMenu.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lay.weight = (float) 0.99;
                    drawerLayout.setLayoutParams(lay);
                } else {
                    lyMenu.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lay.weight = (float) 0.88;
                    drawerLayout.setLayoutParams(lay);
                }
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.loadUrl(viewerPath + pdfPath + zoomPageWidth);

        if (banderaDetalle) {


        }

        /******************EVENTOS DEL MENÚ **********************/
        lyCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPDFController.this, LugaresDePago.class);
                startActivity(i);
                finish();
            }
        });

        lyContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPDFController.this, Contactos.class);
                startActivity(i);
                finish();

            }
        });

        lyFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPDFController.this, Tabulador.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_result:
                findBarHelperNext(mEditText.getText().toString());
                break;
            case R.id.previous_result:
                findBarHelperPrevious(mEditText.getText().toString());
                break;
            case R.id.lyInicio:
                setBanderaPDFActivity("inicio");
                onResume();
                break;
            case R.id.lyCategoria:
                setBanderaPDFActivity("categoria");
                onResume();
                break;
            case R.id.lyContacto:
                setBanderaPDFActivity("contacto");
                onResume();
                break;
            case R.id.btnBuscar:
                lySearch.setVisibility(View.GONE);
                lySearch2.setVisibility(View.VISIBLE);
                break;
            case R.id.btnCancelar:
                lySearch2.setVisibility(View.GONE);
                lySearch.setVisibility(View.VISIBLE);
                mEditText.setText("");
                break;
            case R.id.btnList:
                sidebarToggle();
                break;
        }
    }

    private void sidebarToggle() {
        mWebView.loadUrl("javascript:sidebarToggle()");
    }

    public static void goToPage(final int number) {
        mWebView.loadUrl("javascript:goToPage(" + number + ")");
    }

    private void findBarHelper(final String text) {
        mWebView.loadUrl("javascript:searchPDF('" + text + "')");
    }

    private void findBarHelperNext(final String text) {
        mWebView.loadUrl("javascript:searchPDFNext('" + text + "')");
    }

    private void findBarHelperPrevious(final String text) {
        mWebView.loadUrl("javascript:searchPDFPrevious('" + text + "')");
    }

    public static String getBanderaPDFActivity() {
        return banderaPDFActivity;
    }

    public static void setBanderaPDFActivity(String banderaPDFActivity) {
        ViewPDFController.banderaPDFActivity = banderaPDFActivity;
    }

    public void initEditxt() {
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    lyMenu.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lay.weight = (float) 0.99;
                    drawerLayout.setLayoutParams(lay);
                } else {
                    lyMenu.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lay.weight = (float) 0.88;
                    drawerLayout.setLayoutParams(lay);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            }
        });
    }

    public static boolean isBanderaDetalle() {
        return banderaDetalle;
    }

    public static void setBanderaDetalle(boolean banderaDetalle) {
        ViewPDFController.banderaDetalle = banderaDetalle;
    }
}
