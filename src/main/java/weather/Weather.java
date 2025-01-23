package weather;

import java.net.http.HttpClient;
import java.net.http.HttpRequest; 
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.net.URI;
import java.io.IOException;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;

public class Weather {
    private final String BASE_URL = "http://api.weatherstack.com/";
    private final HttpClient client;
    private final String KEY = "?access_key=eb35d0d175e215d00c503cb916f4c244";

    public Weather(){
        client = HttpClient.newHttpClient();
    }

    public String findWeather(String location, String type) throws IOException, InterruptedException {
        String str = "";
        if(type.equalsIgnoreCase("current")){
            str = findCurrent(location);
        } else if (type.equalsIgnoreCase("yesterday's")){
            str = findHistorical(location);
        } else {
            str = "error";
        }
        return str;
    }

    public String findCurrent(String location)  throws IOException, InterruptedException{
        String url = BASE_URL + "current" + KEY + "&query=" + convertString(location);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String str = "";
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject info = jsonResponse.getJSONObject("current");
            int temperature = info.getInt("temperature");
            str = "Current Temperature in " + location + " is: " + Integer.toString(temperature) + "°C";
        } else {
            str = "error";
        }
        return str;
    }

    public String findHistorical(String location) throws IOException, InterruptedException{
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = yesterday.format(dateFormat);

        String url = BASE_URL + "historical" + KEY + "&query=" + convertString(location) + "&historical_date=" +  date;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());  

        String str = "";
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject yesterdayInfo = jsonResponse.getJSONObject("historical");
            if (yesterdayInfo.has(date)) {
                JSONObject info = yesterdayInfo.getJSONObject(date);
                int temperature = info.getInt("avgtemp");
                str = "Yesterday's Average Temperature in " + location + " is: " + Integer.toString(temperature) + "°C";
            } else {
                str = "Yesterday's Average Temperature in " + location + " doesn't exist";
            }
        }  else {
            str = "error";
        }
        return str;
    }

    public String convertString(String str){
        String modStr = str;
        if(str.contains(" ")){
            modStr = str.replace(" ", "%");
        }
        return modStr;
    }
}
