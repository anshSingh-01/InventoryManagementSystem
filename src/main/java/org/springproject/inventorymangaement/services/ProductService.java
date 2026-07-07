package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.ProductDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.Product;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.ProductRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements DtoImpl<Product, ProductDto> {

       @Autowired
        private ProductRepositoryImpl productRepository;

       public List<ProductDto> findAllProducts(){
            List<Product> products = productRepository.findAll();
           List<ProductDto> productDtos = products.stream()
                   .map(product -> new ProductDto(product))
                   .toList();

           return productDtos;
       }

       public ProductDto findById(UUID id){
                Optional<Product> product = productRepository.findById(id);
                if(!product.isPresent())return null;
                return new ProductDto(product.get());
       }

       public StatusSender DeleteById(UUID id){
                productRepository.deleteById(id);
                return new StatusSender(StatusCode.SUCCESS ,"Successfully Deleted",null);
       }

       public StatusSender addAllProducts(List<ProductDto> productDtos){
                    List<Product> products = productDtos.stream()
                            .map(productDto -> DtoToEntity(productDto)).toList();
                    productRepository.saveAll(products);

                    return new StatusSender(StatusCode.SUCCESS ,"Saved All Products" ,productDtos);
       }

       public StatusSender addProduct(ProductDto productDto){
                productRepository.save(DtoToEntity(productDto));
                return new StatusSender(StatusCode.SUCCESS ,"Product Saved",productDto);
       }

        public StatusSender editProduct(UUID id , ProductDto productDto){
                    Product product = DtoToEntity(productDto);

                    productRepository.save(product);

                    return new StatusSender(StatusCode.SUCCESS ,"Product Edited SuccessFull!!!",product);
        }

    @Override
    public Product DtoToEntity(ProductDto productDto) {
               Product product = productRepository.findByNameAndBrand(productDto.getName(),productDto.getBrand());
               if(product == null)return new Product(productDto.getName(),productDto.getBrand(),productDto.getDescription(),productDto.getCreatedAt(),productDto.getUpdatedAt());
               return product;
    }

    @Override
    public ProductDto EntityToDto(Product product) {
            return new ProductDto(product);
    }
}
