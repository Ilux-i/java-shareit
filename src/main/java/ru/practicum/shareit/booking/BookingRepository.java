package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStateAll(Long userId);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId and
                        booking.start < :time and
                        booking.end > :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStateCurrent(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId and
                        booking.end < :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStatePast(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId and
                        booking.start > :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStateFuture(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId and
                        booking.status = 0
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStateWaiting(Long userId);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.booker.id = :userId and
                        booking.status = 1
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsByStateRejected(Long userId);


    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStateAll(Long userId);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId and
                        booking.start < :time and
                        booking.end > :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStateCurrent(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId and
                        booking.end < :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStatePast(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId and
                        booking.start > :time
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStateFuture(Long userId, LocalDateTime time);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId and
                        booking.status = 0
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStateWaiting(Long userId);

    @Query("""
                    select booking
                    from Booking booking
                    join fetch booking.item
                    join fetch booking.booker
                    where booking.item.owner.id = :userId and
                        booking.status = 1
                    order by booking.start asc
            """)
    Collection<Booking> getBookingsOwnerByStateRejected(Long userId);

    @Query("""
                    select b
                    from Booking b
                    WHERE b.item.id = :itemId AND b.end < :now ORDER BY b.end DESC LIMIT 1
            """)
    Optional<Booking> findLastBookingById(long itemId, LocalDateTime now);

    @Query("""
                    select b
                    from Booking b
                    WHERE b.item.id = :itemId AND b.start > :now ORDER BY b.start ASC LIMIT 1
            """)
    Optional<Booking> findNextBookingById(long itemId, LocalDateTime now);

}