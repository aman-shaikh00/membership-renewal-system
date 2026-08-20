package com.membership.repository;

import com.membership.entity.MembershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipHistoryRepository
        extends JpaRepository<MembershipHistory, Long> {

    List<MembershipHistory>
    findByMembershipIdOrderByRenewedAtDesc(Long membershipId);

    List<MembershipHistory>
    findByMembershipMemberIdOrderByRenewedAtDesc(Long memberId);
}