package com.example.debri_lize.service

import android.util.Log
import com.example.debri_lize.utils.RetrofitInterface
import com.example.debri_lize.base.BaseResponse
import com.example.debri_lize.data.class_.*
import com.example.debri_lize.utils.getJwt
import com.example.debri_lize.utils.getRetrofit
import com.example.debri_lize.view.class_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassService {
    private lateinit var lectureFavoriteView: LectureFavoriteView
    private lateinit var lectureFilterView: LectureFilterView

    private lateinit var createLectureScrapView: CreateLectureScrapView
    private lateinit var cancelLectureScrapView: CancelLectureScrapView

    private lateinit var showLectureDetailView: ShowLectureDetailView

    private lateinit var createLectureLikeView: CreateLectureLikeView
    private lateinit var deleteLectureLikeView: DeleteLectureLikeView


    fun setLectureFavoriteView(lectureFavoriteView: LectureFavoriteView){
        this.lectureFavoriteView = lectureFavoriteView
    }

    fun setLectureFilterView(lectureFilterView: LectureFilterView){
        this.lectureFilterView = lectureFilterView
    }

    fun setCreateLectureScrapView(createLectureScrapView: CreateLectureScrapView){
        this.createLectureScrapView = createLectureScrapView
    }

    fun setCancelLectureScrapView(cancelLectureScrapView: CancelLectureScrapView){
        this.cancelLectureScrapView = cancelLectureScrapView
    }

    fun setShowLectureDetailView(showLectureDetailView: ShowLectureDetailView){
        this.showLectureDetailView = showLectureDetailView
    }

    fun setCreateLectureLikeView(createLectureLikeView: CreateLectureLikeView){
        this.createLectureLikeView = createLectureLikeView
    }

    fun setDeleteLectureLikeView(deleteLectureLikeView: DeleteLectureLikeView){
        this.deleteLectureLikeView = deleteLectureLikeView
    }


    fun showLectureFavorite(userIdx : Int){
        Log.d("lecturefavorite", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.showLectureFavorite(userIdx, getJwt()!!).enqueue(object: Callback<BaseResponse<List<Lecture>>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<List<Lecture>>>, response: Response<BaseResponse<List<Lecture>>>) {
                Log.d("lecturefavorite", "response")
                val resp: BaseResponse<List<Lecture>> = response.body()!!
                Log.d("lecturefavoriteCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->lectureFavoriteView.onLectureFavoriteSuccess(resp.code, resp.result)    //result를 받아서 UI를 구현해야함
                    else->lectureFavoriteView.onLectureFavoriteFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<List<Lecture>>>, t: Throwable) {
                Log.d("lecturefavoriteFail",t.toString())
            }
        })
    }

    fun showLectureSearch(lectureFilter: LectureFilter){
        Log.d("lecturefilter", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.showLectureSearch(lectureFilter.lang,lectureFilter.type,lectureFilter.price,lectureFilter.key,lectureFilter.pageNum, getJwt()!!).enqueue(object: Callback<BaseResponse<SearchLecture>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<SearchLecture>>, response: Response<BaseResponse<SearchLecture>>) {
                Log.d("lecturefilter", "response")
                Log.d("lecturefilterresponse",response.body().toString())
                val resp: BaseResponse<SearchLecture> = response.body()!!
                Log.d("lecturefilterCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->lectureFilterView.onLectureFilterSuccess(resp.code, resp.result)    //result를 받아서 UI를 구현해야함
                    else->lectureFilterView.onLectureFilterFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<SearchLecture>>, t: Throwable) {
                Log.d("lecturefilterFail",t.toString())
            }
        })
    }

    fun createLectureScrap(lectureScrap: LectureScrap){
        Log.d("createLectureScrap", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.createLectureScrap(lectureScrap, getJwt()!!).enqueue(object: Callback<BaseResponse<String>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                Log.d("createLectureScrap", "response")
                val resp: BaseResponse<String> = response.body()!!
                Log.d("createLectureScrapCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->createLectureScrapView.onCreateLectureScrapSuccess(resp.code)    //result를 받아서 UI를 구현해야함
                    else->createLectureScrapView.onCreateLectureScrapFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.d("createLectureScrapFail",t.toString())
            }
        })
    }

    fun cancelLectureScrap(lectureScrap: LectureScrap){
        Log.d("cancelLectureScrap", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.cancelLectureScrap(lectureScrap, getJwt()!!).enqueue(object: Callback<BaseResponse<String>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                Log.d("cancelLectureScrap", "response")
                val resp: BaseResponse<String> = response.body()!!
                Log.d("cancelLectureScrapCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->cancelLectureScrapView.onCancelLectureScrapSuccess(resp.code)    //result를 받아서 UI를 구현해야함
                    else->cancelLectureScrapView.onCancelLectureScrapFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.d("cancelLectureScrapFail",t.toString())
            }
        })
    }

    fun showLectureDetail(lectureIdx: Int){
        Log.d("showLectureDetail", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.showLectureDetail(lectureIdx, getJwt()!!).enqueue(object: Callback<BaseResponse<Lecture>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<Lecture>>, response: Response<BaseResponse<Lecture>>) {
                Log.d("showLectureDetail", "response")
                val resp: BaseResponse<Lecture> = response.body()!!
                Log.d("showLectureDetailCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->showLectureDetailView.onShowLectureDetailSuccess(resp.code, resp.result)    //result를 받아서 UI를 구현해야함
                    else->showLectureDetailView.onShowLectureDetailFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<Lecture>>, t: Throwable) {
                Log.d("showLectureDetailFail",t.toString())
            }
        })
    }

    fun createLectureLike(lectureIdx: Int){
        Log.d("createLectureLike", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.createLectureLike(lectureIdx, getJwt()!!).enqueue(object: Callback<BaseResponse<LikeSuccess>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<LikeSuccess>>, response: Response<BaseResponse<LikeSuccess>>) {
                Log.d("createLectureLike", "response")
                val resp: BaseResponse<LikeSuccess> = response.body()!!
                Log.d("createLectureLikeCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->createLectureLikeView.onCreateLectureLikeSuccess(resp.code)    //result를 받아서 UI를 구현해야함
                    else->createLectureLikeView.onCreateLectureLikeFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<LikeSuccess>>, t: Throwable) {
                Log.d("createLectureLikeFail",t.toString())
            }
        })
    }

    fun deleteLectureLike(lectureIdx: Int){
        Log.d("deleteLectureLike", "enter")
        val classService = getRetrofit().create(RetrofitInterface::class.java)
        classService.deleteLectureLike(lectureIdx, getJwt()!!).enqueue(object: Callback<BaseResponse<LikeSuccess>> {
            //응답이 왔을 때 처리
            override fun onResponse(call: Call<BaseResponse<LikeSuccess>>, response: Response<BaseResponse<LikeSuccess>>) {
                Log.d("deleteLectureLike", "response")
                val resp: BaseResponse<LikeSuccess> = response.body()!!
                Log.d("deleteLectureLikeCode", resp.code.toString())
                when(resp.code){
                    //API code값 사용
                    200->deleteLectureLikeView.onDeleteLectureLikeSuccess(resp.code)    //result를 받아서 UI를 구현해야함
                    else->deleteLectureLikeView.onDeleteLectureLikeFailure(resp.code)   //무슨 오류인지 알아야하므로 code가져가기
                }
            }
            //실패했을 때 처리
            override fun onFailure(call: Call<BaseResponse<LikeSuccess>>, t: Throwable) {
                Log.d("deleteLectureLikeFail",t.toString())
            }
        })
    }

}