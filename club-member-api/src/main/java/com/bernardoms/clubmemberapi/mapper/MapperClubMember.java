package com.bernardoms.clubmemberapi.mapper;

import com.bernardoms.clubmemberapi.model.ClubMember;
import com.bernardoms.clubmemberapi.model.dto.ClubMemberDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperClubMember implements Mapper<ClubMember, ClubMemberDTO> {

    @Override
    public ClubMemberDTO mapToResp(ClubMember mappedDocument) {
        ClubMemberDTO clubMemberDTO = ClubMemberDTO.builder()
                .email(mappedDocument.getEmail())
                .fullName(mappedDocument.getFullName())
                .birthDate(mappedDocument.getBirthDate())
                .build();
        clubMemberDTO.getTeam().setName(mappedDocument.getTeamName());
        return clubMemberDTO;
    }

    @Override
    public ClubMember mapToDocument(ClubMemberDTO mappedResponse) {
        return ClubMember.builder()
                .birthDate(mappedResponse.getBirthDate())
                .fullName(mappedResponse.getFullName())
                .email(mappedResponse.getEmail())
                .teamName(mappedResponse.getTeam().getName())
                .build();
    }
}
