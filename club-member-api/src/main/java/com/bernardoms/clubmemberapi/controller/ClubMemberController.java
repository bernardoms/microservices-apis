package com.bernardoms.clubmemberapi.controller;

import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import com.bernardoms.clubmemberapi.service.ClubMemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clubmembers")
@RequiredArgsConstructor
public class ClubMemberController {
    @NonNull private final ClubMemberService clubMemberService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ClubMemberDTO createClubMember(@RequestBody @Validated ClubMemberDTO clubMemberDTO) {
        clubMemberService.createClubMember(clubMemberDTO);
        return clubMemberService.associateTeamWithCampaigns(clubMemberDTO);
    }
}
