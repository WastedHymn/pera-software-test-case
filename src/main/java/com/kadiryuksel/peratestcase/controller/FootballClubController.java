package com.kadiryuksel.peratestcase.controller;

import com.kadiryuksel.peratestcase.util.ConstantMessages;
import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.dto.FootballPlayerTeamUpdateDto;
import com.kadiryuksel.peratestcase.dto.TeamNameUpdateDto;
import com.kadiryuksel.peratestcase.dto.TeamRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.service.FootballClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class FootballClubController {
    private final FootballClubService footballClubService;

    @PostMapping("/registerFootballTeam")
    public ResponseEntity<String> registerFootballTeam(@RequestBody @Valid TeamRegistrationDto club) {
        String message = footballClubService.addNewFootballTeam(club.getTeamName());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/registerFootballPlayer")
    public ResponseEntity<String> registerFootballPlayer(@RequestBody @Valid FootballPlayerRegistrationDto playerDto) {
        String message = footballClubService.addNewFootballPlayer(playerDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/updateTeamNameById")
    public ResponseEntity<String> updateTeamName(@RequestBody @Valid TeamNameUpdateDto teamNameUpdateDto){
        String message = footballClubService.changeTeamName(teamNameUpdateDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/updatePlayerTeam")
    public ResponseEntity<String> updatePlayerTeam(@RequestBody @Valid FootballPlayerTeamUpdateDto teamUpdateDto){
        String message = footballClubService.changePlayerTeam(teamUpdateDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getAllFootballTeams")
    public ResponseEntity<List<Team>> getAllFootballTeams() {
        List<Team> teams = footballClubService.getAllFootballTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/getTeamPlayers/team/{id}")
    public ResponseEntity<List<Player>> getTeamPlayersById(
            @PathVariable("id")
            @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG) long teamId) {
        List<Player> players = footballClubService.getTeamPlayersByTeamId(teamId);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @DeleteMapping("/deleteTeam/{id}")
    public ResponseEntity<String> deleteTeamById(
            @PathVariable("id")
            @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG) long teamId) {
        String message = footballClubService.deleteTeamById(teamId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/deletePlayer/{id}")
    public ResponseEntity<String> deletePlayerById(
            @PathVariable("id")
            @PositiveOrZero(message = ConstantMessages.POSITIVE_OR_ZERO_MSG) long playerId) {
        String message = footballClubService.deletePlayerById(playerId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
