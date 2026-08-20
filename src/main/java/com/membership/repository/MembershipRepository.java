package com.membership.repository;

import com.membership.entity.Membership;
import com.membership.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MembershipRepository
        extends JpaRepository<Membership, Long> {

    List<Membership> findByMemberIdOrderByStartDateDesc(Long memberId);

    List<Membership> findByStatus(MembershipStatus status);

    List<Membership> findByExpiryDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
            SELECT m
            FROM Membership m
            WHERE m.expiryDate < :today
            AND m.status <> com.membership.enums.MembershipStatus.CANCELLED
            """)
    List<Membership> findExpiredMemberships(LocalDate today);

    @Query("""
            SELECT m
            FROM Membership m
            WHERE m.expiryDate BETWEEN :today AND :reminderDate
            AND m.status = com.membership.enums.MembershipStatus.ACTIVE
            """)
    List<Membership> findMembershipsExpiringSoon(
            LocalDate today,
            LocalDate reminderDate
    );
}