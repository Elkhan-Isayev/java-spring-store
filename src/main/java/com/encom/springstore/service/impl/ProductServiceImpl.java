package com.encom.springstore.service.impl;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Product;
import com.encom.springstore.repository.ProductRepository;
import com.encom.springstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException {
        List<Product> products = new ArrayList<>();
        productRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPageNumber)).forEach(products::add);
        if(products.isEmpty()) {
            throw new ResourceNotFoundException("Can not find any products");
        }
        return products;
    }

    @Override
    public Product getById(int id) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new ResourceNotFoundException("Can not find product with id: " + id);
        }
        else {
            return product;
        }
    }

    @Override
    public Product create(Product product) throws ResourceAlreadyExistsException, BadResourceException {
        if(!StringUtils.isEmpty(product.getName())
        && !StringUtils.isEmpty(product.getDescription())
        && !StringUtils.isEmpty(product.getPrice())
        && !StringUtils.isEmpty(product.getCurrency())
        && !StringUtils.isEmpty(product.getImage())) {
            if(productRepository.existsById(product.getId())) {
                throw new ResourceAlreadyExistsException("Product with id: " + product.getId() + " already exists");
            }
            return productRepository.save(product);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save product");
            badResourceException.addErrorMessage("Product is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void update(int id, Product product) throws BadResourceException, ResourceNotFoundException {
        if(!StringUtils.isEmpty(product.getName())
        && !StringUtils.isEmpty(product.getDescription())
        && !StringUtils.isEmpty(product.getPrice())
        && !StringUtils.isEmpty(product.getCurrency())
        && !StringUtils.isEmpty(product.getImage())) {
            if(!productRepository.existsById(id)) {
                throw new ResourceNotFoundException("Can not find product with id: " + product.getId());
            }
            Product foundedProduct = productRepository.getById(id);
            foundedProduct.setName(product.getName());
            foundedProduct.setDescription(product.getDescription());
            foundedProduct.setPrice(product.getPrice());
            foundedProduct.setCurrency(product.getCurrency());
            foundedProduct.setImage(product.getImage());
            productRepository.save(foundedProduct);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save product");
            badResourceException.addErrorMessage("Product is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void delete(int id) throws ResourceNotFoundException {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Can not find product with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
