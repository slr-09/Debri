package com.example.debri_lize.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.debri_lize.R
import com.example.debri_lize.adapter.class_.ClassLectureRVAdapter
import com.example.debri_lize.data.class_.Lecture
import com.example.debri_lize.data.curriculum.CopyCurriculum
import com.example.debri_lize.data.curriculum.RoadMap
import com.example.debri_lize.data.curriculum.copyRoadMap
import com.example.debri_lize.databinding.ActivityAddRoadmapDetailBinding
import com.example.debri_lize.service.CurriculumService
import com.example.debri_lize.service.RoadMapService
import com.example.debri_lize.view.curriculum.CopyCurriculumView
import com.example.debri_lize.view.curriculum.CopyRoadMapView
import com.example.debri_lize.view.curriculum.ShowRoadMapDetailView
import kotlin.properties.Delegates

class AddRoadmapDetailActivity : AppCompatActivity(), ShowRoadMapDetailView, CopyCurriculumView, CopyRoadMapView {
    lateinit var binding : ActivityAddRoadmapDetailBinding

    lateinit var classLectureRVAdapter: ClassLectureRVAdapter

    var roadMapIdx by Delegates.notNull<Int>()
    val lecture = ArrayList<Lecture>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoadmapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.raw.curriculum).into(binding.itemCurriculumStatusIv)

        //backbtn
        binding.profileCurriculumNextIv.setOnClickListener{
            finish()
        }

        //lecture recycler view
        binding.addRoadmapLectureRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        classLectureRVAdapter = ClassLectureRVAdapter()
        binding.addRoadmapLectureRv.adapter = classLectureRVAdapter

        //data : AddCurriculumChooseActivity -> AddRoadMapDetailActivity
        val intent = intent //전달할 데이터를 받을 Intent
        roadMapIdx = intent.getIntExtra("roadMapIdx", 0)

        //api - 7.5.1 로드맵 상세 조회 api
        var roadMapService = RoadMapService()
        roadMapService.setShowRoadMapDetailView(this)
        if(roadMapIdx==1){ //server
            roadMapService.showRoadMapDetail("server")
        }else{ //front
            roadMapService.showRoadMapDetail("front")
        }

        //수정사항
        //classLectureRVadapter 하나로 합치고 즐찾 후, api 7.5.1 재조회
    }

    override fun onResume() {
        super.onResume()

    }

    //7.5.1 로드맵 상세 조회 api
    override fun onShowRoadMapDetailSuccess(code: Int, result: List<RoadMap>) {
        when(code){
            200->{
                //roadmap[0] : 서버 로드맵
                //roadmap[1] : 안드로이드 로드맵

                //screen
                binding.itemCurriculumNameTv.text = result[0].roadmapName
                binding.itemCurriculumDetailTv.text = result[0].roadmapExplain
                binding.itemCurriculumAuthorTv1.text = result[0].authorName
                binding.itemCurriculumDetailDdayTv2.text = result[0].requireDay.toString()

                //language tag
                binding.itemCurriculumLangTagTv.setBackgroundResource(R.drawable.border_round_transparent_back_10)

                //lecture recycler view
                lecture.clear()
                lecture.apply {
                    for(i in result){
                        for(j in i.roadmapChildCurriList){
                            for(k in j.roadmapChildLectureList){
                                lecture.add(Lecture(k.childLectureIdx, k.childLectureName,k.childChapterNumber,k.childLangTag, k.childMaterialType, k.childPricing, k.userScrap, k.scrapNumber,k.usedCount, k.likeNumber, k.userLike))
                            }
                        }
                    }

                    classLectureRVAdapter.datas = lecture
                    classLectureRVAdapter.notifyDataSetChanged()

                    //click recyclerview item
                    classLectureRVAdapter.setItemClickListener(object : ClassLectureRVAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            val intent = Intent(this@AddRoadmapDetailActivity, LectureDetailActivity::class.java)
                            intent.putExtra("lectureIdx", lecture[position].lectureIdx)
                            intent.putExtra("lectureName", lecture[position].lectureName)
                            startActivity(intent)

                        }
                    })
                }

                binding.itemCurriculumDetailStartBtn.setOnClickListener {

                    //7.5.2 로드맵 to 커리큘럼 api
                    var roadMapService = RoadMapService()
                    roadMapService.setCopyRoadMapView(this)
                    roadMapService.copyRoadmapToCurri(copyRoadMap(result[0].roadmapIdx, result[0].roadmapName,"Back" ,result[0].roadmapExplain))

                    finish()
                }

            }
        }
    }

    override fun onShowRoadMapDetailFailure(code: Int) {

    }

    override fun onCopyCurriculumSuccess(code: Int) {
        when(code){
            200->{

            }
        }
    }

    override fun onCopyCurriculumFailure(code: Int) {

    }

    override fun onCopyRoadMapSuccess(code: Int) {
        when(code){
            200->{

            }
        }
    }

    override fun onCopyRoadMapFailure(code: Int) {

    }


}