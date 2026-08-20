package com.membership.dto;

public record MemberResponse(

        Long id,

        String name,

        String email,

        String phone
) {
}