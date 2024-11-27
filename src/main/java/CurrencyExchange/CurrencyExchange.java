package CurrencyExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyExchange {

    private static Scanner scanner;
    private static Map<String, Double> cache;
    private static String fromCurrency;

    public CurrencyExchange(){
        scanner = new Scanner(System.in);
        cache = new HashMap<>();
        fromCurrency = getFromCurrency();
    }

    public static void main(String[] args) {
        new CurrencyExchange().exchangeCurrency();
    }

    public void exchangeCurrency() {
        if (fromCurrency == null) {
            return;
        }

        if (fromCurrency.equals("usd")) {
            cache.put("eur", getExchangeRate("eur"));
        }
        else if (fromCurrency.equals("eur")) {
            cache.put("usd", getExchangeRate("usd"));
        } else {
            cache.put("eur", getExchangeRate("eur"));
            cache.put("usd", getExchangeRate("usd"));
        }

        while (true) {
            String toCurrency = getToCurrency();
            if (toCurrency.isEmpty()) {
                return;
            }

            if (toCurrency.equals(fromCurrency)) {
                System.out.println("You cannot exchange the same currency.");
                continue;
            }

            double money = getAmountOfMoney();

            double exchangeRate;
            if (checkCache(toCurrency)) {
                exchangeRate = cache.get(toCurrency);
            } else {
                exchangeRate = getExchangeRate(toCurrency);
                cache.put(toCurrency, exchangeRate);
            }

            if (exchangeRate != -1) {
                double exchangeResult = money * exchangeRate;
                System.out.printf("You received %.2f %s\n\n", exchangeResult, toCurrency.toUpperCase());
            }

        }
    }

    private static String getFromCurrency() {
        while (true) {
            System.out.print("Enter the currency you have (or press 'Enter' to exit): > ");
            String userInput = scanner.nextLine().toLowerCase();

            if (userInput.isEmpty()) {
                return null;
            }

            try {
                URL url = new URL("https://www.floatrates.com/daily/" + userInput + ".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                connection.disconnect();

                if (responseCode == 200) {
                    return userInput;
                } else {
                    System.out.println("Invalid currency code or server error.");
                }
            } catch (Exception e) {
                System.out.println("Error fetching exchange rates: " + e.getMessage());
            }
        }
    }

    private static String getToCurrency() {
        System.out.print("Enter the currency you want to get: > ");
        return scanner.nextLine().toLowerCase();
    }

    private static double getExchangeRate(String toCurrency) {
        String url = "https://www.floatrates.com/daily/" + fromCurrency + ".json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject rates = new JSONObject(response.body());

                if (rates.has(toCurrency)) {
                    return rates.getJSONObject(toCurrency).getDouble("rate");
                } else {
                    throw new JSONException("Currency '" + toCurrency + "' not found. Try again.");
                }
            } else {
                throw new IOException("Error fetching exchange rates: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private static boolean checkCache(String toCurrency) {
        System.out.println("Checking the cache...");
        if (cache.containsKey(toCurrency)) {
            System.out.println("It is in the cache!");
            return true;
        } else {
            System.out.println("Sorry, but it is not in the cache!");
            return false;
        }
    }

    private static double getAmountOfMoney() {
        while (true) {
            System.out.print("Enter the amount of money you want to exchange: > ");
            try {
                String userInput = scanner.nextLine();
                double money = Double.parseDouble(userInput);

                if (money <= 0) {
                    throw new NumberFormatException();
                } else {
                    return money;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
