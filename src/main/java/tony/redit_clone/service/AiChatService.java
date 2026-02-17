package tony.redit_clone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tony.redit_clone.dto.ChatRoomMessage;

@Service
public class AiChatService {

    private final RestClient restClient;

    public AiChatService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public ChatRoomMessage getAiResponse(ChatRoomMessage message) {
        if (message == null) {
            return new ChatRoomMessage("System", "Empty message received", "ERROR");
        }
        ChatRoomMessage response = restClient.post()
                .uri("/chat")
                .body(message)
                .retrieve()
                .body(ChatRoomMessage.class);

        if (response == null) {
            return new ChatRoomMessage("System", "Failed to get response from AI", "ERROR");
        }
        return response;
    }
}
