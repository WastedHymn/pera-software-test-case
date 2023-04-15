package com.kadiryuksel.peratestcase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamNameUpdateDto {
    @NotBlank(message = "Enter the new name.")
    private String newTeamName;
    @PositiveOrZero(message = "Team ID cannot be negative value.")
    private long teamId;
}
