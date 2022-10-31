<!-- Thanks for taking the time to write this Pull Request ❤️ -->

## 🚀 Description
Included PageInfo object as an instance variable in the GetCommunityDetailsResponse component. I also included a 
listAllWithPages(Pageable pageable) method which returns Page<Community> object in the CommunityService interface, and implemented in the 
CommunitySDJpaService. This method was used in the listAllCommunities method in the CommunityController class.
<!-- Describe your changes in detail -->

## 📄 Motivation and Context
When you list all communities by GET /communities, you get all the communities in a communities array, you can also page
them using page and size query parameters, but no pageInfo object is returned, which is required for further pagination
on the front-end.
This change was added to include page information to navigate the list of communities returned by the GET /communities
request.
https://github.com/jmprathab/MyHome/issues/262

<!-- Why is this change required? What problem does it solve? -->
<!-- If it fixes an open issue, please link to the issue here. -->

## 🧪 How Has This Been Tested?
Mockito unit test and postman test 

<!-- Please describe in detail how you tested your changes. -->
<!-- Include details of your testing environment, tests ran to see how -->
<!-- your change affects other areas of the code, etc. -->

## 📷 Screenshots (if appropriate)

<!-- Please provide a screenshot of your change -->

## 📦 Types of changes

<!-- What types of changes does your code introduce? Put an `x` in all the boxes that apply: -->

- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)

## ✅ Checklist

<!-- Go over all the following points, and put an `x` in all the boxes that apply. -->
<!-- If you're unsure about any of these, don't hesitate to ask. We're here to help! -->

- [x] My code follows the code style of this project(Do your best to follow code styles. If none apply just skip this).
- [ ] My change requires a change to the documentation.
- [ ] I have updated the documentation accordingly.
