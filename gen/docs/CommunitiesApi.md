# CommunitiesApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addCommunityAdmins**](CommunitiesApi.md#addCommunityAdmins) | **POST** /communities/{communityId}/admins | 
[**createCommunity**](CommunitiesApi.md#createCommunity) | **POST** /communities | 
[**deleteCommunity**](CommunitiesApi.md#deleteCommunity) | **DELETE** /communities/{communityId} | 
[**listAllCommunity**](CommunitiesApi.md#listAllCommunity) | **GET** /communities | 
[**listCommunityAdmins**](CommunitiesApi.md#listCommunityAdmins) | **GET** /communities/{communityId}/admins | 
[**listCommunityDetails**](CommunitiesApi.md#listCommunityDetails) | **GET** /communities/{communityId} | 
[**removeAdminFromCommunity**](CommunitiesApi.md#removeAdminFromCommunity) | **DELETE** /communities/{communityId}/admins/{adminId} | 


<a name="addCommunityAdmins"></a>
# **addCommunityAdmins**
> AddCommunityAdminResponse addCommunityAdmins(communityId, addCommunityAdminRequest)



Add a new admin to the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    AddCommunityAdminRequest addCommunityAdminRequest = new AddCommunityAdminRequest(); // AddCommunityAdminRequest | AddCommunityAdminRequest aggregate fields
    try {
      AddCommunityAdminResponse result = apiInstance.addCommunityAdmins(communityId, addCommunityAdminRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#addCommunityAdmins");
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
 **addCommunityAdminRequest** | [**AddCommunityAdminRequest**](AddCommunityAdminRequest.md)| AddCommunityAdminRequest aggregate fields |

### Return type

[**AddCommunityAdminResponse**](AddCommunityAdminResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If admins were created |  -  |
**404** | If params are invalid |  -  |

<a name="createCommunity"></a>
# **createCommunity**
> CreateCommunityResponse createCommunity(createCommunityRequest)



Create a new community

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    CreateCommunityRequest createCommunityRequest = new CreateCommunityRequest(); // CreateCommunityRequest | CreateCommunityRequest aggregate fields
    try {
      CreateCommunityResponse result = apiInstance.createCommunity(createCommunityRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#createCommunity");
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
 **createCommunityRequest** | [**CreateCommunityRequest**](CreateCommunityRequest.md)| CreateCommunityRequest aggregate fields |

### Return type

[**CreateCommunityResponse**](CreateCommunityResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | If community was created |  -  |

<a name="deleteCommunity"></a>
# **deleteCommunity**
> deleteCommunity(communityId)



Deletion community with given community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    try {
      apiInstance.deleteCommunity(communityId);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#deleteCommunity");
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

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If community was removed |  -  |
**404** | If params are invalid |  -  |

<a name="listAllCommunity"></a>
# **listAllCommunity**
> GetCommunityDetailsResponse listAllCommunity(pageable)



List all communities which are registered

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      GetCommunityDetailsResponse result = apiInstance.listAllCommunity(pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#listAllCommunity");
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

[**GetCommunityDetailsResponse**](GetCommunityDetailsResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns list of communities |  -  |

<a name="listCommunityAdmins"></a>
# **listCommunityAdmins**
> ListCommunityAdminsResponse listCommunityAdmins(communityId, pageable)



List all admins of the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    Pageable pageable = new Pageable(); // Pageable | 
    try {
      ListCommunityAdminsResponse result = apiInstance.listCommunityAdmins(communityId, pageable);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#listCommunityAdmins");
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

[**ListCommunityAdminsResponse**](ListCommunityAdminsResponse.md)

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

<a name="listCommunityDetails"></a>
# **listCommunityDetails**
> GetCommunityDetailsResponse listCommunityDetails(communityId)



Get details about the community given a community id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    try {
      GetCommunityDetailsResponse result = apiInstance.listCommunityDetails(communityId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#listCommunityDetails");
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

[**GetCommunityDetailsResponse**](GetCommunityDetailsResponse.md)

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

<a name="removeAdminFromCommunity"></a>
# **removeAdminFromCommunity**
> removeAdminFromCommunity(communityId, adminId)



Remove of admin associated with a community

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CommunitiesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CommunitiesApi apiInstance = new CommunitiesApi(defaultClient);
    String communityId = "communityId_example"; // String | 
    String adminId = "adminId_example"; // String | 
    try {
      apiInstance.removeAdminFromCommunity(communityId, adminId);
    } catch (ApiException e) {
      System.err.println("Exception when calling CommunitiesApi#removeAdminFromCommunity");
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
 **adminId** | **String**|  |

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
**204** | If admin was removed |  -  |
**404** | If params are invalid |  -  |

