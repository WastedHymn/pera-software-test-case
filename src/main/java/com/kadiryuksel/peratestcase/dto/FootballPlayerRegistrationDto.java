package com.kadiryuksel.peratestcase.dto;

import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.util.ConstantMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
