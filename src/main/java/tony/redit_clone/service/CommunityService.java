package tony.redit_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import tony.redit_clone.dto.CommunityResponse;
import tony.redit_clone.model.Community;
import tony.redit_clone.repository.CommunityRepository;
import tony.redit_clone.repository.FileStorageRepository;
import tony.redit_clone.util.Try;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {
    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private CommunityRepository communityRepository;

    

    public Try<String> createCommunity(String name, String description, MultipartFile banner,  MultipartFile icon) {
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
    public Try<List<CommunityResponse>> listCommunities() {
        Try<List<Community>> result = Try.success(communityRepository.findAll());
        return result.map(communities -> communities.stream().map(community -> {
            CommunityResponse response = new CommunityResponse();
            response.setName(community.getName());
            response.setDescription(community.getDescription());
            response.setBannerImageBytes(
                fileStorageRepository.getFile(community.getBannerImage()) instanceof Try.Success<String> s
                    ? s.value()
                    : ""
            );
            response.setIconImageBytes(
                fileStorageRepository.getFile(community.getIconImage()) instanceof Try.Success<String> s
                    ? s.value()
                    : ""
            );
            return response;
        }).toList());
    }
}
