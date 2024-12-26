package com.mars.NangPaGo.domain.ingredient.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@Document(indexName = "ingredients_dictionary")
public class IngredientElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    public IngredientElastic() {

    }

    public IngredientElastic(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IngredientElastic(Long id, String name) {
        this.id = String.valueOf(id);
        this.name = name;
    }
}
