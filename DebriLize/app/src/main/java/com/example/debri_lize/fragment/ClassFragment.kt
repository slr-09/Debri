package com.example.debri_lize.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debri_lize.adapter.class_.ClassFavoriteRVAdapter
import com.example.debri_lize.adapter.class_.ClassLectureRVAdapter
import com.example.debri_lize.R
import com.example.debri_lize.activity.LectureDetailActivity
import com.example.debri_lize.activity.auth.ProfileActivity
import com.example.debri_lize.data.class_.Lecture
import com.example.debri_lize.data.class_.LectureFilter
import com.example.debri_lize.databinding.FragmentClassBinding
import com.example.debri_lize.service.ClassService
import com.example.debri_lize.utils.getUserIdx
import com.example.debri_lize.view.class_.LectureFavoriteView
import com.example.debri_lize.view.class_.LectureFilterView
import com.google.android.material.bottomsheet.BottomSheetDialog

class ClassFragment : Fragment(), LectureFavoriteView, LectureFilterView {

    lateinit var binding: FragmentClassBinding
    lateinit var classfavoriteRVAdapter: ClassFavoriteRVAdapter
    lateinit var classLectureRVAdapter: ClassLectureRVAdapter

    val classService = ClassService()
    val lectureFilter = LectureFilter()

    var filterNum : Int = 0
    var filterNum2 : Int = 0

//    var datas = ArrayList<Lecture>()
//    var datas_f = ArrayList<Lecture>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        //태그 클릭 효과
//        onRadioButtonClicked()

        //api - lectureFavorite
        classService.setLectureFavoriteView(this)
        classService.showLectureFavorite(getUserIdx()!!)


        //강의 필터
        classService.setLectureFilterView(this)

        //click filter btn
        binding.classFilterIv.setOnClickListener{
            //bottom sheet
            bottomSheet()
        }

        //focus
        binding.classSearchEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.classSearchLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.classSearchIv.setImageResource(R.drawable.btm_nav_search_on)
                } else {
                    //  포커스 뺏겼을 때
                    binding.classSearchLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.classSearchIv.setImageResource(R.drawable.btm_nav_search)
                }
            }
        })

        //click userImg -> profile
//        binding.classDebriUserIv.setOnClickListener{
//            val intent = Intent(context, ProfileActivity::class.java)
//            startActivity(intent)
//        }

        //검색어 입력 : search Lecture
        binding.classSearchEt.addTextChangedListener(object : TextWatcher{
            //입력이 끝날 때
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            //입력하기 전에
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            //타이핑되는 텍스트에 변화가 있을 때
            override fun afterTextChanged(p0: Editable?) {
                val searchText: String = binding.classSearchEt.text.toString()
                Log.d("editText","$searchText")
                lectureFilter.key = searchText
                if(searchText=="")  filterNum2 = 0
                else filterNum2 = 1
                showList()
            }

        })

    }

    //bottom sheet
    private fun bottomSheet(){

        val bottomSheetDialog = BottomSheetDialog(requireContext())

        var bottomSheetView : View = layoutInflater.inflate(R.layout.fragment_bottom_sheet_two, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_two_tv).text = "강의 정렬하기"
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_two_tv1).text = "가나다 순 정렬"
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_two_tv2).text = "좋아요 순 정렬"

        //가나다 순
        touchEvent(bottomSheetView.findViewById(R.id.bottom_sheet_two_tv1))
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_two_tv1).setOnClickListener {

//            datas.sortBy { it.lectureName }
//            datas_f.sortBy { it.lectureName }
            showList()

            bottomSheetDialog.dismiss()
        }
        //좋아요 순
        touchEvent(bottomSheetView.findViewById(R.id.bottom_sheet_two_tv2))
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_two_tv2).setOnClickListener {

//            datas.sortBy { it.likeNumber }
//            datas_f.sortBy { it.likeNumber }
            showList()

            bottomSheetDialog.dismiss()
        }


        binding.classFilterIv.setOnClickListener {
            bottomSheetDialog.show()
        }

        //close button
        bottomSheetView.findViewById<TextView>(R.id.bottom_sheet_close_tv).setOnClickListener {
            bottomSheetDialog.dismiss()
        }


    }

    private fun touchEvent(bind : TextView){
        bind.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        bind.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                    }
                    MotionEvent.ACTION_UP -> {
                        bind.setTextColor(ContextCompat.getColor(context!!, R.color.darkmode_background))
                        bind.performClick()
                    }
                }

                //리턴값이 false면 동작 안됨
                return true //or false
            }


        })
    }

    private fun showList(){
        if(filterNum+filterNum2==0){
            //즐겨찾기 강의 view -> VISIBLE
            binding.classFavoriteRv.visibility = View.VISIBLE
            //필터 강의 리스트 view -> GONE
            binding.classLecturelistRv.visibility = View.GONE
        }else{
            //즐겨찾기 강의 view -> GONE
            binding.classFavoriteRv.visibility = View.GONE
            //필터 강의 view -> VISIBLE
            binding.classLecturelistRv.visibility = View.VISIBLE
        }
        classService.showLectureFavorite(getUserIdx()!!)
        classService.showLectureSearch(lectureFilter)
    }


//    private fun onRadioButtonClicked(){
//        //언어
//        //front
//        binding.classCurriTagFrontCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagBackCb.visibility = View.GONE
//                binding.classCurriTagCCb.visibility = View.GONE
//                binding.classCurriTagPythonCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.lang = button.text.toString()
//            }else{
//                binding.classCurriTagBackCb.visibility = View.VISIBLE
//                binding.classCurriTagCCb.visibility = View.VISIBLE
//                binding.classCurriTagPythonCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.lang = ""
//            }
//
//            showList()
//
//        }
//        //back
//        binding.classCurriTagBackCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagFrontCb.visibility = View.GONE
//                binding.classCurriTagCCb.visibility = View.GONE
//                binding.classCurriTagPythonCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.lang = button.text.toString()
//
//            }else{
//                binding.classCurriTagFrontCb.visibility = View.VISIBLE
//                binding.classCurriTagCCb.visibility = View.VISIBLE
//                binding.classCurriTagPythonCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.lang = ""
//            }
//            showList()
//        }
//        //C language
//        binding.classCurriTagCCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagBackCb.visibility = View.GONE
//                binding.classCurriTagFrontCb.visibility = View.GONE
//                binding.classCurriTagPythonCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.lang = button.text.toString()
//
//            }else{
//                binding.classCurriTagBackCb.visibility = View.VISIBLE
//                binding.classCurriTagFrontCb.visibility = View.VISIBLE
//                binding.classCurriTagPythonCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.lang = ""
//            }
//            showList()
//        }
//        //python
//        binding.classCurriTagPythonCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagBackCb.visibility = View.GONE
//                binding.classCurriTagCCb.visibility = View.GONE
//                binding.classCurriTagFrontCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.lang = button.text.toString()
//
//            }else{
//                binding.classCurriTagBackCb.visibility = View.VISIBLE
//                binding.classCurriTagCCb.visibility = View.VISIBLE
//                binding.classCurriTagFrontCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.lang = ""
//            }
//            showList()
//        }
//
//        //서적 or 영상
//        //서적
//        binding.classCurriTagBookCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagVideoCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.type = button.text.toString()
//
//            }else{
//                binding.classCurriTagVideoCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.type = ""
//            }
//            showList()
//        }
//        //영상
//        binding.classCurriTagVideoCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classCurriTagBookCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.type = button.text.toString()
//
//            }else{
//                binding.classCurriTagBookCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.type = ""
//
//            }
//            showList()
//        }
//
//        //가격
//        //무료
//        binding.classPriceTagForfreeCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classPriceTagNoForfreeCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.price = button.text.toString()
//            }else{
//                binding.classPriceTagNoForfreeCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.price = ""
//            }
//            showList()
//        }
//        //유료
//        binding.classPriceTagNoForfreeCb.setOnCheckedChangeListener { button, checked ->
//            if(checked){
//                binding.classPriceTagForfreeCb.visibility = View.GONE
//
//                filterNum++
//                lectureFilter.price = button.text.toString()
//            }else{
//                binding.classPriceTagForfreeCb.visibility = View.VISIBLE
//
//                filterNum--
//                lectureFilter.price = ""
//            }
//            showList()
//        }
//
//
//
//    }



    override fun onLectureFavoriteSuccess(code: Int, result: List<Lecture>) {
        when(code){
            200->{
                val datas_f = ArrayList<Lecture>()

                //즐겨찾기
                binding.classFavoriteRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                classfavoriteRVAdapter = ClassFavoriteRVAdapter()
                binding.classFavoriteRv.adapter = classfavoriteRVAdapter

                datas_f.apply {
                    for (i in result){
                        datas_f.add(Lecture(i.lectureIdx, i.lectureName, i.chapterNum, i.language, i.media, i.price, i.userScrap, i.scrapNumber, i.usedCount, i.likeNumber, i.userLike, i.lectureDesc, i.srcLink))
                    }

                    classfavoriteRVAdapter.datas_classf = datas_f
                    classfavoriteRVAdapter.notifyDataSetChanged()

                    //item 클릭 시 강의 상세 화면으로 전환?
                    classfavoriteRVAdapter.setItemClickListener(object : ClassFavoriteRVAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {

                            //LectureDetailActivity에 data보내기
                            val intent = Intent(context, LectureDetailActivity::class.java)
                            intent.putExtra("lectureIdx", datas_f[position].lectureIdx)
                            intent.putExtra("lectureName", datas_f[position].lectureName)
                            Log.d("lectureIdxClass", datas_f[position].lectureIdx.toString())
                            startActivity(intent)

                        }
                    })
                }
            }
        }
    }

    override fun onLectureFavoriteFailure(code: Int) {
        Log.d("lecturefavoritefail","$code")
    }

    override fun onLectureFilterSuccess(code: Int, result: List<Lecture>) {
        when(code){
            200->{
                val datas = ArrayList<Lecture>()
                binding.classLecturelistRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                classLectureRVAdapter = ClassLectureRVAdapter()
                binding.classLecturelistRv.adapter = classLectureRVAdapter

                datas.apply {
                    for (i in result) {
                        datas.add(
                            Lecture(i.lectureIdx, i.lectureName, i.chapterNum, i.language, i.media, i.price, i.userScrap, i.scrapNumber, i.usedCount, i.likeNumber, i.userLike, i.lectureDesc, i.srcLink)
                        )
                    }

                    classLectureRVAdapter.datas = datas
                    classLectureRVAdapter.notifyDataSetChanged()

                    //item 클릭 시 강의 상세 화면으로 전환?
                    classLectureRVAdapter.setItemClickListener(object :
                        ClassLectureRVAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {

                            //LectureDetailActivity에 data보내기
                            val intent = Intent(context, LectureDetailActivity::class.java)
                            intent.putExtra("lectureIdx", datas[position].lectureIdx)
                            intent.putExtra("lectureName", datas[position].lectureName)
                            Log.d("lectureIdxClass", datas[position].lectureIdx.toString())
                            startActivity(intent)

                        }
                    })

                }


            }
        }
    }

    override fun onLectureFilterFailure(code: Int) {
        Log.d("lecturefilterfail","$code")
        when(code){
            //검색 결과 없을 때
            7307-> {
                binding.classLecturelistRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                classLectureRVAdapter = ClassLectureRVAdapter()
                binding.classLecturelistRv.adapter = classLectureRVAdapter
            }
        }
    }




}