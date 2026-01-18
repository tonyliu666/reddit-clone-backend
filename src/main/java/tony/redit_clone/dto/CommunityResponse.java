package tony.redit_clone.dto;

import lombok.Data;

/**
 * Data Transfer Object for Community responses.
 * Contains the community details and base64 encoded image data to be sent to
 * the client.
 */
@Data
public class CommunityResponse {
    private String name;
    private String description;
    private String bannerImageBytes;
    private String iconImageBytes;
}
