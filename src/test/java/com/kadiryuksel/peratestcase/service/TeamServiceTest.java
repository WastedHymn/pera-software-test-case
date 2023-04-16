package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.repository.TeamRepository;
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
class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void addTeam_shouldAddNewTeamAndReturnInfoMessage() {
        // Arrange
        String teamName = "Test Team";
        Team newTeam = Team.builder().teamName(teamName).build();
        Team savedTeam = Team.builder().id(1L).teamName(teamName).build();
        when(teamRepository.save(newTeam)).thenReturn(savedTeam);

        String infoMessage = teamService.addTeam(teamName);

        String expectedMessage = String.format("Team added to the team table: %d %s", savedTeam.getId(), savedTeam.getTeamName());

        verify(teamRepository).save(newTeam);
        assertEquals(expectedMessage, infoMessage);
    }

    @Test
    public void updateTeamName_shouldUpdateTeamNameAndReturnMessage() {
        long teamId = 1L;
        String newName = "New Team";
        Team team = Team.builder().id(teamId).teamName("Old Team").build();
        String oldName = team.getTeamName();
        when(teamRepository.findTeamById(teamId)).thenReturn(team);
        when(teamRepository.save(team)).thenReturn(team);

        String message = teamService.updateTeamName(teamId, newName);
        String expectedMessage = String.format("Team %s name updated to %s.", oldName, team.getTeamName());
        verify(teamRepository).findTeamById(teamId);
        verify(teamRepository).save(team);
        assertEquals(expectedMessage, message);
    }

    @Test
    public void getAllTeams_shouldReturnListOfTeams() {
        // Arrange
        List<Team> teams = new ArrayList<>();
        teams.add(Team.builder().id(1L).teamName("Team 1").build());
        teams.add(Team.builder().id(2L).teamName("Team 2").build());
        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.getAllTeams();

        verify(teamRepository).findAll();
        assertEquals(2, result.size());
        assertEquals("Team 1", result.get(0).getTeamName());
        assertEquals("Team 2", result.get(1).getTeamName());
    }

    @Test
    public void getTeamByTeamName_shouldReturnTeam() {
        String teamName = "Test Team";
        Team team = Team.builder().id(1L).teamName(teamName).build();
        when(teamRepository.findTeamByTeamName(teamName)).thenReturn(team);

        Team result = teamService.getTeamByTeamName(teamName);

        verify(teamRepository).findTeamByTeamName(teamName);
        assertEquals(teamName, result.getTeamName());
    }

    @Test
    public void doesTeamExistByTeamId_shouldReturnTrueIfTeamExists() {
        long teamId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);

        boolean result = teamService.doesTeamExistByTeamId(teamId);

        verify(teamRepository).existsById(teamId);
        assertTrue(result);
    }

    @Test
    public void doesTeamExistByTeamName_shouldReturnTrueIfTeamExists() {
        String teamName = "Test Team";
        when(teamRepository.existsTeamByTeamName(teamName)).thenReturn(true);

        boolean result = teamService.doesTeamExistByTeamName(teamName);

        verify(teamRepository).existsTeamByTeamName(teamName);
        assertTrue(result);
    }

    @Test
    public void testDeleteTeamById() {
        long teamId = 1L;
        doNothing().when(teamRepository).deleteById(teamId);
        teamService.deleteTeamById(teamId);
        verify(teamRepository, times(1)).deleteById(teamId);
    }

    @Test
    public void testGetTeamNameByTeamId() {
        long teamId = 1L;
        String teamName = "Team A";
        Team team = Team.builder().id(teamId).teamName(teamName).build();
        when(teamRepository.findTeamById(teamId)).thenReturn(team);
        String result = teamService.getTeamNameByTeamId(teamId);
        assertEquals(teamName, result);
    }

    @Test
    public void testGetTeamById() {
        long teamId = 1L;
        String teamName = "Team A";
        Team team = Team.builder().id(teamId).teamName(teamName).build();
        when(teamRepository.findTeamById(teamId)).thenReturn(team);
        Team result = teamService.getTeamById(teamId);
        assertEquals(team, result);
    }

}