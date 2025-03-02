import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CGroqApiClient {
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama3-70b-8192"; // You can change this to another Groq model
    private static final int TIMEOUT_SECONDS = 30;

    private final String apiKey;
    private final HttpClient client;

    public CGroqApiClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    public String generateResponse(String userMessage) throws IOException, InterruptedException {
        // Prepare the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1024);

        JSONArray messages = new JSONArray();

        // System message to set context
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful AI assistant.");
        messages.put(systemMessage);

        // User message
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.put(userMsg);

        requestBody.put("messages", messages);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check if request was successful
        if (response.statusCode() != 200) {
            throw new IOException("API request failed with status code: " + response.statusCode() +
                    ", response: " + response.body());
        }

        // Parse and extract the generated text
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray choices = responseJson.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");

        return message.getString("content");

    }
}
