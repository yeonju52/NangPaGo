package com.mars.NangPaGo.domain.ingredient.service;

import com.mars.NangPaGo.domain.ingredient.entity.Ingredient;
import com.mars.NangPaGo.domain.ingredient.entity.IngredientElastic;
import com.mars.NangPaGo.domain.ingredient.repository.IngredientElasticRepository;
import com.mars.NangPaGo.domain.ingredient.repository.IngredientRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class IngredientElasticSynchronizer {
    @Autowired private IngredientRepository ingredientRepository;
    @Autowired private IngredientElasticRepository ingredientElasticRepository;

    public String insertIngredientFromMysql() {
        try {
            List<Ingredient> ingredientList;
            ingredientList = ingredientRepository.findAll();

            List<IngredientElastic> ingredientElasticList = new ArrayList<>();
            for (Ingredient ingredient : ingredientList) {
                IngredientElastic ingredientElastic = new IngredientElastic(ingredient.getId(), ingredient.getName());

                ingredientElasticList.add(ingredientElastic);
            }
            ingredientElasticRepository.saveAll(ingredientElasticList);
            return "MySQL로부터 데이터를 성공적으로 삽입했습니다!";
        } catch (Exception e) {
            e.printStackTrace();
            return "MySQL 데이터 삽입 중 오류 발생: " + e.getMessage();
        }
    }

    public String insertIngredientFromCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreSurroundingSpaces()
                .withTrim()
                .withQuote('"'));

            List<IngredientElastic> ingredientElasticList = new ArrayList<>();
            int lineNumber = 1;

            for (CSVRecord record : csvParser) {
                try {
                    String id = record.get("id").trim();
                    String name = record.get("name").trim();

                    IngredientElastic ingredientElastic = new IngredientElastic(id, name);

                    ingredientElasticList.add(ingredientElastic);
                    lineNumber++;
                } catch (Exception e) {
                    System.err.println("Error parsing line (" + lineNumber + "): " + e.getMessage());
                }
            }
            ingredientElasticRepository.saveAll(ingredientElasticList);
            return "CSV 파일로부터 데이터를 성공적으로 삽입했습니다!";
        } catch (Exception e) {
            e.printStackTrace();
            return "CSV 데이터 삽입 중 오류 발생: " + e.getMessage();
        }
    }
}
