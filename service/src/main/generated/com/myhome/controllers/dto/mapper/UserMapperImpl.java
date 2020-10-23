package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.UserDto.UserDtoBuilder;
import com.myhome.domain.User;
import com.myhome.domain.User.UserBuilder;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-23T15:54:07+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
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

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.communityIds( UserMapper.communitiesToIds( user.getCommunities() ) );
        userDto.id( user.getId() );
        userDto.userId( user.getUserId() );
        userDto.name( user.getName() );
        userDto.email( user.getEmail() );
        userDto.encryptedPassword( user.getEncryptedPassword() );

        return userDto.build();
    }
}
