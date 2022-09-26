# HousesApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addCommunityHouses**](HousesApi.md#addCommunityHouses) | **POST** /communities/{communityId}/houses | 
[**addHouseMembers**](HousesApi.md#addHouseMembers) | **POST** /houses/{houseId}/members | 
[**deleteHouseMember**](HousesApi.md#deleteHouseMember) | **DELETE** /houses/{houseId}/members/{memberId} | 
[**getHouseDetails**](HousesApi.md#getHouseDetails) | **GET** /houses/{houseId} | 
[**listAllHouses**](HousesApi.md#listAllHouses) | **GET** /houses | 
[**listAllMembersOfHouse**](HousesApi.md#listAllMembersOfHouse) | **GET** /houses/{houseId}/members | 
[**listCommunityHouses**](HousesApi.md#listCommunityHouses) | **GET** /communities/{communityId}/houses | 
[**removeCommunityHouse**](HousesApi.md#removeCommunityHouse) | **DELETE** /communities/{communityId}/houses/{houseId} | 


<a name="addCommunityHouses"></a>
# **addCommunityHouses**
> AddCommunityHouseResponse addCommunityHouses(communityId, addCommunityHouseRequest)



Add a new house to the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    AddCommunityHouseRequest addCommunityHouseRequest = new AddCommunityHouseRequest(); // AddCommunityHouseRequest | AddCommunityHouseRequest aggregate fields
    try {
      AddCommunityHouseResponse result = apiInstance.addCommunityHouses(communityId, addCommunityHouseRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#addCommunityHouses");
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
 **addCommunityHouseRequest** | [**AddCommunityHouseRequest**](AddCommunityHouseRequest.md)| AddCommunityHouseRequest aggregate fields |

### Return type

[**AddCommunityHouseResponse**](AddCommunityHouseResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If houses were added |  -  |
**400** | If params are invalid |  -  |

<a name="addHouseMembers"></a>
# **addHouseMembers**
> AddHouseMemberResponse addHouseMembers(houseId, addHouseMemberRequest)



Add new members to the house given a house id. Responds with member id which were added

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String houseId = "houseId_example"; // String | 
    AddHouseMemberRequest addHouseMemberRequest = new AddHouseMemberRequest(); // AddHouseMemberRequest | AddHouseMemberRequest aggregate fields
    try {
      AddHouseMemberResponse result = apiInstance.addHouseMembers(houseId, addHouseMemberRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#addHouseMembers");
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
 **houseId** | **String**|  |
 **addHouseMemberRequest** | [**AddHouseMemberRequest**](AddHouseMemberRequest.md)| AddHouseMemberRequest aggregate fields |

### Return type

[**AddHouseMemberResponse**](AddHouseMemberResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | If members were added to house |  -  |
**404** | If parameters are invalid |  -  |

<a name="deleteHouseMember"></a>
# **deleteHouseMember**
> deleteHouseMember(houseId, memberId)



Deletion of member associated with a house

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String houseId = "houseId_example"; // String | 
    String memberId = "memberId_example"; // String | 
    try {
      apiInstance.deleteHouseMember(houseId, memberId);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#deleteHouseMember");
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
 **houseId** | **String**|  |
 **memberId** | **String**|  |

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
**204** | If house member was removed from house |  -  |
**400** | If params are invalid |  -  |

<a name="getHouseDetails"></a>
# **getHouseDetails**
> GetHouseDetailsResponse getHouseDetails(houseId)



Get house detail by a given ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String houseId = "houseId_example"; // String | ID of the house to get
    try {
      GetHouseDetailsResponse result = apiInstance.getHouseDetails(houseId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#getHouseDetails");
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
 **houseId** | **String**| ID of the house to get |

### Return type

[**GetHouseDetailsResponse**](GetHouseDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If house present |  -  |
**404** | If params are invalid |  -  |

<a name="listAllHouses"></a>
# **listAllHouses**
> GetHouseDetailsResponse listAllHouses(pageable)



List all houses of the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      GetHouseDetailsResponse result = apiInstance.listAllHouses(pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#listAllHouses");
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

[**GetHouseDetailsResponse**](GetHouseDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If community exists |  -  |

<a name="listAllMembersOfHouse"></a>
# **listAllMembersOfHouse**
> ListHouseMembersResponse listAllMembersOfHouse(houseId, pageable)



List all members of the house given a house id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String houseId = "houseId_example"; // String | 
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      ListHouseMembersResponse result = apiInstance.listAllMembersOfHouse(houseId, pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#listAllMembersOfHouse");
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
 **houseId** | **String**|  |
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
**200** | If house present |  -  |
**404** | If params are invalid |  -  |

<a name="listCommunityHouses"></a>
# **listCommunityHouses**
> GetHouseDetailsResponse listCommunityHouses(communityId, pageable)



List all houses of the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      GetHouseDetailsResponse result = apiInstance.listCommunityHouses(communityId, pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#listCommunityHouses");
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
 **pageable** | [**Pageable**](.md)|  | [optional]

### Return type

[**GetHouseDetailsResponse**](GetHouseDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If community exists |  -  |
**404** | If params are invalid |  -  |

<a name="removeCommunityHouse"></a>
# **removeCommunityHouse**
> removeCommunityHouse(communityId, houseId)



Remove of house from the community given a community id and a house id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.HousesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    HousesApi apiInstance = new HousesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    String houseId = "houseId_example"; // String | 
    try {
      apiInstance.removeCommunityHouse(communityId, houseId);
    } catch (ApiException e) {
      System.err.println("Exception when calling HousesApi#removeCommunityHouse");
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
 **houseId** | **String**|  |

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
**204** | If house was removed |  -  |
**400** | If params are invalid |  -  |

