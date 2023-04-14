package com.kadiryuksel.peratestcase.repository;

import com.kadiryuksel.peratestcase.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsTeamByTeamName(String teamName);

    boolean existsById(long teamId);

    Team findTeamById(long teamId);

    Team findTeamByTeamName(String teamName);
}
