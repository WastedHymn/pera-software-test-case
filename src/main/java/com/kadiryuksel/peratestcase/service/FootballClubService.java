package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.exception.PlayerAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.TeamAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FootballClubService {
    private final TeamService teamService;
    private final PlayerService playerService;

    public String addNewFootballTeam(String teamName) {
        boolean teamExist = teamService.doesTeamExistByTeamName(teamName);
        if (teamExist)
            throw new TeamAlreadyExistsException(teamName);
        return teamService.AddTeam(teamName);
    }

    public String addNewFootballPlayer(FootballPlayerRegistrationDto playerDto) {
        boolean doesPlayerExist = playerService.doesPlayerExistsByFirstNameAndLastName(
                playerDto.getFirstName(), playerDto.getLastName());
        boolean doesTeamExist = teamService.doesTeamExistByTeamName(playerDto.getTeamName());
        if(doesPlayerExist){
            String playerName =  playerDto.getFirstName() + " " + playerDto.getLastName();
            Player player = playerService.getPlayerByFirstNameAndLastName(playerDto.getFirstName(), playerDto.getLastName());
            throw new PlayerAlreadyExistsException(playerName, player.getTeam().getTeamName());
        }
        if(!doesTeamExist){
            throw new TeamNotFoundException(playerDto.getTeamName());
        }
        Team team = teamService.getTeamByTeamName(playerDto.getTeamName());
        return playerService.addNewFootballPlayer(playerDto, team);
    }

    public List<Team> getAllFootballTeams() {
        return teamService.getAllTeams();
    }

    public List<Player> getTeamPlayersByTeamId(long teamId) {
        return playerService.getPlayersByTeamId(teamId);
    }

    public int getForeignPlayerCount(long teamId) {
        return playerService.getForeignPlayerCountByTeamId(teamId);
    }

    public String deleteTeamById(long teamId) {
        boolean teamExists = teamService.doesTeamExistByTeamId(teamId);
        if (!teamExists)
            throw new TeamNotFoundException(teamId);
        String teamName = teamService.getTeamNameByTeamId(teamId);
        playerService.deleteTeamPlayersByTeamId(teamId);
        teamService.deleteTeamById(teamId);
        return String.format("%s deleted from database.", teamName);
    }
}
