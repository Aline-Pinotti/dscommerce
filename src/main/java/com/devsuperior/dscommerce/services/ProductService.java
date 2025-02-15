package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true) // pra nao dar locking no banco de dados
    public ProductDTO findById(Long id) {

        Product product = repository.findById(id).get(); // retorna um Optional vazio se não existir o id passado, um
                                                         // optional de produto se encontrar
        ProductDTO dto = new ProductDTO(product);
        return dto;
    }

    @Transactional(readOnly = true) // pra nao dar locking no banco de dados
    public Page<ProductDTO> findAll(Pageable pageable) {

        Page<Product> result = repository.findAll(pageable);
        // return result.stream().map(x -> new ProductDTO(x)).toList(); // sem
        // pageable...
        return result.map(x -> new ProductDTO(x)); // map direto porq Page já é um stream do Java
    }
}
