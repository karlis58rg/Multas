package mx.ssg.multas.SqLite;

public class TableInfraccionesOffline {
    public String IdInfraccion,Usuario,Direccion,Contrasena,Fecha,Hora;
    public String Garantia,SalariosMinimos,Condonacion,Pago,StatusPago;

    public void InfraccionesOffline(String idInfraccion, String usuario,String direccion,String contrasena,String fecha,String hora,String garantia,String salariosMinimos,String condonacion,String pago,String statusPago){
        this.IdInfraccion = idInfraccion;
        this.Usuario = usuario;
        this.Direccion = direccion;
        this.Contrasena = contrasena;
        this.Fecha = fecha;
        this.Hora = hora;
        this.Garantia = garantia;
        this.SalariosMinimos = salariosMinimos;
        this.Condonacion = condonacion;
        this.Pago = pago;
        this.StatusPago = statusPago;
    }
}
