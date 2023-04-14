package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
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

    public List<Player> getPlayersByTeamId(long teamId) {
        return playerRepository.getPlayersByTeamId(teamId);
    }

    public Player getPlayerByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.getPlayerByFirstNameAndLastName(firstName, lastName);
    }

    public boolean doesPlayerExistsByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    public int getForeignPlayerCountByTeamId(long teamId) {
        return playerRepository.countByNationalityAndTeamId(Nationality.FOREIGN, teamId);
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
        return String.format("%s added to the player database.", playerName);
    }

    @Transactional
    public void deleteTeamPlayersByTeamId(long teamId) {
        playerRepository.deleteAllByTeamId(teamId);
    }
}
