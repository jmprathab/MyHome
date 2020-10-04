# MyHome Requirements Elicitation

- [MyHome Requirements Elicitation](#myhome-requirements-elicitation)
  * [Description](#description)
  * [User Management](#user-management)
  * [Community Management ✅](#community-management)
  * [House Management ✅](#house-management)
  * [Payment Management](#payment-management)
  * [Amenity Management ✅](#amenity-management)
  * [Amenity Booking Management](#amenity-booking-management)
  * [House Member Document Management ✅](#house-member-document-management)
  * [Application Deployment](#application-deployment)
  * [Report Generation](#report-generation)
  * [Troubleshooting/Prepare for Release](#troubleshooting-prepare-for-release)
  * [Frontend](#frontend)
  * [Future Plans](#future-plans)

## Description

MyHome is an application which helps people to manage their apartment.

## User Management

| Feature                                     | Description                                                  | Notes |
| ------------------------------------------- | ------------------------------------------------------------ | ----- |
| Create Account                              | Create a new user in the application. User gives email for registration. | ✅     |
| Login                                       | Users should login to the system to use the application.     | ✅     |
| Forgot Password                             | Users should be able to reset their password.                |       |
| Disclaimer/License agreement during signup. | Show Disclaimer/License during signup. User has to accept the terms and conditions for sign up. Anything special for GDPR? |       |

## Community Management ✅

| Feature                   | Description                                                  | Notes |
| ------------------------- | ------------------------------------------------------------ | ----- |
| Create a community        | Add a new community. User who creates a community will be the admin of the community. | ✅     |
| Delete a community        | Deleting a community deletes all the houses in the community. Thereby deleting all the entities which belongs to the community. | ✅     |
| Add admins to a community | Admins should be an existing registered user of the application. | ✅     |

## House Management ✅

| Feature                  | Description                                               | Notes |
| ------------------------ | --------------------------------------------------------- | ----- |
| Add house to a community | Houses can be added to a community.                       | ✅     |
| Add members to a house   | Community Admins should be able to add members to a house | ✅     |

## Payment Management

| Feature                            | Description                                                  | Notes |
| ---------------------------------- | ------------------------------------------------------------ | ----- |
| Schedule payment for house members | Community admins should be able to schedule payments for a house |       |
| Schedule recurring payments        | Community admins should be able to schedule recurring payments for a house |       |
| Marking payments as complete       | House members should be able to mark payments as paid.       |       |

## Amenity Management ✅

| Feature                        | Description                                                  | Notes |
| ------------------------------ | ------------------------------------------------------------ | ----- |
| Add amenity by community admin | Community admins should be able to add amenities to a community | ✅     |

## Amenity Booking Management

| Feature           | Description                                                  | Notes |
| ----------------- | ------------------------------------------------------------ | ----- |
| Booking amenities | House members should be able to book amenities for a time slot. |       |
| Delete booking    | House members should be able to delete a future booking      |       |

## House Member Document Management ✅

| Feature                             | Description                                                  | Notes |
| ----------------------------------- | ------------------------------------------------------------ | ----- |
| Add documents for a house member    | Community Admin should be able to add documents for a house member | ✅     |
| Delete documents for a house member | Community Admin should be able to delete documents for a house member | ✅     |

## Application Deployment

| Feature                   | Description                                               | Notes |
| ------------------------- | --------------------------------------------------------- | ----- |
| Dockerize the application | Easy deployment with docker-compose                       |       |
| Deployment with yunohost  | Integrate with [yunohost](https://yunohost.org)           |       |
| 1-Click AWS deployment    | Should give an easy way to deploy the application to AWS. |       |

## Report Generation

| Feature        | Description                                                  | Notes |
| -------------- | ------------------------------------------------------------ | ----- |
| Payment report | Generate all the payments made by a user for a duration.     |       |
| Booking report | Generate all bookings for an amenity during a duration (Can also list the booking in the future) |       |

## Troubleshooting/Prepare for Release

| Feature                   | Description                                                  | Notes |
| ------------------------- | ------------------------------------------------------------ | ----- |
| Logging                   | Do elaborate logging                                         |       |
| MongoDB/MySQL integration | Switch from H2DB to MySQL/MongoDB                            |       |
| Spring boot actuator      | Enable Spring boot actuator endpoints.                       |       |
| Feedback                  | Users of the application to give feedback to the developers. |       |
| Version all APIs          | REST APIs should be versioned                                |       |

## Frontend

| Feature                            | Description                                                  | Notes |
| ---------------------------------- | ------------------------------------------------------------ | ----- |
| PWA                                | Frontend application has to be a Progressive Web App         |       |
| Admin Console for Community Admins | Community Admins should be able to access a special page called "Admin Console" where privileged operations are performed. |       |

## Future Plans

| Feature                                    | Description                                                  | Notes |
| ------------------------------------------ | ------------------------------------------------------------ | ----- |
| Notification system                        | Community Admin to schedule notifications which will be pushed to all users of a community. |       |
| Chat system                                | Users to Chat with Community Admin                           |       |
| Raise a concern                            | User should be able to raise a concern with community admin. Community admin will be notified of the user's concern and can update the status of the concern(To do, Doing, Complete) |       |
| Approve entry to Community by House member | Security desk can request approval from house members before allowing visitors to enter the community. |       |