import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Groq AI Chat Interface =====");
        System.out.print("Please enter your Groq API key: ");
        String apiKey = scanner.nextLine().trim();

        CGroqApiClient client = new CGroqApiClient(apiKey);

        try {
            while (true) {
                System.out.print("\nEnter your message (or 'exit' to quit): ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.println("\n[Sending request to Groq...]");
                try {
                    String response = client.generateResponse(userInput);
                    System.out.println("\n===== GROQ RESPONSE =====");
                    System.out.println(response);
                    System.out.println("=========================");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
            System.out.println("\nThank you for using the Groq AI Chat Interface!");
        }
    }
}