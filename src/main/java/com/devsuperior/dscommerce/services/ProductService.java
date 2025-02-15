package com.devsuperior.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
        
        Product product = repository.findById(id).get(); // retorna um Optional vazio se não existir o id passado, um optional de produto se encontrar
        ProductDTO dto = new ProductDTO(product);
        return dto;
    }
}
