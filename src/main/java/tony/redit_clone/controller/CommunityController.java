package tony.redit_clone.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tony.redit_clone.dto.CommunityResponse;
import tony.redit_clone.model.Community;
import tony.redit_clone.service.CommunityService;
import tony.redit_clone.util.Try;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @PostMapping(value = "/communities-creation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart(value = "banner", required = false) MultipartFile banner,
            @RequestPart(value = "icon", required = false) MultipartFile icon
    ) {
        Try<String> result = communityService.createCommunity(name, description, banner, icon);
        if (result instanceof Try.Failure<String> f) {
            return "Error creating community: " + f.error().getMessage();
        }
        
        return "Successfully created community: ";
    }

    @GetMapping(value="/communities-list")
    public List<CommunityResponse> communitysList() {
        Try<List<CommunityResponse>> result = communityService.listCommunities();
        if (result instanceof Try.Failure<List<CommunityResponse>> f) {
            return List.of();
        }
        return ((Try.Success<List<CommunityResponse>>) result).value(); 
    }
    
}
