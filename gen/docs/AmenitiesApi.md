# AmenitiesApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addAmenityToCommunity**](AmenitiesApi.md#addAmenityToCommunity) | **POST** /communities/{communityId}/amenities | 
[**deleteAmenity**](AmenitiesApi.md#deleteAmenity) | **DELETE** /amenities/{amenityId} | 
[**getAmenityDetails**](AmenitiesApi.md#getAmenityDetails) | **GET** /amenities/{amenityId} | 
[**listAllAmenities**](AmenitiesApi.md#listAllAmenities) | **GET** /communities/{communityId}/amenities | 
[**updateAmenity**](AmenitiesApi.md#updateAmenity) | **PUT** /amenities/{amenityId} | 


<a name="addAmenityToCommunity"></a>
# **addAmenityToCommunity**
> AddAmenityResponse addAmenityToCommunity(communityId, addAmenityRequest)



Adds amenity to community

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AmenitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AmenitiesApi apiInstance = new AmenitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    AddAmenityRequest addAmenityRequest = new AddAmenityRequest(); // AddAmenityRequest | 
    try {
      AddAmenityResponse result = apiInstance.addAmenityToCommunity(communityId, addAmenityRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AmenitiesApi#addAmenityToCommunity");
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
 **communityId** | **String**|  |
 **addAmenityRequest** | [**AddAmenityRequest**](AddAmenityRequest.md)|  | [optional]

### Return type

[**AddAmenityResponse**](AddAmenityResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If amenity add successful |  -  |
**404** | If community not found |  -  |

<a name="deleteAmenity"></a>
# **deleteAmenity**
> deleteAmenity(amenityId)



Remove amenity

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AmenitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AmenitiesApi apiInstance = new AmenitiesApi(defaultClient);
    String amenityId = "amenityId_example"; // String | 
    try {
      apiInstance.deleteAmenity(amenityId);
    } catch (ApiException e) {
      System.err.println("Exception when calling AmenitiesApi#deleteAmenity");
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
**204** | If amenity deleted |  -  |
**404** | If params are invalid |  -  |

<a name="getAmenityDetails"></a>
# **getAmenityDetails**
> GetAmenityDetailsResponse getAmenityDetails(amenityId)



Get details about the amenity

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AmenitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AmenitiesApi apiInstance = new AmenitiesApi(defaultClient);
    String amenityId = "amenityId_example"; // String | Id of the amenity to get details
    try {
      GetAmenityDetailsResponse result = apiInstance.getAmenityDetails(amenityId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AmenitiesApi#getAmenityDetails");
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
 **amenityId** | **String**| Id of the amenity to get details |

### Return type

[**GetAmenityDetailsResponse**](GetAmenityDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If details found |  -  |
**404** | If params are invalid |  -  |

<a name="listAllAmenities"></a>
# **listAllAmenities**
> Set&lt;GetAmenityDetailsResponse&gt; listAllAmenities(communityId)



Get all amenities of community

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AmenitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AmenitiesApi apiInstance = new AmenitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    try {
      Set<GetAmenityDetailsResponse> result = apiInstance.listAllAmenities(communityId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AmenitiesApi#listAllAmenities");
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
 **communityId** | **String**|  |

### Return type

[**Set&lt;GetAmenityDetailsResponse&gt;**](GetAmenityDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns list of amenities |  -  |
**404** | If params are invalid |  -  |

<a name="updateAmenity"></a>
# **updateAmenity**
> updateAmenity(amenityId, updateAmenityRequest)



Update an amenity

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AmenitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AmenitiesApi apiInstance = new AmenitiesApi(defaultClient);
    String amenityId = "amenityId_example"; // String | 
    UpdateAmenityRequest updateAmenityRequest = new UpdateAmenityRequest(); // UpdateAmenityRequest | UpdateAmenityRequest update amenity
    try {
      apiInstance.updateAmenity(amenityId, updateAmenityRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling AmenitiesApi#updateAmenity");
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
 **updateAmenityRequest** | [**UpdateAmenityRequest**](UpdateAmenityRequest.md)| UpdateAmenityRequest update amenity |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If updated successfully |  -  |
**400** | If amenity is not found |  -  |

