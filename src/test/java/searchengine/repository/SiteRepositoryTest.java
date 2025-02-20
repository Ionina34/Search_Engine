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
import searchengine.model.Index;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SiteRepositoryTest {
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void fillingDatabase(){
        siteRepository.deleteAllData();

        Site site1 = new Site();
        site1.setStatus(StatusIndexing.INDEXING);
        site1.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site1.setUrl("http://test1");
        site1.setName("test1");
        site1.setLastError("");
        siteRepository.save(site1);

        Site site2 = new Site();
        site2.setStatus(StatusIndexing.INDEXING);
        site2.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site2.setUrl("http://test2");
        site2.setName("test2");
        site2.setLastError("");
        siteRepository.save(site2);

        entityManager.flush();
    }

    @AfterEach
    public  void clearDatabase(){
        siteRepository.deleteAllData();
    }

    @Test
    @DisplayName("Find site by url")
    public void testFindByUrl_ReturnsSite(){
        Optional<Site> site= siteRepository.findByUrl("http://test1");

        List<Optional<Site>> actual = jdbcTemplate.query(
                "SELECT * FROM sites WHERE url = 'http://test1'",
                (rs,rowNum)-> Optional.of (new Site(rs.getInt("id"),
                        StatusIndexing.valueOf(rs.getString("status")),
                        rs.getTimestamp("status_time"),
                        rs.getString("last_error"),
                        rs.getString("url"),
                        rs.getString("name")))
        );

        assertTrue(site.isPresent());
        assertTrue(actual.get(0).isPresent());
        assertEquals(1,actual.size());
    }

    @Test
    @DisplayName("Find site by name")
    public void testFindByName_ReturnsSite(){
        Optional<Site> site= siteRepository.findByName("test1");

        List<Optional<Site>> actual = jdbcTemplate.query(
                "SELECT * FROM sites WHERE name = 'test1'",
                (rs,rowNum)-> Optional.of (new Site(rs.getInt("id"),
                        StatusIndexing.valueOf(rs.getString("status")),
                        rs.getTimestamp("status_time"),
                        rs.getString("last_error"),
                        rs.getString("url"),
                        rs.getString("name")))
        );

        assertTrue(site.isPresent());
        assertTrue(actual.get(0).isPresent());
        assertEquals(1,actual.size());
    }

    @Test
    @DisplayName("Getting number of sites")
    public void testGetNumSites_ReturnNumber(){
        Integer numSites = siteRepository.getNumSites();

        List<Integer> actual = jdbcTemplate.query(
                "SELECT COUNT(*) FROM sites",
                (rs,rowNum)-> rs.getInt("COUNT(*)")
        );

        assertEquals(numSites,actual.get(0));
        assertEquals(2, numSites);
    }

    @Test
    @DisplayName("Delete all data from table sites")
    public void testDeleteAllData_DeleteAllSites() {
        siteRepository.deleteAllData();
        List<Integer> actual= jdbcTemplate.query(
                "SELECT id FROM sites",
                (rs, rowNum) -> rs.getInt("id")
        );

        assertEquals(0, actual.size());
    }

    @Test
    @Transactional
    @Commit
    @DisplayName("Update identity")
    public void testUpdateIdentity_UpdateIdentity(){
        List<Integer> maxId= jdbcTemplate.query(
                "SELECT max(id) FROM sites",
                (rs,rowNum)-> rs.getInt("max(id)")
        );
        siteRepository.deleteAllData();
        siteRepository.updateIdentity();

        Site site2 = new Site();
        site2.setStatus(StatusIndexing.INDEXING);
        site2.setStatusTime(new Timestamp(System.currentTimeMillis()));
        site2.setUrl("http://test2");
        site2.setName("test2");
        site2.setLastError("");
        siteRepository.save(site2);

        List<Integer> actual= jdbcTemplate.query(
                "SELECT max(id) FROM sites",
                (rs, rowNum) -> rs.getInt("max(id)")
        );

        assertNotEquals(maxId.get(0), actual.get(0));
        assertEquals(1,actual.get(0));
    }
}
