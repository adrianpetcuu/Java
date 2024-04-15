import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Trotineta implements Comparable<Trotineta>, Cloneable, Serializable {
    private String id;
    private float distantaTotala;
    private float vitezaMedie;
    private float vitezaMaxima;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Trotineta() {}

    public Trotineta(String id, float distantaTotala, float vitezaMedie, float vitezaMaxima) {
        this.id = id;
        this.distantaTotala = distantaTotala;
        this.vitezaMedie = vitezaMedie;
        this.vitezaMaxima = vitezaMaxima;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trotineta{");
        sb.append("id='").append(id).append('\'');
        sb.append(", distantaTotala=").append(distantaTotala);
        sb.append(", vitezaMedie=").append(vitezaMedie);
        sb.append(", vitezaMaxima=").append(vitezaMaxima);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Trotineta copy = (Trotineta)super.clone();
        copy.id = this.id;
        copy.distantaTotala = this.distantaTotala;
        copy.vitezaMedie = this.vitezaMedie;
        copy.vitezaMaxima = this.vitezaMaxima;
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Trotineta) {
            Trotineta t = (Trotineta) obj;
            if ((this.distantaTotala - t.distantaTotala >= -10) &&
                    (this.distantaTotala - t.distantaTotala <= 10)) {
                return true;
            }
            else{
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public int compareTo(Trotineta t) {
        if(this.equals(t)){
            return 0;
        }
        else if(this.distantaTotala < t.distantaTotala){
            return -1;
        }
        else {
            return 1;
        }
    }
}


public class Test {

    public static void main(String[] args){
        Trotineta t1 = new Trotineta();
        t1.setId("1");
        t1.setDistantaTotala(100);
        t1.setVitezaMedie(20);
        t1.setVitezaMaxima(30);
        System.out.println(t1.toString());
        Trotineta t2 = new Trotineta("2", 110, 22, 30);
        System.out.println(t2.toString());
        if(t1.equals(t2) == true){
            System.out.println("Sunt egale.");
        }
        else{
            System.out.println("Nu sunt egale.");
        }
        System.out.println(t1.compareTo(t2));

        Map<String, Trotineta> trotinete = citesteTrotinete("trotinete.txt");
        for(var trotineta : trotinete.entrySet()){
            if(trotineta.getValue().getVitezaMaxima() > 50){
                System.out.println(trotineta.getValue());
            }
        }

        afiseazaVitezeMedii(trotinete);

        try {
            salveazaTrotineteRapide(trotinete, "Rapide.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            afiseazaTrotineteRapide("Rapide.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String, Trotineta> citesteTrotinete(String filename){
        Map<String,Trotineta> trotinete = new HashMap<>();
        try {
            FileInputStream fos = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fos);
            BufferedReader br = new BufferedReader(isr);
            String linie;
            while((linie = br.readLine()) != null) {
                String[] linieSeparata = linie.split(" ");
                String id = linieSeparata[0];
                float distantaTotala = Float.parseFloat(linieSeparata[1]);
                float vitezaMedie = Float.parseFloat(linieSeparata[2]);
                float vitezaMaxima = Float.parseFloat(linieSeparata[3]);
                Trotineta t = new Trotineta(id, distantaTotala, vitezaMedie, vitezaMaxima);
                trotinete.put(id, t);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trotinete;
    }

    public static void afiseazaVitezeMedii(Map<String, Trotineta> trotinete) {
        Map<Float, List<Trotineta>> trotineteVitezaMedie = new HashMap<>();
        trotinete.forEach((id, trotineta) -> {
            float vitezaMedie = trotineta.getVitezaMedie();
            trotineteVitezaMedie.putIfAbsent(vitezaMedie, new ArrayList<>());
            trotineteVitezaMedie.get(vitezaMedie).add(trotineta);
        });

        trotineteVitezaMedie.forEach((vitezaMedie, listaTrotinete) -> {
            int numarTrotinete = listaTrotinete.size();
            float sumaDistante = listaTrotinete.stream().map(Trotineta::getDistantaTotala).reduce(0f, Float::sum);
            System.out.println("Viteza medie " + vitezaMedie + " km/h -> " + numarTrotinete + " trotinete, suma distantelor parcurse " + sumaDistante + " km");
        });
    }

    public static void salveazaTrotineteRapide(Map<String, Trotineta> trotinete, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            trotinete.values().stream()
                    .filter(t -> t.getVitezaMedie() > 14 || t.getVitezaMaxima() > 50)
                    .forEach(t -> {
                        try {
                            oos.writeObject(t);
                        } catch (IOException e) {
                            System.err.println("Eroare la scrierea trotinetei " + t.getId() + ": " + e.getMessage());
                        }
                    });
        }
    }

    public static void afiseazaTrotineteRapide(String fileName) throws IOException, ClassNotFoundException {
        System.out.println("Trotinete rapide citite din " + fileName + ":");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof Trotineta) {
                    Trotineta trotineta = (Trotineta) obj;
                    System.out.println(trotineta);
                }
            }
        } catch (EOFException e) {
        }
    }

}

