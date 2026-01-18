package tony.redit_clone.controller;

import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.repository.PostRepository;
import tony.redit_clone.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

/**
 * Controller for managing posts.
 * Provides endpoints for creating and retrieving posts.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class PostController {
    @Autowired
    private PostRepository repo;

    /**
     * Creates a new post.
     *
     * @param post the post to be created
     * @return the created post object
     */
    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        return repo.save(post);
    }

    /**
     * Retrieves all posts.
     *
     * @return a list of all posts
     */
    @GetMapping("/posts")
    public List<Post> all() {
        return repo.findAll();
    }
}
