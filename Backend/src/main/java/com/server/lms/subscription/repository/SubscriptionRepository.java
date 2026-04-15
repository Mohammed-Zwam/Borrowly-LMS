package com.server.lms.subscription.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface SubscriptionRepository extends BaseRepository<Subscription, String> {

    @Query("""
        SELECT s FROM Subscription s
        WHERE s.user.id = :userId
        /* Subscriptions may be deactivated before their scheduled end date, so validate date & active status */
        AND s.isActive = true
        AND s.startDate <= :today AND s.endDate >= :today
    """)
    Optional<Subscription> findActiveSubscriptionByUserId(
            String userId,
            LocalDate today
    );


    /* TO DEACTIVATE EXPIRED SUBSCRIPTIONS */
    @Query("""
        SELECT s FROM Subscription s
        WHERE s.isActive = true
        AND s.endDate < :today
    """)
    List<Subscription> findExpiredActiveSubscriptions();

}
