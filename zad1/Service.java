package zad1;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private final String country;
    private final String code;
    private final String currency;

    public Service(String country) {
        this.country = country;
        this.currency = getCurrency();
        this.code = getCode();
    }

    private static final String API_KEY = "ApiKey";

    public String getWeather(String city) {
        String koordy = getCoordinates(city);
        Map<String, String> coordinates = extractCoordinates(koordy);
        String url = buildWeatherUrl(coordinates.get("lat"), coordinates.get("lon"));
        return RequestorService.sendRequest(url);
    }

    private String getCoordinates(String city) {
        String geoApiUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, API_KEY);
        return RequestorService.sendRequest(geoApiUrl);
    }

    private Map<String, String> extractCoordinates(String coords) {
        Map<String, String> coordinates = new HashMap<>();
        int szer = coords.indexOf("\"lat\":") + "\"lat\":".length();
        int wys = coords.indexOf("\"lon\":") + "\"lon\":".length();
        String lat = coords.substring(szer, coords.indexOf(",", szer));
        String lon = coords.substring(wys, coords.indexOf(",", wys));
        coordinates.put("lat", lat);
        coordinates.put("lon", lon);
        return coordinates;
    }

    private String buildWeatherUrl(String szer, String wys) {
        return String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", szer, wys, API_KEY);
    }

    public Double getRateFor(String currency) {
        String urlString = String.format("https://open.er-api.com/v6/latest/%s", currency);
        String response = RequestorService.sendRequest(urlString);
        Gson gson = new Gson();
        CurrencyData data = gson.fromJson(response, CurrencyData.class);
        Double rate1 = data.getRates().get(currency);
        Double rate2 = data.getRates().get(this.currency);
        return rate1 / rate2;
    }

    public Double getNBPRate() {
        if (country.equals("Poland")) return 1.0;
        String response = RequestorService.sendRequest(String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s/?format=json", currency));
        Gson gson = new Gson();
        NBPRateData fromJson = gson.fromJson(response, NBPRateData.class);
        return fromJson.getRates().get(0).getMid();
    }

    private String getCode() {
        String response = RequestorService.sendRequest(String.format("https://restcountries.com/v3.1/name/%s?fullText=true", country));
        // Parsowanie odpowiedzi uzyw.json
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);

        // Czy odpowiedź zawiera pola
        if (jsonElement instanceof JsonObject) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("ccn3")) {
                String countryCode = jsonObject.get("ccn3").getAsString();
                return countryCode;
            }
        }
        //brak kodu kraju
        return "";
    }

    private String getCurrency() {
        String response = RequestorService.sendRequest(String.format("https://restcountries.com/v3.1/name/%s?fullText=true", country));
        String[] slicikWalut = response.split("\"currencies\"");
        if (slicikWalut.length > 1) {
            String currencyInfo = slicikWalut[1].split("\\{")[1].replaceAll("[\":]", "");
            return currencyInfo;
        }
        return "";
    }

}
