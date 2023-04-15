package com.kadiryuksel.peratestcase.dto;

import com.kadiryuksel.peratestcase.ConstantMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FootballPlayerTeamUpdateDto {
    @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG)
    long playerId;
    @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG)
    long newTeamId;
}
