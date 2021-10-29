package com.gyeongsotone.danplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.gyeongsotone.danplay.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        setContentView(binding.root)

        /* 편의를 위해 로고 클릭 시 메인 이동 활성화 */
        binding.logoDanplay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.signUp.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener{
            signinEmail()
        }


    }

    private fun signinEmail() {
        var loginEmail = binding.loginIdTextedit.text.toString()
        var loginPwd = binding.loginPasswordTextedit.text.toString()
        if (loginEmail.equals("") or loginPwd.equals("")) {
            binding.loginFail.visibility = View.VISIBLE
            binding.loginFail.setText("아이디와 비밀번호를 입력해주세요")
            return
        }
        auth?.signInWithEmailAndPassword(loginEmail, loginPwd)
            ?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    database.child("user").child(task.result?.user!!.uid).get().addOnSuccessListener {
                        Toast.makeText(this, "${it.child("name").value}님 환영합니다!", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener{
                        binding.loginFail.visibility = View.VISIBLE
                    }
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            // 다음 페이지로 넘어가는 Intent
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
