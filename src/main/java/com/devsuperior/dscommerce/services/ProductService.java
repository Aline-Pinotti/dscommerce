package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
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
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {

        Page<Product> result = repository.searchByName(name, pageable);
        return result.map(x -> new ProductMinDTO(x));
    }

    // quando usava o metodo padrão do JPA
    /*
     * public Page<ProductDTO> findAll(Pageable pageable) {
     * 
     * Page<Product> result = repository.findAll(pageable);
     * // return result.stream().map(x -> new ProductDTO(x)).toList(); // sem
     * pageable
     * return result.map(x -> new ProductDTO(x)); // map direto porq Page já é um
     * stream do Java
     * }
     */

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

    // Para não lançar exceção do banco de dados (H2), usamos o propagation,
    // informando que só vai lançar a exceção do DB se não estiver no contexto dessa
    // annotation
    // Propagation.SUPPORTS somente executa a transação se esse método estiver no
    // contexto e outra transação - para capturar o DataIntegrityViolationException

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {// tentando apagar um produto que já tem pedido
            throw new DataBaseException("Falha de integridade referencial");
        }
    }

    /*
     * ANTIGO
     * public void delete(Long id) {
     * try {
     * repository.deleteById(id); // porque não gera mais exceção se tentar deletar
     * // um id que não existe
     * }
     * catch (EmptyResultDataAccessException e) { // não funciona mais
     * throw new ResourceNotFoundException("Recurso não encontrado");
     * }
     * catch (DataIntegrityViolationException e) {
     * throw new DatabaseException("Falha de integridade referencial");
     * }
     * }
     */

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }
}
