package searchengine.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LemmaRepositoryTest {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void fillingDatabase() {
        Site site = new Site();
        site.setStatus(StatusIndexing.INDEXING);
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setUrl("http://test");
        site.setName("test");
        site.setLastError("");
        siteRepository.save(site);

        Site site1 = new Site();
        site1.setStatus(StatusIndexing.INDEXING);
        site1.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site1.setUrl("http://test1");
        site1.setName("test1");
        site1.setLastError("");
        siteRepository.save(site1);

        Lemma lemma1 = new Lemma();
        lemma1.setSite(site);
        lemma1.setLemma("lemma1");
        lemma1.setFrequency(1);
        lemmaRepository.save(lemma1);

        Lemma lemma2 = new Lemma();
        lemma2.setSite(site);
        lemma2.setLemma("lemma2");
        lemma2.setFrequency(1);
        lemmaRepository.save(lemma2);

        Lemma lemma3 = new Lemma();
        lemma3.setSite(site1);
        lemma3.setLemma("lemma2");
        lemma3.setFrequency(1);
        lemmaRepository.save(lemma3);

        entityManager.flush();
    }

    @AfterEach
    public void cleanDatabase() {
        lemmaRepository.deleteAllData();
        siteRepository.deleteAllData();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Get the frequency of the lemma across all sites")
    public void testFindFrequencyByLemmaAndSiteId_SiteNull_ReturnsSumAllFrequency() {
        String lemma = "lemma2";

        Optional<Integer> frequency = lemmaRepository.findFrequencyByLemmaAndSiteId(lemma, null);

        Integer actual = jdbcTemplate.query(
                "SELECT sum(frequency) FROM lemmas WHERE lemma = '" + lemma + "'",
                (rs, rowNum) -> rs.getInt("sum(frequency)")
        ).get(0);

        assertTrue(frequency.isPresent());
        assertEquals(frequency.get(), actual);
        assertEquals(2, actual);
    }

    @Test
    @DisplayName("Get the frequency of a lemma from a given site")
    public void testFindFrequencyByLemmaAndSiteId_SiteRequired_ReturnsFrequency() {
        String lemma = "lemma2";
        Integer siteId = siteRepository.findByName("test1").get().getId();

        Optional<Integer> frequency = lemmaRepository.findFrequencyByLemmaAndSiteId(lemma, siteId);

        Integer actual = jdbcTemplate.query(
                "SELECT sum(frequency) FROM lemmas WHERE lemma = '" + lemma + "' AND site_id = " + siteId,
                (rs, rowNum) -> rs.getInt("sum(frequency)")
        ).get(0);

        assertTrue(frequency.isPresent());
        assertEquals(frequency.get(), actual);
        assertEquals(1, actual);
    }

    @Test
    @DisplayName("Number of lemmas on the site")
    public void testCountLemmasBySiteId_ReturnsNum() {
        Integer siteId = siteRepository.findByName("test").get().getId();

        Integer num = lemmaRepository.countLemmasBySiteId(siteId);

        Integer actual = jdbcTemplate.query(
                "SELECT COUNT(*) FROM lemmas WHERE site_id = " + siteId,
                (rs, rowNum) -> rs.getInt("COUNT(*)")
        ).get(0);

        assertEquals(num, actual);
        assertEquals(2, num);
    }

    @Test
    @DisplayName("Get the object Lemma by lemma and siteId")
    public void testFindByLemmaAndSiteId_LemmaPresent_ReturnsLemma() {
        String lemmaStr = "lemma2";
        Integer siteId = siteRepository.findByName("test1").get().getId();

        Optional<Lemma> lemma = lemmaRepository.findByLemmaAndSiteId(lemmaStr, siteId);

        List<Integer> actualId = jdbcTemplate.query(
                "SELECT id FROM lemmas WHERE lemma = '" + lemmaStr + "' AND site_id = " + siteId,
                (rs, rowNum) -> rs.getInt("id")
        );

        assertTrue(lemma.isPresent());
        assertEquals(1, actualId.size());
        assertEquals(lemma.get().getId(), actualId.get(0));
    }

    @Test
    @DisplayName("Reduce the frequency by 1")
    public void testMinusFrequencyById_UpdateFrequency() {
        String lemma = "lemma1";
        Integer lemmaId = jdbcTemplate.query(
                "SELECT id FROM lemmas WHERE lemma = '" + lemma + "'",
                (rs, rowNum) -> rs.getInt("id")
        ).get(0);

        Integer frequency = jdbcTemplate.query(
                "SELECT frequency FROM lemmas WHERE lemma = '" + lemma + "'",
                (rs, rowNum) -> rs.getInt("frequency")
        ).get(0);

        lemmaRepository.minusFrequencyByListId(List.of(lemmaId));

        Integer actual = jdbcTemplate.query(
                "SELECT frequency FROM lemmas WHERE lemma = '" + lemma + "'",
                (rs, rowNum) -> rs.getInt("frequency")
        ).get(0);

        assertNotEquals(frequency,actual);
        assertEquals(0,actual);
    }

    @Test
    @DisplayName("Checking the method deleteAllData")
    public void testDeleteAllData_DeleteAllLemmas() {
        lemmaRepository.deleteAllData();
        List<Integer> actual= jdbcTemplate.query(
                "SELECT id FROM lemmas",
                (rs, rowNum) -> rs.getInt("id")
        );

        assertEquals(0, actual.size());
    }

    @Test
    @Transactional
    @Commit
    @DisplayName("Checking the method updateIdentity")
    public void testUpdateIdentity_UpdateIdentity(){
        List<Integer> maxId= jdbcTemplate.query(
                "SELECT max(id) FROM lemmas",
                (rs,rowNum)-> rs.getInt("max(id)")
        );
        lemmaRepository.deleteAllData();
        lemmaRepository.updateIdentity();

        Lemma lemma1 = new Lemma();
        lemma1.setSite(siteRepository.findByName("test").get());
        lemma1.setLemma("lemma1");
        lemma1.setFrequency(1);
        lemmaRepository.save(lemma1);

        List<Integer> actual= jdbcTemplate.query(
                "SELECT max(id) FROM lemmas",
                (rs, rowNum) -> rs.getInt("max(id)")
        );

        assertNotEquals(maxId.get(0), actual.get(0));
        assertEquals(1,actual.get(0));
    }
}
