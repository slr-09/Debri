package com.example.debri_lize.adapter.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.debri_lize.R
import com.example.debri_lize.data.post.PostList
import com.example.debri_lize.databinding.ItemPostBinding

class PostRVAdapter : RecyclerView.Adapter<PostRVAdapter.ViewHolder>() {

    var datas = mutableListOf<PostList>()

    inner class ViewHolder(val binding : ItemPostBinding) : RecyclerView.ViewHolder(binding.root){

        val title : TextView = binding.itemPostTitle
        val time : TextView = binding.itemPostTimeTv
        var commentCnt : TextView = binding.itemPostCountCommentTv
        var likeCnt : TextView = binding.postLikeNumTv

        fun bind(item: PostList) {
            title.text = item.postName
            commentCnt.text = "("+item.commentCnt.toString()+")"

            if(item.likeCnt!! > 99) likeCnt.text = "99+"
            else if(item.likeCnt!! < 1) binding.itemPostLikeLayout.visibility = View.INVISIBLE
            else likeCnt.text = item.likeCnt.toString()

            if(item.likeStatus == "LIKE")   binding.itemPostLikeIv.setImageResource(R.drawable.ic_like_on)
            else binding.itemPostLikeIv.setImageResource(R.drawable.ic_like_off)

            if(item.timeAfterCreated == 0){
                time.text = "방금 전"
            }else if (item.timeAfterCreated < 60) {
                time.text = item.timeAfterCreated.toString() + "분 전"
            }else if (item.timeAfterCreated < 1440){
                var hour = item.timeAfterCreated/60
                time.text = hour.toString() + "시간 전"
            }else if (item.timeAfterCreated < 43200){
                var day = item.timeAfterCreated/1440
                time.text = day.toString() + "일 전"
            }else if (item.timeAfterCreated < 525600){
                var month = item.timeAfterCreated/43200
                time.text = month.toString() + "달 전"
            }else{
                var year = item.timeAfterCreated/525600
                time.text = year.toString() + "년 전"
            }


        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

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



    //
    override fun getItemCount(): Int = datas.size

    //검색어 입력시 필터
    fun filterList(filteredList: ArrayList<PostList>) {
        datas = filteredList
        notifyDataSetChanged()
    }

}