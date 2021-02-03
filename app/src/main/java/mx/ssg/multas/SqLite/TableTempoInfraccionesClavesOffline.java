package mx.ssg.multas.SqLite;

public class TableTempoInfraccionesClavesOffline {
    public String IdInfraccion,Clave,Descripcion,SalMinimos;

    public void TempoInfraccionesClavesOffline(String idInfraccion, String clave,String descripcion,String salMinimos){
        this.IdInfraccion = idInfraccion;
        this.Clave= clave;
        this.Descripcion = descripcion;
        this.SalMinimos = salMinimos;
    }
}
