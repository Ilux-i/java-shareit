package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
                    select exists (
                        select 1
                        from Booking b
                            where b.booker.id = :userId and
                                b.item.id = :itemId and
                                b.end < :date
                            )
            """)
    boolean checkingAbilityComment(Long userId, Long itemId, LocalDateTime date);

    @Query("""
                    select c
                    from Comment as c
                    where c.item.id = :itemId
            """)
    List<Comment> findCommentsByItemId(Long itemId);

}