package moe.victorique.blackjack.repo;

import lombok.NonNull;
import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.model.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<Game, UUID> {

    @Transactional
    int deleteByDeviceOrToken(@Nullable String device, @Nullable UUID token);

    Optional<Game> findByDeviceAndStatus(final @NonNull String device, final @NonNull PlayStatus status);

    Optional<Game> findByTokenAndStatus(final @NonNull UUID token, final @NonNull PlayStatus status);

    List<Game> findAllByDeviceAndStatusNot(final @NonNull String device, final @NonNull PlayStatus status);


}
