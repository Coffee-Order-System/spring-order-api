package gc.cafe.order.controller;

import gc.cafe.order.model.Product;
import gc.cafe.order.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        model.addAttribute("product", new ProductDto());
        return "admin/new-product";
    }

    @PostMapping(NEW_PRODUCT)
    public String submitProduct(ProductDto productDto) {
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
                .ifPresent(product -> model.addAttribute("product", product));
        return "admin/product";
    }

    @GetMapping(UPDATE_PRODUCT + "/{productId}")
    public String updateProductPage(Model model, @PathVariable String productId) {
        productService.getProduct(productId)
                .ifPresent(product -> model.addAttribute("product", product));
        return "admin/update-product";
    }

    @PostMapping(UPDATE_PRODUCT + "/{productId}")
    public String updateProduct(@PathVariable String productId, ProductDto productDto) {
        productService.updateProduct(productId,
                                productDto.getProductName(),
                                productDto.getPrice(),
                                productDto.getCategory(),
                                productDto.getDescription());
        return "redirect:/admin/products/" + productId;
    }

}
