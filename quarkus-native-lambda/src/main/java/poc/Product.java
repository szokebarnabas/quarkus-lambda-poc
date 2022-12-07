package poc;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public record Product(String id, String name, BigDecimal price) {}