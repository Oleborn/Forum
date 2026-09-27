package oleborn.forumservice.repository;

import oleborn.forumservice.dictionary.ReactionType;
import oleborn.forumservice.model.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реакции на сообщения.
 */
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {

    Optional<Reaction> findByUserIdAndPostId(
            UUID userId,
            UUID postId
    );

    @Query("select r.type from Reaction r where r.user.id = :userId and r.post.id = :postId")
    Optional<ReactionType> findTypeByUserIdAndPostId(
            UUID userId,
            UUID postId
    );

    @Query("""
            select r.type as type, count(r) as count
            from Reaction r
            where r.post.id = :postId
            group by r.type
            order by r.type
            """)
    List<ReactionCountProjection> findCountsByPostId(UUID postId);

    /**
     * Агрегированное количество реакций одного типа.
     */
    interface ReactionCountProjection {

        ReactionType getType();

        long getCount();
    }
}
