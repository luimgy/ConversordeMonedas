package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Cliente HTTP para interactuar con la API de ExchangeRate-API y obtener tasas de cambio,
 * utilizando la API moderna HttpClient de Java 11+.
 */
public class ClienteHTTPDivisas {

    private static final String API_KEY = "cdfe9c8117c115880488ef4a"; // Tu clave API
    // Creamos una única instancia de HttpClient para ser reutilizada (es más eficiente)
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    /**
     * Realiza una solicitud HTTP GET a la URL dada y devuelve el cuerpo de la respuesta como un String.
     */
    private static String obtenerRespuestaDeAPI(String urlString) throws Exception {
        // Fase 5: Construyendo la Solicitud (HttpRequest)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();

        // Fase 4 y 6: Usando HttpClient para enviar la solicitud y recibiendo la respuesta (HttpResponse)
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Error al consultar la API. Código de respuesta: " + response.statusCode());
        }
    }

    /**
     * Obtiene la tasa de cambio entre dos divisas.
     */
    public static double obtenerTasaCambio(String divisaBase, String divisaObjetivo) throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + divisaBase;
        String responseBody = obtenerRespuestaDeAPI(url);

        // Fase 7: Analizando la respuesta en formato JSON
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        if (conversionRates != null && conversionRates.has(divisaObjetivo)) {
            return conversionRates.get(divisaObjetivo).getAsDouble();
        } else {
            throw new IllegalArgumentException("La divisa objetivo '" + divisaObjetivo + "' no se encuentra.");
        }
    }

    /**
     * Obtiene la lista completa de divisas soportadas por la API.
     */
    public static List<String> obtenerDivisasSoportadas() throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";
        String responseBody = obtenerRespuestaDeAPI(url);

        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        if (conversionRates != null) {
            Set<String> keys = conversionRates.keySet();
            List<String> divisas = new ArrayList<>(keys);
            Collections.sort(divisas); // Ordenamos alfabéticamente
            return divisas;
        } else {
            throw new RuntimeException("No se encontraron tasas de conversión en la respuesta.");
        }
    }
}