package moe.victorique.blackjack.repo;

import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByDeviceAndStatus(final String device, final PlayStatus status);
}