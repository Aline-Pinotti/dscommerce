package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    private Long id;

    @Size(min = 3, max = 80, message = "O nome precisa ter entre 3 e 80 caracteres")
    // @NotNull -- verifica se a informação não veio no JSON (ela simplesmente não
    // vem na estrutura)
    // @NotEmpty -- quando veio, mas está sem informação ex: "name" : ""
    @NotBlank(message = "Campo requerido") // também verifica se a informação não veio no JSON ex "name" : " "
    private String name;

    @Size(min = 10, max = 1000, message = "A descrição precisa ter no mínimo 10 caracteres")
    private String description;

    @Positive(message = "O preço deve ser positivo")
    private Double price;
    private String imgUrl;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
