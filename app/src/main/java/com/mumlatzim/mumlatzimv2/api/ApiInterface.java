package com.mumlatzim.mumlatzimv2.api;

import com.mumlatzim.mumlatzimv2.models.category.Category;
import com.mumlatzim.mumlatzimv2.models.category.Tags;
import com.mumlatzim.mumlatzimv2.models.comment.Comments;
import com.mumlatzim.mumlatzimv2.models.post.Post;
import com.mumlatzim.mumlatzimv2.models.post.PostDetails;
import com.mumlatzim.mumlatzimv2.models.post.PostReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET(HttpParams.API_CATEGORIES)
    Call<List<Category>> getCategories(@Query(HttpParams.PER_PAGE) int itemCount);

    @GET(HttpParams.API_TAGS)
    Call<List<Tags>> getTags(@Query(HttpParams.PAGE) int PageNo);

    @GET(HttpParams.API_POSTS)
    Call<List<Post>> getLatestPosts(@Query(HttpParams.PAGE) int pageNo);

    @GET(HttpParams.API_FEATURED_POSTS)
    Call<List<Post>> getFeaturedPosts(@Query(HttpParams.PAGE) int pageNo);

    @GET(HttpParams.API_POSTS)
    Call<List<Post>> getPostsByCategory(@Query(HttpParams.PAGE) int pageNo, @Query(HttpParams.CATEGORIES) int categoryId);

    @GET(HttpParams.API_POSTS)
    Call<List<Post>> getPostsByTags(@Query(HttpParams.PAGE) int pageNo, @Query(HttpParams.TAGS) int tagsId);

    @GET(HttpParams.API_POST_DETAILS)
    Call<PostDetails> getPostDetails(@Path(HttpParams.ID) int postId);

    @GET(HttpParams.API_POSTS)
    Call<List<Post>> getSearchedPosts(@Query(HttpParams.PAGE) int pageNo, @Query(HttpParams.SEARCH) String searchedText);

    @GET
    Call<List<Comments>> getComments(@Url String url, @Query(HttpParams.PER_PAGE) int pageCount);


    @FormUrlEncoded
    @POST(HttpParams.API_REPORT)
    Call<PostReport> getPostReport(
                                   @Field(HttpParams.REPORT_ACTION) String action,
                                   @Field(HttpParams.ID) int reportId,
                                   @Field(HttpParams.REPORT_REASON) String reason,
                                   @Field(HttpParams.REPORT_EMAIL) String email,
                                   @Field(HttpParams.REPORT_REPORTER) String reporter);


    @FormUrlEncoded
    @POST(HttpParams.API_COMMENT)
    Call<String> postComment(@Field(HttpParams.COMMENT_AUTHOR_NAME) String name,
                             @Field(HttpParams.COMMENT_AUTHOR_EMAIL) String email,
                             @Field(HttpParams.COMMENT_CONTENT) String content,
                             @Query(HttpParams.POST) int postID);


}
