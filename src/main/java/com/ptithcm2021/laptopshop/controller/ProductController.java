package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ProductSearchProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductFilterRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import com.ptithcm2021.laptopshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PatchMapping("/update/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable long id, @RequestBody UpdateProductRequest request){
        return ApiResponse.<ProductResponse>builder().data(productService.updateProduct(request, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder().message("Deleted product successful").build();
    }

    @PostMapping("/create")
    public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest){
        return ApiResponse.<ProductResponse>builder().data(productService.createProduct(productRequest)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable long id){
        return ApiResponse.<ProductResponse>builder().data(productService.getProduct(id)).build();
    }

//    @GetMapping("/item")
//    public ApiResponse<PageWrapper<ItemProductProjection>> getOneItemOfProduct(@RequestParam(defaultValue = "10") int size,
//                                                                               @RequestParam (defaultValue = "0") int page){
//        Pageable pageable = Pageable.ofSize(size).withPage(page);
//        return ApiResponse.<PageWrapper<ItemProductProjection>>builder().data(productService.getItemProducts(pageable)).build();
//    }

    @GetMapping("/item-filter")
    public ApiResponse<PageWrapper<ItemProductResponse>> getItemOfProductFilter(@RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam (defaultValue = "0") int page,
                                                                              ProductFilterRequest productFilterRequest){
        return ApiResponse.<PageWrapper<ItemProductResponse>>builder().data(productService.getItemProductsFilter(size, page, productFilterRequest)).build();
    }

    @GetMapping("/search")
    public ApiResponse<PagedModel<ItemProductProjection>> searchProduct(@RequestParam String keyword,
                                                                          @RequestParam (defaultValue = "0") int page,
                                                                          @RequestParam (defaultValue = "10") int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ApiResponse.<PagedModel<ItemProductProjection>>builder().data(productService.searchProduct(keyword, pageable)).build();
    }

    @GetMapping()
    public ApiResponse<PagedModel<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(required = false) String keyword,
                                                                   @RequestParam(required = false) Integer brandId){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ApiResponse.<PagedModel<ProductResponse>>builder().data(productService.getProducts(pageable, keyword, brandId)).build();
    }

    @GetMapping("/sort-options")
    public ApiResponse<List<String>> getSortOptions(){
        return ApiResponse.<List<String>>builder().data(productService.getSortOptions()).build();
    }

}
