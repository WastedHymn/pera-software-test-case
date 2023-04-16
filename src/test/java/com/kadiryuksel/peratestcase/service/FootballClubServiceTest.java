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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballClubServiceTest {
    @Mock
    private PlayerService playerService;
    @Mock
    private TeamService teamService;

    //@Mock
    //private Logger logger;

    @InjectMocks
    private FootballClubService footballClubService;

    @Test
    public void testCheckTeamByTeamId_teamExists() {
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);

        footballClubService.checkTeamByTeamId(1L);
    }

    @Test
    public void testCheckTeamByTeamId_teamDoesNotExist() {
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> footballClubService.checkTeamByTeamId(1L));
    }

    @Test
    public void testCheckPlayerById_PlayerExists() {
        long playerId = 123L;
        when(playerService.doesPlayerExistById(playerId)).thenReturn(true);

        footballClubService.checkPlayerById(playerId);
    }

    @Test
    public void testCheckPlayerById_PlayerNotFound() {
        long playerId = 123L;
        when(playerService.doesPlayerExistById(playerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> footballClubService.checkPlayerById(playerId));
    }

    @Test
    public void testCheckGoalkeeperLimit_withLimit() {
        int numberOfGoalkeepers = PlayerService.MAX_GOALKEEPER_COUNT;
        String teamName = "Test Team";

        assertThrows(PlayerLimitException.class, () -> footballClubService.checkGoalkeeperLimit(numberOfGoalkeepers, teamName));
    }

    @Test
    public void testCheckGoalkeeperLimit_belowLimit() {
        int numberOfGoalkeepers = PlayerService.MAX_GOALKEEPER_COUNT - 1;
        String teamName = "Test Team";

        footballClubService.checkGoalkeeperLimit(numberOfGoalkeepers, teamName);
    }

    @Test
    public void testCheckMaxPlayerLimit_withinLimit() {
        int numberOfPlayers = 10;
        String teamName = "Test Team";

        footballClubService.checkMaxPlayerLimit(numberOfPlayers, teamName);
    }

    @Test
    public void testCheckMaxPlayerLimit_atLimit() {
        int numberOfPlayers = PlayerService.MAX_PLAYER_COUNT;
        String teamName = "Test Team";
        assertThrows(PlayerLimitException.class, () -> footballClubService.checkMaxPlayerLimit(numberOfPlayers, teamName));
    }

    @Test
    public void testCheckForeignPlayerLimit_numberOfForeignPlayersIsLessThanMax() {
        String teamName = "Team A";
        int numberOfForeignPlayers = PlayerService.MAX_FOREIGN_COUNT - 1;


        footballClubService.checkForeignPlayerLimit(numberOfForeignPlayers, teamName);
    }

    @Test
    public void testCheckForeignPlayerLimit_numberOfForeignPlayersIsEqualToMax() {
        String teamName = "Team B";
        int numberOfForeignPlayers = PlayerService.MAX_FOREIGN_COUNT;

        assertThrows(PlayerLimitException.class, () -> footballClubService.checkForeignPlayerLimit(numberOfForeignPlayers, teamName));
    }

    @Test
    public void testCheckPlayerByFirstNameAndLastName_playerDoesNotExist() {
        when(playerService.doesPlayerExistByFirstNameAndLastName(anyString(), anyString())).thenReturn(false);
        footballClubService.checkPlayerByFirstNameAndLastName("John", "Doe");
    }

    @Test
    public void testCheckPlayerByFirstNameAndLastName_playerExists() {
        when(playerService.doesPlayerExistByFirstNameAndLastName(anyString(), anyString())).thenReturn(true);

        Player player = new Player();
        player.setFirstName("John");
        player.setLastName("Doe");
        Team team = new Team();
        team.setTeamName("MyTeam");
        player.setTeam(team);
        when(playerService.getPlayerByFirstNameAndLastName("John", "Doe")).thenReturn(player);

        assertThrows(PlayerAlreadyExistsException.class, () -> footballClubService.checkPlayerByFirstNameAndLastName("John", "Doe"));
    }

    @Test
    public void testAddNewFootballTeam_teamDoesNotExist() {
        String teamName = "New Team";
        when(teamService.doesTeamExistByTeamName(anyString())).thenReturn(false);
        when(teamService.addTeam(teamName)).thenReturn(teamName);

        String addedTeamName = footballClubService.addNewFootballTeam(teamName);
        assertEquals(teamName, addedTeamName);
    }

    @Test
    public void testAddNewFootballTeam_teamAlreadyExists() {
        String teamName = "Existing Team";
        when(teamService.doesTeamExistByTeamName(anyString())).thenReturn(true);

        assertThrows(TeamAlreadyExistsException.class, () -> footballClubService.addNewFootballTeam(teamName));
    }

    @Test
    public void testAddNewFootballPlayer_Success() {
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        when(teamService.getTeamById(anyLong())).thenReturn(Team.builder().teamName("test team").id(1L).build());
        when(playerService.getPlayerCountByTeamId(anyLong())).thenReturn(10);
        when(playerService.addNewFootballPlayer(any(FootballPlayerRegistrationDto.class), any(Team.class))).thenReturn("Player Added Successfully");

        // create a player DTO
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setTeamId(1L);
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.LOCAL);
        playerDto.setPlayerType(PlayerType.DEFENDER);

        String result = footballClubService.addNewFootballPlayer(playerDto);
        assertEquals("Player Added Successfully", result);
    }

    @Test
    public void testAddNewFootballPlayer_TeamDoesNotExist() {
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(false);

        // create a player DTO
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setTeamId(1L);
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.LOCAL);
        playerDto.setPlayerType(PlayerType.MIDFIELDER);

        assertThrows(NotFoundException.class, () -> footballClubService.addNewFootballPlayer(playerDto));
    }

    @Test
    public void testAddNewFootballPlayer_PlayerAlreadyExists() {
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        when(teamService.getTeamById(anyLong())).thenReturn(new Team());
        when(playerService.doesPlayerExistByFirstNameAndLastName(anyString(), anyString())).thenReturn(true);
        when(playerService
                .getPlayerByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(
                        Player.builder()
                                .firstName("John").lastName("Doe")
                                .team(Team.builder().teamName("test team").build())
                                .build()
                );
        // create a player DTO
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setTeamId(1L);
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.LOCAL);
        playerDto.setPlayerType(PlayerType.FORWARD);

        assertThrows(PlayerAlreadyExistsException.class, () -> footballClubService.addNewFootballPlayer(playerDto));
    }

    @Test
    public void testAddNewFootballPlayer_maxPlayerLimitExceeded() {
        // create a football player registration DTO
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setTeamId(1);
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.LOCAL);
        playerDto.setPlayerType(PlayerType.FORWARD);

        Team team = new Team();
        team.setId(1L);
        team.setTeamName("Team A");
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        when(playerService.doesPlayerExistByFirstNameAndLastName(anyString(), anyString())).thenReturn(false);
        when(playerService.getPlayerCountByTeamId(1L)).thenReturn(PlayerService.MAX_PLAYER_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.addNewFootballPlayer(playerDto));

        // verify that the team and player service methods were called as expected
        verify(teamService, times(1)).getTeamById(1);
        verify(playerService, times(1)).getPlayerCountByTeamId(1);
        verifyNoMoreInteractions(teamService, playerService);
    }

    @Test
    public void testAddNewFootballPlayer_goalkeeperLimitExceeded() {
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.FOREIGN);
        playerDto.setPlayerType(PlayerType.GOALKEEPER);
        playerDto.setTeamId(1L);

        Team team = new Team();
        team.setId(1L);
        team.setTeamName("Team 1");

        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        when(playerService.getPlayerCountByTeamId(anyLong())).thenReturn(10);
        when(playerService.getGoalkeeperCountByTeamId(anyLong())).thenReturn(PlayerService.MAX_GOALKEEPER_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.addNewFootballPlayer(playerDto));
    }

    @Test
    public void testAddNewFootballPlayer_foreignPlayerLimitExceeded() {
        FootballPlayerRegistrationDto playerDto = new FootballPlayerRegistrationDto();
        playerDto.setFirstName("John");
        playerDto.setLastName("Doe");
        playerDto.setNationality(Nationality.FOREIGN);
        playerDto.setPlayerType(PlayerType.FORWARD);
        playerDto.setTeamId(1L);

        Team team = new Team();
        team.setId(1L);
        team.setTeamName("Team 1");

        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        when(playerService.getPlayerCountByTeamId(anyLong())).thenReturn(10);
        when(playerService.getForeignPlayerCountByTeamId(anyLong())).thenReturn(PlayerService.MAX_FOREIGN_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.addNewFootballPlayer(playerDto));
    }

    @Test
    public void testGetAllFootballTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(Team.builder().teamName("Team A").build());
        teams.add(Team.builder().teamName("Team B").build());
        teams.add(Team.builder().teamName("Team C").build());
        when(teamService.getAllTeams()).thenReturn(teams);

        List<Team> result = footballClubService.getAllFootballTeams();
        assertEquals(3, result.size());
        assertEquals("Team A", result.get(0).getTeamName());
        assertEquals("Team B", result.get(1).getTeamName());
        assertEquals("Team C", result.get(2).getTeamName());
    }

    @Test
    public void testGetTeamPlayersByTeamId() {
        long teamId = 1L;

        List<Player> players = new ArrayList<>();

        players.add(
                Player.builder().firstName("John").lastName("Doe").playerType(PlayerType.FORWARD).nationality(Nationality.LOCAL).team(Team.builder().teamName("test team").id(1L).build()).build()
        );

        players.add(
                Player.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .playerType(PlayerType.DEFENDER)
                        .nationality(Nationality.FOREIGN)
                        .team(Team.builder().teamName("test team").id(1L).build())
                        .build()
        );

        players.add(
                Player.builder()
                        .firstName("Jack")
                        .lastName("Smith")
                        .playerType(PlayerType.MIDFIELDER)
                        .nationality(Nationality.LOCAL)
                        .team(Team.builder().teamName("test team").id(1L).build())
                        .build()
        );

        when(playerService.getPlayersByTeamId(teamId)).thenReturn(players);

        List<Player> teamPlayers = footballClubService.getTeamPlayersByTeamId(teamId);
        assertEquals(players, teamPlayers);
    }

    @Test
    public void testDeleteTeamById_teamExists() {
        long teamId = 1L;
        String teamName = "Team A";
        when(teamService.getTeamNameByTeamId(teamId)).thenReturn(teamName);
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);
        String expectedMessage = String.format("Team %s deleted from database.", teamName);

        assertEquals(expectedMessage, footballClubService.deleteTeamById(teamId));

        verify(teamService, times(1)).getTeamNameByTeamId(teamId);
        verify(playerService, times(1)).deleteTeamPlayersByTeamId(teamId);
        verify(teamService, times(1)).deleteTeamById(teamId);
    }

    @Test
    public void testDeleteTeamById_teamDoesNotExist() {
        long teamId = 1L;
        when(teamService.doesTeamExistByTeamId(teamId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> footballClubService.deleteTeamById(teamId));

        verify(teamService, times(1)).doesTeamExistByTeamId(teamId);
        verify(playerService, times(0)).deleteTeamPlayersByTeamId(teamId);
        verify(teamService, times(0)).deleteTeamById(teamId);
    }

    @Test
    public void testDeletePlayerById_playerExists() {
        Player player = Player.builder()
                .firstName("John")
                .lastName("Doe")
                .id(1L)
                .team(
                        Team.builder().teamName("Test Team").id(1L).build()
                )
                .build();

        when(playerService.getPlayerById(player.getId())).thenReturn(player);
        when(playerService.doesPlayerExistById(player.getId())).thenReturn(true);

        String expectedMessage = "Player John Doe deleted from team Test Team.";
        String actualMessage = footballClubService.deletePlayerById(player.getId());

        verify(playerService, times(1)).deletePlayerById(player.getId());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testDeletePlayerById_playerDoesNotExist() {
        long playerId = 1L;

        when(playerService.doesPlayerExistById(playerId)).thenReturn(false);

        verify(playerService, never()).deletePlayerById(playerId);
        assertThrows(NotFoundException.class, () -> footballClubService.deletePlayerById(playerId));
    }

    @Test
    public void testChangeTeamName_Success() {
        long teamId = 1L;
        String newTeamName = "New Team Name";
        TeamNameUpdateDto updateDto = new TeamNameUpdateDto(newTeamName, teamId);
        when(teamService.updateTeamName(teamId, newTeamName)).thenReturn("Team name updated successfully.");
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(true);

        String result = footballClubService.changeTeamName(updateDto);

        assertEquals("Team name updated successfully.", result);
        verify(teamService, times(1)).updateTeamName(teamId, newTeamName);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void testChangeTeamName_TeamDoesNotExist() {
        long teamId = 1L;
        String newTeamName = "New Team Name";
        TeamNameUpdateDto updateDto = new TeamNameUpdateDto(newTeamName, teamId);
        when(teamService.doesTeamExistByTeamId(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, ()-> footballClubService.changeTeamName(updateDto));
    }

    @Test
    public void testChangePlayerTeam_Success() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team oldTeam = new Team(1L, "Old Team");
        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        Player player = Player.builder()
                .firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.DEFENDER)
                .team(oldTeam)
                .id(1L)
                .build();

        //new team exists
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(true);
        //get new team by id
        when(teamService.getTeamById(2L)).thenReturn(newTeam);
        //the player exists
        when(playerService.doesPlayerExistById(playerId)).thenReturn(true);
        //get player by id
        when(playerService.getPlayerById(1L)).thenReturn(player);
        //return 4 for number of the players in the team
        when(playerService.getPlayerCountByTeamId(newTeamId)).thenReturn(4);
        //return 3 for number of the foreign players in the team
        when(playerService.getForeignPlayerCountByTeamId(newTeamId)).thenReturn(3);

        footballClubService.changePlayerTeam(teamUpdateDto);
        verify(playerService, times(1)).updatePlayerTeam(playerId, newTeam);
    }

    @Test
    public void testChangePlayerTeam_TeamDoesNotExist() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        //new team does not exist
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> footballClubService.changePlayerTeam(teamUpdateDto));
        verify(playerService, times(0)).updatePlayerTeam(playerId, newTeam);
    }

    @Test
    public void testChangePlayerTeam_PlayerDoesNotExist() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        //new team exists
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(true);
        //the player does not exist
        when(playerService.doesPlayerExistById(playerId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> footballClubService.changePlayerTeam(teamUpdateDto));
        verify(playerService, times(0)).updatePlayerTeam(playerId, newTeam);
    }

    @Test
    public void testChangePlayerTeam_PlayerLimitExceeded() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team oldTeam = new Team(1L, "Old Team");
        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        Player player = Player.builder()
                .firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.DEFENDER)
                .team(oldTeam)
                .id(1L)
                .build();

        //new team exists
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(true);
        //get new team by id
        when(teamService.getTeamById(2L)).thenReturn(newTeam);
        //the player exists
        when(playerService.doesPlayerExistById(playerId)).thenReturn(true);
        //get player by id
        when(playerService.getPlayerById(1L)).thenReturn(player);
        //return maximum for number of the players in the team
        when(playerService.getPlayerCountByTeamId(newTeamId)).thenReturn(PlayerService.MAX_PLAYER_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.changePlayerTeam(teamUpdateDto));
        verify(playerService, times(0)).updatePlayerTeam(playerId, newTeam);
    }

    @Test
    public void testChangePlayerTeam_ForeignPlayerLimitExceeded() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team oldTeam = new Team(1L, "Old Team");
        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        Player player = Player.builder()
                .firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.DEFENDER)
                .team(oldTeam)
                .id(1L)
                .build();

        //new team exists
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(true);
        //get new team by id
        when(teamService.getTeamById(2L)).thenReturn(newTeam);
        //the player exists
        when(playerService.doesPlayerExistById(playerId)).thenReturn(true);
        //get player by id
        when(playerService.getPlayerById(1L)).thenReturn(player);
        //return 11 number of the players in the team
        when(playerService.getPlayerCountByTeamId(newTeamId)).thenReturn(11);
        //return 3 for number of the foreign players in the team
        when(playerService.getForeignPlayerCountByTeamId(newTeamId)).thenReturn(PlayerService.MAX_FOREIGN_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.changePlayerTeam(teamUpdateDto));
        verify(playerService, times(0)).updatePlayerTeam(playerId, newTeam);
    }

    @Test
    public void testChangePlayerTeam_GoalkeeperLimitExceeded() {
        long playerId = 1L;
        long newTeamId = 2L;

        Team oldTeam = new Team(1L, "Old Team");
        Team newTeam = new Team(newTeamId, "New Team");
        FootballPlayerTeamUpdateDto teamUpdateDto = new FootballPlayerTeamUpdateDto(playerId, newTeamId);

        Player player = Player.builder()
                .firstName("John")
                .lastName("Doe")
                .nationality(Nationality.FOREIGN)
                .playerType(PlayerType.GOALKEEPER)
                .team(oldTeam)
                .id(1L)
                .build();

        //new team exists
        when(teamService.doesTeamExistByTeamId(newTeamId)).thenReturn(true);
        //get new team by id
        when(teamService.getTeamById(2L)).thenReturn(newTeam);
        //the player exists
        when(playerService.doesPlayerExistById(playerId)).thenReturn(true);
        //get player by id
        when(playerService.getPlayerById(1L)).thenReturn(player);
        //return 11 number of the players in the team
        when(playerService.getPlayerCountByTeamId(newTeamId)).thenReturn(11);
        //return 3 for number of the foreign players in the team
        when(playerService.getForeignPlayerCountByTeamId(newTeamId)).thenReturn(3);
        //return maximum number of goalkeepers in the team
        when(playerService.getGoalkeeperCountByTeamId(newTeamId)).thenReturn(PlayerService.MAX_GOALKEEPER_COUNT);

        assertThrows(PlayerLimitException.class, () -> footballClubService.changePlayerTeam(teamUpdateDto));
        verify(playerService, times(0)).updatePlayerTeam(playerId, newTeam);
    }

}