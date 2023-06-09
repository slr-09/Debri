package com.example.debri_lize.adapter.class_

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.debri_lize.R
import com.example.debri_lize.activity.MainActivity
import com.example.debri_lize.data.class_.LectureScrap
import com.example.debri_lize.data.class_.Lecture
import com.example.debri_lize.databinding.ItemClassFavoriteBinding
import com.example.debri_lize.service.ClassService
import com.example.debri_lize.utils.getUserIdx
import com.example.debri_lize.view.class_.CancelLectureScrapView
import com.example.debri_lize.view.class_.CreateLectureScrapView

class ClassLectureRVAdapter : RecyclerView.Adapter<ClassLectureRVAdapter.ViewHolder>(),
    CreateLectureScrapView, CancelLectureScrapView {
    var datas = mutableListOf<Lecture>()


    inner class ViewHolder(val binding: ItemClassFavoriteBinding) : RecyclerView.ViewHolder(binding.root){

        val lectureName : TextView = binding.itemClassFavTitleTv
        val chapterNum : TextView = binding.itemClassFavChapterTv
        val language : TextView = binding.itemClassFavTagLanguageTv
        val media : TextView = binding.itemClassFavMediaTagTv
        val price : TextView = binding.itemClassFavPriceTagTv
        val likeNum : TextView = binding.itemClassLikenumTv
        val usedCount : TextView = binding.itemClassUsedCountTv

        fun bind(item: Lecture){
            lectureName.text = item.lectureName
            chapterNum.text = "(" + item.chapterNum.toString()+"챕터)"
            language.text = item.language
            media.text = "#" + item.media
            price.text = "#" + item.price
            likeNum.text = item.likeNumber.toString()
            usedCount.text = item.usedCount.toString()

            when(language.text){
                "Front" -> language.setBackgroundResource(R.drawable.border_round_transparent_front_10)
                "Back" -> language.setBackgroundResource(R.drawable.border_round_transparent_back_10)
                "C 언어" -> language.setBackgroundResource(R.drawable.border_round_transparent_c_10)
                "Python" -> language.setBackgroundResource(R.drawable.border_round_transparent_python_10)
            }

            Log.d("lectureitem",item.toString())

            if(item.userScrap)   binding.itemClassFavoriteIv.setImageResource(R.drawable.ic_favorite_on)
            else    binding.itemClassFavoriteIv.setImageResource(R.drawable.ic_favorite_off)

            if(item.userLike)   binding.itemClassLikeIv.setImageResource(R.drawable.ic_like_on)
            else    binding.itemClassLikeIv.setImageResource(R.drawable.ic_like_off)


        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemClassFavoriteBinding = ItemClassFavoriteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
//        Log.d("lecturedata",datas[position].toString())

        //즐겨찾기 생성
        holder.binding.itemClassFavoriteIv.setOnClickListener {
            if(datas[position].userLike){
                //즐겨찾기 해제 - userlike가 false로 들어오는 에러있음 아ㅏㅁ두
                holder.binding.itemClassFavoriteIv.setImageResource(R.drawable.ic_favorite_off)

                val cancelLectureScrap = LectureScrap(getUserIdx()!!, datas[position].lectureIdx!!)

                //api
                val classService = ClassService()
                classService.setCancelLectureScrapView(this)
                classService.cancelLectureScrap(cancelLectureScrap)
            }else{
                //즐겨찾기 생성
                holder.binding.itemClassFavoriteIv.setImageResource(R.drawable.ic_favorite_on)

                var createLectureScrap = LectureScrap(getUserIdx()!!, datas[position].lectureIdx!!)

//                Log.d("createlecturescrap",createLectureScrap.toString())

                //api
                val classService = ClassService()
                classService.setCreateLectureScrapView(this)
                classService.createLectureScrap(createLectureScrap)
            }

        }

        //recyclerview item 클릭하면 fragment
        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int = datas.size


    override fun onCreateLectureScrapSuccess(code: Int) {
        when(code){
            200->{

            }
        }
    }

    override fun onCreateLectureScrapFailure(code: Int) {

    }

    override fun onCancelLectureScrapSuccess(code: Int) {

    }

    override fun onCancelLectureScrapFailure(code: Int) {

    }

}