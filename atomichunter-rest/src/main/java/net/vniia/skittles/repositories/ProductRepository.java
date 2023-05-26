package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Product;
import net.vniia.skittles.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface ProductRepository extends JpaRepository<Product, Long> {
}
