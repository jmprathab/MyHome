package com.myhome.security.filters;

import com.myhome.domain.User;
import com.myhome.services.CommunityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunityAuthorizationFilter extends BasicAuthenticationFilter {
  private final CommunityService communityService;
  private static final String UUID_PATTERN =
      "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
  private static final Pattern ADD_AMENITY_REQUEST_PATTERN =
      Pattern.compile("/communities/" + UUID_PATTERN + "/amenities");

  public CommunityAuthorizationFilter(AuthenticationManager authenticationManager,
      CommunityService communityService) {
    super(authenticationManager);
    this.communityService = communityService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    Matcher urlMatcher = ADD_AMENITY_REQUEST_PATTERN.matcher(request.getRequestURI());

    if (urlMatcher.find() && !isUserCommunityAdmin(request)) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    super.doFilterInternal(request, response, chain);
  }

  private boolean isUserCommunityAdmin(HttpServletRequest request) {
    String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String communityId = request.getRequestURI().split("/")[2];

    return communityService.findCommunityAdminsById(communityId, null)
        .flatMap(admins -> admins.stream()
            .map(User::getUserId)
            .filter(userId::equals)
            .findFirst()
        )
        .isPresent();
  }
}
