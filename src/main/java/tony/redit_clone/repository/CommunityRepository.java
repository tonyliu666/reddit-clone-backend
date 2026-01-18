package tony.redit_clone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tony.redit_clone.model.Community;

/**
 * Repository interface for {@link Community} entities.
 * Uses MongoDB for persistent storage.
 */
public interface CommunityRepository extends MongoRepository<Community, String> {

    /**
     * Finds a community by its unique name.
     *
     * @param name the name of the community to search for
     * @return an {@link java.util.Optional} containing the community if found, or
     *         empty otherwise
     */
    java.util.Optional<Community> findOneByName(String name);
}
