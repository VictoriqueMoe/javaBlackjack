package moe.victorique.blackjack.repo;


import moe.victorique.blackjack.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends CrudRepository<Stat, String> {
}
