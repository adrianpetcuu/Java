import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Seminar_Xml_Json {

    public static List<Produs> citireXml(String caleFisier) throws Exception{
        List<Produs> produse = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(caleFisier));
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("produs");
        for(int i = 0; i < nodeList.getLength(); i++){
            Element element = (Element)nodeList.item(i);

            int cod = Integer.parseInt(element.getElementsByTagName("cod").item(0).getTextContent());
            String denumire = element.getElementsByTagName("denumire").item(0).getTextContent();
            NodeList trzNodeList = element.getElementsByTagName("tranzactie");
            List<Tranzactie> tranzactii = new ArrayList<>();
            for(int j = 0; j < trzNodeList.getLength(); j++){
                Element elementTranzactie = (Element)trzNodeList.item(j);
                TipTranzactie tip = TipTranzactie.valueOf(elementTranzactie.getAttribute("tip").toUpperCase());
                int cantitate = Integer.parseInt(elementTranzactie.getAttribute("cantitate"));

                Tranzactie tranzactie = new Tranzactie(tip, cantitate);
                tranzactii.add(tranzactie);
            }
            Produs produs = new Produs(cod, denumire, tranzactii);
            produse.add(produs);
        }
        return produse;
    }

    public static void salvareXml(String caleFisier, List<Produs> produse) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("produse");
        doc.appendChild(root);

        for(Produs produs : produse){
            Element elementProdus = doc.createElement("produs");

            Element codProdus = doc.createElement("cod");
            codProdus.setTextContent(String.valueOf(produs.getCod()));
            Element denumireProdus = doc.createElement("denumire");
            denumireProdus.setTextContent(produs.getDenumire());
            Element tranzactiiProdus = doc.createElement("tranzactii");
            for(Tranzactie tranzactie : produs.getTranzactii()){
                Element elementTranzactie = doc.createElement("tranzactie");

                elementTranzactie.setAttribute("tip", String.valueOf(tranzactie.getTip()).toLowerCase());
                elementTranzactie.setAttribute("cantitate", String.valueOf(tranzactie.getCantitate()));
                tranzactiiProdus.appendChild(elementTranzactie);
            }

            elementProdus.appendChild(codProdus);
            elementProdus.appendChild(denumireProdus);
            elementProdus.appendChild(tranzactiiProdus);

            root.appendChild(elementProdus);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        // Setăm proprietăți pentru formatare și indentare
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(caleFisier);

        transformer.transform(source, streamResult);
    }

    public static void salvareJson(String caleFisier, List<Produs> produse){
        try (FileOutputStream fos = new FileOutputStream(caleFisier);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw)) {
            JSONArray jsonArray = new JSONArray();
            for(Produs produs : produse){
                JSONObject objProdus = new JSONObject();
                objProdus.put("cod", produs.getCod());
                objProdus.put("denumire", produs.getDenumire());
                JSONArray arrayTranzactii = new JSONArray();
                for(Tranzactie tranzactie : produs.getTranzactii()){
                    JSONObject objTranzactie = new JSONObject();
                    objTranzactie.put("cantitate", tranzactie.getCantitate());
                    objTranzactie.put("tip", tranzactie.getTip().toString().toLowerCase());
                    arrayTranzactii.put(objTranzactie);
                }
                objProdus.put("tranzactii", arrayTranzactii);
                jsonArray.put(objProdus);
            }
            bw.write(jsonArray.toString(4));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Produs> citireJson(String caleFisier) throws Exception {
        List<Produs> produseDinJson = new ArrayList<>();
        FileInputStream fis = new FileInputStream(caleFisier);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        JSONTokener tokener = new JSONTokener(br);
        JSONArray jsonArray = new JSONArray(tokener);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject objProdus = jsonArray.getJSONObject(i);
            int cod = objProdus.getInt("cod");
            String denumire = objProdus.getString("denumire");
            List<Tranzactie> tranzactii = new ArrayList<>();
            JSONArray arrayTranzactii = objProdus.getJSONArray("tranzactii");
            for(int j = 0; j < arrayTranzactii.length(); j++){
                JSONObject objTranzactie = arrayTranzactii.getJSONObject(j);
                TipTranzactie tip = TipTranzactie.valueOf(objTranzactie.getString("tip").toUpperCase());
                int cantitate = objTranzactie.getInt("cantitate");
                Tranzactie tranzactie = new Tranzactie(tip, cantitate);
                tranzactii.add(tranzactie);
            }
            Produs produs = new Produs(cod, denumire, tranzactii);
            produseDinJson.add(produs);
        }
        return produseDinJson;
    }

    public static void main(String[] args) throws Exception {
        List<Produs> produse = citireXml("dateIN\\produse.xml");
        produse.forEach(System.out::println);

        salvareXml("dateOUT\\produseXml.xml", produse);

        salvareJson("dateOUT\\produseJson.json", produse);

        List<Produs> produseDinJson = citireJson("dateOUT\\produseJson.json");
        System.out.println();
        System.out.println();
        produseDinJson.forEach(System.out::println);
    }

}
