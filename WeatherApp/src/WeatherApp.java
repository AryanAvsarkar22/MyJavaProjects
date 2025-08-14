import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {
    public static void main(String[] args) {
        String apiKey = "YOUR_API_KEY"; // Replace with your OpenWeatherMap API key
        String city = "London"; // Change as needed
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONObject json = new JSONObject(response.toString());
            System.out.println("Weather in " + city + ":");
            System.out.println("Temperature: " + json.getJSONObject("main").getDouble("temp") + "°C");
            System.out.println("Condition: " + json.getJSONArray("weather").getJSONObject(0).getString("description"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
