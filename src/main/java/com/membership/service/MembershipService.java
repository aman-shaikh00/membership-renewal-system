package com.membership.service;

import com.membership.dto.MembershipHistoryResponse;
import com.membership.dto.MembershipRequest;
import com.membership.dto.MembershipResponse;
import com.membership.entity.Member;
import com.membership.entity.Membership;
import com.membership.entity.MembershipHistory;
import com.membership.enums.MembershipStatus;
import com.membership.repository.MemberRepository;
import com.membership.repository.MembershipHistoryRepository;
import com.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipHistoryRepository historyRepository;

    @Transactional
    public MembershipResponse registerMembership(
            Long memberId,
            MembershipRequest request
    ) {

        Member member = findMember(memberId);

        Membership membership = new Membership();

        membership.setMember(member);
        membership.setPlan(request.plan());
        membership.setStartDate(request.startDate());
        membership.setExpiryDate(
                calculateExpiry(
                        request.startDate(),
                        request.plan().name()
                )
        );
        membership.setStatus(MembershipStatus.ACTIVE);

        Membership saved =
                membershipRepository.save(membership);

        saveHistory(saved);

        return toResponse(saved);
    }

    @Transactional
    public MembershipResponse renewMembership(
            Long membershipId,
            MembershipRequest request
    ) {

        Membership membership =
                findMembership(membershipId);

        if (membership.getStatus() ==
                MembershipStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled membership cannot be renewed"
            );
        }

        LocalDate newStartDate =
                membership.getExpiryDate().isBefore(LocalDate.now())
                        ? LocalDate.now()
                        : membership.getExpiryDate();

        membership.setPlan(request.plan());
        membership.setStartDate(newStartDate);
        membership.setExpiryDate(
                calculateExpiry(
                        newStartDate,
                        request.plan().name()
                )
        );
        membership.setStatus(MembershipStatus.ACTIVE);

        Membership saved =
                membershipRepository.save(membership);

        saveHistory(saved);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MembershipResponse getMembership(Long id) {

        return toResponse(findMembership(id));
    }

    @Transactional(readOnly = true)
    public List<MembershipResponse> getMemberMemberships(
            Long memberId
    ) {

        findMember(memberId);

        return membershipRepository
                .findByMemberIdOrderByStartDateDesc(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MembershipHistoryResponse> getHistory(
            Long membershipId
    ) {

        findMembership(membershipId);

        return historyRepository
                .findByMembershipIdOrderByRenewedAtDesc(membershipId)
                .stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    @Transactional
    public void updateMembershipStatuses() {

        LocalDate today = LocalDate.now();
        LocalDate reminderDate = today.plusDays(7);

        List<Membership> expired =
                membershipRepository.findExpiredMemberships(today);

        for (Membership membership : expired) {
            membership.setStatus(MembershipStatus.EXPIRED);
        }

        membershipRepository.saveAll(expired);

        List<Membership> expiringSoon =
                membershipRepository.findMembershipsExpiringSoon(
                        today,
                        reminderDate
                );

        for (Membership membership : expiringSoon) {
            membership.setStatus(
                    MembershipStatus.EXPIRING_SOON
            );
        }

        membershipRepository.saveAll(expiringSoon);
    }

    private LocalDate calculateExpiry(
            LocalDate startDate,
            String plan
    ) {

        return switch (plan) {

            case "MONTHLY" ->
                    startDate.plusMonths(1).minusDays(1);

            case "QUARTERLY" ->
                    startDate.plusMonths(3).minusDays(1);

            case "HALF_YEARLY" ->
                    startDate.plusMonths(6).minusDays(1);

            case "YEARLY" ->
                    startDate.plusYears(1).minusDays(1);

            default ->
                    throw new RuntimeException(
                            "Invalid membership plan"
                    );
        };
    }

    private void saveHistory(Membership membership) {

        MembershipHistory history =
                new MembershipHistory();

        history.setMembership(membership);
        history.setPlan(membership.getPlan());
        history.setStartDate(membership.getStartDate());
        history.setExpiryDate(membership.getExpiryDate());
        history.setRenewedAt(LocalDateTime.now());

        historyRepository.save(history);
    }

    private Member findMember(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Member not found with id: " + id
                        ));
    }

    private Membership findMembership(Long id) {

        return membershipRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Membership not found with id: " + id
                        ));
    }

    private MembershipResponse toResponse(
            Membership membership
    ) {

        return new MembershipResponse(
                membership.getId(),
                membership.getMember().getId(),
                membership.getMember().getName(),
                membership.getMember().getEmail(),
                membership.getPlan(),
                membership.getStatus(),
                membership.getStartDate(),
                membership.getExpiryDate()
        );
    }

    private MembershipHistoryResponse toHistoryResponse(
            MembershipHistory history
    ) {

        return new MembershipHistoryResponse(
                history.getId(),
                history.getMembership().getId(),
                history.getPlan(),
                history.getStartDate(),
                history.getExpiryDate(),
                history.getRenewedAt()
        );
    }
}