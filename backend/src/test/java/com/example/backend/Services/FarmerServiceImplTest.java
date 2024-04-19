package com.example.backend.Services;

import com.example.backend.dto.request.AddFarmRequest;
import com.example.backend.dto.request.EditFarmRequest;
import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.GetFarmByIdResponse;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;
import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.ImagesRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.impl.FarmerServiceImpl;
import com.example.backend.utils.Awsutils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FarmerServiceImplTest {
        static final double LATITUDE = 123.456;
        static final double LONGITUDE = 789.012;
        static final double LATITUDE_SET = 15.0;
        static final double LONGITUDE_SET = 5.0;
        static final int SECOND_FARM_ID = 2;
        static final int EXPECTED_RESULT_SIZE = 2;
        static final int GENERATE_NUMBER_OF_FARMS = 2;
        static final int GENERATE_ALL_NUMBER_OF_FARMS = 3;
        static final int EXPECTED_RESULT_SIZE_ALL_FARM = 3;

        @BeforeEach
        public void Setup() {
                MockitoAnnotations.openMocks(this);
        }

        @Mock
        private UserRepository userRepositoryMock;

        @Mock
        private ModelMapper modelMapperMock;

        @Mock
        private FarmRepository farmRepositoryMock;

        @Mock
        private ProductRepository productRepositoryMock;

        @Mock
        private ImagesRepository imagesRepositoryMock;

        @Mock
        private Awsutils awsutilsMock;;

        @InjectMocks
        private FarmerServiceImpl farmerServiceMock;

        @Test
        public void testAddFarm() {
                // Arrange
                AddFarmRequest farmRequest = new AddFarmRequest();
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("testuser@example.com");
                User user = new User();
                user.setEmail("testuser@example.com");
                when(userRepositoryMock.findByEmail(anyString())).thenReturn(user);
                Farms farm = new Farms();
                when(modelMapperMock.map(any(), eq(Farms.class))).thenReturn(farm);
                when(farmRepositoryMock.save(any(Farms.class))).thenReturn(farm);
                List<Farms> userFarms = new ArrayList<>();
                userFarms.add(farm);
                when(farmRepositoryMock.findByUser(any(User.class))).thenReturn(userFarms);

                MultipartFile[] multipartFiles = {
                                new MockMultipartFile("file1", new byte[0]),
                                new MockMultipartFile("file2", new byte[0])
                };
                when(awsutilsMock.uploadFileToS3(any(MultipartFile.class), anyString(), anyInt()))
                                .thenReturn("dummy-url");

                // Act
                List<FarmDto> result = farmerServiceMock.addFarm(farmRequest, multipartFiles, principal);

                // Assert
                assertEquals(1, result.size()); // Ensure that the result contains one farm
        }

        @Test
        public void testEditFarm() {
                EditFarmRequest farmRequest = new EditFarmRequest(1, "New Name", "New Address", "New Description",
                                LATITUDE,
                                LONGITUDE);
                MultipartFile[] multipartFiles = {
                                new MockMultipartFile("file1", "file1.jpg", "image/jpeg", new byte[0]) };
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("testuser@example.com");

                Farms existingFarm = new Farms();
                existingFarm.setId(1);
                existingFarm.setName("Old Name");
                existingFarm.setAddress("Old Address");
                existingFarm.setLng(LATITUDE_SET);
                existingFarm.setLat(LONGITUDE_SET);
                existingFarm.setDescription("Old Description");
                Images image = new Images();
                image.setImg_url("image_url.jpg");
                List<Images> images = new ArrayList<>();
                images.add(image);
                existingFarm.setImages(images);

                when(farmRepositoryMock.findById(anyInt())).thenReturn(existingFarm);
                when(farmRepositoryMock.save(any(Farms.class))).thenReturn(existingFarm);

                // Act
                String result = farmerServiceMock.editFarm(farmRequest, multipartFiles, principal);

                System.out.println("HEREE?" + existingFarm.getName());
                // Assert
                assertEquals("Farm details edited successfully", result);
                verify(awsutilsMock).deleteFilefromS3(eq("image_url.jpg"));
        }

        @Test
        public void testDeleteFarmNull() {
                when(farmRepositoryMock.findById(anyInt())).thenReturn(null);
                assertThrows(ApiRequestException.class, () -> farmerServiceMock.deleteFarm(anyInt()));
        }

        @Test
        public void testGetAllUserFarms() {
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("testuser@example.com");

                User user = new User();
                user.setId(1);
                user.setEmail("testuser@example.com");

                Images image = new Images();
                image.setImg_url("image_url.jpg");
                List<Images> images = new ArrayList<>();
                images.add(image);

                Farms farm1 = new Farms();
                farm1.setId(1);
                farm1.setImages(images);

                Farms farm2 = new Farms();
                farm2.setId(SECOND_FARM_ID);
                farm2.setImages(images);
                List<Farms> userFarms = List.of(farm1, farm2);

                when(userRepositoryMock.findByEmail(anyString())).thenReturn(user);
                when(farmRepositoryMock.findByUser(user)).thenReturn(userFarms);
                //
                List<FarmDto> result = farmerServiceMock.getFarms(null, principal);
                // // Assert
                assertEquals(EXPECTED_RESULT_SIZE, result.size());
        }

        @Test
        public void testGetAllUserFarmWithSearch() {
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("testuser@example.com");

                User user = new User();
                user.setId(1);
                user.setEmail("testuser@example.com");

                Images image = new Images();
                image.setImg_url("image_url.jpg");
                List<Images> images = new ArrayList<>();
                images.add(image);

                Farms farm = new Farms();
                farm.setId(1);
                farm.setName("Farm Name");
                farm.setAddress("Farm Address");
                farm.setLat(LATITUDE);
                farm.setLng(LONGITUDE);
                farm.setImages(images);

                List<Farms> userFarms = List.of(farm);

                when(userRepositoryMock.findByEmail(anyString())).thenReturn(user);
                when(farmRepositoryMock.findByUser(user)).thenReturn(userFarms);
                //
                List<FarmDto> result = farmerServiceMock.getFarms("Farm", principal);
                // // Assert
                assertEquals(1, result.size());
        }

        @Test
        public void testGetAllFarmsWithSearch() {
                String farmName = "Farm 1";
                List<Farms> matchingFarms = createFarmsList(GENERATE_NUMBER_OF_FARMS); // Create a list of 2 farms with
                                                                                       // the
                                                                                       // provided
                when(farmRepositoryMock.findByNameIgnoreCaseContaining(farmName)).thenReturn(matchingFarms);

                // Act
                List<FarmDto> result = farmerServiceMock.getAllFarms(farmName);

                // Assert
                assertEquals(EXPECTED_RESULT_SIZE, result.size()); //
        }

        @Test
        public void testGetAllFarmsWithNullSearch() {
                String farmName = "Farm 1";
                List<Farms> matchingFarms = createFarmsList(GENERATE_NUMBER_OF_FARMS); // Create a list of 2 farms with
                                                                                       // the
                                                                                       // provided
                // name
                when(farmRepositoryMock.findByNameIgnoreCaseContaining(farmName)).thenReturn(new ArrayList<>());
                when(farmRepositoryMock.findAll()).thenReturn(matchingFarms);

                // Act
                List<FarmDto> result = farmerServiceMock.getAllFarms(farmName);

                // Assert
                assertEquals(EXPECTED_RESULT_SIZE, result.size()); //
        }

        @Test
        public void testGetAllFarmsWithoutSearch() {
                String farmName = "";
                List<Farms> matchingFarms = createFarmsList(GENERATE_ALL_NUMBER_OF_FARMS); // Create a list of 2 farms
                                                                                           // with the
                                                                                           // provided
                // name
                when(farmRepositoryMock.findAll()).thenReturn(matchingFarms);

                // Act
                List<FarmDto> result = farmerServiceMock.getAllFarms(farmName);

                // Assert
                assertEquals(EXPECTED_RESULT_SIZE_ALL_FARM, result.size()); //
        }

        @Test
        public void testGetFarmsWithNoUser() {
                Principal principal = mock(Principal.class);
                when(userRepositoryMock.findByEmail(anyString())).thenReturn(null);
                assertThrows(ApiRequestException.class,
                                () -> farmerServiceMock.getFarms(null, principal));
        }

        @Test
        public void testGetFarmById() {
                int farmId = 1;
                Images image = new Images();
                image.setImg_url("image_url.jpg");
                List<Images> images = new ArrayList<>();
                images.add(image);

                Farms farm = new Farms();
                farm.setId(farmId);
                farm.setName("Farm Name");
                farm.setAddress("Farm Address");
                farm.setLat(LATITUDE);
                farm.setLng(LONGITUDE);
                farm.setImages(images);

                when(farmRepositoryMock.findById(farmId)).thenReturn(farm);

                // Act
                GetFarmByIdResponse result = farmerServiceMock.getFarmById(farmId);

                // Assert
                assertNotNull(result);
        }

        @Test
        public void testGetFarmById_WhenFarmDoesNotExist() {
                // Arrange
                when(farmRepositoryMock.findById(anyInt())).thenReturn(null);

                // Act
                assertThrows(ApiRequestException.class, () -> farmerServiceMock.getFarmById(anyInt()));

        }

        @Test
        public void testDeleteFarm() {
                // Images
                List<Images> images = new ArrayList<>();
                Images image = new Images();
                image.setImg_url("image_url.jpg");
                images.add(image);

                Farms farm = new Farms();
                Product product1 = new Product();
                product1.setImages(images);

                Product product2 = new Product();
                product2.setImages(images);
                farm.setProduct(List.of(product1, product2));

                when(farmRepositoryMock.findById(anyInt())).thenReturn(farm);
                when(farmRepositoryMock.findById(anyInt())).thenReturn(farm);
                when(farmRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.of(farm));

                doNothing().when(productRepositoryMock).deleteById(anyInt());
                doNothing().when(imagesRepositoryMock).deleteById(anyInt());
                doNothing().when(farmRepositoryMock).deleteById(anyInt());

                farmerServiceMock.deleteFarm(anyInt());

                // Assert
                verify(productRepositoryMock, times(EXPECTED_RESULT_SIZE)).deleteById(anyInt()); // Verify product
                                                                                                 // deletion
                verify(imagesRepositoryMock, times(EXPECTED_RESULT_SIZE)).deleteById(anyInt()); // Verify image deletion
                                                                                                // (each product may
                                                                                                // have
                // multiple images)
                verify(farmRepositoryMock, times(1)).deleteById(anyInt()); // Verify farm deletion
        }

        private List<Farms> createFarmsList(int count) {
                List<Farms> farms = new ArrayList<>();
                Images image = new Images();
                image.setImg_url("image_url.jpg");
                List<Images> images = new ArrayList<>();
                images.add(image);
                for (int i = 0; i < count; i++) {
                        Farms farm = new Farms();
                        farm.setId((i + 1));
                        farm.setName("Farm " + (i + 1));
                        farm.setImages(images);
                        farms.add(farm);
                }
                return farms;
        }
}
