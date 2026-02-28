package tony.redit_clone.dto;

/**
 * Data Transfer Object for chat response from the Go backend.
 * 
 * @param reply the message content from the AI
 */
public record ChatReply(String reply) {
}
