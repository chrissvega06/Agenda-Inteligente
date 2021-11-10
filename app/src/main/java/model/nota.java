package model;

public class nota {
    private String uid;
    private String info;
    private String fecha;
    private String direccion;

    //public publicacion(String uid, String info, String fecha, String direccion) {
      //  this.uid = uid;
       // this.info = info;
        //this.fecha = fecha;
        //this.direccion = direccion;
    //}

    public nota() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {

        return info;
    }
}
