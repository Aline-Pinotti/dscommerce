package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true) // pra nao dar locking no banco de dados
    public ProductDTO findById(Long id) {

        // retorna um Optional vazio se não existir o id passado, um Optional com o
        // produto se existir
        // Product product = repository.findById(id).get();

        // utilizando o orElseThrow do Optional para lançar uma exceção caso o id não
        // exista -
        // *substitui o try catch*
        // Product product = repository.findById(id).orElseThrow(() -> new
        // IllegalArgumentException("Id not found")); //sem exception customizada
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")); // interceptando a exceção
                                                                                             // do Optional e lançando a
                                                                                             // própria exceção
        // importante lançar as próprias exceções e serviços para manter a arquitetura
        // organizada, a camada de serviço é responsável pelas próprias exceções
        ProductDTO dto = new ProductDTO(product);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {

        Page<Product> result = repository.findAll(pageable);
        // return result.stream().map(x -> new ProductDTO(x)).toList(); // sem pageable
        return result.map(x -> new ProductDTO(x)); // map direto porq Page já é um stream do Java
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        Product entity = new Product();
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);
        return new ProductDTO(entity);

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);

            copyDtoToEntity(dto, entity);

            entity = repository.save(entity);
            return new ProductDTO(entity);

        } catch (EntityNotFoundException e) { // vai ser interceptado pelo Handler
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }
}
