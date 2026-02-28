package tony.redit_clone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tony.redit_clone.dto.ChatReply;
import tony.redit_clone.dto.ChatRoomMessage;

@Service
public class AiChatService {

    private final RestClient restClient;

    public AiChatService(RestClient.Builder restClientBuilder,
            @Value("${mcp.server.url}") String mcpServerUrl) {
        this.restClient = restClientBuilder.baseUrl(mcpServerUrl).build();
    }

    public ChatRoomMessage getAiResponse(ChatRoomMessage message) {
        if (message == null) {
            return new ChatRoomMessage("System", "Empty message received", "ERROR");
        }
        ChatReply response = restClient.post()
                .uri("/chat")
                .body(message)
                .retrieve()
                .body(ChatReply.class);
        if (response == null || response.reply() == null) {
            return new ChatRoomMessage("System", "Failed to get response from AI", "ERROR");
        }

        return new ChatRoomMessage("AI", response.reply(), "CHAT");
    }
}
