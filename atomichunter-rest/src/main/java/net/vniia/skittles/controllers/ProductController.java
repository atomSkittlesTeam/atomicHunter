package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.ProductDto;
import net.vniia.skittles.entities.Request;
import net.vniia.skittles.readers.ProductReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductReader productReader;

    @GetMapping("all")
    public List<ProductDto> getAllProducts() {
        return productReader.getAllProducts();
    }

}
