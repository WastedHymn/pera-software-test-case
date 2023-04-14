package com.kadiryuksel.peratestcase.dto;

import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FootballPlayerRegistrationDto {
    @NotBlank(message = "Enter a valid first name.")
    private String firstName;
    @NotBlank(message = "Enter a valid last name.")
    private String lastName;
    private Nationality nationality;
    private PlayerType playerType;
    @NotBlank(message = "Enter a valid team name.")
    private String teamName;
}
