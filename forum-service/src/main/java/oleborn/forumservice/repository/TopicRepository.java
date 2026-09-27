package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Разделы форума.
 */
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    @Query("""
            select t from Topic t
            where (:search is null or lower(t.title) like lower(concat('%', :search, '%')))
              and (:deleted is null
                   or (:deleted = true and t.deletedAt is not null)
                   or (:deleted = false and t.deletedAt is null))
            """)
    Page<Topic> findAllByFilter(
            String search,
            Boolean deleted,
            Pageable pageable
    );
}
