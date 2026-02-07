package tony.redit_clone.dto;

/**
 * Data Transfer Object for chat room messages.
 * Represents a message sent in a chat room with sender information, content, and type.
 *
 * @param sender  the username of the message sender
 * @param content the message content
 * @param type    the message type (e.g., "CHAT", "JOIN", "LEAVE")
 */
public record ChatRoomMessage(String sender, String content, String type) {
}
