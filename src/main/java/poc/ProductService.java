package poc;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductService {

    public List<Product> getAllProducts() {
        Product product1 = new Product(UUID.randomUUID().toString(), "Milk", BigDecimal.valueOf(450.5));
        Product product2 = new Product(UUID.randomUUID().toString(), "Bread", BigDecimal.valueOf(800.25));
        return List.of(product1, product2);
    }
}
