package com.androiddesdecero.rxretrofitudemy.api;

import com.androiddesdecero.rxretrofitudemy.model.Contributor;
import com.androiddesdecero.rxretrofitudemy.model.GitHubRepo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServiceApi {

    //Sin Rx
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>> getReposForUser(@Path("user") String user);

    //Con Rx
    @GET("/users/{user}/repos")
    Single<List<GitHubRepo>> getReposForUserRx(@Path("user") String user);

    //Con Rx
    @GET("/repos/{user}/{repo}/contributors")
    Observable<List<Contributor>> getReposContriForUserRx(@Path("user") String user, @Path("repo") String repo);
}
