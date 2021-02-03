package mx.ssg.multas.SqLite;

public class TableVehiculoOffline {
    public String IdInfraccion,Placa,NoSerie,Marca,Version,Clase,Tipo,Modelo,Combustible;
    public String Cilindros,Color,Uso,Puertas,NoMotor,Propietario,Direccion,Colonia;
    public String Localidad,Estatus,Observaciones,Email;

    public void VehiculoOffline(String idInfraccion, String placa,String noSerie,String marca,String version,String clase,String tipo,String modelo,String combustible,String cilindros,String color,String uso,String puertas,String noMotor,String propietario,String direccion,String colonia,String localidad,String estatus,String observaciones,String email){
        this.IdInfraccion = idInfraccion;
        this.Placa = placa;
        this.NoSerie = noSerie;
        this.Marca = marca;
        this.Version = version;
        this.Clase = clase;
        this.Tipo = tipo;
        this.Modelo = modelo;
        this.Combustible = combustible;
        this.Cilindros = cilindros;
        this.Color = color;
        this.Uso = uso;
        this.Puertas = puertas;
        this.NoMotor = noMotor;
        this.Propietario = propietario;
        this.Direccion = direccion;
        this.Colonia = colonia;
        this.Localidad = localidad;
        this.Estatus = estatus;
        this.Observaciones = observaciones;
        this.Email = email;
    }
}
