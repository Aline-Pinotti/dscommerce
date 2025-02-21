package com.devsuperior.dscommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dscommerce.entities.Product;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
     * para busca paginada
     * vamos usar o Search (em vez de find) para não misturar com os Query Methods
     * (o JPA usa como padrão o Find)
     * 
     * se criamos um método customizado, recebendo um pageable como último
     * argumento, ele vai te entregar um resultado paginado
     * integrado com o Framework.. necessário ser uma consulta com JPQL
     */

    @Query("SELECT obj FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Product> searchByName(String name, Pageable pageable);// colocar somente o porcentagem fica menos compatível
                                                               // com JPQL ('%:name%')

}
