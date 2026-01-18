package tony.redit_clone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tony.redit_clone.model.Post;

/**
 * Repository interface for {@link Post} entities.
 * Uses MongoDB for persistent storage.
 */
public interface PostRepository extends MongoRepository<Post, String> {
}
