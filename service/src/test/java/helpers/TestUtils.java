package helpers;

import com.myhome.configuration.properties.mail.MailProperties;
import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.Amenity;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.HouseMemberDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import static helpers.TestUtils.CommunityHouseHelpers.getTestHouses;
import static helpers.TestUtils.General.generateUniqueId;
import static helpers.TestUtils.UserHelpers.getTestUsers;

public class TestUtils {

  public static class General {

    public static byte[] getImageAsByteArray(int height, int width) throws IOException {
      BufferedImage documentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      try (ByteArrayOutputStream imageBytesStream = new ByteArrayOutputStream()) {
        ImageIO.write(documentImage, "jpg", imageBytesStream);
        return imageBytesStream.toByteArray();
      }
    }

    public static String generateUniqueId() {
      return UUID.randomUUID().toString();
    }
  }

  public static class CommunityHouseHelpers {

    public static Set<CommunityHouse> getTestHouses(int count) {
      return Stream
          .generate(() -> new CommunityHouse()
              .withHouseId(generateUniqueId())
              .withName("default-house-name")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

    public static CommunityHouse getTestCommunityHouse() {
      return new CommunityHouse()
          .withHouseId(generateUniqueId())
          .withName("default-community-name");
    }

    public static CommunityHouse getTestCommunityHouse(String houseId) {
      return new CommunityHouse()
          .withHouseId(houseId)
          .withName("default-community-name");
    }
  }

  public static class HouseMemberHelpers {

    public static Set<HouseMember> getTestHouseMembers(int count) {
      return Stream
          .generate(() -> new HouseMember()
              .withMemberId(generateUniqueId())
              .withName("default-house-member-name")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }
    public static HouseMember getTestHouseMember() {
      return new HouseMember()
              .withMemberId(generateUniqueId())
              .withName("default-house-member-name");
    }
  }

  public static class CommunityHelpers {

    public static Set<Community> getTestCommunities(int count) {
      return Stream.iterate(0, n -> n + 1)
          .map(index -> getTestCommunity(
              generateUniqueId(),
              "default-community-name" + index,
              "default-community-district" + index,
              0, 0)
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

    public static Community getTestCommunity() {
      return getTestCommunity(
          generateUniqueId(),
          "default-community-name",
          "default-community-district",
          0, 0);
    }

    public static Community getTestCommunity(User admin) {
      Community testCommunity = getTestCommunity();
      admin.getCommunities().add(testCommunity);
      testCommunity.setAdmins(Collections.singleton(admin));
      return testCommunity;
    }

    public static Community getTestCommunity(String communityId, String communityName, String communityDistrict, int adminsCount, int housesCount) {
      Community testCommunity = new Community(
          new HashSet<>(),
          new HashSet<>(),
          communityName,
          communityId,
          communityDistrict,
          new HashSet<>()
      );
      Set<CommunityHouse> communityHouses = getTestHouses(housesCount);
      communityHouses.forEach(house -> house.setCommunity(testCommunity));
      Set<User> communityAdmins = getTestUsers(adminsCount);
      communityAdmins.forEach(user -> user.getCommunities().add(testCommunity));

      testCommunity.setHouses(communityHouses);
      testCommunity.setAdmins(communityAdmins);
      return testCommunity;
    }
  }

  public static class AmenityHelpers {

    public static Amenity getTestAmenity(String amenityId, String amenityDescription) {
      return new Amenity()
          .withAmenityId(amenityId)
          .withDescription(amenityDescription)
          .withCommunity(CommunityHelpers.getTestCommunity());
    }

    public static Set<Amenity> getTestAmenities(int count) {
      return Stream
          .generate(() -> new Amenity()
              .withAmenityId(generateUniqueId())
              .withName("default-amenity-name")
              .withDescription("default-amenity-description")
          )
          .limit(count)
          .collect(Collectors.toSet());
    }

  }

  public static class UserHelpers {

    public static Set<User> getTestUsers(int count) {
      return Stream.iterate(0, n -> n + 1)
          .map(index -> new User(
              "default-user-name" + index,
              generateUniqueId(),
              "default-user-email" + index,
              false,
              "default-user-password" + index,
              new HashSet<>(),
              new HashSet<>())
          )
          .limit(count)
          .collect(Collectors.toSet());
    }
  }

  public static class MailPropertiesHelper {

    public static MailProperties getTestMailProperties() {
      MailProperties testMailProperties = new MailProperties();
      testMailProperties.setHost("test host");
      testMailProperties.setUsername("testUsername");
      testMailProperties.setPassword("test password");
      testMailProperties.setPort(0);
      testMailProperties.setProtocol("test protocol");
      testMailProperties.setDebug(false);
      testMailProperties.setDevMode(false);
      return testMailProperties;
    }

  }

  public static class PaymentHelpers {

    public static PaymentDto getTestPaymentDto(BigDecimal charge, String type, String description, boolean recurring, LocalDate dueDate, UserDto admin, HouseMemberDto member) {

      return PaymentDto.builder()
          .charge(charge)
          .type(type)
          .description(description)
          .recurring(recurring)
          .dueDate(dueDate.toString())
          .admin(admin)
          .member(member)
          .build();
    }
    public static Payment getTestPaymentNullFields() {
      //Only 'recurring' field will be not null, but false
      return new Payment(
          null,
          null,
          null,
          null,
          false,
          null,
          null,
          null);
    }
  }
}
