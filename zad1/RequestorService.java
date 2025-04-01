/**
 *
 *  @author Jaros Jakub S24479
 *
 */



package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestorService {
    public static String sendRequest(String request) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(request);

            // Otwórz połączenie URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Ustaw metodę na GETa
            conn.setRequestMethod("GET");

            // Stwórz strumienia wejściowego z połączenia
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Zwróć odpowied
        return response.toString();
    }

}