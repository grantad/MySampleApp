//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.16
//
package com.amazonaws.mobile;

import com.amazonaws.mobilehelper.config.AWSMobileHelperConfiguration;
import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub 437ec95d-cb70-4c06-a453-f62a20adeceb aws-my-sample-app-android-v0.16";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("us-east-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "us-east-1:edcc70fa-eb0d-46f2-ab4a-36511950a68e";
    // Google Client ID for Web application
    public static final String GOOGLE_CLIENT_ID =
        "219791913249-su9h5fqdhh2hesqj48s5to1vn2s5kf5b.apps.googleusercontent.com";
    // S3 BUCKET
    public static final String AMAZON_S3_USER_FILES_BUCKET =
        "comicgeek-userfiles-mobilehub-396237625";
    // S3 BUCKET REGION
    public static final Regions AMAZON_S3_USER_FILES_BUCKET_REGION =
        Regions.fromName("us-east-1");
    public static final Regions AMAZON_DYNAMODB_REGION =
       Regions.fromName("us-east-1");
    public static final String AMAZON_COGNITO_USER_POOL_ID =
        "us-east-1_ERJ5sQS6T";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_ID =
        "1k0pfi52kj4fal4e2erkrbo9kq";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_SECRET =
        "1kr09ne7dmrnl5ji3a397leplfmbrbke4i5g72q9dkvkvb7cnbqb";

    private static final AWSMobileHelperConfiguration helperConfiguration = new AWSMobileHelperConfiguration.Builder()
        .withCognitoRegion(AMAZON_COGNITO_REGION)
        .withCognitoIdentityPoolId(AMAZON_COGNITO_IDENTITY_POOL_ID)
        .withCognitoUserPool(AMAZON_COGNITO_USER_POOL_ID,
            AMAZON_COGNITO_USER_POOL_CLIENT_ID, AMAZON_COGNITO_USER_POOL_CLIENT_SECRET)
        .build();
    /**
     * @return the configuration for AWSKit.
     */
    public static AWSMobileHelperConfiguration getAWSMobileHelperConfiguration() {
        return helperConfiguration;
    }
}
