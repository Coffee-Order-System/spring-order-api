package gc.cafe.order.api;

import static gc.cafe.order.api.ProductApi.PREFIX;

import gc.cafe.order.mapper.ProductMapper;
import gc.cafe.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PREFIX)
public class ProductApi {

    protected static final String PREFIX = "/api/v1";
    protected static final String PRODUCTS = "/products";

    private final ProductService productService;

    @GetMapping(PRODUCTS)
    public ResponseEntity productList() {
        return ResponseEntity.ok(productService.getAllProducts().stream()
                .map(ProductMapper::productToProductDto)
                .toList());
    }

}


