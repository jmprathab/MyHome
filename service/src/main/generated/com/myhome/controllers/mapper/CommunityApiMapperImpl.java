package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.model.CommunityHouseName;
import com.myhome.model.CreateCommunityRequest;
import com.myhome.model.CreateCommunityResponse;
import com.myhome.model.GetCommunityDetailsResponseCommunity;
import com.myhome.model.GetHouseDetailsResponseCommunityHouse;
import com.myhome.model.ListCommunityAdminsResponseCommunityAdmin;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-22T18:22:42+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class CommunityApiMapperImpl implements CommunityApiMapper {

    @Override
    public CommunityDto createCommunityRequestToCommunityDto(CreateCommunityRequest request) {
        if ( request == null ) {
            return null;
        }

        CommunityDto communityDto = new CommunityDto();

        communityDto.setName( request.getName() );
        communityDto.setDistrict( request.getDistrict() );

        return communityDto;
    }

    @Override
    public GetCommunityDetailsResponseCommunity communityToRestApiResponseCommunity(Community community) {
        if ( community == null ) {
            return null;
        }

        GetCommunityDetailsResponseCommunity getCommunityDetailsResponseCommunity = new GetCommunityDetailsResponseCommunity();

        getCommunityDetailsResponseCommunity.setCommunityId( community.getCommunityId() );
        getCommunityDetailsResponseCommunity.setName( community.getName() );
        getCommunityDetailsResponseCommunity.setDistrict( community.getDistrict() );

        return getCommunityDetailsResponseCommunity;
    }

    @Override
    public Set<GetCommunityDetailsResponseCommunity> communitySetToRestApiResponseCommunitySet(Set<Community> communitySet) {
        if ( communitySet == null ) {
            return null;
        }

        Set<GetCommunityDetailsResponseCommunity> set = new HashSet<GetCommunityDetailsResponseCommunity>( Math.max( (int) ( communitySet.size() / .75f ) + 1, 16 ) );
        for ( Community community : communitySet ) {
            set.add( communityToRestApiResponseCommunity( community ) );
        }

        return set;
    }

    @Override
    public CreateCommunityResponse communityToCreateCommunityResponse(Community community) {
        if ( community == null ) {
            return null;
        }

        CreateCommunityResponse createCommunityResponse = new CreateCommunityResponse();

        createCommunityResponse.setCommunityId( community.getCommunityId() );

        return createCommunityResponse;
    }

    @Override
    public Set<ListCommunityAdminsResponseCommunityAdmin> communityAdminSetToRestApiResponseCommunityAdminSet(Set<User> communityAdminSet) {
        if ( communityAdminSet == null ) {
            return null;
        }

        Set<ListCommunityAdminsResponseCommunityAdmin> set = new HashSet<ListCommunityAdminsResponseCommunityAdmin>( Math.max( (int) ( communityAdminSet.size() / .75f ) + 1, 16 ) );
        for ( User user : communityAdminSet ) {
            set.add( userAdminToResponseAdmin( user ) );
        }

        return set;
    }

    @Override
    public ListCommunityAdminsResponseCommunityAdmin userAdminToResponseAdmin(User user) {
        if ( user == null ) {
            return null;
        }

        ListCommunityAdminsResponseCommunityAdmin listCommunityAdminsResponseCommunityAdmin = new ListCommunityAdminsResponseCommunityAdmin();

        listCommunityAdminsResponseCommunityAdmin.setAdminId( user.getUserId() );

        return listCommunityAdminsResponseCommunityAdmin;
    }

    @Override
    public Set<CommunityHouse> communityHouseNamesSetToCommunityHouseSet(Set<CommunityHouseName> communityHouseNamesSet) {
        if ( communityHouseNamesSet == null ) {
            return null;
        }

        Set<CommunityHouse> set = new HashSet<CommunityHouse>( Math.max( (int) ( communityHouseNamesSet.size() / .75f ) + 1, 16 ) );
        for ( CommunityHouseName communityHouseName : communityHouseNamesSet ) {
            set.add( communityHouseNameToCommunityHouse( communityHouseName ) );
        }

        return set;
    }

    @Override
    public Set<GetHouseDetailsResponseCommunityHouse> communityHouseSetToRestApiResponseCommunityHouseSet(Set<CommunityHouse> communityHouse) {
        if ( communityHouse == null ) {
            return null;
        }

        Set<GetHouseDetailsResponseCommunityHouse> set = new HashSet<GetHouseDetailsResponseCommunityHouse>( Math.max( (int) ( communityHouse.size() / .75f ) + 1, 16 ) );
        for ( CommunityHouse communityHouse1 : communityHouse ) {
            set.add( communityHouseToGetHouseDetailsResponseCommunityHouse( communityHouse1 ) );
        }

        return set;
    }

    protected CommunityHouse communityHouseNameToCommunityHouse(CommunityHouseName communityHouseName) {
        if ( communityHouseName == null ) {
            return null;
        }

        CommunityHouse communityHouse = new CommunityHouse();

        communityHouse.setName( communityHouseName.getName() );

        return communityHouse;
    }

    protected GetHouseDetailsResponseCommunityHouse communityHouseToGetHouseDetailsResponseCommunityHouse(CommunityHouse communityHouse) {
        if ( communityHouse == null ) {
            return null;
        }

        GetHouseDetailsResponseCommunityHouse getHouseDetailsResponseCommunityHouse = new GetHouseDetailsResponseCommunityHouse();

        getHouseDetailsResponseCommunityHouse.setHouseId( communityHouse.getHouseId() );
        getHouseDetailsResponseCommunityHouse.setName( communityHouse.getName() );

        return getHouseDetailsResponseCommunityHouse;
    }
}
