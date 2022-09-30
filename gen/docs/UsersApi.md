# UsersApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**confirmEmail**](UsersApi.md#confirmEmail) | **GET** /users/{userId}/email-confirm/{emailConfirmToken} | 
[**getUserDetails**](UsersApi.md#getUserDetails) | **GET** /users/{userId} | 
[**listAllHousemates**](UsersApi.md#listAllHousemates) | **GET** /users/{userId}/housemates | 
[**listAllUsers**](UsersApi.md#listAllUsers) | **GET** /users | 
[**resendConfirmEmailMail**](UsersApi.md#resendConfirmEmailMail) | **GET** /users/{userId}/email-confirm-resend | 
[**signUp**](UsersApi.md#signUp) | **POST** /users | 
[**usersPasswordPost**](UsersApi.md#usersPasswordPost) | **POST** /users/password | 


<a name="confirmEmail"></a>
# **confirmEmail**
> confirmEmail(userId, emailConfirmToken)



Confirm user email

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | Id of the user for confirm email
    String emailConfirmToken = "emailConfirmToken_example"; // String | Tocken from user email
    try {
      apiInstance.confirmEmail(userId, emailConfirmToken);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#confirmEmail");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**| Id of the user for confirm email |
 **emailConfirmToken** | **String**| Tocken from user email |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Email was successfully confirmed |  -  |
**400** | Email confirmation error |  -  |

<a name="getUserDetails"></a>
# **getUserDetails**
> GetUserDetailsResponseUser getUserDetails(userId)



Get details of a user given userId

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | ID of the user to get
    try {
      GetUserDetailsResponseUser result = apiInstance.getUserDetails(userId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#getUserDetails");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**| ID of the user to get |

### Return type

[**GetUserDetailsResponseUser**](GetUserDetailsResponseUser.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If userId is valid. Response body has the details |  -  |
**404** | If userId is invalid |  -  |

<a name="listAllHousemates"></a>
# **listAllHousemates**
> ListHouseMembersResponse listAllHousemates(userId, pageable)



Lists all members from all houses of a user

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | ID of the user for which to find housemates
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      ListHouseMembersResponse result = apiInstance.listAllHousemates(userId, pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#listAllHousemates");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**| ID of the user for which to find housemates |
 **pageable** | [**Pageable**](.md)|  | [optional]

### Return type

[**ListHouseMembersResponse**](ListHouseMembersResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns list of all members from all houses of the specified user |  -  |

<a name="listAllUsers"></a>
# **listAllUsers**
> GetUserDetailsResponse listAllUsers(pageable)



Lists all users

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    UsersApi apiInstance = new UsersApi(defaultClient);
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      GetUserDetailsResponse result = apiInstance.listAllUsers(pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#listAllUsers");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageable** | [**Pageable**](.md)|  | [optional]

### Return type

[**GetUserDetailsResponse**](GetUserDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns list of users |  -  |

<a name="resendConfirmEmailMail"></a>
# **resendConfirmEmailMail**
> resendConfirmEmailMail(userId)



Resend email confirm mail

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | ID of the user for resend confirm email
    try {
      apiInstance.resendConfirmEmailMail(userId);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#resendConfirmEmailMail");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**| ID of the user for resend confirm email |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Email was successfully send |  -  |
**400** | Email send error |  -  |

<a name="signUp"></a>
# **signUp**
> CreateUserResponse signUp(createUserRequest)



Create a new user

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UsersApi apiInstance = new UsersApi(defaultClient);
    CreateUserRequest createUserRequest = new CreateUserRequest(); // CreateUserRequest | CreateUserRequest aggregate fields
    try {
      CreateUserResponse result = apiInstance.signUp(createUserRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#signUp");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createUserRequest** | [**CreateUserRequest**](CreateUserRequest.md)| CreateUserRequest aggregate fields |

### Return type

[**CreateUserResponse**](CreateUserResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | If user created |  -  |
**409** | If user already exists |  -  |

<a name="usersPasswordPost"></a>
# **usersPasswordPost**
> usersPasswordPost(action, forgotPasswordRequest)



Request reset password or reset password

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String action = "action_example"; // String | Acton with user password (forgot or reset)
    ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(); // ForgotPasswordRequest | 
    try {
      apiInstance.usersPasswordPost(action, forgotPasswordRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#usersPasswordPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **action** | **String**| Acton with user password (forgot or reset) | [enum: FORGOT, RESET]
 **forgotPasswordRequest** | [**ForgotPasswordRequest**](ForgotPasswordRequest.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If password reset |  -  |
**400** | If wrong password reset token |  -  |

