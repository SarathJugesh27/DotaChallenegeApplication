package gg.bayes.challenge.persistence.model;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedNativeQuery(name = "CombatLogEntryEntity.getListOfHeroBasedOnMatchId",
        query = "select actor as hero, count(actor) as kills from dota_combat_log where match_id = ?1 and entry_type = 'HERO_KILLED' group by actor",
        resultSetMapping = "Mapping.HeroKills")
@SqlResultSetMapping(name = "Mapping.HeroKills",
        classes = @ConstructorResult(targetClass = HeroKills.class, columns = {
                @ColumnResult(name = "hero"),
                @ColumnResult(name = "kills", type = Integer.class)
        }))
@NamedNativeQuery(name = "CombatLogEntryEntity.getPurchasedItemsWithTimestampBasedOnHero",
        query = "select item as item, entry_timestamp as timestamp from dota_combat_log where match_id = ?1 and entry_type = 'ITEM_PURCHASED' and actor =?2",
        resultSetMapping = "Mapping.HeroItem")
@SqlResultSetMapping(name = "Mapping.HeroItem",
        classes = @ConstructorResult(targetClass = HeroItem.class, columns = {
                @ColumnResult(name = "item"),
                @ColumnResult(name = "timestamp",type = Long.class)
        }))
@NamedNativeQuery(name = "CombatLogEntryEntity.getCountOfSpellsUsedByHero",
        query = "select ability as spell, count(ability) as casts from dota_combat_log where entry_type = 'SPELL_CAST' and match_id = ?1 and actor = ?2 group by ability",
        resultSetMapping = "Mapping.HeroSpells")
@SqlResultSetMapping(name = "Mapping.HeroSpells",
        classes = @ConstructorResult(targetClass = HeroSpells.class, columns = {
                @ColumnResult(name = "spell"),
                @ColumnResult(name = "casts", type = Integer.class)
        }))
@NamedNativeQuery(name = "CombatLogEntryEntity.getDamageInflictedByHeroWithTotalDamage",
        query = "select target as target, count(target) as damageInstances, sum(damage) as totalDamage from dota_combat_log where entry_type = 'DAMAGE_DONE' and match_id =?1 and actor = ?2 group by target",
        resultSetMapping = "Mapping.HeroDamage")
@SqlResultSetMapping(name = "Mapping.HeroDamage",
        classes = @ConstructorResult(targetClass = HeroDamage.class, columns = {
                @ColumnResult(name = "target"),
                @ColumnResult(name = "damageInstances", type = Integer.class),
                @ColumnResult(name = "totalDamage", type = Integer.class)
        }))
@Getter
@Setter
@Entity
@Table(name = "dota_combat_log")
public class CombatLogEntryEntity {

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dota_combat_log_sequence_generator"
    )
    @SequenceGenerator(
            name = "dota_combat_log_sequence_generator",
            sequenceName = "dota_combat_log_sequence",
            allocationSize = 1
    )
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    private MatchEntity match;

    @NotNull
    @Column(name = "entry_timestamp")
    private Long timestamp;

    @NotNull
    @Column(name = "entry_type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "actor")
    private String actor;

    @Column(name = "target")
    private String target;

    @Column(name = "ability")
    private String ability;

    @Column(name = "ability_level")
    private Integer abilityLevel;

    @Column(name = "item")
    private String item;

    @Column(name = "damage")
    private Integer damage;

    public enum Type {
        ITEM_PURCHASED,
        HERO_KILLED,
        SPELL_CAST,
        DAMAGE_DONE
    }
}
