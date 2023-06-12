package com.library.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.library.dto.ProductDto;
import com.library.model.Product;
import com.library.repository.ProductRepository;
import com.library.service.ProductService;
import com.library.utils.ImageUpload;

import org.springframework.data.domain.Pageable;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUpload imageUpload;


    @Override
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product product = new Product();
            if(imageProduct == null){
                product.setImage(null);
            }else{
                if(imageUpload.uploadImage(imageProduct)){
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setAuthor(productDto.getAuthor());
            product.setReleaseDate(productDto.getReleaseDate());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(productDto.getCategory());
            product.setNumberOfPages(productDto.getNumberOfPages());
            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Product update(MultipartFile imageProduct ,ProductDto productDto) {
        try {
            Product product = productRepository.findById(productDto.getId()).get();
            if(imageProduct.isEmpty()== true){
            }else{
            	
                if(imageUpload.checkExisted(imageProduct) == false){
                    imageUpload.uploadImage(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setAuthor(productDto.getAuthor());
            product.setReleaseDate(productDto.getReleaseDate());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(productDto.getCategory());
            product.setNumberOfPages(productDto.getNumberOfPages());
            product.setCategory(productDto.getCategory());
            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteById(Long id) {
        
        productRepository.deleteById(id);
    }

   

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setAuthor(product.getAuthor());
        productDto.setReleaseDate(product.getReleaseDate());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImage(product.getImage());
        productDto.setCategory(product.getCategory());
        productDto.setNumberOfPages(product.getNumberOfPages());
        return productDto;
    }




    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).get();
    }
    @Override
    public List<Product> getProductsInCategory(Long categoryId) {
        return productRepository.getProductsInCategory(categoryId);
    }

}
