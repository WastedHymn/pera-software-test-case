package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.dto.FootballPlayerTeamUpdateDto;
import com.kadiryuksel.peratestcase.dto.TeamNameUpdateDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.exception.NotFoundException;
import com.kadiryuksel.peratestcase.exception.PlayerAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.PlayerLimitException;
import com.kadiryuksel.peratestcase.exception.TeamAlreadyExistsException;
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

    public void checkTeamByTeamId(long teamId) {
        boolean teamExists = teamService.doesTeamExistByTeamId(teamId);
        if (!teamExists) {
            String message = String.format("Team ID: %d does not exists.", teamId);
            throw new NotFoundException(message);
        }
    }

    public void checkPlayerById(long playerId) {
        boolean playerExists = playerService.doesPlayerExistById(playerId);
        if (!playerExists) {
            String message = String.format("Player ID: %d not found.", playerId);
            throw new NotFoundException(message);
        }
    }

    public void checkGoalkeeperLimit(int numberOfGoalkeepers, String teamName) {
        if (numberOfGoalkeepers == PlayerService.MAX_GOALKEEPER_COUNT) {
            String message = String.format("Team %s already has %d goalkeepers in the team.", teamName, PlayerService.MAX_GOALKEEPER_COUNT);
            logger.warn(message);
            throw new PlayerLimitException(message);
        }
    }

    public void checkMaxPlayerLimit(int numberOfPlayers, String teamName) {
        if (numberOfPlayers == PlayerService.MAX_PLAYER_COUNT) {
            String message = String.format("Team %s already has %d players in the team.", teamName, PlayerService.MAX_PLAYER_COUNT);
            logger.warn(message);
            throw new PlayerLimitException(message);
        }
    }

    public void checkForeignPlayerLimit(int numberOfForeignPlayers, String teamName) {
        if (numberOfForeignPlayers == PlayerService.MAX_FOREIGN_COUNT) {
            String message = String.format("Team %s already has %d foreign players in the team.", teamName, PlayerService.MAX_FOREIGN_COUNT);
            logger.warn(message);
            throw new PlayerLimitException(message);
        }
    }

    public void checkPlayerByFirstNameAndLastName(String firstName, String lastName){
        boolean doesPlayerExist = playerService.doesPlayerExistByFirstNameAndLastName(
                firstName, lastName);
        //Check if the player exists
        if (doesPlayerExist) {
            Player player = playerService.getPlayerByFirstNameAndLastName(firstName, lastName);
            String playerName = String.format("%s %s", player.getFirstName(), player.getLastName());
            String message = String.format("Player %s already exists in %s.", playerName, player.getTeam().getTeamName());
            logger.warn(message);
            throw new PlayerAlreadyExistsException(message);
        }
    }

    public String addNewFootballTeam(String teamName) {
        boolean teamExist = teamService.doesTeamExistByTeamName(teamName);
        if (teamExist) {
            String message = String.format("Team %s already exists.", teamName);
            throw new TeamAlreadyExistsException(message);
        }
        return teamService.addTeam(teamName);
    }

    public String addNewFootballPlayer(FootballPlayerRegistrationDto playerDto) {
        checkTeamByTeamId(playerDto.getTeamId());

        Team team = teamService.getTeamById(playerDto.getTeamId());

        checkPlayerByFirstNameAndLastName(playerDto.getFirstName(), playerDto.getLastName());
        int teamPlayerCount = playerService.getPlayerCountByTeamId(team.getId());
        //Check player count limit
        checkMaxPlayerLimit(teamPlayerCount, team.getTeamName());

        //Check if the player is a goalkeeper
        if (playerDto.getPlayerType() == PlayerType.GOALKEEPER) {
            int goalkeeperCount = playerService.getGoalkeeperCountByTeamId(team.getId());
            checkGoalkeeperLimit(goalkeeperCount, team.getTeamName());
        }

        //Check if the player is foreign
        if (playerDto.getNationality() == Nationality.FOREIGN) {
            int foreignCount = playerService.getForeignPlayerCountByTeamId(team.getId());
            checkForeignPlayerLimit(foreignCount, team.getTeamName());
        }
        return playerService.addNewFootballPlayer(playerDto, team);
    }

    public List<Team> getAllFootballTeams() {
        return teamService.getAllTeams();
    }

    public List<Player> getTeamPlayersByTeamId(long teamId) {
        return playerService.getPlayersByTeamId(teamId);
    }

    public String deleteTeamById(long teamId) {
        checkTeamByTeamId(teamId);

        String teamName = teamService.getTeamNameByTeamId(teamId);
        playerService.deleteTeamPlayersByTeamId(teamId);
        teamService.deleteTeamById(teamId);
        return String.format("Team %s deleted from database.", teamName);
    }

    public String deletePlayerById(long playerId) {
        checkPlayerById(playerId);

        Player player = playerService.getPlayerById(playerId);
        playerService.deletePlayerById(playerId);
        return String.format("Player %s %s deleted from team %s.", player.getFirstName(), player.getLastName(), player.getTeam().getTeamName());
    }

    public String changeTeamName(TeamNameUpdateDto updateDto) {
        checkTeamByTeamId(updateDto.getTeamId());
        return teamService.updateTeamName(updateDto.getTeamId(), updateDto.getNewTeamName());
    }

    public String changePlayerTeam(FootballPlayerTeamUpdateDto teamUpdateDto) {
        checkTeamByTeamId(teamUpdateDto.getNewTeamId());
        checkPlayerById(teamUpdateDto.getPlayerId());

        Team team = teamService.getTeamById(teamUpdateDto.getNewTeamId());
        Player player = playerService.getPlayerById(teamUpdateDto.getPlayerId());

        //Check maximum number of players limit
        int playerCount = playerService.getPlayerCountByTeamId(team.getId());
        checkMaxPlayerLimit(playerCount, team.getTeamName());

        //Check maximum number of foreign players limit
        if (player.getNationality() == Nationality.FOREIGN) {
            int foreignCount = playerService.getForeignPlayerCountByTeamId(team.getId());
            checkForeignPlayerLimit(foreignCount, team.getTeamName());
        }

        //Check maximum number of goalkeepers limit
        if (player.getPlayerType() == PlayerType.GOALKEEPER) {
            int goalkeeperCount = playerService.getGoalkeeperCountByTeamId(team.getId());
            checkGoalkeeperLimit(goalkeeperCount, team.getTeamName());
        }
        return playerService.updatePlayerTeam(teamUpdateDto.getPlayerId(), team);
    }
}
