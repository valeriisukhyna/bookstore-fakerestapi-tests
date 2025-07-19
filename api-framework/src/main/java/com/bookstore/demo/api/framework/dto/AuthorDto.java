package com.bookstore.demo.api.framework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Integer id;
    @JsonProperty("idBook")
    private Integer bookId;
    private String firstName;
    private String lastName;
}
