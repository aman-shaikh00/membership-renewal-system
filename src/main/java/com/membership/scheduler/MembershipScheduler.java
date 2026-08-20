package com.membership.scheduler;

import com.membership.entity.Membership;
import com.membership.repository.MembershipRepository;
import com.membership.service.EmailService;
import com.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MembershipScheduler {

    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkMembershipExpiry() {

        membershipService.updateMembershipStatuses();

        LocalDate today = LocalDate.now();
        LocalDate reminderDate = today.plusDays(7);

        List<Membership> expiringMemberships =
                membershipRepository.findMembershipsExpiringSoon(
                        today,
                        reminderDate
                );

        for (Membership membership : expiringMemberships) {

            emailService.sendExpiryReminder(
                    membership.getMember().getEmail(),
                    membership.getMember().getName(),
                    membership.getExpiryDate().toString()
            );
        }
    }
}