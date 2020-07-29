package com.myhome.security;

import com.myhome.controllers.dto.UserDto;


public interface UserDetailFetcher {
  UserDto getUserDetailsByUsername(String username);
}
