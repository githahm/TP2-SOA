package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "ICalculatrice", targetNamespace = "http://service/")
public interface ICalculatrice {

    @WebMethod(operationName = "somme")
    @WebResult(name = "resultat")
    double somme(@WebParam(name = "a") double a, @WebParam(name = "b") double b);

    @WebMethod(operationName = "soustraction")
    @WebResult(name = "resultat")
    double soustraction(@WebParam(name = "a") double a, @WebParam(name = "b") double b);

    @WebMethod(operationName = "multiplication")
    @WebResult(name = "resultat")
    double multiplication(@WebParam(name = "a") double a, @WebParam(name = "b") double b);

    @WebMethod(operationName = "division")
    @WebResult(name = "resultat")
    double division(@WebParam(name = "a") double a, @WebParam(name = "b") double b);
}