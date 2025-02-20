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
public class PageRepositoryTest {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private IndexRepository indexRepository;

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
        site1.setStatus(StatusIndexing.INDEXED);
        site1.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site1.setUrl("http://test1");
        site1.setName("test1");
        site1.setLastError("");
        siteRepository.save(site1);

        Page page = new Page();
        page.setSite(site);
        page.setContent("<html></html>");
        page.setCode(200);
        page.setPath("");
        pageRepository.save(page);

        Page page1 = new Page();
        page1.setSite(site1);
        page1.setContent("<html></html>");
        page1.setCode(200);
        page1.setPath("");
        pageRepository.save(page1);

        Page page2 = new Page();
        page2.setSite(site1);
        page2.setContent("<html></html>");
        page2.setCode(200);
        page2.setPath("");
        pageRepository.save(page2);

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

        Lemma lemma4 = new Lemma();
        lemma4.setSite(site1);
        lemma4.setLemma("lemma2");
        lemma4.setFrequency(1);
        lemmaRepository.save(lemma4);

        Lemma lemma3 = new Lemma();
        lemma3.setSite(site);
        lemma3.setLemma("lemma3");
        lemma3.setFrequency(1);
        lemmaRepository.save(lemma3);

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

        Index index3 = new Index();
        index3.setPage(page1);
        index3.setLemma(lemma4);
        index3.setRank(3);
        indexRepository.save(index3);

        Index index4 = new Index();
        index4.setPage(page2);
        index4.setLemma(lemma3);
        index4.setRank(4);
        indexRepository.save(index4);

        entityManager.flush();
    }

    @AfterEach
    public void cleanDatabase() {
        indexRepository.deleteAllData();
        lemmaRepository.deleteAllData();
        pageRepository.deleteAllData();
        siteRepository.deleteAllData();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Select the pages with the lemma from the list of pages")
    public void testFindPageByLemmaAndPageId_WithoutRequiredPage_ReturnsEmptyList() {
        String lemma = "lemma3";
        List<Integer> listIdPages = jdbcTemplate.query(
                "SELECT id FROM pages",
                (rs, rowNum) -> rs.getInt("id")
        );
        listIdPages.remove(listIdPages.size() - 1);

        List<Page> pages = pageRepository.findPageByLemmaAndPageId(lemma, listIdPages);

        assertTrue(pages.isEmpty());
    }

    @Test
    @DisplayName("Get the pages with the required terminal among all sites")
    public void testFindPageByLemmaAndSiteId_SiteNull_ReturnsAllPages() {
        String lemma = "lemma2";
        List<Page> pages = pageRepository.findPageByLemmaAndSiteId(lemma, null);

        assertEquals(2, pages.size());

        List<Integer> sitesId = jdbcTemplate.query(
                "SELECT site_id FROM pages WHERE id IN(" + pages.get(0).getId() + ", " + pages.get(1).getId() + ") " +
                        "GROUP BY site_id",
                (rs, rowNum) -> rs.getInt("site_id")
        );

        assertEquals(2, sitesId.size());
    }

    @Test
    @DisplayName("Get pages with the desired terminal of a specific site")
    public void testFindPageByLemmaAndSiteId_SiteIsSet_ReturnsPages() {
        String lemma = "lemma2";
        Site site = siteRepository.findByName("test1").get();
        List<Page> pages = pageRepository.findPageByLemmaAndSiteId(lemma, site.getId());

        assertEquals(1, pages.size());

        List<Integer> sitesId = jdbcTemplate.query(
                "SELECT site_id FROM pages WHERE id IN(" + pages.get(0).getId() + ") " +
                        "GROUP BY site_id",
                (rs, rowNum) -> rs.getInt("site_id")
        );

        assertEquals(site.getId(), sitesId.get(0));
    }

    @Test
    @DisplayName("Get the number of site pages")
    public void testCountPagesBySiteId_ReturnNumPages(){
        List<Integer> siteId=jdbcTemplate.query(
                "SELECT min(id) FROM sites",
                (rs, rowNum) -> rs.getInt("min(id)")
        );

        Integer pagesCount= pageRepository.countPagesBySiteId(siteId.get(0));
        assertEquals(1,pagesCount);
    }

    @Test
    @DisplayName("Search for a site page by path")
    public void testFindByPathAndSite_ReturnSite(){
        Site site = jdbcTemplate.query(
                "SELECT * FROM sites",
                (rs,rowNum)-> new Site(
                        rs.getInt("id"),
                        StatusIndexing.valueOf(rs.getString("status")),
                        rs.getTimestamp("status_time"),
                        rs.getString("last_error"),
                        rs.getString("url"),
                        rs.getString("name")
                )
        ).get(0);
        String path="";

        Optional<Page> page = pageRepository.findByPathAndSite(path,site);

        Integer actualId = jdbcTemplate.query(
                "SELECT id FROM pages WHERE path = '" + path + "' AND site_id = " + site.getId(),
                (rs,rowNum)-> rs.getInt("id")
        ).get(0);

        assertTrue(page.isPresent());
        assertEquals(page.get().getId(), actualId);
    }

    @Test
    @DisplayName("Checking the method deleteAllData")
    public void testDeleteAllData_DeleteAllSites() {
        indexRepository.deleteAllData();
        lemmaRepository.deleteAllData();
        pageRepository.deleteAllData();
        List<Integer> actual= jdbcTemplate.query(
                "SELECT id FROM pages",
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
                "SELECT max(id) FROM pages",
                (rs,rowNum)-> rs.getInt("max(id)")
        );
        indexRepository.deleteAllData();
        lemmaRepository.deleteAllData();
        pageRepository.deleteAllData();
        pageRepository.updateIdentity();

        Page page = new Page();
        page.setSite(siteRepository.findByName("test").get());
        page.setContent("<html></html>");
        page.setCode(200);
        page.setPath("");
        pageRepository.save(page);

        List<Integer> actual= jdbcTemplate.query(
                "SELECT max(id) FROM pages",
                (rs, rowNum) -> rs.getInt("max(id)")
        );

        assertNotEquals(maxId.get(0), actual.get(0));
        assertEquals(1,actual.get(0));
    }
}
