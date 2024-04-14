package pachete;

import java.io.Serializable;

public class Trotineta implements Comparable<Trotineta>, Serializable {
    private String idTrotineta;
    private float distantaTotala;
    private float vitezaMedie;
    private float vitezaMaxima;

    public Trotineta(String idTrotineta, float distantaTotala, float vitezaMedie, float vitezaMaxima) {
        this.idTrotineta = idTrotineta;
        this.distantaTotala = distantaTotala;
        this.vitezaMedie = vitezaMedie;
        this.vitezaMaxima = vitezaMaxima;
    }

    public String getIdTrotineta() {
        return idTrotineta;
    }

    public void setIdTrotineta(String idTrotineta) {
        this.idTrotineta = idTrotineta;
    }

    public float getDistantaTotala() {
        return distantaTotala;
    }

    public void setDistantaTotala(float distantaTotala) {
        this.distantaTotala = distantaTotala;
    }

    public float getVitezaMedie() {
        return vitezaMedie;
    }

    public void setVitezaMedie(float vitezaMedie) {
        this.vitezaMedie = vitezaMedie;
    }

    public float getVitezaMaxima() {
        return vitezaMaxima;
    }

    public void setVitezaMaxima(float vitezaMaxima) {
        this.vitezaMaxima = vitezaMaxima;
    }

    @Override
    public String toString() {
        return "Trotineta{" +
                "idTrotineta='" + idTrotineta + '\'' +
                ", distantaTotala=" + distantaTotala +
                ", vitezaMedie=" + vitezaMedie +
                ", vitezaMaxima=" + vitezaMaxima +
                '}';
    }

    @Override
    public int compareTo(Trotineta other) {
        return Float.compare(this.distantaTotala, other.distantaTotala);
    }
}
