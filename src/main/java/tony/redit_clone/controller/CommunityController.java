package tony.redit_clone.controller;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tony.redit_clone.model.Community;
import tony.redit_clone.repository.CommunityRepository;
import tony.redit_clone.service.FileStorageService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class CommunityController {

    @Autowired
    private CommunityRepository communityRepo;
    @Autowired
    private FileStorageService storageService;

    @PostMapping(value = "/communities-creation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart(value = "banner", required = false) MultipartFile banner,
            @RequestPart(value = "icon", required = false) MultipartFile icon
    ) {
        Community community = new Community();
        community.setName(name);
        community.setDescription(description);

        // optional: save banner & icon into GridFS, DB, or file system
        if (banner != null && !banner.isEmpty()) {
            System.out.println("Banner file: " + banner.getOriginalFilename());
            // check whether the same file name exists in mongoDB
            try {
                String bannerFileId = storageService.store(banner);
                community.setBannerImage(bannerFileId);
                System.out.println("Stored banner with ID: " + bannerFileId);
            } catch (RuntimeException e) {
                return "File with name '" + banner.getOriginalFilename() + "' already exists.";
            }    
        }
        if (icon != null && !icon.isEmpty()) {
            try {
                String iconFileId = storageService.store(icon);
                community.setIconImage(iconFileId);
                System.out.println("Stored icon with ID: " + iconFileId);
            } catch (RuntimeException e) {
                return "File with name '" + icon.getOriginalFilename() + "' already exists.";
            }
        }
        communityRepo.save(community);
        return "Successfully created community: " + community.getName();
    }

    
}
