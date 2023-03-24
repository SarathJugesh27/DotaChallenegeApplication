package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CombatLogEntryRepository combatLogEntryRepository;

    public long ingestCombatLog(String combatLog) {
        long count = 0;
        Set<CombatLogEntryEntity> combatLogEntryEntitySet = new HashSet<>();
        List<String> lineList = combatLog.lines().collect(Collectors.toList());
        MatchEntity match = new MatchEntity();
        for(String line : lineList) {
            logger.info("Line : {}", line);
            if (!line.substring(15).startsWith("npc_dota_hero") || !interestedEvents(line))
                continue;
            CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
            count++;
            String[] words = line.split(" ");
            combatLogEntryEntity.setMatch(match);
            combatLogEntryEntity.setTimestamp(
                    LocalTime.parse(words[0].substring(1,13)).getLong(ChronoField.MILLI_OF_DAY));

            if (line.contains("buys")) {
                populateCombatLogForBuy(combatLogEntryEntity, words);
            } else if (line.contains("casts")) {
                populateCombaLogForCast(combatLogEntryEntity, words);
            } else if (line.contains("hits")) {
                populateCombaLogForHits(combatLogEntryEntity, words);
            } else if (line.contains("killed")) {
                populateCombaLogForKill(combatLogEntryEntity, words);
            }

            combatLogEntryEntitySet.add(combatLogEntryEntity);

            logger.info("Type : {}", combatLogEntryEntity.getType());
            logger.info("Timestamp : {}", combatLogEntryEntity.getTimestamp());
            logger.info("Actor : {}", combatLogEntryEntity.getActor());
            logger.info("Item : {}", combatLogEntryEntity.getItem());
            logger.info("Ability : {}", combatLogEntryEntity.getAbility());
            logger.info("Target : {}", combatLogEntryEntity.getTarget());
            logger.info("AbilityLevel : {}", combatLogEntryEntity.getAbilityLevel());
            logger.info("Damage : {}", combatLogEntryEntity.getDamage());
        }
        match.setCombatLogEntries(combatLogEntryEntitySet);
        matchRepository.save(match);
        return count;
    }

    public List<HeroKills> getMatch(Long matchId) {
        List<HeroKills> heroKillsList = combatLogEntryRepository.getListOfHeroBasedOnMatchId(matchId);
        return heroKillsList;
    }

    public List<HeroItem> getHeroItems(Long matchId, String heroName) {
        List<HeroItem> heroItemList = combatLogEntryRepository.getPurchasedItemsWithTimestampBasedOnHero(matchId, heroName);
        return heroItemList;
    }

    public List<HeroSpells> getHeroSpells(Long matchId, String heroName) {
        List<HeroSpells> heroSpellsList = combatLogEntryRepository.getCountOfSpellsUsedByHero(matchId, heroName);
        return heroSpellsList;
    }

    public List<HeroDamage> getHeroDamages(Long matchId, String heroName) {
        List<HeroDamage> heroDamageList = combatLogEntryRepository.getDamageInflictedByHeroWithTotalDamage(matchId, heroName);
        return heroDamageList;
    }

    private void populateCombatLogForBuy(CombatLogEntryEntity combatLogEntryEntity, String[] words) {
        combatLogEntryEntity.setActor(words[1].substring(14));
        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.ITEM_PURCHASED);
        combatLogEntryEntity.setItem(words[4].substring(5));
    }

    private void populateCombaLogForCast(CombatLogEntryEntity combatLogEntryEntity, String[] words) {
        combatLogEntryEntity.setActor(words[1].substring(14));
        combatLogEntryEntity.setAbility(words[4]);
        combatLogEntryEntity.setAbilityLevel(Integer.valueOf(words[6].substring(0,1)));
        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.SPELL_CAST);
    }

    private void populateCombaLogForHits(CombatLogEntryEntity combatLogEntryEntity, String[] words) {
        combatLogEntryEntity.setActor(words[1].substring(14));
        combatLogEntryEntity.setTarget(words[3].substring(14));
        combatLogEntryEntity.setDamage(Integer.valueOf(words[7]));
        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);
    }

    private void populateCombaLogForKill(CombatLogEntryEntity combatLogEntryEntity, String[] words) {
        combatLogEntryEntity.setTarget(words[1].substring(14));
        combatLogEntryEntity.setActor(words[5].substring(14));
        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.HERO_KILLED);
    }

    private boolean interestedEvents(String line) {
        if(line.contains("buys") || line.contains("casts") || line.contains("killed") || line.contains("damage"))
            return true;
        return false;
    }

}
