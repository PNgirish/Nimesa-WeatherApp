package nimesa;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiUrl = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

        try {
            String weatherData = fetchData(apiUrl);
            JSONArray weatherList = new JSONObject(weatherData).getJSONArray("list");

            while (true) {
                System.out.println("1. Get Temperature");
                System.out.println("2. Get Wind Speed");
                System.out.println("3. Get Pressure");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        System.out.print("Enter date and time (YYYY-MM-DD HH:MM:SS): ");
                        String dateTime = scanner.nextLine();
                        double temperature = getTemperature(weatherList, dateTime);
                        System.out.println("Temperature: " + temperature + "°C");
                        break;
                    case 2:
                        System.out.print("Enter date and time (YYYY-MM-DD HH:MM:SS): ");
                        dateTime = scanner.nextLine();
                        double windSpeed = getWindSpeed(weatherList, dateTime);
                        System.out.println("Wind Speed: " + windSpeed + " m/s");
                        break;
                    case 3:
                        System.out.print("Enter date and time (YYYY-MM-DD HH:MM:SS): ");
                        dateTime = scanner.nextLine();
                        double pressure = getPressure(weatherList, dateTime);
                        System.out.println("Pressure: " + pressure + " hPa");
                        break;
                    case 0:
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error fetching data from API: " + e.getMessage());
        }
    }

    public static String fetchData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } else {
            throw new IOException("API request failed with response code: " + responseCode);
        }
    }

    public static double getTemperature(JSONArray weatherList, String dateTime) {
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject weather = weatherList.getJSONObject(i);
            if (weather.getString("dt_txt").equals(dateTime)) {
                JSONObject main = weather.getJSONObject("main");
                return main.getDouble("temp");
            }
        }
        return Double.NaN;
    }

    public static double getWindSpeed(JSONArray weatherList, String dateTime) {
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject weather = weatherList.getJSONObject(i);
            if (weather.getString("dt_txt").equals(dateTime)) {
                JSONObject wind = weather.getJSONObject("wind");
                return wind.getDouble("speed");
            }
        }
        return Double.NaN;
    }

    public static double getPressure(JSONArray weatherList, String dateTime) {
        for (int i = 0; i < weatherList.length(); i++) {
            JSONObject weather = weatherList.getJSONObject(i);
            if (weather.getString("dt_txt").equals(dateTime)) {
                JSONObject main = weather.getJSONObject("main");
                return main.getDouble("pressure");
            }
        }
        return Double.NaN;
    }
}

