/*
 * Swagger MyHome - OpenAPI 3.0
 * This is a OpenApi specification for MyHome backend service.
 *
 * The version of the OpenAPI document: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openapitools.client.model.GetUserDetailsResponseUser;

/**
 * GetUserDetailsResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-09-26T12:06:21.393815800+03:00[Asia/Hebron]")
public class GetUserDetailsResponse {
  public static final String SERIALIZED_NAME_USERS = "users";
  @SerializedName(SERIALIZED_NAME_USERS)
  private Set<GetUserDetailsResponseUser> users = new LinkedHashSet<GetUserDetailsResponseUser>();


  public GetUserDetailsResponse users(Set<GetUserDetailsResponseUser> users) {
    
    this.users = users;
    return this;
  }

  public GetUserDetailsResponse addUsersItem(GetUserDetailsResponseUser usersItem) {
    this.users.add(usersItem);
    return this;
  }

   /**
   * Get users
   * @return users
  **/
  @ApiModelProperty(required = true, value = "")

  public Set<GetUserDetailsResponseUser> getUsers() {
    return users;
  }


  public void setUsers(Set<GetUserDetailsResponseUser> users) {
    this.users = users;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetUserDetailsResponse getUserDetailsResponse = (GetUserDetailsResponse) o;
    return Objects.equals(this.users, getUserDetailsResponse.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(users);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetUserDetailsResponse {\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

