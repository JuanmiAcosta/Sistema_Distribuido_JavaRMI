package usuario;

import java.io.Serializable;

public class Usuario implements Serializable{
    
    private String usuario;
    private String contrasenia;
    private int num_donaciones;
    private float total_donaciones;
    
    public Usuario(){
        this.usuario = "";
        this.contrasenia = "";
        this.num_donaciones = 0;
        this.total_donaciones = 0;
    }

    public Usuario(String usuario, String contrasenia) {     
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.num_donaciones = 0;
        this.total_donaciones = 0;      
    }

    public String getUsuario() {
        return this.usuario;
    }

    public String getContrasenia() {
        return this.contrasenia;
    }

    public int getNumDonaciones() {
        return this.num_donaciones;
    }

    public float getTotalDonaciones() {
        return this.total_donaciones;
    }

    public void setNumDonaciones(int n) {
        this.num_donaciones = n;
    }

    public void setTotal_donado(float t) {
        this.total_donaciones = t;
    }

    public void incNumDonaciones() {
        this.num_donaciones++;
    }
    
    public void incTotalDonado(float cuantia) {
        this.total_donaciones+=cuantia;
    }
    
    
}