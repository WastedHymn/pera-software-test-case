package com.kadiryuksel.peratestcase.dto;

import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.util.ConstantMessages;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class FootballPlayerRegistrationDto {
    @NotBlank(message = "Enter a valid first name.")
    private String firstName;
    @NotBlank(message = "Enter a valid last name.")
    private String lastName;
    private Nationality nationality;
    private PlayerType playerType;
    @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG)
    private long teamId;
}
