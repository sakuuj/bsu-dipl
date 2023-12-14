package by.bsu.fpmi.productservice.repository;

import by.bsu.fpmi.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
