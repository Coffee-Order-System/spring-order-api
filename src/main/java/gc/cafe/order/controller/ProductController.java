package gc.cafe.order.controller;

import gc.cafe.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(ProductController.ADMIN)
public class ProductController {

    protected static final String ADMIN = "/admin";
    protected static final String PRODUCTS = "/products";
    protected static final String NEW_PRODUCT = "/new-product";

    private final ProductService productService;

    @GetMapping(PRODUCTS)
    public String productsPage() {
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

}
