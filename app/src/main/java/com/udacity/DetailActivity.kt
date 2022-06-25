package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        title = getString(R.string.title_activity_detail)

        filename.text = intent.getStringExtra(FILE_NAME_KEY)
        status.text = intent.getStringExtra(STATUS_KEY)

        ok.setOnClickListener { close() }
    }

    private fun close() {
        finish()
    }

    companion object {
        const val STATUS_KEY = "STATUS_KEY"
        const val FILE_NAME_KEY = "FILE_NAME_KEY"
    }
}
