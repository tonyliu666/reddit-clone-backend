package tony.redit_clone.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import tony.redit_clone.model.Community;


public interface CommunityRepository extends MongoRepository<Community, String> {
    
    java.util.Optional<Community> findOneByName(String name);
}
