package tony.redit_clone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "communities")
public class Community {
    
    @Id
    private String id;                

    // name should be unique
    @Indexed(unique = true)
    private String name;              
    private String description;       
    private String slug;         // URL-friendly identifier: example, "r/java-programming"      
    
    private String createdBy;        
    private Instant createdAt;      

    private List<String> moderators; 
    private List<String> members;    
    private Long memberCount;         

    private List<String> rules;             
    private String bannerImage;     
    private String iconImage;     
    
    private Boolean isPrivate;      
    private List<String> tags;        
}
