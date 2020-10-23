package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.UserDto.UserDtoBuilder;
import com.myhome.domain.User;
import com.myhome.model.CreateUserRequest;
import com.myhome.model.CreateUserResponse;
import com.myhome.model.GetUserDetailsResponseUser;
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
public class UserApiMapperImpl implements UserApiMapper {

    @Override
    public UserDto createUserRequestToUserDto(CreateUserRequest createUserRequest) {
        if ( createUserRequest == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.name( createUserRequest.getName() );
        userDto.email( createUserRequest.getEmail() );
        userDto.password( createUserRequest.getPassword() );

        return userDto.build();
    }

    @Override
    public Set<GetUserDetailsResponseUser> userSetToRestApiResponseUserSet(Set<User> userSet) {
        if ( userSet == null ) {
            return null;
        }

        Set<GetUserDetailsResponseUser> set = new HashSet<GetUserDetailsResponseUser>( Math.max( (int) ( userSet.size() / .75f ) + 1, 16 ) );
        for ( User user : userSet ) {
            set.add( userToGetUserDetailsResponseUser( user ) );
        }

        return set;
    }

    @Override
    public CreateUserResponse userDtoToCreateUserResponse(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        CreateUserResponse createUserResponse = new CreateUserResponse();

        createUserResponse.setUserId( userDto.getUserId() );
        createUserResponse.setName( userDto.getName() );
        createUserResponse.setEmail( userDto.getEmail() );

        return createUserResponse;
    }

    @Override
    public GetUserDetailsResponseUser userDtoToGetUserDetailsResponse(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        GetUserDetailsResponseUser getUserDetailsResponseUser = new GetUserDetailsResponseUser();

        getUserDetailsResponseUser.setUserId( userDto.getUserId() );
        getUserDetailsResponseUser.setName( userDto.getName() );
        getUserDetailsResponseUser.setEmail( userDto.getEmail() );
        Set<String> set = userDto.getCommunityIds();
        if ( set != null ) {
            getUserDetailsResponseUser.setCommunityIds( new HashSet<String>( set ) );
        }

        return getUserDetailsResponseUser;
    }

    protected GetUserDetailsResponseUser userToGetUserDetailsResponseUser(User user) {
        if ( user == null ) {
            return null;
        }

        GetUserDetailsResponseUser getUserDetailsResponseUser = new GetUserDetailsResponseUser();

        getUserDetailsResponseUser.setUserId( user.getUserId() );
        getUserDetailsResponseUser.setName( user.getName() );
        getUserDetailsResponseUser.setEmail( user.getEmail() );

        return getUserDetailsResponseUser;
    }
}
