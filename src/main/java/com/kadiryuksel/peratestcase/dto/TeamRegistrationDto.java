package com.kadiryuksel.peratestcase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamRegistrationDto {
    @NotBlank(message = "Enter a football club name.")
    private String teamName;
}
