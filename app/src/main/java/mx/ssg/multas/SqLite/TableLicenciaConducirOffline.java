package mx.ssg.multas.SqLite;

public class TableLicenciaConducirOffline {
    public String IdInfraccion, NoLicencia, ApellidoPL, ApellidoML, NombreL, TipoCalle, Calle, Numero, Colonia, Cp, Municipio, Estado, FechaExpedicion;
    public String FechaVencimiento, TipoVigencia, TipoLecencia, Rfc, Homo, GrupoSanguineo, RequerimientosEspeciales, Email, Observaciones;

    public void licenciaConducirOffline(String idInfraccion, String noLicencia, String apellidoPL, String apellidoML, String nombreL, String tipoCalle, String calle, String numero, String colonia, String cp, String municipio, String estado, String fechaExpedicion, String fechaVencimiento, String tipoVigencia, String tipoLecencia, String rfc, String homo, String grupoSanguineo, String requerimientosEspeciales, String email, String observaciones){
        this.IdInfraccion = idInfraccion;
        this.NoLicencia = noLicencia;
        this.ApellidoPL = apellidoPL;
        this.ApellidoML = apellidoML;
        this.NombreL = nombreL;
        this.TipoCalle = tipoCalle;
        this.Calle = calle;
        this.Numero = numero;
        this.Colonia = colonia;
        this.Cp = cp;
        this.Municipio = municipio;
        this.Estado = estado;
        this.FechaExpedicion = fechaExpedicion;
        this.FechaVencimiento = fechaVencimiento;
        this.TipoVigencia = tipoVigencia;
        this.TipoLecencia = tipoLecencia;
        this.Rfc = rfc;
        this.Homo = homo;
        this.GrupoSanguineo = grupoSanguineo;
        this.RequerimientosEspeciales = requerimientosEspeciales;
        this.Email = email;
        this.Observaciones = observaciones;
    }
}
