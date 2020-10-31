package com.myhome.repositories.specifications;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.domain.AmenityBookingItem_;
import com.myhome.domain.Amenity_;
import java.time.LocalDateTime;
import javax.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class AmenityBookingItemSpecifications {

  public static Specification<AmenityBookingItem> amenityIdEquals(String amenityId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(
            root.get(AmenityBookingItem_.AMENITY).get(Amenity_.AMENITY_ID),
            amenityId);
  }

  public static Specification<AmenityBookingItem> startDateAfter(LocalDateTime startDate) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get(AmenityBookingItem_.BOOKING_START_DATE),
            startDate);
  }

  public static Specification<AmenityBookingItem> endDateNullOrBefore(LocalDateTime endDate) {
    return (root, query, criteriaBuilder) -> {
      Path<LocalDateTime> endDatePath = root.get(AmenityBookingItem_.BOOKING_END_DATE);

      return criteriaBuilder.or(
          criteriaBuilder.isNull(endDatePath),
          criteriaBuilder.lessThanOrEqualTo(endDatePath, endDate)
      );
    };
  }
}
