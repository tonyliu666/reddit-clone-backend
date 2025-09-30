package tony.redit_clone.service;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service 
public class FileStorageService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String store(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();

            // ✅ Check if a file with the same name already exists
            GridFSFile existingFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(filename)));
            if (existingFile != null) {
                throw new RuntimeException("File with name '" + filename + "' already exists in GridFS");
            }

            // ✅ Store file in GridFS
            Object fileId = gridFsTemplate.store(file.getInputStream(), filename);
            return fileId.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    

}
