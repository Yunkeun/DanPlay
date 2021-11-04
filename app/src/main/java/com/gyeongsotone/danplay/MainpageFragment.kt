package com.gyeongsotone.danplay

import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainpageFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_mainpage, container, false) as ViewGroup
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        var current_user = auth.currentUser!!
        var name = viewGroup!!.findViewById<View>(R.id.text_name) as TextView
        var my_match = viewGroup!!.findViewById<View>(R.id.text_my_match) as TextView
        var text_sports1 = viewGroup!!.findViewById<View>(R.id.text_sports1) as TextView
        var text_sports2 = viewGroup!!.findViewById<View>(R.id.text_sports2) as TextView
        var text_sports3 = viewGroup!!.findViewById<View>(R.id.text_sports3) as TextView
        var text_sports4 = viewGroup!!.findViewById<View>(R.id.text_sports4) as TextView
        var btn_sports1 = viewGroup!!.findViewById<View>(R.id.btn_sports1) as ImageButton
        var btn_sports2 = viewGroup!!.findViewById<View>(R.id.btn_sports2) as ImageButton
        var btn_sports3 = viewGroup!!.findViewById<View>(R.id.btn_sports3) as ImageButton
        var btn_sports4 = viewGroup!!.findViewById<View>(R.id.btn_sports4) as ImageButton

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.child("user").child(current_user.uid).children){
                    if(snapshot.key.equals("name")){
                        name.setText("${snapshot.value}")
                    }
                    else if(snapshot.key.equals("preference")){
                        text_sports1.setText("${snapshot.child("0").value}")
                        text_sports2.setText("${snapshot.child("1").value}")
                        text_sports3.setText("${snapshot.child("2").value}")
                        text_sports4.setText("${snapshot.child("3").value}")
                        setImage(text_sports1, btn_sports1)
                        setImage(text_sports2, btn_sports2)
                        setImage(text_sports3, btn_sports3)
                        setImage(text_sports4, btn_sports4)
                    }
                    else if(snapshot.key.equals("matchId")){
                        var match0 = snapshot.child("0").value.toString()
                        var match1 = snapshot.child("1").value.toString()
                        var temp = ""
                        for(snapshot in dataSnapshot.child("match").children){
                            if(snapshot.key.equals(match0)){
                                var match_time = snapshot.child("playTime").value.toString()
                                var match_sports = snapshot.child("sports").value.toString()
                                temp = match_time.slice(IntRange(5, 15))
                                temp = temp + " " + match_sports
                                //my_match.setText(temp)
                            }
                            if(snapshot.key.equals(match1)){
                                var match_time = snapshot.child("playTime").value.toString()
                                var match_sports = snapshot.child("sports").value.toString()
                                temp = temp + "\n" + match_time.slice(IntRange(5, 15))
                                temp = temp + " " + match_sports
                                //my_match.setText(temp)
                            }
                            my_match.setText(temp)
                        }
                    }
                    else{
                        // ???
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(getActivity(), "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })

        return viewGroup
    }

    fun setImage(text_sports: TextView, btn_sports: ImageButton){
        when(text_sports.getText()){
            "테니스" -> btn_sports.setImageResource(R.drawable.tennis)
            "축구" -> btn_sports.setImageResource(R.drawable.soccer)
            "농구" -> btn_sports.setImageResource(R.drawable.basket)
            "족구" -> btn_sports.setImageResource(R.drawable.jokgoo)
            "풋살" -> btn_sports.setImageResource(R.drawable.logo_danplay)
            else -> null
        }
    }
}

/*
        btn_sports1.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }
        btn_sports2.setOnClickListener {
           btn_sports2.setImageResource(R.drawable.soccer)
        }
 */
/*database.child("user").child(current_user.uid).get().addOnSuccessListener {
          name.setText("${it.child("name").value}")
          text_sports1.setText("${it.child("preference").child("0").value}")
          text_sports2.setText("${it.child("preference").child("1").value}")
          text_sports3.setText("${it.child("preference").child("2").value}")
          text_sports4.setText("${it.child("preference").child("3").value}")

          setImage(text_sports1, btn_sports1)
          setImage(text_sports2, btn_sports2)
          setImage(text_sports3, btn_sports3)
          setImage(text_sports4, btn_sports4)
      }.addOnFailureListener{
          Toast.makeText(getActivity(), "DB 읽기 실패", Toast.LENGTH_LONG).show()
      }*/