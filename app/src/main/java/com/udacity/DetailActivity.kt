package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var motionLayout: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        title = getString(R.string.title_activity_detail)

        filename.text = intent.getStringExtra(FILE_NAME_KEY)
        status.text = intent.getStringExtra(STATUS_KEY)

        ok.setOnClickListener {
            motionLayout.transitionToEnd()
        }

        motionLayout = findViewById(R.id.motion_layout)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) { finish() }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        })
    }

    companion object {
        const val STATUS_KEY = "STATUS_KEY"
        const val FILE_NAME_KEY = "FILE_NAME_KEY"
    }
}
