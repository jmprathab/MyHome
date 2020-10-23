package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.Community;
import com.myhome.domain.User;
import com.myhome.domain.User.UserBuilder;
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
public class CommunityMapperImpl implements CommunityMapper {

    @Override
    public Community communityDtoToCommunity(CommunityDto communityDto) {
        if ( communityDto == null ) {
            return null;
        }

        Community community = new Community();

        community.setId( communityDto.getId() );
        community.setAdmins( userDtoSetToUserSet( communityDto.getAdmins() ) );
        community.setName( communityDto.getName() );
        community.setCommunityId( communityDto.getCommunityId() );
        community.setDistrict( communityDto.getDistrict() );

        return community;
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.name( userDto.getName() );
        user.userId( userDto.getUserId() );
        user.email( userDto.getEmail() );
        user.encryptedPassword( userDto.getEncryptedPassword() );

        return user.build();
    }

    protected Set<User> userDtoSetToUserSet(Set<UserDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<User> set1 = new HashSet<User>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserDto userDto : set ) {
            set1.add( userDtoToUser( userDto ) );
        }

        return set1;
    }
}
