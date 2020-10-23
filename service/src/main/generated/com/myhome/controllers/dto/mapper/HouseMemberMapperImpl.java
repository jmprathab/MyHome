package com.myhome.controllers.dto.mapper;

import com.myhome.domain.HouseMember;
import com.myhome.model.HouseMemberDto;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-22T18:22:41+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class HouseMemberMapperImpl implements HouseMemberMapper {

    @Override
    public Set<com.myhome.model.HouseMember> houseMemberSetToRestApiResponseHouseMemberSet(Set<HouseMember> houseMemberSet) {
        if ( houseMemberSet == null ) {
            return null;
        }

        Set<com.myhome.model.HouseMember> set = new HashSet<com.myhome.model.HouseMember>( Math.max( (int) ( houseMemberSet.size() / .75f ) + 1, 16 ) );
        for ( HouseMember houseMember : houseMemberSet ) {
            set.add( houseMemberToHouseMember( houseMember ) );
        }

        return set;
    }

    @Override
    public Set<HouseMember> houseMemberDtoSetToHouseMemberSet(Set<HouseMemberDto> houseMemberDtoSet) {
        if ( houseMemberDtoSet == null ) {
            return null;
        }

        Set<HouseMember> set = new HashSet<HouseMember>( Math.max( (int) ( houseMemberDtoSet.size() / .75f ) + 1, 16 ) );
        for ( HouseMemberDto houseMemberDto : houseMemberDtoSet ) {
            set.add( houseMemberDtoToHouseMember( houseMemberDto ) );
        }

        return set;
    }

    @Override
    public Set<com.myhome.model.HouseMember> houseMemberSetToRestApiResponseAddHouseMemberSet(Set<HouseMember> houseMemberSet) {
        if ( houseMemberSet == null ) {
            return null;
        }

        Set<com.myhome.model.HouseMember> set = new HashSet<com.myhome.model.HouseMember>( Math.max( (int) ( houseMemberSet.size() / .75f ) + 1, 16 ) );
        for ( HouseMember houseMember : houseMemberSet ) {
            set.add( houseMemberToHouseMember( houseMember ) );
        }

        return set;
    }

    protected com.myhome.model.HouseMember houseMemberToHouseMember(HouseMember houseMember) {
        if ( houseMember == null ) {
            return null;
        }

        com.myhome.model.HouseMember houseMember1 = new com.myhome.model.HouseMember();

        houseMember1.setMemberId( houseMember.getMemberId() );
        houseMember1.setName( houseMember.getName() );

        return houseMember1;
    }

    protected HouseMember houseMemberDtoToHouseMember(HouseMemberDto houseMemberDto) {
        if ( houseMemberDto == null ) {
            return null;
        }

        HouseMember houseMember = new HouseMember();

        houseMember.setId( houseMemberDto.getId() );
        houseMember.setMemberId( houseMemberDto.getMemberId() );
        houseMember.setName( houseMemberDto.getName() );

        return houseMember;
    }
}
