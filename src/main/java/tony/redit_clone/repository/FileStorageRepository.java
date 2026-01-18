package tony.redit_clone.repository;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.client.gridfs.model.GridFSFile;
import tony.redit_clone.util.*;

/**
 * Service-based repository for managing file storage using MongoDB GridFS.
 * Provides methods for saving and retrieving files as Base64 strings.
 */
@Service
public class FileStorageRepository {

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    @Autowired
    public FileStorageRepository(GridFsTemplate gridFsTemplate, GridFsOperations operations) {
        this.gridFsTemplate = gridFsTemplate;
        this.operations = operations;
    }

    /**
     * Retrieves a file from GridFS by its ID.
     *
     * @param id the unique identifier of the file
     * @return a {@link Try} containing the file content as a Base64 encoded string
     *         if successful
     */
    public Try<String> getFile(String id) {
        return Try.ofChecked(() -> {
            GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
            return file;
        }).flatMap(file -> {
            if (file == null) {
                return Try.failure(new RuntimeException("File not found with id: " + id));
            }
            GridFsResource resource = operations.getResource(file);
            return Try.success(resource);
        }).flatMap(resource -> Try.ofChecked(() -> {
            try (var inputStream = resource.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                return Base64.getEncoder().encodeToString(bytes);
            }
        }));
    }

    /**
     * Stores a multipart file in GridFS.
     * Performs validation to ensure filename is present and the file doesn't
     * already exist.
     *
     * @param file the multipart file to be saved
     * @return a {@link Try} containing the generated file ID string if successful
     */
    public Try<String> saveFile(MultipartFile file) {
        return Try.of(file::getOriginalFilename)

                // Step 1: check filename not null/empty
                .filter(filename -> filename != null && !filename.trim().isEmpty())

                // Step 2: check input stream exists
                .flatMap((String filename) -> Try.ofChecked(() -> {
                    if (file.getInputStream() == null) {
                        throw new RuntimeException("File input stream is null for: " + filename);
                    }
                    return filename;
                }))

                // Step 3: check if file already exists
                .flatMap((String filename) -> Try.of(() -> {
                    GridFSFile existingFile = gridFsTemplate.findOne(
                            new Query(Criteria.where("filename").is(filename)));
                    if (existingFile != null) {
                        throw new RuntimeException("File already exists: " + filename);
                    }
                    return filename;
                }))

                // Step 4: store file
                .flatMap(filename -> Try.ofChecked(() -> {
                    var fileId = gridFsTemplate.store(file.getInputStream(), filename, file.getContentType());
                    return fileId.toString();
                }));
    }
}
