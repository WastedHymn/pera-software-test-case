package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.exception.PlayerAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.PlayerLimitException;
import com.kadiryuksel.peratestcase.exception.TeamAlreadyExistsException;
import com.kadiryuksel.peratestcase.exception.TeamNotFoundException;
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
        return teamService.AddTeam(teamName);
    }

    public String addNewFootballPlayer(FootballPlayerRegistrationDto playerDto) {
        boolean doesTeamExist = teamService.doesTeamExistByTeamName(playerDto.getTeamName());
        //Check if the team does not exist
        if (!doesTeamExist) {
            logger.warn(
                    String.format("Team %s does not exist.", playerDto.getTeamName())
            );
            throw new TeamNotFoundException(playerDto.getTeamName());
        }
        Team team = teamService.getTeamByTeamName(playerDto.getTeamName());

        boolean doesPlayerExist = playerService.doesPlayerExistsByFirstNameAndLastName(
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
        if (!teamExists)
            throw new TeamNotFoundException(teamId);
        String teamName = teamService.getTeamNameByTeamId(teamId);
        playerService.deleteTeamPlayersByTeamId(teamId);
        teamService.deleteTeamById(teamId);
        return String.format("Team %s deleted from database.", teamName);
    }
}
