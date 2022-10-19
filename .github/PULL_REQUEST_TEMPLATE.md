<!-- Thanks for taking the time to write this Pull Request ❤️ -->

## 🚀 Description

<!-- Describe your changes in detail -->
The major change made in this pull requests is the addition of implementation of a new feature: Get all bookings for an 
amenity with an optional time range, the pattern of the time format used here is "dd-MM-yyyy[['T']HH[:mm][:ss]]".
Due to the addition of this feature, changes where made to the certain files such as, api.yml, AmenityBookingItemRepository.java, 
BookingController.java, BookingService.java,BookingSDJpaService.java, postman collection and a new utils directory 
containing StringTimeFormatConverter.java.

## 📄 Motivation and Context

<!-- Why is this change required? What problem does it solve? -->
This change is required to enable implementation of the requested feature. For instance, the newly added StringTimeFormatConverter.java
file provides a set of functions for easy conversion of string time formats(optional time component) to LocalDateTime,
which the generated LocalDateTime output was passed as a parameter to the database querying function.

<!-- If it fixes an open issue, please link to the issue here. -->
issue link: https://github.com/jmprathab/MyHome/issues/114

## 🧪 How Has This Been Tested?

<!-- Please describe in detail how you tested your changes. -->
Ran tests using postman.
<!-- Include details of your testing environment, tests ran to see how -->
<!-- your change affects other areas of the code, etc. -->


## 📷 Screenshots (if appropriate)

<!-- Please provide a screenshot of your change -->

## 📦 Types of changes

<!-- What types of changes does your code introduce? Put an `x` in all the boxes that apply: -->

- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)

## ✅ Checklist

<!-- Go over all the following points, and put an `x` in all the boxes that apply. -->
<!-- If you're unsure about any of these, don't hesitate to ask. We're here to help! -->

- [x] My code follows the code style of this project(Do your best to follow code styles. If none apply just skip this).
- [x] My change requires a change to the documentation.
- [x] I have updated the documentation accordingly.
