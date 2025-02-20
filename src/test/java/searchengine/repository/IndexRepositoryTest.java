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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.*;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IndexRepositoryTest {

    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void fillingDatabase() {
        Site site = new Site();
        site.setStatus(StatusIndexing.INDEXING);
        site.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site.setUrl("http://test");
        site.setName("test");
        site.setLastError("");
        siteRepository.save(site);

        Page page = new Page();
        page.setSite(site);
        page.setContent("<html></html>");
        page.setCode(200);
        page.setPath("");
        pageRepository.save(page);

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

        Index index1 = new Index();
        index1.setPage(page);
        index1.setLemma(lemma1);
        index1.setRank(1);
        indexRepository.save(index1);

        Index index2 = new Index();
        index2.setPage(page);
        index2.setLemma(lemma2);
        index2.setRank(2);
        indexRepository.save(index2);

        entityManager.flush();
    }

    @AfterEach
    public void clearDatabase() {
        indexRepository.deleteAllData();
        lemmaRepository.deleteAllData();
        pageRepository.deleteAllData();
        siteRepository.deleteAllData();

        entityManager.flush();
        entityManager.clear();
    }


    @Test
    @DisplayName("Checking the method findLemmasIdByPage when there is 1 page int the database ")
    public void testFindLemmasIdByPage_OnePage_ReturnsAllId() {
        List<Page> pages = pageRepository.findAll();
        List<Integer> lemmas = indexRepository.findLemmasIdByPage(pages.get(0).getId());

        List<Integer> actual = jdbcTemplate.query(
                "SELECT lemma_id FROM indexes",
                (rs, rowNum) -> rs.getInt("lemma_id")
        );

        assertArrayEquals(lemmas.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("Checking the method findLemmasIdByPage when there are many pages int the database ")
    public void testFindLemmasIdByPage_MorePage_ReturnsRequiredId() {
        List<Site> sites = siteRepository.findAll();
        Page page = new Page();
        page.setSite(sites.get(0));
        page.setContent("<html></html>");
        page.setCode(200);
        page.setPath("");
        page = pageRepository.save(page);
        entityManager.flush();

        List<Integer> lemmas = indexRepository.findLemmasIdByPage(page.getId());

        List<Integer> actual = jdbcTemplate.query(
                "SELECT lemma_id FROM indexes where page_id = " + page.getId(),
                (rs, rowNum) -> rs.getInt("lemma_id")
        );

        pageRepository.delete(page);
        assertArrayEquals(lemmas.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("Checking the method getRankByLemmasAndPageId")
    public void testGetRankByLemmasAndPageId_ReturnsValues() {
        List<String> lemmas = List.of("lemma1", "lemma2");
        Page page = pageRepository.findAll().get(0);
        List<Integer> ranks = indexRepository.getRankByLemmasAndPageId(lemmas, page.getId());
        List<Integer> actual = jdbcTemplate.query(
                "SELECT `rank` FROM indexes",
                (rs, rowNum) -> rs.getInt("rank")
        );

        assertArrayEquals(ranks.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("Checking the method deleteByPage")
    public void testDeleteByPage_DeleteIndexesByPage() {
        Page page = pageRepository.findAll().get(0);
        indexRepository.deleteByPage(page.getId());
        List<Integer> actual = jdbcTemplate.query(
                "SELECT id FROM indexes",
                (rs, rowNum) -> rs.getInt("id")
        );

        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Checking the method deleteAllData")
    public void testDeleteAllData_DeleteAllIndexes() {
        indexRepository.deleteAllData();
        List<Integer> actual= jdbcTemplate.query(
                "SELECT id FROM indexes",
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
                "SELECT max(id) FROM indexes",
                (rs,rowNum)-> rs.getInt("max(id)")
        );
        indexRepository.deleteAllData();
        indexRepository.updateIdentity();

        Index index1 = new Index();
        index1.setPage(pageRepository.findAll().get(0));
        index1.setLemma(lemmaRepository.findAll().get(0));
        index1.setRank(1);
        indexRepository.save(index1);

        List<Integer> actual= jdbcTemplate.query(
                "SELECT max(id) FROM indexes",
                (rs, rowNum) -> rs.getInt("max(id)")
        );

        assertNotEquals(maxId.get(0), actual.get(0));
        assertEquals(1,actual.get(0));
    }
}
