package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.dto.TeamNameUpdateDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.exception.PlayerAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.PlayerLimitException;
import com.kadiryuksel.peratestcase.exception.TeamAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FootballClubService {
    private final TeamService teamService;
    private final PlayerService playerService;
    private final Logger logger = LoggerFactory.getLogger(FootballClubService.class);

    public String addNewFootballTeam(String teamName) {
        boolean teamExist = teamService.doesTeamExistByTeamName(teamName);
        if (teamExist)
            throw new TeamAlreadyExistsException(teamName);
        return teamService.addTeam(teamName);
    }

    public String addNewFootballPlayer(FootballPlayerRegistrationDto playerDto) {
        boolean doesTeamExist = teamService.doesTeamExistByTeamName(playerDto.getTeamName());
        //Check if the team does not exist
        if (!doesTeamExist) {
            String message = String.format("Team %s does not exist.", playerDto.getTeamName());
            logger.warn(message);
            throw new NotFoundException(message);
        }
        Team team = teamService.getTeamByTeamName(playerDto.getTeamName());

        boolean doesPlayerExist = playerService.doesPlayerExistByFirstNameAndLastName(
                playerDto.getFirstName(), playerDto.getLastName());
        //Check if the player exists
        if (doesPlayerExist) {
            String playerName = String.format("%s %s", playerDto.getFirstName(), playerDto.getLastName());
            String message = String.format("Player %s already exists in %s.", playerName, team.getTeamName());
            logger.warn(message);
            throw new PlayerAlreadyExistsException(message);
        }

        int teamPlayerCount = playerService.getPlayerCountByTeamId(team.getId());
        //Check player count limit
        if (teamPlayerCount == PlayerService.MAX_PLAYER_COUNT) {
            String message = String.format("Team %s already has %d players in the team.", team.getTeamName(), PlayerService.MAX_PLAYER_COUNT);
            logger.warn(message);
            throw new PlayerLimitException(message);
        }

        //Check if the player is a goalkeeper
        if (playerDto.getPlayerType() == PlayerType.GOALKEEPER) {
            int goalkeeperCount = playerService.getGoalkeeperCountByTeamId(team.getId());
            if (goalkeeperCount >= PlayerService.MAX_GOALKEEPER_COUNT) {
                String message = String.format("Team %s already has %d goalkeeper in the team.", team.getTeamName(), PlayerService.MAX_GOALKEEPER_COUNT);
                logger.warn(message);
                throw new PlayerLimitException(message);//GoalkeeperLimitException(message);
            }
        }

        //Check if the player is foreign
        if (playerDto.getNationality() == Nationality.FOREIGN) {
            int foreignCount = playerService.getForeignPlayerCountByTeamId(team.getId());
            if (foreignCount == PlayerService.MAX_FOREIGN_COUNT) {
                String message = String.format("Team %s already has %d foreign players in the team.", team.getTeamName(), PlayerService.MAX_FOREIGN_COUNT);
                logger.warn(message);
                throw new PlayerLimitException(message);
            }
        }
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
        if (!teamExists){
            String message = String.format("Team ID: %d does not exists.", teamId);
            throw new NotFoundException(message);
        }

        String teamName = teamService.getTeamNameByTeamId(teamId);
        playerService.deleteTeamPlayersByTeamId(teamId);
        teamService.deleteTeamById(teamId);
        return String.format("Team %s deleted from database.", teamName);
    }

    public String deletePlayerById(long playerId) {
        boolean playerExists = playerService.doesPlayerExistById(playerId);
        if (!playerExists){
            String message = String.format("Player ID: %d not found.", playerId);
            throw new NotFoundException(message);
        }
        Player player = playerService.getPlayerById(playerId);
        playerService.deletePlayerById(playerId);
        return String.format("Player %s %s deleted from team %s.", player.getFirstName(), player.getLastName(), player.getTeam().getTeamName());
    }

    public String changeTeamName(TeamNameUpdateDto updateDto){
        boolean teamExists = teamService.doesTeamExistByTeamId(updateDto.getTeamId());
        if(!teamExists){
            String message = String.format("Team ID: %d does not exists.", updateDto.getTeamId());
            throw new NotFoundException(message);
        }
        return teamService.updateTeamName(updateDto.getTeamId(), updateDto.getNewTeamName());
    }
}
