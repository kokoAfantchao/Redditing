package com.push.apiwrapper.oauth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OAuthService {

  @POST("/access_token")
  Call<Token> getUserLessToken(@Query("grant_type") String grantType,
                               @Query("Client_id") String clientId);

  @POST("/access_token")
  Call<Token> getToken(@Query("grant_type") String grantTyp,
                       @Query("code") String code,
                       @Query("redirect_uri") String redirect_Uri);


}
