// package tony.redit_clone;

// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.web.multipart.MultipartFile;
// import tony.redit_clone.repository.CommunityRepository;
// import tony.redit_clone.repository.FileStorageRepository;
// import tony.redit_clone.service.FileStorageService;
// import tony.redit_clone.util.Try;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class FileStorageServiceTest {

//     @Mock
//     private FileStorageRepository fileStorageRepository;

//     @Mock
//     private CommunityRepository communityRepository;

//     @InjectMocks
//     private FileStorageService fileStorageService;

//     public FileStorageServiceTest() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testCreateCommunity() {
//         MultipartFile banner = mock(MultipartFile.class);
//         when(banner.getOriginalFilename()).thenReturn("test-banner.png");
//         MultipartFile icon = mock(MultipartFile.class);
//         when(icon.getOriginalFilename()).thenReturn("test-icon.png");
//         Try<String> bannerId = fileStorageRepository.saveFile(banner);
//         System.out.println("Banner ID: " + bannerId);
//         when(bannerId).thenReturn(Try.success("123"));
//         when(fileStorageRepository.saveFile(icon)).thenReturn(Try.success("456"));
//         Try<String> result = fileStorageService.createCommunity("MyCommunity", "desc", banner, icon);
//         System.out.println("Test result: " + result);
//         assertTrue(result.isSuccess());
//         verify(communityRepository, times(1)).save(any());
//     }
// }
package tony.redit_clone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import tony.redit_clone.repository.FileStorageRepository;
import tony.redit_clone.service.CommunityService;
import tony.redit_clone.repository.CommunityRepository;
import tony.redit_clone.util.Try;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @InjectMocks
    private CommunityService fileStorageService;  // Service under test

    @Mock
    private FileStorageRepository fileStorageRepository;  // mocked repo

    @Mock
    private CommunityRepository communityRepository;      // mocked repo

    @Test
    void testCreateCommunityWithMockMultipartFile() {
        // Arrange
        MultipartFile banner = new MockMultipartFile(
                "banner",                // field name
                "test-banner.png",       // original filename
                "image/png",             // content type
                "fake-banner".getBytes() // file content
        );

        MultipartFile icon = new MockMultipartFile(
                "icon",
                "test-icon.png",
                "image/png",
                "fake-icon".getBytes()
        );
        Try<String> bannerId = fileStorageRepository.saveFile(banner);

        // Stub repository methods
        when(bannerId).thenReturn(Try.success("123"));
        when(fileStorageRepository.saveFile(icon)).thenReturn(Try.success("456"));

        // Act
        Try<String> result = fileStorageService.createCommunity("MyCommunity", "desc", banner, icon);

        // Assert
        assertTrue(result.isSuccess());
        verify(communityRepository, times(1)).save(any());  // check save called
    }
}
