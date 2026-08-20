package com.membership.service;

import com.membership.dto.MemberRequest;
import com.membership.dto.MemberResponse;
import com.membership.entity.Member;
import com.membership.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse registerMember(MemberRequest request) {

        if (memberRepository.existsByEmailIgnoreCase(request.email())) {
            throw new RuntimeException(
                    "Member with this email already exists"
            );
        }

        Member member = new Member();

        member.setName(request.name());
        member.setEmail(request.email());
        member.setPhone(request.phone());

        return toResponse(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {

        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(Long id) {

        return toResponse(findMember(id));
    }

    private Member findMember(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Member not found with id: " + id
                        ));
    }

    private MemberResponse toResponse(Member member) {

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone()
        );
    }
}