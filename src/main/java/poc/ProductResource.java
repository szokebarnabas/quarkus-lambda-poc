package poc;

import static io.quarkus.vertx.web.Route.HttpMethod.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;
import java.util.List;

public class ProductResource {

    private ObjectMapper objectMapper;
    private ProductService productService;

    @Inject
    public ProductResource(ObjectMapper objectMapper, ProductService productService) {
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Route(path = "/products", methods = GET, produces = "application/json")
    void getAllProducts(RoutingContext context) throws JsonProcessingException {
        HttpServerResponse response = context.response();
        List<Product> productList = productService.getAllProducts() ;

        response.headers().set("Content-Type", "application/json");
        response.setStatusCode(200).end(objectMapper.writeValueAsString(productList));
    }

}
