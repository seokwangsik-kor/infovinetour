package com.infovine.tour.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.infovine.tour.R

class EstimateFinishActivity : BaseActivity() {

    var finish : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_finish)

        finish = findViewById(R.id.estimate_finish)
        finish!!.setOnClickListener {
//            val intent = Intent(mContext, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}