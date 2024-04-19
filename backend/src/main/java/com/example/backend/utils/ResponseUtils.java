package com.example.backend.utils;

import com.example.backend.dto.response.*;
import com.example.backend.entities.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseUtils {

    public static FarmDto convertFarmResponse(Farms current_farm) {
        FarmDto farmDTO = new FarmDto();
        farmDTO.setId(current_farm.getId());
        farmDTO.setName(current_farm.getName());
        farmDTO.setAddress(current_farm.getAddress());
        farmDTO.setDescription(current_farm.getDescription());
        farmDTO.setLat(current_farm.getLat());
        farmDTO.setLng(current_farm.getLng());

        for (Images images : current_farm.getImages()) {
            farmDTO.addImage(images);
        }

        return farmDTO;
    }

    public static ProductDto convertProductResponse(Product product) {
        long startTime = System.nanoTime();
        ProductDto dto = new ProductDto();
        dto.setProductName(product.getProductName());
        dto.setId(product.getId());
        dto.setStock(product.getStock());
        dto.setPrice(product.getPrice());
        dto.setUnit(product.getUnit());
        dto.setProductDescription(product.getProductDescription());

        Category category = product.getCategory();

        for (Images images : product.getImages()) {
            dto.addImage(images);
        }

        long afterS3 = System.nanoTime();
        if (category != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            dto.setCategory(categoryDto);
        }
        return dto;
    }
    public static UserDTO convertUserResponse(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setRole(user.getRole());
        return dto;
    }
    public static OrderDto convertOrderResponse(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setProduct(order.getProduct());
        orderDto.setProductName(order.getProduct().getProductName());
        orderDto.setProductDescription(order.getProduct().getProductDescription());
        orderDto.setFarm(order.getFarm());
        orderDto.setFarmName(order.getFarm().getName());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderValue(order.getOrderValue());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setOrderPaymentMethod(order.getOrderPaymentMethod());

        for (Images image : order.getProduct().getImages()) {
            orderDto.addImage(image);
        }
        return orderDto;
    }
}
