package com.membership.controller;

import com.membership.dto.MemberRequest;
import com.membership.dto.MemberResponse;
import com.membership.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse registerMember(
            @Valid @RequestBody MemberRequest request
    ) {
        return memberService.registerMember(request);
    }

    @GetMapping
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(
            @PathVariable Long id
    ) {
        return memberService.getMember(id);
    }
}