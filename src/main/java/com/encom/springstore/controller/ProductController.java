package com.encom.springstore.controller;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Product;
import com.encom.springstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/product")
public class ProductController {
    private final ProductService productService;

    //  return all with pagination and filter logic
    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(value="rowPerPage", defaultValue="5") int rowPerPageNumber) {
        try {
            List<Product> products = productService.getAll(pageNumber, rowPerPageNumber);
            return ResponseEntity.ok(products);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("findAll: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    //  return specific product by id
    @GetMapping(value = "/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProductById(@PathVariable int productID) {
        try {
            Product foundProduct = productService.getById(productID);
            return ResponseEntity.ok(foundProduct);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception exception) {
            log.error("findProductById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // return new created product
    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.create(product);
            return new ResponseEntity<Product>(createdProduct, HttpStatus.CREATED);
        }
        catch (ResourceAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("addProduct: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // change existed product
    @PutMapping(value="/{productID}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable int productID) {
        try {
            productService.update(productID, product);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("updateProduct: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // delete product by id
    @DeleteMapping(value="/{productID}")
    public ResponseEntity<Void> deleteProductById(@PathVariable int productID) {
        try {
            productService.delete(productID);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("deleteProductById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
