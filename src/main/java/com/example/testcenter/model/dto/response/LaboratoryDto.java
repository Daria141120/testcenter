package com.example.testcenter.model.dto.response;

import com.example.testcenter.model.db.entity.Laboratory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

/**
 * DTO for {@link Laboratory}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaboratoryDto {
    Long id;
    String name;
    String description;
}