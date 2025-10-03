package tony.redit_clone.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.client.gridfs.model.GridFSFile;
import tony.redit_clone.util.*;

@Service 
public class FileStorageRepository {

    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public FileStorageRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public Try<String> saveFile(MultipartFile file) {
        return Try.of(file::getOriginalFilename)

            // Step 1: check filename not null/empty
            .filter(filename -> filename != null && !filename.trim().isEmpty())

            // Step 2: check input stream exists
            .flatMap((String filename) -> 
                Try.ofChecked(() -> {
                    if (file.getInputStream() == null) {
                        throw new RuntimeException("File input stream is null for: " + filename);
                    }
                    return filename;
                })
            )

            // Step 3: check if file already exists
            .flatMap((String filename) -> 
                Try.of(() -> {
                    GridFSFile existingFile = gridFsTemplate.findOne(
                        new Query(Criteria.where("filename").is(filename))
                    );
                    if (existingFile != null) {
                        throw new RuntimeException("File already exists: " + filename);
                    }
                    return filename;
                })
            )

            // Step 4: store file
            .flatMap(filename -> 
                Try.ofChecked(() -> {
                    var fileId = gridFsTemplate.store(file.getInputStream(), filename, file.getContentType());
                    return fileId.toString();
                })
            );
        }
}

