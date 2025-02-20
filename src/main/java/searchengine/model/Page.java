package searchengine.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс, представляющий страницу в базе данных.
 * Сожержит информацию о связи мужду сайтами {@link Site} и индексами {@link Index},
 * URI страницы, HTTP код и содержимое страницы.
 */
@Entity
@Table(name = "pages")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="site_id")
    private Site site;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String path;

    @Column(nullable = false)
    private int code;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "page", cascade = CascadeType.PERSIST)
    private Set<Index> indexes=new HashSet<>();

    public Page(Integer id, Site site, String path, int code, String content) {
        this.id = id;
        this.site = site;
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public Page(String path, String content) {
        this.path = path;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(site, page.site) && Objects.equals(path, page.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, path);
    }
}
