package com.in6225.project.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY, defaultImpl = UserBasicDTO.class)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = UserDetailsDTO.class, name = "userDetailsDTO")
//})
//@JsonIgnoreProperties("type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserDetailsDTO.class)
})
public class UserBasicDTO {
    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    private String title;

    private String avatar;

    @NotEmpty(message = "Email is required")
    private String email;
}
