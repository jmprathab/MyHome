package com.myhome.controllers.mapper;

import com.myhome.domain.CommunityHouse;
import com.myhome.model.GetHouseDetailsResponseCommunityHouse;
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
public class HouseApiMapperImpl implements HouseApiMapper {

    @Override
    public Set<GetHouseDetailsResponseCommunityHouse> communityHouseSetToRestApiResponseCommunityHouseSet(Set<CommunityHouse> communityHouse) {
        if ( communityHouse == null ) {
            return null;
        }

        Set<GetHouseDetailsResponseCommunityHouse> set = new HashSet<GetHouseDetailsResponseCommunityHouse>( Math.max( (int) ( communityHouse.size() / .75f ) + 1, 16 ) );
        for ( CommunityHouse communityHouse1 : communityHouse ) {
            set.add( communityHouseToRestApiResponseCommunityHouse( communityHouse1 ) );
        }

        return set;
    }

    @Override
    public GetHouseDetailsResponseCommunityHouse communityHouseToRestApiResponseCommunityHouse(CommunityHouse communityHouse) {
        if ( communityHouse == null ) {
            return null;
        }

        GetHouseDetailsResponseCommunityHouse getHouseDetailsResponseCommunityHouse = new GetHouseDetailsResponseCommunityHouse();

        getHouseDetailsResponseCommunityHouse.setHouseId( communityHouse.getHouseId() );
        getHouseDetailsResponseCommunityHouse.setName( communityHouse.getName() );

        return getHouseDetailsResponseCommunityHouse;
    }
}
