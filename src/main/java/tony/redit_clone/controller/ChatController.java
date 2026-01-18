package tony.redit_clone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.Message;

/**
 * Controller for handling real-time chat messages.
 * This class manages message mapping and broadcasting using Spring's messaging
 * support.
 */
public class ChatController {
    /**
     * Processes a chat message and broadcasts it to all subscribers of the
     * "/topic/messages" topic.
     *
     * @param message the received chat message
     * @return the same message to be broadcasted
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        return message;
    }
}
