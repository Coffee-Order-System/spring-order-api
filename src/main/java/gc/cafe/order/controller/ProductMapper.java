package gc.cafe.order.controller;

import gc.cafe.order.dto.ProductDto;
import gc.cafe.order.model.Product;

public interface ProductMapper {

    static ProductDto productToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId().toString());
        productDto.setProductName(product.getProductName());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setDescription(product.getDescription());
        productDto.setCreatedAt(product.getCreatedAt().toString());
        productDto.setCreatedAt(product.getUpdatedAt().toString());
        return productDto;
    }

}
