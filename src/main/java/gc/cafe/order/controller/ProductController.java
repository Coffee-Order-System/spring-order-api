package gc.cafe.order.controller;

import gc.cafe.order.dto.ProductDto;
import gc.cafe.order.mapper.ProductMapper;
import gc.cafe.order.service.ProductService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(ProductController.ADMIN)
public class ProductController {

    protected static final String ADMIN = "/admin";
    protected static final String PRODUCTS = "/products";
    protected static final String NEW_PRODUCT = "/new-product";
    protected static final String UPDATE_PRODUCT = "/update-product";

    private final ProductService productService;

    @GetMapping(PRODUCTS)
    public String productsPage(Model model) {
        List<ProductDto> productDtos = productService.getAllProducts().stream()
                .map(ProductMapper::productToProductDto)
                .toList();
        model.addAttribute("products", productDtos);
        return "admin/products";
    }

    @GetMapping(NEW_PRODUCT)
    public String newProductPage(Model model) {
        model.addAttribute(new ProductDto());
        return "admin/new-product";
    }

    @PostMapping(NEW_PRODUCT)
    public String submitProduct(@Valid ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/new-product";
        }
        productService.addProduct(
                productDto.getProductName(),
                productDto.getPrice(),
                productDto.getCategory(),
                productDto.getDescription());
        return "redirect:/admin/products";
    }

    @GetMapping(PRODUCTS + "/{productId}")
    public String getProductPage(Model model, @PathVariable String productId) {
        productService.getProduct(productId)
                .ifPresent(product -> model.addAttribute(ProductMapper.productToProductDto(product)));
        return "admin/product";
    }

    @GetMapping(UPDATE_PRODUCT + "/{productId}")
    public String updateProductPage(Model model, @PathVariable String productId) {
        productService.getProduct(productId)
                .ifPresent(product -> model.addAttribute(ProductMapper.productToProductDto(product)));
        return "admin/update-product";
    }

    @PostMapping(UPDATE_PRODUCT + "/{productId}")
    public String updateProduct(@PathVariable String productId, @Valid ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/update-product";
        }
        productService.updateProduct(productId,
                                productDto.getProductName(),
                                productDto.getPrice(),
                                productDto.getCategory(),
                                productDto.getDescription());
        return "redirect:/admin/products/" + productId;
    }

}
