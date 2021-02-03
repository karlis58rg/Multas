package mx.ssg.multas.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {
    JSONObject vehiculoOffline;
    public static final String DataBase_Name = "AppMultas";
    public static final int Database_Version = 6 ;

    public static final String Table_Tabulador = "CatTabulador";
    public static final String Create_Tabulador = "CREATE TABLE IF NOT EXISTS " + Table_Tabulador +"(Clave INTEGER PRIMARY KEY, Descripcion TEXT NOT NULL UNIQUE, Articulos TEXT ,IdFraccion TEXT , SalMinimos INTEGER)";
    public static final String Delete_Tabulador = "DROP TABLE IF EXISTS "+Table_Tabulador;

    public static final String Table_VehiculosOffline = "VehiculosOffline";
    public static final String Create_VehiculosOffline = "CREATE TABLE IF NOT EXISTS " + Table_VehiculosOffline +"(IdInfraccion TEXT PRIMARY KEY, Placa TEXT , NoSerie TEXT ,Marca TEXT , Version TEXT, Clase TEXT, Tipo TEXT, Modelo TEXT, Combustible TEXT, Cilindros TEXT, Color TEXT, Uso TEXT, Puertas TEXT, NoMotor TEXT, Propietario TEXT, Direccion TEXT, Colonia TEXT, Localidad TEXT, Estatus TEXT, Observaciones TEXT, Email TEXT)";

    public static final String Table_LicenciaConducirOffline = "LicenciaConducirOffline";
    public static final String Create_LicenciaConducirOffline = "CREATE TABLE IF NOT EXISTS " + Table_LicenciaConducirOffline +"(IdInfraccion TEXT PRIMARY KEY, NoLicencia TEXT , ApellidoPL TEXT ,ApellidoML TEXT , NombreL TEXT, TipoCalle TEXT, Calle TEXT, Numero TEXT, Colonia TEXT, Cp TEXT, Municipio TEXT, Estado TEXT, FechaExpedicion TEXT, FechaVencimiento TEXT, TipoVigencia TEXT, TipoLecencia TEXT, Rfc TEXT, Homo TEXT, GrupoSanguineo TEXT, RequerimientosEspeciales TEXT, Email TEXT, Observaciones TEXT)";


    public static final String Table_InfraccionesOffline = "InfraccionesOffline";
    public static final String Create_InfraccionesOffline = "CREATE TABLE IF NOT EXISTS " + Table_InfraccionesOffline +"(IdInfraccion TEXT PRIMARY KEY, Usuario TEXT , Direccion TEXT, Contrasena TEXT, Fecha TEXT, Hora TEXT, Garantia TEXT, SalariosMinimos TEXT, Condonacion TEXT, Pago TEXT, StatusPago TEXT)";

    public static final String Table_TempoInfraccionesClavesOffline = "TempoInfraccionesClavesOffline";
    public static final String Create_TempoInfraccionesClavesOffline = "CREATE TABLE IF NOT EXISTS " + Table_TempoInfraccionesClavesOffline +"(IdInfraccion TEXT PRIMARY KEY, Clave TEXT, Descripcion TEXT, SalMinimos TEXT)";


    public DataHelper(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Tabulador);
        db.execSQL(Create_VehiculosOffline);
        db.execSQL(Create_LicenciaConducirOffline);
        db.execSQL(Create_InfraccionesOffline);
        db.execSQL(Create_TempoInfraccionesClavesOffline);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Delete_Tabulador);
        onCreate(db);
    }

    /************************************************** TABULADOR ********************************************************************/
    public void insertTabulador(int clave,String descripcion,String articulos,String idInfraccion,int salMinimos){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
                values.put("Clave",clave);
                values.put("Descripcion",descripcion);
                values.put("Articulos",articulos);
                values.put("IdFraccion",idInfraccion);
                values.put("SalMinimos",salMinimos);
                dbSqLiteDatabase.insert(Table_Tabulador,null,values);
                dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){e.printStackTrace();}
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }

    public ArrayList<String> getAllTabulador(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_Tabulador;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String asehCargo = cursor.getString(cursor.getColumnIndex("Descripcion"));
                    list.add(asehCargo);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    /************************************************** VEHICULOS OFFLINE ********************************************************************/
    public void insertVehiculosOffline(String IdInfraccion, String Placa, String NoSerie, String Marca, String Version, String Clase , String Tipo ,String Modelo, String Combustible, String Cilindros, String Color, String Uso, String Puertas, String NoMotor, String Propietario, String Direccion, String Colonia, String Localidad, String Estatus, String Observaciones, String Email){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdInfraccion",IdInfraccion);
            values.put("Placa",Placa);
            values.put("NoSerie",NoSerie);
            values.put("Marca",Marca);
            values.put("Version",Version);
            values.put("Clase",Clase);
            values.put("Tipo",Tipo);
            values.put("Modelo",Modelo);
            values.put("Combustible",Combustible);
            values.put("Cilindros",Cilindros);
            values.put("Color",Color);
            values.put("Uso",Uso);
            values.put("Puertas",Puertas);
            values.put("NoMotor",NoMotor);
            values.put("Propietario",Propietario);
            values.put("Direccion",Direccion);
            values.put("Colonia",Colonia);
            values.put("Localidad",Localidad);
            values.put("Estatus",Estatus);
            values.put("Observaciones",Observaciones);
            values.put("Email",Email);
            dbSqLiteDatabase.insert(Table_VehiculosOffline,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){e.printStackTrace();}
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }

    public ArrayList<String> getAllVehiculosOffline(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_VehiculosOffline;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            System.out.println(cursor);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String valor1 = cursor.getString(cursor.getColumnIndex("IdInfraccion"));
                        list.add(cursor.getString(cursor.getColumnIndex("IdInfraccion")));
                    } while (cursor.moveToNext());
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public List<TableVehiculoOffline> getVehiculosAndInsertThem(){
        //ArrayList<String> list = new ArrayList<String>();
        List<TableVehiculoOffline> items = new ArrayList<TableVehiculoOffline>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_VehiculosOffline;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            System.out.println(cursor);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String valor1 = cursor.getString(cursor.getColumnIndex("IdInfraccion"));
                        String valor2 = cursor.getString(cursor.getColumnIndex("Placa"));
                        String valor3 = cursor.getString(cursor.getColumnIndex("NoSerie"));
                        String valor4 = cursor.getString(cursor.getColumnIndex("Marca"));
                        String valor5 = cursor.getString(cursor.getColumnIndex("Version"));
                        String valor6 = cursor.getString(cursor.getColumnIndex("Clase"));
                        String valor7 = cursor.getString(cursor.getColumnIndex("Tipo"));
                        String valor8 = cursor.getString(cursor.getColumnIndex("Modelo"));
                        String valor9 = cursor.getString(cursor.getColumnIndex("Combustible"));
                        String valor10 = cursor.getString(cursor.getColumnIndex("Cilindros"));
                        String valor11 = cursor.getString(cursor.getColumnIndex("Color"));
                        String valor12 = cursor.getString(cursor.getColumnIndex("Uso"));
                        String valor13 = cursor.getString(cursor.getColumnIndex("Puertas"));
                        String valor14 = cursor.getString(cursor.getColumnIndex("NoMotor"));
                        String valor15 = cursor.getString(cursor.getColumnIndex("Propietario"));
                        String valor16 = cursor.getString(cursor.getColumnIndex("Direccion"));
                        String valor17 = cursor.getString(cursor.getColumnIndex("Colonia"));
                        String valor18 = cursor.getString(cursor.getColumnIndex("Localidad"));
                        String valor19 = cursor.getString(cursor.getColumnIndex("Estatus"));
                        String valor20 = cursor.getString(cursor.getColumnIndex("Observaciones"));
                        String valor21 = cursor.getString(cursor.getColumnIndex("Email"));
                        TableVehiculoOffline table = new TableVehiculoOffline();
                        table.VehiculoOffline(valor1,valor2,valor3,valor4,valor5,valor6,valor7,valor8,valor9,valor10,valor11,valor12,valor13,valor14,valor15,valor16,valor17,valor18,valor19,valor20,valor21);
                        table.IdInfraccion = valor1;
                        table.Placa = valor2;
                        table.NoSerie = valor3;
                        table.Marca = valor4;
                        table.Version = valor5;
                        table.Clase = valor6;
                        table.Tipo = valor7;
                        table.Modelo = valor8;
                        table.Combustible = valor9;
                        table.Cilindros = valor10;
                        table.Color = valor11;
                        table.Uso = valor12;
                        table.Puertas = valor13;
                        table.NoMotor = valor14;
                        table.Propietario = valor15;
                        table.Direccion = valor16;
                        table.Colonia = valor17;
                        table.Localidad = valor18;
                        table.Estatus = valor19;
                        table.Observaciones = valor20;
                        table.Email = valor21;
                        items.add(table);
                    } while (cursor.moveToNext());
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return items;
    }

    public void deleteAllVehiculosOffLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ Table_VehiculosOffline);
        db.close();
    }

    /************************************************** LICENCIA OFFLINE ********************************************************************/
    public void insertLicenciaConducirOffline(String IdInfraccion,String NoLicencia, String ApellidoPL, String ApellidoML, String NombreL, String TipoCalle, String Calle, String Numero, String Colonia, String Cp, String Municipio, String Estado, String FechaExpedicion, String FechaVencimiento, String TipoVigencia, String TipoLecencia, String Rfc, String Homo, String GrupoSanguineo, String RequerimientosEspeciales, String Email, String Observaciones){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdInfraccion",IdInfraccion);
            values.put("NoLicencia",NoLicencia);
            values.put("ApellidoPL",ApellidoPL);
            values.put("ApellidoML",ApellidoML);
            values.put("NombreL",NombreL);
            values.put("TipoCalle",TipoCalle);
            values.put("Calle",Calle);
            values.put("Numero",Numero);
            values.put("Colonia",Colonia);
            values.put("Cp",Cp);
            values.put("Municipio",Municipio);
            values.put("Estado",Estado);
            values.put("FechaExpedicion",FechaExpedicion);
            values.put("FechaVencimiento",FechaVencimiento);
            values.put("TipoVigencia",TipoVigencia);
            values.put("TipoLecencia",TipoLecencia);
            values.put("Rfc",Rfc);
            values.put("Homo",Homo);
            values.put("GrupoSanguineo",GrupoSanguineo);
            values.put("RequerimientosEspeciales",RequerimientosEspeciales);
            values.put("Email",Email);
            values.put("Observaciones",Observaciones);
            dbSqLiteDatabase.insert(Table_LicenciaConducirOffline,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){e.printStackTrace();}
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }

    public ArrayList<String> getAllLicenciaOffline(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_LicenciaConducirOffline;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            System.out.println(cursor);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(cursor.getString(cursor.getColumnIndex("IdInfraccion")));
                    } while (cursor.moveToNext());
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public List<TableLicenciaConducirOffline> getLicenciasAndInsertThem(){
        //ArrayList<String> list = new ArrayList<String>();
        List<TableLicenciaConducirOffline> items = new ArrayList<TableLicenciaConducirOffline>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_LicenciaConducirOffline;
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            System.out.println(cursor);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String valor1 = cursor.getString(cursor.getColumnIndex("IdInfraccion"));
                        String valor2 = cursor.getString(cursor.getColumnIndex("NoLicencia"));
                        String valor3 = cursor.getString(cursor.getColumnIndex("ApellidoPL"));
                        String valor4 = cursor.getString(cursor.getColumnIndex("ApellidoML"));
                        String valor5 = cursor.getString(cursor.getColumnIndex("NombreL"));
                        String valor6 = cursor.getString(cursor.getColumnIndex("TipoCalle"));
                        String valor7 = cursor.getString(cursor.getColumnIndex("Calle"));
                        String valor8 = cursor.getString(cursor.getColumnIndex("Numero"));
                        String valor9 = cursor.getString(cursor.getColumnIndex("Colonia"));
                        String valor10 = cursor.getString(cursor.getColumnIndex("Cp"));
                        String valor11 = cursor.getString(cursor.getColumnIndex("Municipio"));
                        String valor12 = cursor.getString(cursor.getColumnIndex("Estado"));
                        String valor13 = cursor.getString(cursor.getColumnIndex("FechaExpedicion"));
                        String valor14 = cursor.getString(cursor.getColumnIndex("FechaVencimiento"));
                        String valor15 = cursor.getString(cursor.getColumnIndex("TipoVigencia"));
                        String valor16 = cursor.getString(cursor.getColumnIndex("TipoLecencia"));
                        String valor17 = cursor.getString(cursor.getColumnIndex("Rfc"));
                        String valor18 = cursor.getString(cursor.getColumnIndex("Homo"));
                        String valor19 = cursor.getString(cursor.getColumnIndex("GrupoSanguineo"));
                        String valor20 = cursor.getString(cursor.getColumnIndex("RequerimientosEspeciales"));
                        String valor21 = cursor.getString(cursor.getColumnIndex("Email"));
                        String valor22 = cursor.getString(cursor.getColumnIndex("Observaciones"));
                        TableLicenciaConducirOffline table = new TableLicenciaConducirOffline();
                        table.licenciaConducirOffline(valor1,valor2,valor3,valor4,valor5,valor6,valor7,valor8,valor9,valor10,valor11,valor12,valor13,valor14,valor15,valor16,valor17,valor18,valor19,valor20,valor21,valor22);
                        table.IdInfraccion = valor1;
                        table.NoLicencia = valor2;
                        table.ApellidoPL = valor3;
                        table.ApellidoML = valor4;
                        table.NombreL = valor5;
                        table.TipoCalle = valor6;
                        table.Calle = valor7;
                        table.Numero = valor8;
                        table.Colonia = valor9;
                        table.Cp = valor10;
                        table.Municipio = valor11;
                        table.Estado = valor12;
                        table.FechaExpedicion = valor13;
                        table.FechaVencimiento = valor14;
                        table.TipoVigencia = valor15;
                        table.TipoLecencia = valor16;
                        table.Rfc = valor17;
                        table.Homo = valor18;
                        table.GrupoSanguineo = valor19;
                        table.RequerimientosEspeciales = valor20;
                        table.Email = valor21;
                        table.Observaciones = valor22;
                        items.add(table);
                    } while (cursor.moveToNext());
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return items;
    }

    public void deleteAllLicenciasOffLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ Table_LicenciaConducirOffline);
        db.close();
    }
 }


