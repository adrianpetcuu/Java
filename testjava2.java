[12:05, 15.04.2024] Vlad Fac: Achizitii.java : package needed;

import java.io.BufferedReader;
import java.io.Serializable;

public class Achizitii implements Comparable<Achizitii>, Serializable {
    private String cod;
    private int[] d_achizitie = new int[3];
    private int cantitate;
    private int pret;

    public Achizitii(){
        cod = "";
        for(int i=0;i<d_achizitie.length;i++){
            d_achizitie[i] = 0;
        }
        cantitate = 0;
        pret = 0;
    }

    public Achizitii(String cod, int[] d_achizitie, int cantitate, int pret){
        this.cod = cod;
        for(int i=0;i<this.d_achizitie.length;i++){
            this.d_achizitie[i] = d_achizitie[i];
        }
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public static int valoare(int cantitate, int pret){
        return cantitate*pret;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Produs{");
        sb.append("Cod= ").append(getCod()).append(", Data= ").append(d_achizitie[0]).append("-").append(d_achizitie[1]).append("-");
        sb.append(d_achizitie[2]).append(", Cantitate= ").append(getCantitate()).append(", Pret= ").append(getPret());
        return sb.toString();
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int[] getD_achizitie() {
        return d_achizitie;
    }

    public void setD_achizitie(int[] d_achizitie) {
        this.d_achizitie = d_achizitie;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public int compareTo(Achizitii o) {
        int valoare1 = valoare(cantitate,pret);
        int valoare2 = valoare(o.cantitate, o.pret);
        if(valoare1 < valoare2){
            return -1;
        }else if(valoare1 == valoare2){
            return 0;
        }else {
            return 1;
        }
    }
}

main.java:  import needed.Achizitii;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Achizitii produs = new Achizitii("1234", new int[]{7,6,2022}, 100, 20);
        //Achizitii produs2 = new Achizitii("12234", new int[]{9,6,2032}, 120, 18);
       // System.out.println(produs);
       // System.out.println(produs2);
        //System.out.println(produs.compareTo(produs2));
        List<Achizitii> produse = citesteProduse("achizitii.txt");
        for(Achizitii produs : produse){
            int[] vect = new int[3];
            vect = produs.getD_achizitie();
           if(vect[2] > 31/2 && produs.getCantitate() > 100){
               //System.out.println(produs);
           }
        }
        for(Achizitii produs : produse){
            afisareBinar(produs, "produseFrecvente.dat");
        }

       citire_afisareBinar("produseFrecvente.dat");
    }

    public static List<Achizitii> citesteProduse(String filename){
        List<Achizitii> produse = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                if(tokens.length == 6){
                    String cod = tokens[0];
                    int zi = Integer.parseInt(tokens[1]);
                    int luna = Integer.parseInt(tokens[2]);
                    int an = Integer.parseInt(tokens[3]);
                    int cantitate = Integer.parseInt(tokens[4]);
                    int pret = Integer.parseInt(tokens[5]);

                    produse.add(new Achizitii(cod, new int[]{zi,luna,an},cantitate, pret));
                }
            }
            return produse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afisareBinar(Achizitii produs, String filename){
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))){
            outputStream.writeObject(produs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void citire_afisareBinar(String filename){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))){
            Achizitii produs = (Achizitii)objectInputStream.readObject();
            System.out.println(produs);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}











try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("cars.dat"))){
            outputStream.writeObject(cars);
            System.out.println("Fisierul binar a fost creat");
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("cars.dat"))){
            List<Car> masini = null;
            try {
                masini = (List<Car>)inputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            for(Car c : masini){
                System.out.println(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
