package com.example.debri_lize.response

import com.google.gson.annotations.SerializedName

data class PostDetailResponse(
    @SerializedName(value = "isSuccess") val isSuccess:Boolean,
    @SerializedName(value = "returnCode") val code:Int,
    @SerializedName(value = "returnMsg") val message:String,
    @SerializedName(value = "result") val result: PostDetail,
)

data class PostDetail(
    @SerializedName(value = "boardIdx") var boardIdx : Int,
    @SerializedName(value = "postIdx") var postIdx : Int,
    @SerializedName(value = "authorIdx") var authorIdx : Int,
    @SerializedName(value = "authorName") var authorName : String,
    @SerializedName(value = "postName") var postName : String,
    @SerializedName(value = "likeNumber") var likeCnt : Int,
    @SerializedName(value = "userLike") var likeStatus : Boolean? = false,
    @SerializedName(value = "userScrap") var scrapStatus : Boolean? = false,
    @SerializedName(value = "commentNumber") var commentCnt : Int,
    @SerializedName(value = "timeAfterCreated") var timeAfterCreated : Int,
    @SerializedName(value = "contents") var postContents : String
)
