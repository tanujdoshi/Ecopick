package com.example.backend.services.impl;

import com.example.backend.dto.request.AddProductRequest;
import com.example.backend.dto.request.EditProductRequest;
import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.entities.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.*;
import com.example.backend.services.ProductService;
import com.example.backend.utils.Awsutils;
import com.example.backend.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FarmRepository farmRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Awsutils awsutils;
    private final ImagesRepository imagesRepository;

    /**
     * adds new product to the database
     * @param AddProductRequest DTO containing the product information
     * @param multipartFiles images of the product
     * @param principal user token
     * @return list of user products
     */
    @Override
    public List<ProductDto> addProduct(AddProductRequest AddProductRequest, final MultipartFile[] multipartFiles,
            Principal principal) {
        try {

            Farms farm = farmRepository.findById(AddProductRequest.getFarm_id());

            Category category = categoryRepository.findById(AddProductRequest.getCategory_id());

            Product product = modelMapper.map(AddProductRequest, Product.class);
            product.setFarm(farm);
            product.setCategory(category);
            Product savedProduct = productRepository.save(product);

            List<Images> productImages = new ArrayList<>();
            for (MultipartFile file : multipartFiles) {
                String url = awsutils.uploadFileToS3(file, "PRODUCT", savedProduct.getId());
                Images image = new Images();
                image.setProduct(savedProduct);
                image.setImg_url(url);
                imagesRepository.save(image);
                System.out.println("-=-=-=-=- " + url);
                productImages.add(image);
            }
            product.setImages(productImages);

            List<Product> userProducts = productRepository.findByFarm(farm);
            return userProducts.stream().map(ResponseUtils::convertProductResponse).collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            List<ProductDto> products = new ArrayList<>();
            return products;
        }
    }

    /**
     * removes product from the database
     * @param id product id
     */
    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ApiRequestException("Product not found");
        }
        List<Images> productImages = product.getImages();
        for (Images image : productImages) {
            imagesRepository.deleteById(image.getId());
        }
        // deleting product
        productRepository.deleteById(product.getId());

    }

    /**
     * edit the details of the product
     * @param editProductRequest DTO containing the details that need to be changed
     * @param files new images of the product
     * @param principal user token
     * @return returns the edited product
     */
    @Override
    public Product editProduct(EditProductRequest editProductRequest, MultipartFile[] files, Principal principal) {

        // List<ProductDto> editedProductList = new ArrayList<>();
        Product product =  productRepository.findById(editProductRequest.getId());
        try {
            
            product.setProductName(editProductRequest.getProductName());
            product.setProductDescription(editProductRequest.getProductDescription());
            product.setPrice(editProductRequest.getPrice());
            product.setStock(editProductRequest.getStock());
            product.setUnit(editProductRequest.getUnit());
            productRepository.save(product);

            List<Images> imgArr = product.getImages();

            for(Images image:imgArr){
                awsutils.deleteFilefromS3(image.getImg_url());
            }
            imgArr.clear();
            
            for(MultipartFile item :files){
                String url = awsutils.uploadFileToS3(item, "Product", product.getId());
                Images img = new Images();
                img.setProduct(product);
                img.setImg_url(url);
                imagesRepository.save(img);
                System.out.println("-=-=-=-=-  " + url);
                imgArr.add(img);
            }

            product.setImages(imgArr);
            productRepository.save(product);
            return product;
            // editedProductList = productRepository.findByFarm(product.getFarm()).stream().map(this::convertProductResponse).collect(Collectors.toList());
        }
        catch (Exception e) {
            System.out.println(e);
            throw new ApiRequestException("Product not found" );
        }
        
        
    }

    /**
     * retrieves the product based on the ID
     * @param id product id
     * @return the product information
     */
    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id);
        ProductDto gpid = new ProductDto();
        if(product!=null){
            gpid.setProductName(product.getProductName());
            gpid.setProductDescription(product.getProductDescription());
            gpid.setPrice(product.getPrice());
            gpid.setStock(product.getStock());
            gpid.setUnit(product.getUnit());
            gpid.setPrebook(product.isPrebook());
            gpid.setProductCategory(product.getCategory());
            gpid.setFarm(product.getFarm());
            
            for(Images images: product.getImages()){
                gpid.addImage(images);
            }
            return gpid;
        }
        else {
            throw new ApiRequestException("Product not found with id " + id);
        }
    }

    /**
     * gets the all products of the farmer
     * @param principal user token
     * @return list of all products of the farmer
     */
    @Override
    public List<ProductDto> getFarmerProducts(Principal principal, String searchTerm) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        List<Farms> userFarms = farmRepository.findByUser(user);
        List<Product> allProducts = new ArrayList<>();

        for (Farms farm : userFarms) {
            List<Product> products = productRepository.findByFarm(farm);
            allProducts.addAll(products);
        }
        if (searchTerm != null) {
            System.out.println("Search term " + searchTerm);
            String search = searchTerm.toLowerCase();
            Stream<Product> filteredProducts = allProducts.stream()
                    .filter(product -> product.getProductName().toLowerCase()
                            .contains(search));

            Stream<ProductDto> mappedResponses = filteredProducts
                    .map(ResponseUtils::convertProductResponse);

            List<ProductDto> resultList = mappedResponses
                    .toList();
            return resultList;
        }
        return allProducts.stream().map(ResponseUtils::convertProductResponse).collect(Collectors.toList());
    }

    /**
     * retrieves all products from the database that match the given infix
     * @param searchTerm infix to be used for searching
     * @return list of products that contain the infix
     */
    @Override
    public List<ProductDto> getAllProducts(String searchTerm) {
        List<Product> allProducts = new ArrayList<>();
        String productName = searchTerm;
        if (!Objects.equals(productName, "")) {
            allProducts = productRepository.findByProductNameContaining(productName);
            if (allProducts.isEmpty()) {
                allProducts = productRepository.findAll();
            }
        } else {
            allProducts = productRepository.findAll();
        }
        return allProducts.stream().map(ResponseUtils::convertProductResponse).collect(Collectors.toList());
    }
}
