# PaymentsApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listAllAdminScheduledPayments**](PaymentsApi.md#listAllAdminScheduledPayments) | **GET** /communities/{communityId}/admins/{adminId}/payments | 
[**listAllMemberPayments**](PaymentsApi.md#listAllMemberPayments) | **GET** /members/{memberId}/payments | 
[**listPaymentDetails**](PaymentsApi.md#listPaymentDetails) | **GET** /payments/{paymentId} | 
[**schedulePayment**](PaymentsApi.md#schedulePayment) | **POST** /payments | 


<a name="listAllAdminScheduledPayments"></a>
# **listAllAdminScheduledPayments**
> ListAdminPaymentsResponse listAllAdminScheduledPayments(communityId, adminId, pageable)



Get all payments scheduled by the specified admin

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    String communityId = "communityId_example"; // String | The id of community
    String adminId = "adminId_example"; // String | The id of admin
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      ListAdminPaymentsResponse result = apiInstance.listAllAdminScheduledPayments(communityId, adminId, pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#listAllAdminScheduledPayments");
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
 **communityId** | **String**| The id of community |
 **adminId** | **String**| The id of admin |
 **pageable** | [**Pageable**](.md)|  | [optional]

### Return type

[**ListAdminPaymentsResponse**](ListAdminPaymentsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If communityId and adminId are valid. Response body has the details |  -  |
**404** | If communityId or adminId are invalid |  -  |

<a name="listAllMemberPayments"></a>
# **listAllMemberPayments**
> ListMemberPaymentsResponse listAllMemberPayments(memberId)



Get all payments for the specified member

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    String memberId = "memberId_example"; // String | Member Id to use for getting all payments
    try {
      ListMemberPaymentsResponse result = apiInstance.listAllMemberPayments(memberId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#listAllMemberPayments");
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
 **memberId** | **String**| Member Id to use for getting all payments |

### Return type

[**ListMemberPaymentsResponse**](ListMemberPaymentsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If memberId is valid. Response body has the details |  -  |
**404** | If memberId is invalid |  -  |

<a name="listPaymentDetails"></a>
# **listPaymentDetails**
> SchedulePaymentResponse listPaymentDetails(paymentId)



Get details about a payment with the given payment id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    String paymentId = "paymentId_example"; // String | Payment ID
    try {
      SchedulePaymentResponse result = apiInstance.listPaymentDetails(paymentId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#listPaymentDetails");
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
 **paymentId** | **String**| Payment ID |

### Return type

[**SchedulePaymentResponse**](SchedulePaymentResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If payment is valid. Response body has the details |  -  |
**404** | If paymentId is invalid |  -  |

<a name="schedulePayment"></a>
# **schedulePayment**
> SchedulePaymentResponse schedulePayment(schedulePaymentRequest)



Schedule a new payment

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    SchedulePaymentRequest schedulePaymentRequest = new SchedulePaymentRequest(); // SchedulePaymentRequest | CreateUserRequest aggregate fields
    try {
      SchedulePaymentResponse result = apiInstance.schedulePayment(schedulePaymentRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#schedulePayment");
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
 **schedulePaymentRequest** | [**SchedulePaymentRequest**](SchedulePaymentRequest.md)| CreateUserRequest aggregate fields |

### Return type

[**SchedulePaymentResponse**](SchedulePaymentResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | If schedule payment is created |  -  |

