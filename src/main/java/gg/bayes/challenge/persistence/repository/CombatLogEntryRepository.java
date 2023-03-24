package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {
    // TODO: add the necessary methods for your solution

    @Query(nativeQuery = true)
    List<HeroKills> getListOfHeroBasedOnMatchId(Long matchId);

    @Query(nativeQuery = true)
    List<HeroItem> getPurchasedItemsWithTimestampBasedOnHero(Long matchId, String heroName);

    @Query(nativeQuery = true)
    List<HeroSpells> getCountOfSpellsUsedByHero(Long matchId, String heroName);

    @Query(nativeQuery = true)
    List<HeroDamage> getDamageInflictedByHeroWithTotalDamage(Long matchId, String heroName);
}
