# DocumentsApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteHouseMemberDocument**](DocumentsApi.md#deleteHouseMemberDocument) | **DELETE** /members/{memberId}/documents | 
[**getHouseMemberDocument**](DocumentsApi.md#getHouseMemberDocument) | **GET** /members/{memberId}/documents | 
[**updateHouseMemberDocument**](DocumentsApi.md#updateHouseMemberDocument) | **PUT** /members/{memberId}/documents | 
[**uploadHouseMemberDocument**](DocumentsApi.md#uploadHouseMemberDocument) | **POST** /members/{memberId}/documents | 


<a name="deleteHouseMemberDocument"></a>
# **deleteHouseMemberDocument**
> deleteHouseMemberDocument(memberId)



Delete house member&#39;s documents

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DocumentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    DocumentsApi apiInstance = new DocumentsApi(defaultClient);
    String memberId = "memberId_example"; // String | 
    try {
      apiInstance.deleteHouseMemberDocument(memberId);
    } catch (ApiException e) {
      System.err.println("Exception when calling DocumentsApi#deleteHouseMemberDocument");
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
**204** | If document deleted |  -  |
**404** | If params are invalid |  -  |

<a name="getHouseMemberDocument"></a>
# **getHouseMemberDocument**
> byte[] getHouseMemberDocument(memberId)



Returns house member&#39;s documents

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DocumentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    DocumentsApi apiInstance = new DocumentsApi(defaultClient);
    String memberId = "memberId_example"; // String | 
    try {
      byte[] result = apiInstance.getHouseMemberDocument(memberId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DocumentsApi#getHouseMemberDocument");
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
 **memberId** | **String**|  |

### Return type

**byte[]**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: image/jpeg

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | If document present |  -  |
**404** | If params are invalid |  -  |

<a name="updateHouseMemberDocument"></a>
# **updateHouseMemberDocument**
> updateHouseMemberDocument(memberId, memberDocument)



Update house member&#39;s documents

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DocumentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    DocumentsApi apiInstance = new DocumentsApi(defaultClient);
    String memberId = "memberId_example"; // String | 
    File memberDocument = new File("/path/to/file"); // File | 
    try {
      apiInstance.updateHouseMemberDocument(memberId, memberDocument);
    } catch (ApiException e) {
      System.err.println("Exception when calling DocumentsApi#updateHouseMemberDocument");
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
 **memberId** | **String**|  |
 **memberDocument** | **File**|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If document updated |  -  |
**409** | If document update error |  -  |
**413** | If document file too large |  -  |
**404** | If params are invalid |  -  |

<a name="uploadHouseMemberDocument"></a>
# **uploadHouseMemberDocument**
> uploadHouseMemberDocument(memberId, memberDocument)



Add house member&#39;s documents

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DocumentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    DocumentsApi apiInstance = new DocumentsApi(defaultClient);
    String memberId = "memberId_example"; // String | 
    File memberDocument = new File("/path/to/file"); // File | 
    try {
      apiInstance.uploadHouseMemberDocument(memberId, memberDocument);
    } catch (ApiException e) {
      System.err.println("Exception when calling DocumentsApi#uploadHouseMemberDocument");
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
 **memberId** | **String**|  |
 **memberDocument** | **File**|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | If document saved |  -  |
**409** | If document save error |  -  |
**413** | If document file too large |  -  |
**404** | If params are invalid |  -  |

