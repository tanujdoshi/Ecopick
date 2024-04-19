package com.example.backend.controller;

import com.example.backend.dto.request.EditFarmRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.backend.dto.request.AddFarmRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.GetFarmByIdResponse;
import com.example.backend.services.FarmerService;
import java.util.*;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/farmer")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    /**
     * Endpoint to get the farms of a particular user
     * @param searchTerm infix used to filter the search
     * @param principal user token
     * @return returns a list of farms
     */
    @GetMapping("/own-farms")
    public ResponseEntity<List<FarmDto>> getFarms(@RequestParam(name = "searchTerm", required = false) String searchTerm,
            Principal principal) {
        List<FarmDto> userFarms = farmerService.getFarms(searchTerm, principal);
        return ResponseEntity.ok(userFarms);
    }

    /**
     * Endpoint to add farms
     * @param farmRequest request containing new farm information
     * @param files images of the farms
     * @param principal user token
     * @return all farms of the farmer
     */
    @PostMapping("/addfarm")
    public ResponseEntity<Map> addFarm(@ModelAttribute AddFarmRequest farmRequest,
            @RequestParam(value = "files") MultipartFile[] files, Principal principal) {
        System.out.println("files" + files);
        List<FarmDto> AllFarmerFarms = farmerService.addFarm(farmRequest, files,
                principal);
        Map<String, Object> response = new HashMap<>();
        response.put("AllFarm", AllFarmerFarms);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint to edit farms
     * @param farmRequest request containing farm information that needs to be changed
     * @param files new images for the farm
     * @param principal user token
     * @return String response indicating success or failure
     */
    @PostMapping("/editfarm")
    public ResponseEntity<Map> editFarm(@ModelAttribute EditFarmRequest farmRequest,
            @RequestPart(value = "files", required = false) MultipartFile[] files, Principal principal) {
        String editFarmResponse = farmerService.editFarm(farmRequest, files, principal);
        Map<String, Object> response = new HashMap<>();
        response.put("message", principal.getName());
        response.put("Result", editFarmResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint to delete the farm
     * @param id farm id used to identify the farm
     * @return String response indicating success or failure
     */
    @DeleteMapping("/farms/{id}")
    public ResponseEntity<ApiResponse> deleteFarm(@PathVariable int id) {
        farmerService.deleteFarm(id);
        ApiResponse response = new ApiResponse();
        response.setMessage("Farm deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to find a specific farm by its id
     * @param farmId farm id used to identify farm
     * @return the farm information
     */
    @GetMapping("/getFarm/{farmId}")
    public ResponseEntity<GetFarmByIdResponse> getFarmById(@PathVariable int farmId) {
        GetFarmByIdResponse response = farmerService.getFarmById(farmId);
        return ResponseEntity.ok(response);
    }

}
