package searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий лемму в базе данны.
 * Содержит информацию о связи между сайтами {@link Site} и ииндексами {@link Index},
 * а так же часота.
 */
@Entity
@Table(name = "lemmas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private int frequency;

    @OneToMany(mappedBy = "lemma", cascade = CascadeType.PERSIST)
    private Set<Index> indexes=new HashSet<>();

    public Lemma(Integer id, Site site, String lemma, int frequency) {
        this.id = id;
        this.site = site;
        this.lemma = lemma;
        this.frequency = frequency;
    }
}
