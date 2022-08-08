package com.example.debri_lize.data

import com.example.debri_lize.data.auth.UserLogin
import com.example.debri_lize.data.auth.UserSignup
import com.example.debri_lize.data.post.*
import com.example.debri_lize.data.post.Comment
import com.example.debri_lize.data.post.Post
import com.example.debri_lize.response.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    //token
    @PATCH("api/jwt/refresh")
    fun token(@Header("ACCESS-TOKEN") authToken: String, @Body refreshToken : String): Call<TokenResponse>

    //회원가입
    @POST("api/user/signUp")
    fun signUp(@Body user: UserSignup): Call<AuthResponse>

    //로그인
    @POST("api/auth/login")
    fun login(@Body user : UserLogin): Call<AuthResponse>

    //즐겨찾기 안한 게시판 보여주기
    @GET("api/board/unscrap/getList")
    fun showUnScrapBoardList(@Header("ACCESS-TOKEN") authToken: String) : Call<BoardResponse>

    //즐겨찾기 게시판 보여주기
    @GET("api/board/scrap/getList")
    fun showScrapBoardList(@Header("ACCESS-TOKEN") authToken: String) : Call<BoardResponse>

    //게시물 작성
    @POST("api/post/create")
    fun createPost(@Body post: Post, @Header("ACCESS-TOKEN") authToken: String): Call<PostResponse>

    //게시물 수정하기
    @PATCH("api/post/{postIdx}")
    fun editPost(@Body editPost : EditPost, @Path("postIdx") postIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //전체 게시판 모음
    @GET("api/board/allList")
    fun showBoardList(@Header("ACCESS-TOKEN") authToken: String) : Call<BoardResponse>

    //게시물 삭제
    @PATCH("api/post/{postIdx}/status")
    fun deletePost(@Path("postIdx") postIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //[전체 게시판] 게시물 조회
    @POST("api/post/getSearchList")
    fun showPostList(@Body keyword: String, @Header("ACCESS-TOKEN") authToken: String) : Call<PostResponse>

    //[특정 게시판] 게시물 조회
    @GET("api/post/getList/{boardIdx}")
    fun showEachPostList(@Path("boardIdx") boardIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<PostResponse>

    //게시물 상세 조회
    @GET("api/post/get/{postIdx}")
    fun showPostDetail(@Path("postIdx") postIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<PostDetailResponse>

    //댓글 작성
    @POST("api/comment/replyOnPost/create")
    fun createComment(@Body comment: Comment): Call<CommentResponse>

    //대댓글 작성
    @POST("api/comment/replyOnReply/create")
    fun createCocomment(@Body cocomment: Cocomment): Call<CommentResponse>

    //댓글, 대댓글 조회
    @GET("api/comment/get/{postIdx}")
    fun showComment(@Path("postIdx") postIdx: Int) : Call<CommentListResponse>

    //댓글, 대댓글 삭제
    @PATCH("api/comment/delete/{commentIdx}")
    fun deleteComment(@Path("commentIdx") commentIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<CommentResponse>

    //게시물 좋아요 생성
    @POST("api/post/like")
    fun createPostLike(@Body postLikeCreate: PostLikeCreate, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //게시물 좋아요 취소
    @PATCH("api/post/like/cancel")
    fun cancelPostLike(@Body postLikeCancel: PostLikeCancel, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //게시물 스크랩 생성
    @POST("api/post/scrap/{postIdx}")
    fun createPostScrap(@Path("postIdx") postIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //게시물 스크랩 해제
    @POST("api/post/unscrap/{postIdx}")
    fun cancelPostScrap(@Path("postIdx") postIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<DeletePostResponse>

    //즐겨찾는 강의 보여주기
    @GET("api/lecture/getScrapList/{userIdx}")
    fun showLectureFavorite(@Path("userIdx") userIdx: Int, @Header("ACCESS-TOKEN") authToken: String) : Call<LectureResponse>

    //검색 강의 보여주기
    @GET("api/lecture/search")
    fun showLectureSearch(@Query ("lang") lang:String?,@Query ("type") type:String?,@Query ("price") price:String?,@Query ("key") key:String?, @Header("ACCESS-TOKEN") authToken: String) : Call<LectureResponse>

    //게시물 신고하기
    @POST("api/report/postReport")
    fun reportPost(@Body report: ReportPost, @Header("ACCESS-TOKEN") authToken: String) : Call<ReportResponse>

    //댓글, 대댓글 신고하기
    @POST("api/report/commentReport")
    fun reportComment(@Body report: ReportComment, @Header("ACCESS-TOKEN") authToken: String) : Call<ReportResponse>

}