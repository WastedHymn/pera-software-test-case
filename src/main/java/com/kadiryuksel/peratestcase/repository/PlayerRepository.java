package com.kadiryuksel.peratestcase.repository;

import com.kadiryuksel.peratestcase.entity.Player;
import com.kadiryuksel.peratestcase.enums.Nationality;
import com.kadiryuksel.peratestcase.enums.PlayerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player getPlayerById(long playerId);

    List<Player> getPlayersByTeamId(long teamId);

    int countByNationalityAndTeamId(Nationality nationality, long teamId);

    int countByPlayerTypeAndTeamId(PlayerType type, long teamId);

    int countByTeamId(long teamId);

    void deleteAllByTeamId(long teamId);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Player getPlayerByFirstNameAndLastName(String firstName, String lastName);
}
