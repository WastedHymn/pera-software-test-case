package com.kadiryuksel.peratestcase.service;

import com.kadiryuksel.peratestcase.entity.Team;
import com.kadiryuksel.peratestcase.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final static Logger logger = LoggerFactory.getLogger(TeamService.class);

    @Transactional
    public String addTeam(String teamName) {
        Team newTeam = Team.builder().teamName(teamName).build();
        Team savedTeam = teamRepository.save(newTeam);
        String infoMessage = String.format("Team added to the team table: %d %s", savedTeam.getId(), savedTeam.getTeamName());
        logger.info(infoMessage);
        return infoMessage;
    }

    @Transactional String updateTeamName(long teamId,String newName){
        Team team = teamRepository.findTeamById(teamId);
        String oldTeamName = team.getTeamName();
        team.setTeamName(newName);
        Team updatedTeam = teamRepository.save(team);
        String message = String.format("Team %s name updated to %s.", oldTeamName, updatedTeam.getTeamName());
        logger.info(message);
        return message;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamByTeamName(String teamName) {
        return teamRepository.findTeamByTeamName(teamName);
    }

    public boolean doesTeamExistByTeamId(long teamId) {
        return teamRepository.existsById(teamId);
    }

    public boolean doesTeamExistByTeamName(String teamName) {
        return teamRepository.existsTeamByTeamName(teamName);
    }

    @Transactional
    public void deleteTeamById(long teamId) {
        teamRepository.deleteById(teamId);
        logger.warn(String.format("Team deleted from database(ID: %d).", teamId));
    }

    public String getTeamNameByTeamId(long teamId) {
        return teamRepository.findTeamById(teamId).getTeamName();
    }
}
