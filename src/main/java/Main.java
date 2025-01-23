
import weather.Weather;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Weather weatherClient = new Weather();
        while(true){
            System.out.print("Enter the city of where you would like the weather from (enter q to quit): ");
            String weatherLocation = scanner.nextLine();
            if(weatherLocation.equals("q")){
                break;
            }
            // Code to add yesterday's weather feature, can't retrieve information due to current API subscription plan
            // System.out.print("Enter if you would like the current or yesterday's weather (enter q to quit): ");
            // String weatherType = scanner.nextLine();
            // if(weatherType.equals("q")){
            //     break;
            // }
            // String info = weatherClient.findWeather(weatherLocation, weatherType);
            String info = weatherClient.findCurrent(weatherLocation);
            if(info.equals("error")){
                System.out.println("error");
                break;
            } 
            System.out.println(info);
        }
        scanner.close();
        System.out.println("exiting program");
    }
}