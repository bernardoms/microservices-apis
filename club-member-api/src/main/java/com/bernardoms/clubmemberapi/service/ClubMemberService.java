package com.bernardoms.clubmemberapi.service;

import com.bernardoms.clubmemberapi.config.Config;
import com.bernardoms.clubmemberapi.mapper.Mapper;
import com.bernardoms.clubmemberapi.model.ClubMember;
import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import com.bernardoms.clubmemberapi.model.dto.TeamDTO;
import com.bernardoms.clubmemberapi.repository.ClubMemberRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubMemberService {
    @NonNull
    private final ClubMemberRepository clubMemberRepository;
    @NonNull
    private final Config config;
    @NonNull
    private final RestTemplate restTemplate;
    @NonNull
    private final Mapper<ClubMember, ClubMemberDTO> mapper;

    @HystrixCommand(fallbackMethod = "createClubMemberFallBack" )
    public ClubMemberDTO associateTeamWithCampaigns(ClubMemberDTO clubMemberDTO) {

        log.info("Associating campaigns for user " + clubMemberDTO.getEmail());

        var uri =UriComponentsBuilder.fromHttpUrl(config.getTeamEndpointAPI()).path("/teams/{teamName}/associate/campaigns")
               .buildAndExpand(clubMemberDTO.getTeam().getName());

        ResponseEntity<TeamDTO> teamResponse = restTemplate.getForEntity(uri.toUriString(), TeamDTO.class);

        clubMemberDTO.setTeam(teamResponse.getBody());

        return clubMemberDTO;
    }

    private ClubMemberDTO createClubMemberFallBack(ClubMemberDTO clubMemberDTO) {
        log.info("Circuit open... Executing fallback");
        return clubMemberDTO;
    }

    public void createClubMember(ClubMemberDTO clubMemberDTO) {
        Optional<ClubMember> optionalClubMember = clubMemberRepository.findByEmail(clubMemberDTO.getEmail());

        if (optionalClubMember.isEmpty()) {
            log.info("User with email: " + clubMemberDTO.getEmail() + " not exists, creating user ");
            clubMemberRepository.save(mapper.mapToDocument(clubMemberDTO));
        }
    }
}
