# BookingsApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteBooking**](BookingsApi.md#deleteBooking) | **DELETE** /amenities/{amenityId}/bookings/{bookingId} | 


<a name="deleteBooking"></a>
# **deleteBooking**
> deleteBooking(amenityId, bookingId)



Remove booking

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BookingsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    BookingsApi apiInstance = new BookingsApi(defaultClient);
    String amenityId = "amenityId_example"; // String | 
    String bookingId = "bookingId_example"; // String | 
    try {
      apiInstance.deleteBooking(amenityId, bookingId);
    } catch (ApiException e) {
      System.err.println("Exception when calling BookingsApi#deleteBooking");
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
 **amenityId** | **String**|  |
 **bookingId** | **String**|  |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If booking deleted |  -  |
**404** | If params are invalid |  -  |

