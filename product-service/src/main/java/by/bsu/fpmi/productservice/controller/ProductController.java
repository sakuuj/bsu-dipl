package by.bsu.fpmi.productservice.controller;


import by.bsu.fpmi.productservice.dto.ProductRequest;
import by.bsu.fpmi.productservice.dto.ProductResponse;
import by.bsu.fpmi.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        log.info("SOME LOG INFO  FDFDFFD ");
        return productService.getAllProducts();
    }

}
