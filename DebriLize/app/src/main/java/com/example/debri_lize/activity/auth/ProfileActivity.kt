package com.example.debri_lize.activity.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debri_lize.adapter.home.CurriculumRVAdapter
import com.example.debri_lize.data.curriculum.Curriculum
import com.example.debri_lize.databinding.ActivityProfileBinding
import com.example.debri_lize.utils.ApplicationClass

class ProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityProfileBinding
    lateinit var curriculumRVAdapter: CurriculumRVAdapter
    val datas = ArrayList<Curriculum>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater) //binding 초기화
        setContentView(binding.root)

        //click logout
        binding.profileMenuLogoutTv.setOnClickListener{
            logout()
            val intent = Intent(this, LoginActivity::class.java)
            //모든 화면 초기화
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.profileCurriculumRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        curriculumRVAdapter = CurriculumRVAdapter()
        binding.profileCurriculumRv.adapter = curriculumRVAdapter

        datas.clear()

        //data : 전체
        datas.apply {

            datas.add(Curriculum("자바의 정석"))
            datas.add(Curriculum("자바의 정석"))
            datas.add(Curriculum("자바의 정석"))

            curriculumRVAdapter.datas = datas
            curriculumRVAdapter.notifyDataSetChanged()

            //click recyclerview item
            curriculumRVAdapter.setItemClickListener(object : CurriculumRVAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {


                }
            })
        }
    }

    private fun logout(){ //jwt=0으로 만들기 : 저장된 값X
        val editor = ApplicationClass.mSharedPreferences.edit()
        editor.remove("jwt")
        editor.apply()
    }
}