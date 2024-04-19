package com.example.backend.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.HomeResponse;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Product;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.services.HomeService;
import com.example.backend.utils.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

        private final FarmRepository farmRepository;
        private final ProductRepository productRepository;

        /**
         * Gets information required for the HOMEPAGE
         * @return returns the top 8 farms and top 8 products to be displayed on the home page
         */
        @Override
        public HomeResponse getHomeMeta() {
                List<Farms> topFarms = farmRepository.findTop8ByOrderByIdDesc();
                List<FarmDto> farms = topFarms.stream().map(ResponseUtils::convertFarmResponse)
                                .collect(Collectors.toList());

                List<Product> latestProducts = productRepository.findTop8ByOrderByIdDesc();

                Stream<Product> productStream = latestProducts.stream();
                Stream<ProductDto> productDtoStream = productStream.map(ResponseUtils::convertProductResponse);
                List<ProductDto> products = productDtoStream.collect(Collectors.toList());

                HomeResponse homeResponse = new HomeResponse();
                homeResponse.setFarms(farms);
                homeResponse.setProducts(products);
                return homeResponse;
        }

}
