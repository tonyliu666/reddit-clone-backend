package tony.redit_clone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import tony.redit_clone.repository.CommunityRepository;
import tony.redit_clone.service.CommunityService;
import tony.redit_clone.util.Try;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.containers.MongoDBContainer;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommunityControllerIntegrationTest {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.data.mongodb.uri", mongo.getReplicaSetUrl());
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommunityRepository communityRepository;

    @MockBean       
    private CommunityService fileStorageService;

    @Test
    void testCreateCommunity() throws Exception {
        MockMultipartFile banner = new MockMultipartFile("banner", "banner.png", "image/png", "data".getBytes());
        MockMultipartFile icon = new MockMultipartFile("icon", "icon.png", "image/png", "data".getBytes());

        mockMvc.perform(multipart("/api/v1/communities-creation")
                .file(banner)
                .file(icon)
                .param("name", "TestCommunity")
                .param("description", "Testing Testcontainers"))
            .andExpect(status().isOk());
        assertTrue(
        communityRepository.findOneByName("TestCommunity").isPresent(),
        "Community should have been inserted into Testcontainers MongoDB"
        );
              
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
