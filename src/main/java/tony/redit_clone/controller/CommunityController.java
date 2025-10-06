package tony.redit_clone.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tony.redit_clone.service.FileStorageService;
import tony.redit_clone.util.Try;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class CommunityController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/communities-creation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart(value = "banner", required = false) MultipartFile banner,
            @RequestPart(value = "icon", required = false) MultipartFile icon
    ) {
        Try<String> result = fileStorageService.createCommunity(name, description, banner, icon);
        if (result instanceof Try.Failure<String> f) {
            return "Error creating community: " + f.error().getMessage();
        }
        
        return "Successfully created community: ";
    }
    
}
