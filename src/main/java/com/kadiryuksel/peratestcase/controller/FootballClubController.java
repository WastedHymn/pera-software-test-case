package com.kadiryuksel.peratestcase.controller;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
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

    @GetMapping("/getAllFootballTeams")
    public ResponseEntity<List<Team>> getAllFootballTeams() {
        List<Team> teams = footballClubService.getAllFootballTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/getTeamPlayers/team/{id}")
    public ResponseEntity<List<Player>> getTeamPlayersById(
            @PathVariable("id")
            @PositiveOrZero(message = "Id must be zero or higher value.") long id) {
        List<Player> players = footballClubService.getTeamPlayersByTeamId(id);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @DeleteMapping("/deleteTeam/{id}")
    public ResponseEntity<String> deleteTeamById(
            @PathVariable("id")
            @PositiveOrZero(message = "Id must be zero or higher value.") long teamId) {
        String message = footballClubService.deleteTeamById(teamId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getForeignPlayerCount/club/{id}")
    public int foreignPlayerCount(@PathVariable("id") long teamId) {
        return footballClubService.getForeignPlayerCount(teamId);
    }
}
