package tony.redit_clone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.dto.ChatRoomMessage;

/**
 * Controller for handling real-time chat messages.
 * This class manages message mapping and broadcasting using Spring's messaging
 * support.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final tony.redit_clone.service.AiChatService aiChatService;

    public ChatController(tony.redit_clone.service.AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * Processes a chat message and broadcasts it to all subscribers of the
     * "/topic/messages" topic.
     *
     * @param message the received chat message
     * @return the same message to be broadcasted
     */
    @MessageMapping("/messages/chat")
    @SendTo("/topic/messages")
    public ChatRoomMessage sendMessage(ChatRoomMessage message) {
        return aiChatService.getAiResponse(message);
    }
}
