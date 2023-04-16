package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.dto.FootballPlayerRegistrationDto;
import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import com.kadiryuksel.peratestcase.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private PlayerService playerService;

    @Test
    public void testDoesPlayerExistByFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";
        when(playerRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);

        boolean result = playerService.doesPlayerExistByFirstNameAndLastName(firstName, lastName);

        assertTrue(result);
        verify(playerRepository).existsByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    public void testDoesPlayerExistById() {
        long playerId = 1L;
        when(playerRepository.existsById(playerId)).thenReturn(true);

        boolean result = playerService.doesPlayerExistById(playerId);

        assertTrue(result);
        verify(playerRepository).existsById(playerId);
    }

    @Test
    public void testGetPlayerById() {
        // Arrange
        long playerId = 1L;
        Player player = new Player();
        when(playerRepository.getPlayerById(playerId)).thenReturn(player);

        Player result = playerService.getPlayerById(playerId);

        assertNotNull(result);
        assertEquals(player, result);
        verify(playerRepository).getPlayerById(playerId);
    }

    @Test
    public void testGetPlayersByTeamId() {
        long teamId = 1L;
        List<Player> players = new ArrayList<>();
        when(playerRepository.getPlayersByTeamId(teamId)).thenReturn(players);

        List<Player> result = playerService.getPlayersByTeamId(teamId);

        assertNotNull(result);
        assertEquals(players, result);
        verify(playerRepository).getPlayersByTeamId(teamId);
    }

    @Test
    public void testGetPlayerByFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";
        Player player = Player.builder().firstName(firstName).lastName(lastName).build();
        when(playerRepository.getPlayerByFirstNameAndLastName(firstName, lastName)).thenReturn(player);

        Player result = playerService.getPlayerByFirstNameAndLastName(firstName, lastName);

        assertNotNull(result);
        assertEquals(player, result);
        verify(playerRepository).getPlayerByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    public void testGetForeignPlayerCountByTeamId() {
        // Arrange
        long teamId = 1L;
        int count = 2;
        when(playerRepository.countByNationalityAndTeamId(Nationality.FOREIGN, teamId)).thenReturn(count);

        int result = playerService.getForeignPlayerCountByTeamId(teamId);

        assertEquals(count, result);
        verify(playerRepository).countByNationalityAndTeamId(Nationality.FOREIGN, teamId);
    }

    @Test
    public void testGetGoalkeeperCountByTeamId() {
        long teamId = 1L;
        int count = 1;
        when(playerRepository.countByPlayerTypeAndTeamId(PlayerType.GOALKEEPER, teamId)).thenReturn(count);

        int result = playerService.getGoalkeeperCountByTeamId(teamId);

        assertEquals(count, result);
        verify(playerRepository).countByPlayerTypeAndTeamId(PlayerType.GOALKEEPER, teamId);
    }

    @Test
    public void addNewFootballPlayer_ValidPlayerDtoAndTeam_Success() {
        FootballPlayerRegistrationDto playerDto = FootballPlayerRegistrationDto.builder()
                .firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.GOALKEEPER)
                .build();
        Team team = new Team(1L, "Test Team");

        Player savedPlayer = Player.builder().
                firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.GOALKEEPER)
                .team(team)
                .build();
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        String result = playerService.addNewFootballPlayer(playerDto, team);
        String playerName = savedPlayer.getFirstName() + " " + savedPlayer.getLastName();
        String expected = String.format("%s added to the team %s.", playerName, savedPlayer.getTeam().getTeamName());
        assertEquals(expected, result);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void updatePlayerTeam_ValidPlayerIdAndNewTeam_Success() {
        // Arrange
        Team oldTeam = new Team(1L, "Old Team");
        Team newTeam = new Team(2L, "New Team");
        Player player = Player.builder().
                firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.GOALKEEPER)
                .team(oldTeam)
                .build();
        Player updatedPlayer = Player.builder().
                firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.GOALKEEPER)
                .team(newTeam)
                .build();
        when(playerRepository.getPlayerById(1L)).thenReturn(player);
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);
        String playerName = String.format("%s %s", player.getFirstName(), player.getLastName());
        String expectedMessage = String.format("%s 's team changed from %s to %s.", playerName, oldTeam.getTeamName(), newTeam.getTeamName());
        String result = playerService.updatePlayerTeam(1L, newTeam);

        assertEquals(expectedMessage, result);
        assertEquals(newTeam, player.getTeam());
        verify(playerRepository, times(1)).getPlayerById(1L);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void deleteTeamPlayersByTeamId_ValidTeamId_Success() {
        long teamId = 1L;

        playerService.deleteTeamPlayersByTeamId(teamId);

        verify(playerRepository, times(1)).deleteAllByTeamId(teamId);
    }

    @Test
    public void deletePlayerById_ValidPlayerId_Success() {
        long playerId = 1L;

        playerService.deletePlayerById(playerId);

        verify(playerRepository, times(1)).deleteById(playerId);
    }

    @Test
    public void testGetPlayerCountByTeamId() {
        long teamId = 1L;
        int expectedCount = 10;
        when(playerRepository.countByTeamId(teamId)).thenReturn(expectedCount);

        int actualCount = playerService.getPlayerCountByTeamId(teamId);

        verify(playerRepository).countByTeamId(teamId);
        assertEquals(expectedCount, actualCount);
    }
}