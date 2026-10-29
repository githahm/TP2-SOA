package service;

import javax.jws.WebService;

@WebService(
        endpointInterface = "service.ICalculatrice",
        serviceName = "CalculatriceWS",
        portName = "CalculatricePort",
        targetNamespace = "http://service/"
)
public class Calculatrice implements ICalculatrice {

    @Override
    public double somme(double a, double b) {
        return a + b;
    }

    @Override
    public double soustraction(double a, double b) {
        return a - b;
    }

    @Override
    public double multiplication(double a, double b) {
        return a * b;
    }

    @Override
    public double division(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division par zero impossible");
        }
        return a / b;
    }
}