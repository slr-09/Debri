package com.example.debri_lize.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debri_lize.R
import com.example.debri_lize.activity.MainActivity
import com.example.debri_lize.activity.auth.ProfileActivity
import com.example.debri_lize.adapter.home.ChapterRVAdapter
import com.example.debri_lize.adapter.home.LectureRVAdapter
import com.example.debri_lize.data.curriculum.*
import com.example.debri_lize.databinding.FragmentHomeBinding
import com.example.debri_lize.service.CurriculumService
import com.example.debri_lize.view.curriculum.DeleteCurriculumView
import com.example.debri_lize.view.curriculum.MyCurriculumListView
import com.example.debri_lize.view.curriculum.ShowCurriculumDetailView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class HomeFragment : Fragment(), MyCurriculumListView, ShowCurriculumDetailView, DeleteCurriculumView {

    lateinit var context: MainActivity

    lateinit var binding: FragmentHomeBinding
    lateinit var chapterRVAdapter: ChapterRVAdapter
    lateinit var lectureRVAdapter: LectureRVAdapter

    var arrayImg = arrayOf(R.drawable.ic_lecture_green, R.drawable.ic_lecture_purple, R.drawable.ic_lecture_red)

    val chapter = ArrayList<ChapterList>()
    val lecture = ArrayList<LectureList>()

    val myCurriculum = ArrayList<Curriculum>() //my curriculum list
    private var curriculumIdx by Delegates.notNull<Int>()
    private lateinit var status : String

    //api
    val curriculumService = CurriculumService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        //click userImg -> profile
        binding.homeDebriUserIv.setOnClickListener{
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }

        //bottomSheet
        bottomSheetSetting()

        //click next -> AddCurriculumFragment : 수정예정
        binding.homeCurriculumNextIv.setOnClickListener{
            val passBundleBFragment = AddCurriculumFragment()
            //fragment to fragment
            activity?.supportFragmentManager!!.beginTransaction()
                .replace(R.id.main_frm, passBundleBFragment)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        //api - 8.2 커리큘럼 리스트 조회 api : 내가 추가한 커리큘럼들
        curriculumService.setMyCurriculumListView(this)
        curriculumService.myCurriculumList()
    }

    //context 받아오기기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity
    }

    //bottom sheet
    private fun bottomSheetSetting(){

        lateinit var bottomSheetView : View
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetView = layoutInflater.inflate(R.layout.fragment_bottom_sheet_four, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        if(status=="INACTIVE"){ //현재 커리큘럼 : 비공개 

            bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv1).text = "공개로 전환하기"

            //비공개 -> 공개
            bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv1).setOnClickListener {
                binding.homeCurriculumHideTv.text = "공개 중"
                binding.homeCurriculumHideIv.setImageResource(R.drawable.ic_open)
                bottomSheetDialog.dismiss()
            }


        }
        else{ //현재 커리큘럼 : 공개
            bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv1).text = "비공개로 전환하기"


        }

        //커리큘럼 이름 변경하기
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv2).setOnClickListener {
            //add dialog code

            bottomSheetDialog.dismiss()
        }

        //커리큘럼 초기화하기
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv3).setOnClickListener {
            //add dialog code

            bottomSheetDialog.dismiss()
        }

        //커리큘럼 삭제하기
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_four_tv4).setOnClickListener {
            //add dialog code

            //api - 8.6 커리큘럼 삭제 api
            curriculumService.setDeleteCurriculumView(this)
            curriculumService.deleteCurriculum(curriculumIdx)

            bottomSheetDialog.dismiss()
        }

        binding.homeCurriculumSettingLayout.setOnClickListener {
            bottomSheetDialog.show()
        }

        //close button
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_close_tv).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    //timestamp to date
    private fun timestampToDate(timestamp: Timestamp): String? {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일")
        val date = sdf.format(timestamp)

        return date+"에 완성함"
    }

    private fun waveAnimation(progressRate : Int){
        binding.waveLoadingView.setProgressValue(progressRate);
        binding.waveLoadingView.setAnimDuration(3000);
        binding.waveLoadingView.startAnimation()
    }

    //api
    //8.2 커리큘럼 리스트 조회 api : 내가 추가한 커리큘럼들 (데이터만 사용)
    override fun onMyCurriculumListSuccess(code: Int, result: List<Curriculum>) {
        when(code){
            200->{
                for(i in result){
                    myCurriculum.add(Curriculum(i.curriculumIdx, i.curriculumName, i.curriculumAuthor))
                }

                //나의 커리큘럼이 없으면,
                if(myCurriculum.size==0){

                    val passBundleBFragment = AddCurriculumFragment()
                    //fragment to fragment
                    activity?.supportFragmentManager!!.beginTransaction()
                        .replace(R.id.main_frm, passBundleBFragment)
                        .commit()

                }else{ //있으면

                    //api - 8.3 커리큘럼 상세 조회 api : 홈
                    curriculumService.setShowCurriculumDetailView(this)
                    curriculumService.showCurriculumDetail(result[0].curriculumIdx)

                }
            }
        }
    }

    override fun onMyCurriculumListFailure(code: Int) {

    }

    //8.3 커리큘럼 상세 조회 api : 홈
    override fun onShowCurriculumDetailSuccess(code: Int, result: CurriculumDetail) {
        when(code){
            200->{
                if(myCurriculum.size==1){
                    //커리큘럼 개수 1개
                    if(myCurriculum[0].curriculumIdx==result.curriculumIdx){
                        //첫번째 커리큘럼일 경우
                        binding.homeCurriculumPreviousIv.visibility = View.INVISIBLE
                        binding.homeCurriculumNextIv.setOnClickListener{
                            val passBundleBFragment = AddCurriculumFragment()
                            //fragment to fragment
                            activity?.supportFragmentManager!!.beginTransaction()
                                .replace(R.id.main_frm, passBundleBFragment)
                                .commit()
                        }
                    }
                }else{
                    //커리큘럼 개수 2개 이상
                    //else if 구문으로 수정
                    if(myCurriculum[0].curriculumIdx==result.curriculumIdx){
                        //첫번째 커리큘럼일 경우
                        binding.homeCurriculumPreviousIv.visibility = View.INVISIBLE
                        binding.homeCurriculumNextIv.setOnClickListener{
                            curriculumService.showCurriculumDetail(result.curriculumIdx+1)
                        }
                    }

                    if(myCurriculum[myCurriculum.size-1].curriculumIdx==result.curriculumIdx){
                        //마지막 커리큘럼일 경우
                        binding.homeCurriculumPreviousIv.visibility = View.VISIBLE
                        binding.homeCurriculumPreviousIv.setOnClickListener{
                            curriculumService.showCurriculumDetail(result.curriculumIdx-1)
                        }
                        binding.homeCurriculumNextIv.setOnClickListener{
                            val passBundleBFragment = AddCurriculumFragment()
                            //fragment to fragment
                            activity?.supportFragmentManager!!.beginTransaction()
                                .replace(R.id.main_frm, passBundleBFragment)
                                .commit()
                        }
                    }else{
                        //중간 커리큘럼일 경우
                        binding.homeCurriculumPreviousIv.visibility = View.VISIBLE
                        binding.homeCurriculumPreviousIv.setOnClickListener{
                            curriculumService.showCurriculumDetail(result.curriculumIdx-1)
                        }
                        binding.homeCurriculumNextIv.setOnClickListener{
                            curriculumService.showCurriculumDetail(result.curriculumIdx+1)
                        }
                    }
                }

                //홈 화면
                curriculumIdx = result.curriculumIdx
                binding.homeCurriculumTitleTv.text = result.curriculumName //커리큘럼 이름
                binding.homeCurriculumDateTv.text = timestampToDate(result.createdAt) //커리큘럼 생성 날짜

                //공개 or 비공개
                if(result.visibleStatus=="ACTIVE"){ //공개
                    binding.homeCurriculumHideIv.setImageResource(R.drawable.ic_open)
                    binding.homeCurriculumHideTv.text = "공개 중"
                }else{ //비공개
                    binding.homeCurriculumHideIv.setImageResource(R.drawable.ic_hide)
                    binding.homeCurriculumHideTv.text = "비공개"
                }

                //dday

                //progress rate
                waveAnimation(result.progressRate.toInt())
                binding.homeCurriculumProgressTv2.text = result.progressRate.toString()

                //활성 or 비활성화
                status = result.status
                if(result.status=="ACTIVE"){
                    //활성화
                    binding.homeCurriculumLectureImgRv.visibility = View.VISIBLE
                    binding.borderLine.visibility = View.VISIBLE
                    binding.homeCurriculumActiveCircle.visibility = View.VISIBLE
                    binding.homeCurriculumInactiveLayout.visibility = View.GONE
                    binding.homeCurriculumInactiveCircle.visibility = View.GONE

                    binding.homeCurriculumLectureImgRv.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    chapterRVAdapter = ChapterRVAdapter()
                    binding.homeCurriculumLectureImgRv.adapter = chapterRVAdapter

                    chapter.clear()

                    chapter.apply {

                        for((j, i) in result.chapterListResList!!.withIndex()){
                            //else if로 변경
                            if(j%3==0){
                                chapter.add(ChapterList(i.chIdx,i.chName,i.chNum,i.language,i.chComplete,i.progressOrder,i.completeChNum,arrayImg[0]))
                            }
                            if(j%3==1){
                                chapter.add(ChapterList(i.chIdx,i.chName,i.chNum,i.language,i.chComplete,i.progressOrder,i.completeChNum,arrayImg[0]))
                            }
                            if(j%3==2){
                                chapter.add(ChapterList(i.chIdx,i.chName,i.chNum,i.language,i.chComplete,i.progressOrder,i.completeChNum,arrayImg[0]))
                            }
                        }

                        chapterRVAdapter.datas = chapter
                        chapterRVAdapter.notifyDataSetChanged()

                        //click recyclerview item
                        chapterRVAdapter.setItemClickListener(object : ChapterRVAdapter.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {


                            }
                        })
                    }
                }else{
                    //비활성화
                    binding.homeCurriculumLectureImgRv.visibility = View.GONE
                    binding.borderLine.visibility = View.GONE
                    binding.homeCurriculumActiveCircle.visibility = View.GONE
                    binding.homeCurriculumInactiveCircle.visibility = View.VISIBLE
                    binding.homeCurriculumInactiveLayout.visibility = View.VISIBLE

                }

                //관련 강의자료
                binding.homeCurriculumLectureRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                lectureRVAdapter = LectureRVAdapter()
                binding.homeCurriculumLectureRv.adapter = lectureRVAdapter

                lecture.clear()

                lecture.apply {
                    for(i in result.lectureListResList){
                        lecture.add(LectureList(i.lectureIdx,i.lectureName,i.language,i.chNum,i.progressRate))
                    }

                    lectureRVAdapter.datas = lecture
                    lectureRVAdapter.notifyDataSetChanged()

                    //click recyclerview item
                    lectureRVAdapter.setItemClickListener(object : LectureRVAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {


                        }
                    })
                }


            }
        }
    }

    override fun onShowCurriculumDetailFailure(code: Int) {

    }

    override fun onDeleteCurriculumViewSuccess(code: Int) {
        when(code){
            200->{
                Toast.makeText(context, "커리큘럼 삭제", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDeleteCurriculumViewFailure(code: Int) {

    }




}