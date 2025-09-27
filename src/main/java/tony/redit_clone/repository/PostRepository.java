package tony.redit_clone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tony.redit_clone.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {
}
