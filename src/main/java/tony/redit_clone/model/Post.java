package tony.redit_clone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a post created by a user within a community.
 * A post consists of a title, content, and is identified by a unique ID.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    /**
     * The unique identifier for the post.
     */
    @Id
    private String id;

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The main content or body of the post.
     */
    private String content;
}
