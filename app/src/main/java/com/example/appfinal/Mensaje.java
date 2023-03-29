package com.example.appfinal;

import java.util.Date;

public class Mensaje {

    private String mensaje;
    private Date fechaHora;
    private String uid;

    private String uidPersona;

    public Mensaje() {
    }

    public Mensaje(String mensaje, Date fechaHora, String uid, String uidPersona) {
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.uid = uid;
        this.uidPersona = uidPersona;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaHora(long time) {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidPersona() {
        return uidPersona;
    }

    public void setUidPersona(String uidPersona) {
        this.uidPersona = uidPersona;
    }
}
