package com.push.redditing.datalayer.repository;

import android.support.annotation.NonNull;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import io.reactivex.Flowable;
import net.dean.jraw.RedditClient;

import javax.inject.Inject;

public class OAuthRepository {

    ApiService apiService;

    public  OAuthRepository(ApiService apiService){
        this.apiService = apiService;
    }


    public String getOAuthUrl(){
        return  apiService.getAuthorizationUlr();
    }

    public RedditClient getAuthorizeClient(@NonNull String callBackUrl ) {
        return apiService.getOAuthtoken(callBackUrl);
    }
}
