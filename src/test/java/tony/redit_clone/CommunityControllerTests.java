package tony.redit_clone;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import tony.redit_clone.controller.CommunityController;
import tony.redit_clone.service.CommunityService;
import tony.redit_clone.util.Try;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityController.class)
class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService fileStorageService;

    @Test
    void testCreateCommunity_Success() throws Exception {
        // Arrange: mock MultipartFiles
        MockMultipartFile banner = new MockMultipartFile(
                "banner", "banner.png", "image/png", "banner-data".getBytes()
        );

        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.png", "image/png", "icon-data".getBytes()
        );

        // Stub the service to return success
        when(fileStorageService.createCommunity(any(), any(), any(), any()))
                .thenReturn(Try.success(" "));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/communities-creation")
                        .file(banner)
                        .file(icon)
                        .param("name", "MyCommunity")
                        .param("description", "A test community")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully created community: "));
    }

    @Test
    void testCreateCommunity_Failure() throws Exception {
        // Arrange
        MockMultipartFile banner = new MockMultipartFile(
                "banner", "banner.png", "image/png", "banner-data".getBytes()
        );

        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.png", "image/png", "icon-data".getBytes()
        );

        // Stub the service to return failure
        when(fileStorageService.createCommunity(any(), any(), any(), any()))
                .thenReturn(Try.failure(new RuntimeException("Storage error")));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/communities-creation")
                        .file(banner)
                        .file(icon)
                        .param("name", "MyCommunity")
                        .param("description", "A test community")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Error creating community: Storage error"));
    }

    @Test
    void testCreateCommunity_DuplicateName() throws Exception {
        // Arrange
        MockMultipartFile banner = new MockMultipartFile(
                "banner", "banner.png", "image/png", "banner-data".getBytes()
        );

        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.png", "image/png", "icon-data".getBytes()
        );

        // Stub the service to return failure for duplicate name
        when(fileStorageService.createCommunity(any(), any(), any(), any()))
                .thenReturn(Try.failure(new RuntimeException("Community name already exists")));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/communities-creation")
                        .file(banner)
                        .file(icon)
                        .param("name", "ExistingCommunity")
                        .param("description", "A test community")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Error creating community: Community name already exists"));
    }
}
