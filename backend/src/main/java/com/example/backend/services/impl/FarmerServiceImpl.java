package com.example.backend.services.impl;

import com.example.backend.dto.request.EditFarmRequest;

import com.example.backend.entities.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import com.example.backend.dto.request.AddFarmRequest;
import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.GetFarmByIdResponse;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.ImagesRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.FarmerService;
import com.example.backend.utils.Awsutils;
import com.example.backend.utils.ResponseUtils;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final ModelMapper modelMapper;
    private final Awsutils awsutils;
    private final ImagesRepository imagesRepository;
    private final ProductRepository productRepository;

    /**
     * creates new farm and stores the data in the database
     * @param farmRequest Contains all details needed to create a new farm
     * @param multipartFiles Used to add images of the farm
     * @param principal user token
     * @return returns the farm object
     */
    @Override
    public List<FarmDto> addFarm(AddFarmRequest farmRequest, final MultipartFile[] multipartFiles,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if(user.getRole()==Role.valueOf("CUSTOMER")) {
            user.setRole(Role.valueOf("FARMER"));
            userRepository.save(user);
        }
        Farms farm = modelMapper.map(farmRequest, Farms.class);
        System.out.println(farm);
        farm.setUser(user);
        Farms savedFarm = farmRepository.save(farm);
        System.out.println(savedFarm.getId());

        List<Images> farmImages = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String url = awsutils.uploadFileToS3(file, "FARM", savedFarm.getId());
            Images image = new Images();
            image.setFarm(savedFarm);
            image.setImg_url(url);
            imagesRepository.save(image);
            farmImages.add(image);
        }
        farm.setImages(farmImages);

        List<Farms> userFarms = farmRepository.findByUser(user);
        return userFarms.stream().map(ResponseUtils::convertFarmResponse).collect(Collectors.toList());
    }

    /**
     * Edits the farm based on data provided
     * @param farmRequest contains all data about a farm, if change is not needed the field will be null
     * @param multipartFiles for new images of the farm, null if no change is needed
     * @param principal user token
     * @return success confirmation
     */
    @Override
    public String editFarm(EditFarmRequest farmRequest, MultipartFile[] multipartFiles, Principal principal) {
        Farms farm = farmRepository.findById(farmRequest.getId());
        updateIndividualFields(farmRequest, farm);
        if (multipartFiles != null) {
            List<Images> farmImages = farm.getImages();
            for (Images image : farmImages) {
                awsutils.deleteFilefromS3(image.getImg_url());
            }
            farmImages.clear();
            for (MultipartFile file : multipartFiles) {
                String url = awsutils.uploadFileToS3(file, "FARM", farm.getId());
                Images image = new Images();
                image.setFarm(farm);
                image.setImg_url(url);
                imagesRepository.save(image);
                System.out.println("-=-=-=-=- " + url);
                farmImages.add(image);
            }
            farm.setImages(farmImages);
        }
        farmRepository.save(farm);
        return "Farm details edited successfully";
    }

    /**
     * Removes a farm and all its related objects from the database
     * @param id farm id
     */
    @Override
    public void deleteFarm(int id) {
        Farms farm = farmRepository.findById(id);
        if (farm == null) {
            throw new ApiRequestException("Farm not found");
        }

        List<Product> products = farm.getProduct();
        // looping through products
        for (Product product : products) {
            // first deleting related images as products are associated with images
            List<Images> productImages = product.getImages();
            for (Images image : productImages) {
                imagesRepository.deleteById(image.getId());
            }
            // deleting product
            productRepository.deleteById(product.getId());
        }
        // deleting farm
        farmRepository.deleteById(id);
    }

    /**
     * gets all farms of an user
     * @param searchTerm infix of the farm name to be searched for
     * @param principal user token
     * @return List of farms of the user
     */
    @Override
    public List<FarmDto> getFarms(String searchTerm, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        List<Farms> userFarms = farmRepository.findByUser(user);
        if (searchTerm != null) {
            String search = searchTerm.toLowerCase();
            Stream<Farms> filteredFarms = userFarms.stream()
                    .filter(farm -> farm.getName().toLowerCase()
                            .contains(search));

            Stream<FarmDto> mappedResponses = filteredFarms
                    .map(ResponseUtils::convertFarmResponse);

            List<FarmDto> resultList = mappedResponses
                    .collect(Collectors.toList());

            return resultList;

            // return userFarms.stream().filter(
            // farm ->
            // farm.getName().toLowerCase().contains(farmerOwnFarmRequest.getSearchTerm().toLowerCase()))
            // .map(ResponseUtils::convertFarmResponse)
            // .collect(Collectors.toList());
        }
        return userFarms.stream().map(ResponseUtils::convertFarmResponse).collect(Collectors.toList());
    }

    /**
     * gets all farms that matchjes the infix
     * @param farmName infix of the farm name
     * @return all farms that matches the given infix
     */
    @Override
    public List<FarmDto> getAllFarms(String farmName) {
        List<Farms> allFarms;
        if (!Objects.equals(farmName, "")) {
            System.out.println("showing farms with name" + farmName);
            allFarms = farmRepository.findByNameIgnoreCaseContaining(farmName);
            if (allFarms.isEmpty()) {
                System.out.println("specific Farm not present showing all farms instead");
                allFarms = farmRepository.findAll();
            }
        } else {
            allFarms = farmRepository.findAll();
        }
        return allFarms.stream().map(ResponseUtils::convertFarmResponse).collect(Collectors.toList());
    }

    /**
     * Gets farm with its ID
     * @param id farm ID
     * @return returns the farmID
     */
    @Override
    public GetFarmByIdResponse getFarmById(int id) {
        Farms farm = farmRepository.findById(id);
        GetFarmByIdResponse gfid = new GetFarmByIdResponse();
        if (farm != null) {
            gfid.setName(farm.getName());
            gfid.setAddress(farm.getAddress());
            gfid.setLat(farm.getLat());
            gfid.setLng(farm.getLng());
            gfid.setDescription(farm.getDescription());
            for (Images images : farm.getImages()) {
                gfid.fetchImage(images);
            }
            return gfid;
        }

        else {
            throw new ApiRequestException("Farm not found with id " + id);
        }

    }

    /**
     * to update the farm values, used by editFarm method
     * @param farmRequest contains the data that is meant to replace the existing data
     * @param farm farm that needs to be edited
     */
    public void updateIndividualFields(EditFarmRequest farmRequest, Farms farm) {
        if (farmRequest.getName() != null) {
            farm.setName(farmRequest.getName());
        }
        if (farmRequest.getAddress() != null) {
            farm.setAddress(farmRequest.getAddress());
        }
        if (farmRequest.getLng() != 0) {
            farm.setLng(farmRequest.getLng());
        }
        if (farmRequest.getLat() != 0) {
            farm.setLat(farmRequest.getLat());
        }
        if (farmRequest.getDescription() != "") {
            farm.setDescription(farmRequest.getDescription());
        }
        farmRepository.save(farm);
    }

}