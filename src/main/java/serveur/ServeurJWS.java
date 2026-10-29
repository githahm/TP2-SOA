package serveur;

import service.Calculatrice;

import javax.xml.ws.Endpoint;

public class ServeurJWS {

    private static final String URL = "http://localhost:8080/calculatrice";

    public static void main(String[] args) {
        try {
            Endpoint.publish(URL, new Calculatrice());
            System.out.println("Service publie : " + URL);
            System.out.println("WSDL : " + URL + "?wsdl");
            System.out.println("XSD  : " + URL + "?xsd=1");
        } catch (Exception e) {
            System.out.println("Echec de la publication : " + e.getMessage());
        }
    }
}