package com.membership.dto;

import com.membership.enums.MembershipPlan;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MembershipRequest(

        @NotNull(message = "Membership plan is required")
        MembershipPlan plan,

        @NotNull(message = "Start date is required")
        LocalDate startDate
) {
}