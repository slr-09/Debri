package com.example.debri_lize.activity.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.debri_lize.R
import com.example.debri_lize.activity.AddRoadmapDetailActivity
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.agree2TF
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.agree2TermCheck
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.agree3TF
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.agree3TermCheck
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.certificationTF
import com.example.debri_lize.activity.auth.SignUpActivity.Singleton.userID
import com.example.debri_lize.data.auth.UserSignup
import com.example.debri_lize.service.AuthService
import com.example.debri_lize.view.auth.SignUpView
import com.example.debri_lize.databinding.ActivitySignupBinding
import com.example.debri_lize.utils.saveSendEmailTF
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.concurrent.thread

class SignUpActivity:AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding
    private var agree1TF: Boolean = false
    private var idTF : Boolean = true
    private var pwTF : Boolean = true
    private var pwCkTF : Boolean = true
    private var birthTF : Boolean = true
    private var nicknameTF : Boolean = true

    object Singleton {
        var userID = ""

        //인증 여부
        var certificationTF : Boolean = false


        //termCheck = true : 이용약관 내용 확인 후 동의
        var agree2TermCheck : Boolean = false
        var agree3TermCheck : Boolean = false

        var agree2TF: Boolean = false
        var agree3TF: Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //password dot size
        binding.signUpPasswordEt.transformationMethod = LoginActivity.CustomPasswordTransformationMethod()
        binding.signUpPasswordCheckEt.transformationMethod = LoginActivity.CustomPasswordTransformationMethod()

        saveSendEmailTF(false)

        //전체 동의
        binding.signUpAgree1Layout.setOnClickListener {
            if(!agree1TF){
                agree1TF = true
                agree2TF = true
                agree3TF = true

            }else{
                agree1TF = false
                agree2TF = false
                agree3TF = false
            }
            clickBackgroundChange()
        }

        //이용약관 (필수)
        binding.signUpAgree2Layout.setOnClickListener{
            if(!agree2TermCheck){
                val intent = Intent(this, SignUpTermActivity::class.java)
                intent.putExtra("agreeIdx", 2)
                startActivity(intent)

            }else {
                //클릭했을 때 체크전환
                binding.signUpAgree2Layout.setOnClickListener {
                    if(!agree2TF){
                        binding.signUpBox2OnIv.visibility = View.VISIBLE
                        agree2TF = true
                        if(agree3TF) {
                            agree1TF = true
                            binding.signUpBox1OnIv.visibility = View.VISIBLE
                        }
                    }else{
                        binding.signUpBox1OnIv.visibility = View.GONE
                        binding.signUpBox2OnIv.visibility = View.GONE
                        agree1TF = false
                        agree2TF = false
                    }
                    clickBackgroundChange()
                }
            }

        }

        //이용약관 (선택)
        binding.signUpAgree3Layout.setOnClickListener{
            if(!agree3TermCheck){
                val intent = Intent(this, SignUpTermActivity::class.java)
                intent.putExtra("agreeIdx", 3)
                startActivity(intent)

            }else{
                //클릭했을 때 체크전환
                binding.signUpAgree3Layout.setOnClickListener {
                    if(!agree3TF){
                        binding.signUpBox3OnIv.visibility = View.VISIBLE
                        agree3TF = true
                        if(agree2TF) {
                            agree1TF = true
                            binding.signUpBox1OnIv.visibility = View.VISIBLE
                        }
                    }else{
                        binding.signUpBox1OnIv.visibility = View.GONE
                        binding.signUpBox3OnIv.visibility = View.GONE
                        agree1TF = false
                        agree3TF = false
                    }
                    clickBackgroundChange()
                }
            }

        }

        //가입완료 버튼을 누르면 회원가입 끝
        binding.signUpSignUpBtn.setOnClickListener{
            signUp()
        }

        //back
        binding.signUpBackIv.setOnClickListener{
            finish()
        }

        //email 인증
        binding.signUpIdEt.setOnClickListener{
            startActivity(Intent(this, SignUpEmailActivity::class.java))
        }

        //focus effect
        setFocus()

    }

    override fun onStart() {
        super.onStart()
        binding.signUpIdEt.text = userID
        clickBackgroundChange()

    }


    //회원가입
    //사용자가 입력한 값 가져오기
    private fun getUser() : UserSignup {
        val id : String = binding.signUpIdEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()
        val password2 : String = binding.signUpPasswordCheckEt.text.toString()
        var nickname : String = binding.signUpNicknameEt.text.toString()
        val birthday : String = binding.signUpBirthEt.text.toString()

        return UserSignup(id, password, password2, nickname, birthday)
    }

    //회원가입 진행(서버이용)
    private fun signUp(){
        val id : String = binding.signUpIdEt.text.toString().trim()
        val password : String = binding.signUpPasswordEt.text.toString().trim()
        val password2 : String = binding.signUpPasswordCheckEt.text.toString().trim()
        var nickname : String = binding.signUpNicknameEt.text.toString().trim()
        val birthday : String = binding.signUpBirthEt.text.toString().trim()

        //이메일 형식이 잘못된 경우
        if(isEmail(id) && certificationTF){
            idTF = true
        }else{
            Log.d("id","$id")
//            Toast.makeText(this, "아이디 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            idTF = false

            //return
        }


        //비밀번호와 비밀번호 확인이 일치하지 않는 경우 && 공백인 경우
        if(password == password2 && password.isNotBlank() && password2.isNotBlank()){
            pwTF = true
            pwCkTF = true
        }else{
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            Log.d("pw","$password")
            pwTF = false
            pwCkTF = false

            //return
        }

        //닉네임 형식이 맞지 않는 경우
        if(nickname.isEmpty()){
//            Toast.makeText(this, "닉네임 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            nicknameTF = false

            //return
        }else nicknameTF = true

        //생일 형식이 맞지 않는 경우
        if(birthday.isEmpty()){
//            Toast.makeText(this, "생일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            birthTF = false

            //return
        }else birthTF = true

        inputFormatCheck()

        //시작하기 버튼 클릭 효과
        if(idTF && pwTF && pwCkTF && birthTF && nicknameTF && agree2TF){
            binding.signUpSignUpBtn.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.white))
            binding.signUpSignUpBtn.setBackgroundResource(R.drawable.border_round_debri_transparent_6)
        }else{
            binding.signUpSignUpBtn.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.red))
            binding.signUpSignUpBtn.setBackgroundResource(R.drawable.border_round_red_transparent_6)
            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpSignUpBtn.setTextColor(Color.parseColor("#50ffffff"))
                    binding.signUpSignUpBtn.setBackgroundResource(R.drawable.border_round_gray_clearness8_10)

                }
            }
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        //만든 API 호출
        authService.signUp(getUser())

    }


    //입력 형식 틀렸을 때 효과
    private fun inputFormatCheck(){
        if(!idTF){
            binding.signUpIdLayout.setBackgroundResource(R.drawable.border_round_red_transparent_10)
            binding.signUpIdTv.setBackgroundResource(R.drawable.border_line_left_red_2)

            //1초 후 효과 없애기
            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpIdLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpIdTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        }
        if(!pwTF || !pwCkTF){
            binding.signUpPasswordLayout.setBackgroundResource(R.drawable.border_round_red_transparent_10)
            binding.signUpPasswordTv.setBackgroundResource(R.drawable.border_line_left_red_2)

            binding.signUpPasswordCheckLayout.setBackgroundResource(R.drawable.border_round_red_transparent_10)
            binding.signUpPasswordCheckTv.setBackgroundResource(R.drawable.border_line_left_red_2)

            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpPasswordLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpPasswordTv.setBackgroundResource(R.drawable.border_line_left)

                    binding.signUpPasswordCheckLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpPasswordCheckTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        }
        if(!nicknameTF){
            binding.signUpNicknameLayout.setBackgroundResource(R.drawable.border_round_red_transparent_10)
            binding.signUpNicknameTv.setBackgroundResource(R.drawable.border_line_left_red_2)
            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpNicknameLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpNicknameTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        }
        if(!birthTF){
            binding.signUpBirthLayout.setBackgroundResource(R.drawable.border_round_red_transparent_10)
            binding.signUpBirthTv.setBackgroundResource(R.drawable.border_line_left_red_2)
            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpBirthLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpBirthTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        }
        if(!agree2TF){
            binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_transparent_red_6)


            thread(start = true){
                Thread.sleep(1300)
                runOnUiThread{
                    binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_transparent_white_6)
                }
            }
        }
    }

    

    override fun onSignUpSuccess(code : Int) {
        when(code){
            //개발할 때는 userIdx 저장이 필요할수도
            200-> {
                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }


    override fun onSignUpFailure(code : Int) {
        when(code){
            //개발할 때는 userIdx 저장이 필요할수도
            3020, 3021, 3022, 3023, 3024, 3025, 3026, 3027, 1000-> {
                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
            }
        }

    }


    

    //입력이 이메일 형식인지 확인
    fun isEmail(email: String?): Boolean {
        var returnValue = false
        val regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(email)
        if (m.matches()) {
            returnValue = true
        }
        return returnValue
    }


    //약관 클릭
    private fun clickBackgroundChange(){
        //click agree1
        if(agree1TF){
            binding.signUpAgreeLayout.setBackgroundResource(R.drawable.border_round_debri_gray_6)
            binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_debri_gray_6)
            binding.signUpAgree3Layout.setBackgroundResource(R.drawable.border_round_debri_gray_6)

            binding.signUpBox1OnIv.visibility = View.VISIBLE
            binding.signUpBox2OnIv.visibility = View.VISIBLE
            binding.signUpBox3OnIv.visibility = View.VISIBLE

        }else{
            binding.signUpAgreeLayout.setBackgroundResource(R.drawable.border_round_transparent_gray_6)
            binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_transparent_gray_6)
            binding.signUpAgree3Layout.setBackgroundResource(R.drawable.border_round_transparent_gray_6)

            binding.signUpBox1OnIv.visibility = View.GONE
            binding.signUpBox2OnIv.visibility = View.GONE
            binding.signUpBox3OnIv.visibility = View.GONE
        }

        //click agree2
        if(agree2TF){
            binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_transparent_debri_6)
            binding.signUpBox2OnIv.visibility = View.VISIBLE
        }else{
            binding.signUpAgree2Layout.setBackgroundResource(R.drawable.border_round_transparent_white_6)
            binding.signUpBox2OnIv.visibility = View.GONE
        }

        //click agree3
        if(agree3TF){
            binding.signUpAgree3Layout.setBackgroundResource(R.drawable.border_round_transparent_debri_6)
            binding.signUpBox3OnIv.visibility = View.VISIBLE
        }else{
            binding.signUpAgree3Layout.setBackgroundResource(R.drawable.border_round_transparent_white_6)
            binding.signUpBox3OnIv.visibility = View.GONE
        }

    }


    //focus effect
    private fun setFocus(){
        //focus on ID
        binding.signUpIdEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.signUpIdLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.signUpIdTv.setBackgroundResource(R.drawable.border_line_left_debri)
                } else {
                    //  포커스 뺏겼을 때
                    binding.signUpIdLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpIdTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        })

        //focus on password
        binding.signUpPasswordEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.signUpPasswordLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.signUpPasswordTv.setBackgroundResource(R.drawable.border_line_left_debri)
                } else {
                    //  포커스 뺏겼을 때
                    binding.signUpPasswordLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpPasswordTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        })

        //focus on passwordCheck
        binding.signUpPasswordCheckEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.signUpPasswordCheckLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.signUpPasswordCheckTv.setBackgroundResource(R.drawable.border_line_left_debri)
                } else {
                    //  포커스 뺏겼을 때
                    binding.signUpPasswordCheckLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpPasswordCheckTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        })

        //focus on birth
        binding.signUpBirthEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.signUpBirthLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.signUpBirthTv.setBackgroundResource(R.drawable.border_line_left_debri)
                } else {
                    //  포커스 뺏겼을 때
                    binding.signUpBirthLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpBirthTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        })

        //focus on nickname
        binding.signUpNicknameEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    //  포커스시
                    binding.signUpNicknameLayout.setBackgroundResource(R.drawable.border_round_debri_transparent_10)
                    binding.signUpNicknameTv.setBackgroundResource(R.drawable.border_line_left_debri)
                } else {
                    //  포커스 뺏겼을 때
                    binding.signUpNicknameLayout.setBackgroundResource(R.drawable.border_round_white_transparent_10)
                    binding.signUpNicknameTv.setBackgroundResource(R.drawable.border_line_left)
                }
            }
        })



    }
}