package tony.redit_clone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents a community in the Reddit clone.
 * A community is a group where users can post content, discuss topics, and
 * interact.
 * It contains details like name, description, members, and visual assets.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "communities")
public class Community {

    /**
     * The unique identifier for the community.
     */
    @Id
    private String id;

    /**
     * The name of the community. Must be unique.
     */
    private String name;

    /**
     * A brief description of what the community is about.
     */
    private String description;

    /**
     * A URL-friendly identifier for the community (e.g., "r/java-programming").
     */
    private String slug; // URL-friendly identifier: example, "r/java-programming"

    /**
     * The username of the user who created the community.
     */
    private String createdBy;

    /**
     * The timestamp when the community was created.
     */
    private Instant createdAt;

    /**
     * A list of usernames of the community moderators.
     */
    private List<String> moderators;

    /**
     * A list of usernames of the community members.
     */
    private List<String> members;

    /**
     * The total count of members in the community.
     */
    private Long memberCount;

    /**
     * A list of rules that govern the community.
     */
    private List<String> rules;

    /**
     * The file ID or URL of the banner image for the community.
     */
    private String bannerImage;

    /**
     * The file ID or URL of the icon image for the community.
     */
    private String iconImage;

    /**
     * Indicates whether the community is private (invite-only) or public.
     */
    private Boolean isPrivate;

    /**
     * A list of tags associated with the community for categorization and search.
     */
    private List<String> tags;
}
