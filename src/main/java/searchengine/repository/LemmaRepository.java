package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Lemma;

import java.util.List;
import java.util.Optional;

public interface LemmaRepository extends JpaRepository<Lemma, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE lemmas AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM lemmas", nativeQuery = true)
    void deleteAllData();

    @Modifying
    @Transactional
    @Query(value = "UPDATE lemmas SET frequency = frequency - 1 WHERE id IN(:id)",
            nativeQuery = true)
    void updateFrequencyById(@Param("id") List<Integer> id);

    Optional<Lemma> findByLemma(String lemma);

    @Query(value = "SELECT COUNT(*) FROM lemmas WHERE site_id = :siteId"
            , nativeQuery = true)
    Integer countLemmasBySiteId(@Param("siteId") Integer siteId);

    @Query(value = "SELECT frequency FROM lemmas WHERE lemma = :lemma",
    nativeQuery = true)
    Optional<Integer> findFrequencyByLemma(@Param("lemma") String lemma);
}
