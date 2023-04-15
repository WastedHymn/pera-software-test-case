package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    public static final int MAX_GOALKEEPER_COUNT = 2;
    public static final int MAX_FOREIGN_COUNT = 6;
    public static final int MAX_PLAYER_COUNT = 18;

    public boolean doesPlayerExistByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    public boolean doesPlayerExistById(long playerId) {
        return playerRepository.existsById(playerId);
    }

    public Player getPlayerById(long playerId) {
        return playerRepository.getPlayerById(playerId);
    }

    public List<Player> getPlayersByTeamId(long teamId) {
        return playerRepository.getPlayersByTeamId(teamId);
    }

    public Player getPlayerByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.getPlayerByFirstNameAndLastName(firstName, lastName);
    }

    public int getForeignPlayerCountByTeamId(long teamId) {
        return playerRepository.countByNationalityAndTeamId(Nationality.FOREIGN, teamId);
    }

    public int getGoalkeeperCountByTeamId(long teamId) {
        return playerRepository.countByPlayerTypeAndTeamId(PlayerType.GOALKEEPER, teamId);
    }

    public int getPlayerCountByTeamId(long teamId) {
        return playerRepository.countByTeamId(teamId);
    }

    @Transactional
    public String addNewFootballPlayer(FootballPlayerRegistrationDto playerDto, Team team) {
        Player newPlayer = Player.builder()
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .nationality(playerDto.getNationality())
                .playerType(playerDto.getPlayerType())
                .team(team)
                .build();
        Player savedPlayer = playerRepository.save(newPlayer);
        String playerName = savedPlayer.getFirstName() + " " + savedPlayer.getLastName();
        logger.info(String.format("New football player added to the player table -> %s", savedPlayer));
        return String.format("%s added to the team %s.", playerName, savedPlayer.getTeam().getTeamName());
    }

    @Transactional
    public String updatePlayerTeam(long playerId, Team newTeam) {
        Player player = playerRepository.getPlayerById(playerId);
        String oldTeamStr = player.getTeam().getTeamName();
        player.setTeam(newTeam);
        Player updatedPlayer = playerRepository.save(player);
        String newTeamStr = updatedPlayer.getTeam().getTeamName();
        String playerName = String.format("%s %s", player.getFirstName(), player.getLastName());
        String message = String.format("%s 's team changed from %s to %s.", playerName, oldTeamStr, newTeamStr);
        logger.info(message);
        return message;
    }

    @Transactional
    public void deleteTeamPlayersByTeamId(long teamId) {
        playerRepository.deleteAllByTeamId(teamId);
    }

    @Transactional
    public void deletePlayerById(long playerId) {
        playerRepository.deleteById(playerId);
    }
}
