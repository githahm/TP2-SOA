# CalculatriceWS – SOAP Web Service with Jakarta XML Web Services

TP2 – Architecture SOA et services web (LSI3-GLSI, 2025-2026).

A Java application that exposes a SOAP web service performing four simple operations
(sum, subtraction, multiplication, division), published with `Endpoint.publish` and
tested with SoapUI.

## Architecture

```
CalculatriceWS/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── service/
            │   ├── ICalculatrice.java
            │   └── Calculatrice.java
            └── serveur/
                └── ServeurJWS.java
```

| File | Role |
|------|------|
| `pom.xml` | Maven configuration (Java 1.8, JAX-WS dependencies) |
| `service/ICalculatrice.java` | Service interface annotated with `@WebService`, `@WebMethod`, `@WebParam`, `@WebResult` |
| `service/Calculatrice.java` | Business implementation of the interface |
| `serveur/ServeurJWS.java` | Publishes the service with `Endpoint.publish` |

## Maven dependencies

| Dependency | Purpose |
|------------|---------|
| `jakarta.xml.ws:jakarta.xml.ws-api:2.3.3` | JAX-WS API (`Endpoint`, ...) |
| `jakarta.jws:jakarta.jws-api:2.1.0` | Annotations (`@WebService`, `@WebMethod`, `@WebParam`) |
| `com.sun.xml.ws:jaxws-rt:2.3.3` | Runtime that executes the service |

These versions belong to the `javax.*` package family, so the imports are
`javax.jws.*` and `javax.xml.ws.*`.

## Service design

- **Interface and class annotated with `@WebService`**: the interface defines the contract,
  the class links to it with `endpointInterface`.
- **`@WebMethod`** gives each operation an explicit name in the WSDL
  (`somme`, `soustraction`, `multiplication`, `division`).
- **`@WebParam`** names the parameters `a` and `b`, so the XSD and SoapUI requests are readable.
- **`@WebResult`** names the returned value `resultat`.
- **Division by zero** throws an `IllegalArgumentException`, returned to the client as a SOAP fault.

## Prerequisites

- JDK 8 or newer
- IntelliJ IDEA
- SoapUI (open-source edition)
- Internet access on the first build (Maven downloads the dependencies)
- Port `8080` free (otherwise change `URL` in `ServeurJWS.java`)

## Running the service in IntelliJ IDEA

1. Open IntelliJ IDEA and choose **File > Open**, then select the `CalculatriceWS` folder
   (the one containing `pom.xml`). Confirm **Open as Project**.
2. Wait for Maven to import the project, or use **Maven > Reload project**. Check that the
   Project SDK is set under **File > Project Structure > Project**.
3. Open `src/main/java/serveur/ServeurJWS.java`.
4. Click the green arrow next to `public static void main` and choose **Run 'ServeurJWS.main()'**.
5. The console prints the service URL. The program keeps running: this is the server.
   Stop it with the red square button when you are done.

## Service endpoints

| Resource | URL |
|----------|-----|
| Service | `http://localhost:8080/calculatrice` |
| WSDL | `http://localhost:8080/calculatrice?wsdl` |
| Generated XSD | `http://localhost:8080/calculatrice?xsd=1` |

Open the WSDL and the XSD in a browser to see how the Java class maps to the contract
(messages, types, port type, binding).

## Testing with SoapUI

1. Launch SoapUI and choose **File > New SOAP Project**.
2. Paste `http://localhost:8080/calculatrice?wsdl` as the **Initial WSDL** and click **OK**.
3. Expand an operation (for example `somme`) and double-click **Request 1**.
4. Replace the `?` placeholders with values:

```xml
<a>5</a>
<b>3</b>
```

5. Click the green arrow to send the request. The response contains:

```xml
<resultat>8.0</resultat>
```

Expected results with `a = 5` and `b = 3`:

| Operation | Result |
|-----------|--------|
| `somme` | 8.0 |
| `soustraction` | 2.0 |
| `multiplication` | 15.0 |
| `division` | 1.6666666666666667 |

With `b = 0`, `division` returns a SOAP fault containing the message
`Division par zero impossible`.

## Troubleshooting

- **`Address already in use`**: another application uses port 8080. Change the port in
  `ServeurJWS.java` and use the new URL in SoapUI.
- **Dependencies not found**: run **Maven > Reload project** and check your internet connection.
- **WSDL not reachable**: make sure `ServeurJWS` is still running before importing the WSDL in SoapUI.
- **Firewall prompt on Windows**: click **Allow access**.