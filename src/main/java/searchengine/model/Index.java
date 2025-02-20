package searchengine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий индекс в базе данных.
 * Сожержит информацию о связи между страницами {@link Page} и леммами
 * {@link Lemma}, а так же их рейтинг.
 */
@Entity
@Table(name = "indexes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne
    @JoinColumn(name = "lemma_id")
    private Lemma lemma;

    @Column(nullable = false, name = "`rank`")
    private float rank;
}
