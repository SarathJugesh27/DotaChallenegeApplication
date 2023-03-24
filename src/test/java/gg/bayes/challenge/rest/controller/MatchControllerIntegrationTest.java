package gg.bayes.challenge.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/*
 * Integration test template to get you started. Add tests and make modifications as you see fit.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    private static final String COMBATLOG_FILE_1 = "/data/combatlog_1.log.txt";
    private static final String COMBATLOG_FILE_2 = "/data/combatlog_2.log.txt";

    @Autowired
    private MockMvc mvc;

    private Map<String, Long> matchIds;

    @BeforeAll
    void setup() throws Exception {
        // Populate the database with all events from both sample data files and store the returned
        // match IDS.
        matchIds = Map.of(
                COMBATLOG_FILE_1, ingestMatch(COMBATLOG_FILE_1),
                COMBATLOG_FILE_2, ingestMatch(COMBATLOG_FILE_2));
    }

    // TODO: add your tests
    // Replace this test method with the tests that you consider appropriate to test your implementation.
    @Test
    void getMatchTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/api/match/1");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String expected = "[\n" +
                "    {\n" +
                "        \"hero\": \"_badguys_melee\",\n" +
                "        \"kills\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"_goodguys_melee\",\n" +
                "        \"kills\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"abyssal_underlord\",\n" +
                "        \"kills\": 6\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"bane\",\n" +
                "        \"kills\": 2\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"bloodseeker\",\n" +
                "        \"kills\": 11\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"death_prophet\",\n" +
                "        \"kills\": 9\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"dragon_knight\",\n" +
                "        \"kills\": 3\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"mars\",\n" +
                "        \"kills\": 6\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"pangolier\",\n" +
                "        \"kills\": 5\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"puck\",\n" +
                "        \"kills\": 7\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"rubick\",\n" +
                "        \"kills\": 4\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"snapfire\",\n" +
                "        \"kills\": 2\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"uys_tower3_mid\",\n" +
                "        \"kills\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"ys_tower1_mid\",\n" +
                "        \"kills\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"hero\": \"ys_tower2_mid\",\n" +
                "        \"kills\": 1\n" +
                "    }\n" +
                "]";
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void getHeroItemsTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/api/match/1/abyssal_underlord/items");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String expected = "[\n" +
                "    {\n" +
                "        \"item\": \"energy_booster\",\n" +
                "        \"timestamp\": 1324464\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_soul_ring\",\n" +
                "        \"timestamp\": 844448\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"ring_of_regen\",\n" +
                "        \"timestamp\": 1806113\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"ring_of_health\",\n" +
                "        \"timestamp\": 1123980\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_magic_wand\",\n" +
                "        \"timestamp\": 753604\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"magic_wand\",\n" +
                "        \"timestamp\": 791461\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"magic_stick\",\n" +
                "        \"timestamp\": 652229\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"gauntlets\",\n" +
                "        \"timestamp\": 844082\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_pipe\",\n" +
                "        \"timestamp\": 1455132\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"ring_of_regen\",\n" +
                "        \"timestamp\": 1454466\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"cloak\",\n" +
                "        \"timestamp\": 1173901\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"ring_of_regen\",\n" +
                "        \"timestamp\": 843915\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"pipe\",\n" +
                "        \"timestamp\": 1487158\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_mekansm\",\n" +
                "        \"timestamp\": 1807680\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"headdress\",\n" +
                "        \"timestamp\": 1454966\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"tpscroll\",\n" +
                "        \"timestamp\": 1647619\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"boots\",\n" +
                "        \"timestamp\": 922296\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"guardian_greaves\",\n" +
                "        \"timestamp\": 2005132\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"headdress\",\n" +
                "        \"timestamp\": 1806280\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"arcane_boots\",\n" +
                "        \"timestamp\": 1324464\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_headdress\",\n" +
                "        \"timestamp\": 1806280\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"gauntlets\",\n" +
                "        \"timestamp\": 844248\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"hood_of_defiance\",\n" +
                "        \"timestamp\": 1200328\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_headdress\",\n" +
                "        \"timestamp\": 1454966\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"soul_ring\",\n" +
                "        \"timestamp\": 844448\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"chainmail\",\n" +
                "        \"timestamp\": 1806480\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"recipe_guardian_greaves\",\n" +
                "        \"timestamp\": 1994401\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"wind_lace\",\n" +
                "        \"timestamp\": 754670\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"ring_of_regen\",\n" +
                "        \"timestamp\": 1179833\n" +
                "    },\n" +
                "    {\n" +
                "        \"item\": \"mekansm\",\n" +
                "        \"timestamp\": 1807946\n" +
                "    }\n" +
                "]";
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), false);
    }

    @Test
    void getHeroSpellsTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/api/match/1/pangolier/spells");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String expected = "[\n" +
                "    {\n" +
                "        \"spell\": \"pangolier_gyroshell\",\n" +
                "        \"casts\": 7\n" +
                "    },\n" +
                "    {\n" +
                "        \"spell\": \"pangolier_gyroshell_stop\",\n" +
                "        \"casts\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"spell\": \"pangolier_shield_crash\",\n" +
                "        \"casts\": 26\n" +
                "    },\n" +
                "    {\n" +
                "        \"spell\": \"pangolier_swashbuckle\",\n" +
                "        \"casts\": 34\n" +
                "    }\n" +
                "]";
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void getHeroDamagesTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/api/match/1/abyssal_underlord/damage");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String expected = "[\n" +
                "    {\n" +
                "        \"target\": \"bane\",\n" +
                "        \"damage_instances\": 68,\n" +
                "        \"total_damage\": 3483\n" +
                "    },\n" +
                "    {\n" +
                "        \"target\": \"bloodseeker\",\n" +
                "        \"damage_instances\": 196,\n" +
                "        \"total_damage\": 6172\n" +
                "    },\n" +
                "    {\n" +
                "        \"target\": \"death_prophet\",\n" +
                "        \"damage_instances\": 76,\n" +
                "        \"total_damage\": 5865\n" +
                "    },\n" +
                "    {\n" +
                "        \"target\": \"mars\",\n" +
                "        \"damage_instances\": 22,\n" +
                "        \"total_damage\": 1450\n" +
                "    },\n" +
                "    {\n" +
                "        \"target\": \"rubick\",\n" +
                "        \"damage_instances\": 28,\n" +
                "        \"total_damage\": 1690\n" +
                "    }\n" +
                "]";
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
    }
    /*@Test
    void someTest() {
        assertThat(mvc).isNotNull();
    }*/

    /**
     * Helper method that ingests a combat log file and returns the match id associated with all parsed events.
     *
     * @param file file path as a classpath resource, e.g.: /data/combatlog_1.log.txt.
     * @return the id of the match associated with the events parsed from the given file
     * @throws Exception if an error happens when reading or ingesting the file
     */
    private Long ingestMatch(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);

        return Long.parseLong(mvc.perform(post("/api/match")
                                         .contentType(MediaType.TEXT_PLAIN)
                                         .content(fileContent))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString());
    }
}
