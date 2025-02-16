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

        Product entity = repository.getReferenceById(id);

        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);
        return new ProductDTO(entity);

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
