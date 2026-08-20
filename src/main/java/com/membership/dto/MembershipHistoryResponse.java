package com.membership.dto;

import com.membership.enums.MembershipPlan;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MembershipHistoryResponse(

        Long id,

        Long membershipId,

        MembershipPlan plan,

        LocalDate startDate,

        LocalDate expiryDate,

        LocalDateTime renewedAt
) {
}