package com.membership.controller;

import com.membership.dto.MembershipHistoryResponse;
import com.membership.dto.MembershipRequest;
import com.membership.dto.MembershipResponse;
import com.membership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/member/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipResponse registerMembership(
            @PathVariable Long memberId,
            @Valid @RequestBody MembershipRequest request
    ) {
        return membershipService.registerMembership(
                memberId,
                request
        );
    }

    @PutMapping("/{membershipId}/renew")
    public MembershipResponse renewMembership(
            @PathVariable Long membershipId,
            @Valid @RequestBody MembershipRequest request
    ) {
        return membershipService.renewMembership(
                membershipId,
                request
        );
    }

    @GetMapping("/{membershipId}")
    public MembershipResponse getMembership(
            @PathVariable Long membershipId
    ) {
        return membershipService.getMembership(
                membershipId
        );
    }

    @GetMapping("/member/{memberId}")
    public List<MembershipResponse> getMemberMemberships(
            @PathVariable Long memberId
    ) {
        return membershipService.getMemberMemberships(
                memberId
        );
    }

    @GetMapping("/{membershipId}/history")
    public List<MembershipHistoryResponse> getHistory(
            @PathVariable Long membershipId
    ) {
        return membershipService.getHistory(
                membershipId
        );
    }
}