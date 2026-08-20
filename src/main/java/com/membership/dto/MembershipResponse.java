package com.membership.dto;

import com.membership.enums.MembershipPlan;
import com.membership.enums.MembershipStatus;

import java.time.LocalDate;

public record MembershipResponse(

        Long id,

        Long memberId,

        String memberName,

        String memberEmail,

        MembershipPlan plan,

        MembershipStatus status,

        LocalDate startDate,

        LocalDate expiryDate
) {
}