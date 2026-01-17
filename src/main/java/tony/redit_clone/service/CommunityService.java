package tony.redit_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import tony.redit_clone.aspect.RateLimited;
import tony.redit_clone.dto.CommunityResponse;
import tony.redit_clone.model.Community;
import tony.redit_clone.repository.CommunityRepository;
import tony.redit_clone.repository.FileStorageRepository;
import tony.redit_clone.util.Try;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Service
public class CommunityService {
    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Creates a new community with the specified details and images.
     *
     * @param name        the name of the community (must be unique)
     * @param description a brief description of the community
     * @param banner      the banner image file for the community
     * @param icon        the icon image file for the community
     * @return a {@link Try} containing the ID of the created community if
     *         successful,
     *         or a {@link Try.Failure} if validation fails or an error occurs.
     * @implSpec
     *           This implementation first checks if a community with the given name
     *           already exists using
     *           {@link CommunityRepository#findOneByName(String)}.
     *           If unique, it initializes a new {@link Community} entity and saves
     *           the banner and icon images using
     *           {@link FileStorageRepository#saveFile(MultipartFile)}.
     *           Finally, it persists the community entity to the database.
     */
    public Try<String> createCommunity(String name, String description, MultipartFile banner, MultipartFile icon) {
        // check name is unique
        if (communityRepository.findOneByName(name).isPresent()) {
            return Try.failure(new RuntimeException("Community name already exists"));
        }
        Community community = new Community();
        community.setName(name);
        community.setDescription(description);

        Try<String> bannerId = this.fileStorageRepository.saveFile(banner);
        if (bannerId instanceof Try.Failure<String> f) {
            return Try.failure(f.error());
        }
        Try<String> iconId = this.fileStorageRepository.saveFile(icon);
        if (iconId instanceof Try.Failure<String> f) {
            return Try.failure(f.error());
        }
        community.setBannerImage(((Try.Success<String>) bannerId).value());
        community.setIconImage(((Try.Success<String>) iconId).value());
        this.communityRepository.save(community);
        return Try.success(community.getId());
    }

    /**
     * Retrieves a list of all existing communities.
     *
     * @return a {@link Try} containing a list of {@link CommunityResponse} objects
     *         representing all communities.
     * @implSpec
     *           This implementation retrieves all {@link Community} entities from
     *           {@link CommunityRepository#findAll()}. It then streams the results
     *           to map each entity to a {@link CommunityResponse}.
     *           <p>
     *           For each community, it attempts to retrieve the banner and icon
     *           image
     *           content from {@link FileStorageRepository#getFile(String)}. If
     *           retrieving
     *           an image fails, an empty string is used as a fallback for the image
     *           bytes.
     */
    public Try<List<CommunityResponse>> listCommunities() {
        Try<List<Community>> result = Try.success(communityRepository.findAll());
        return result.map(communities -> communities.stream().map(community -> {
            CommunityResponse response = new CommunityResponse();
            response.setName(community.getName());
            response.setDescription(community.getDescription());
            response.setBannerImageBytes(
                    fileStorageRepository.getFile(community.getBannerImage()) instanceof Try.Success<String> s
                            ? s.value()
                            : "");
            response.setIconImageBytes(
                    fileStorageRepository.getFile(community.getIconImage()) instanceof Try.Success<String> s
                            ? s.value()
                            : "");
            return response;
        }).toList());
    }

    /**
     * Triggers a test retry operation with exponential backoff.
     * The operation will fail and be retried according to the configured policy.
     *
     * @implSpec
     *           This implementation uses {@link Retryable} to retry the method up
     *           to 3 times.
     *           The backoff strategy is defined by {@link Backoff} with an initial
     *           delay of
     *           1000ms and a multiplier of 3.0, resulting in increasing delays
     *           between attempts
     *           (e.g., 1s -> 3s -> 9s).
     *           <p>
     *           The method is also annotated with {@link RateLimited} to enforce
     *           rate limits.
     *           It delegates the actual execution to {@link #privateRetry()}.
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, // initial delay = 1 sec
            multiplier = 3.0 // exponential backoff (1s → 3s → 9s → 27s → 81s)
    ))
    @RateLimited
    public void testRetry() {
        privateRetry();
    }

    private void privateRetry() {
        String threadName = Thread.currentThread().getName();
        long now = System.currentTimeMillis();
        System.out.println("Retry attempt at: " + now + " on thread: " + threadName);

        restTemplate.getForObject(
                "http://localhost:8080/api/v1/failed",
                String.class);
    }
}
