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
import org.openapitools.client.model.HouseMemberDto;

/**
 * AddHouseMemberRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-09-26T12:06:21.393815800+03:00[Asia/Hebron]")
public class AddHouseMemberRequest {
  public static final String SERIALIZED_NAME_MEMBERS = "members";
  @SerializedName(SERIALIZED_NAME_MEMBERS)
  private Set<HouseMemberDto> members = new LinkedHashSet<HouseMemberDto>();


  public AddHouseMemberRequest members(Set<HouseMemberDto> members) {
    
    this.members = members;
    return this;
  }

  public AddHouseMemberRequest addMembersItem(HouseMemberDto membersItem) {
    this.members.add(membersItem);
    return this;
  }

   /**
   * Get members
   * @return members
  **/
  @ApiModelProperty(required = true, value = "")

  public Set<HouseMemberDto> getMembers() {
    return members;
  }


  public void setMembers(Set<HouseMemberDto> members) {
    this.members = members;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddHouseMemberRequest addHouseMemberRequest = (AddHouseMemberRequest) o;
    return Objects.equals(this.members, addHouseMemberRequest.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(members);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddHouseMemberRequest {\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
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

