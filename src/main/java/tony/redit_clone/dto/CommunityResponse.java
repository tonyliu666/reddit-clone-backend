package tony.redit_clone.dto;

import lombok.Data;

@Data
public class CommunityResponse {
    private String name; 
    private String description;
    private String bannerImageBytes;
    private String iconImageBytes;
}
