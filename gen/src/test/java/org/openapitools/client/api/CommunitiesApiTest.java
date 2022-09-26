/*
 * Swagger MyHome - OpenAPI 3.0
 * This is a OpenApi specification for MyHome backend service.
 *
 * The version of the OpenAPI document: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.AddCommunityAdminRequest;
import org.openapitools.client.model.AddCommunityAdminResponse;
import org.openapitools.client.model.CreateCommunityRequest;
import org.openapitools.client.model.CreateCommunityResponse;
import org.openapitools.client.model.GetCommunityDetailsResponse;
import org.openapitools.client.model.ListCommunityAdminsResponse;
import org.openapitools.client.model.Pageable;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CommunitiesApi
 */
@Ignore
public class CommunitiesApiTest {

    private final CommunitiesApi api = new CommunitiesApi();

    
    /**
     * 
     *
     * Add a new admin to the community given a community id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void addCommunityAdminsTest() throws ApiException {
        String communityId = null;
        AddCommunityAdminRequest addCommunityAdminRequest = null;
        AddCommunityAdminResponse response = api.addCommunityAdmins(communityId, addCommunityAdminRequest);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Create a new community
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createCommunityTest() throws ApiException {
        CreateCommunityRequest createCommunityRequest = null;
        CreateCommunityResponse response = api.createCommunity(createCommunityRequest);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Deletion community with given community id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteCommunityTest() throws ApiException {
        String communityId = null;
        api.deleteCommunity(communityId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * List all communities which are registered
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void listAllCommunityTest() throws ApiException {
        Pageable pageable = null;
        GetCommunityDetailsResponse response = api.listAllCommunity(pageable);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * List all admins of the community given a community id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void listCommunityAdminsTest() throws ApiException {
        String communityId = null;
        Pageable pageable = null;
        ListCommunityAdminsResponse response = api.listCommunityAdmins(communityId, pageable);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Get details about the community given a community id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void listCommunityDetailsTest() throws ApiException {
        String communityId = null;
        GetCommunityDetailsResponse response = api.listCommunityDetails(communityId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Remove of admin associated with a community
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void removeAdminFromCommunityTest() throws ApiException {
        String communityId = null;
        String adminId = null;
        api.removeAdminFromCommunity(communityId, adminId);

        // TODO: test validations
    }
    
}
