package io.lundgren.hnreader.presentation.ui

import android.os.Bundle
import io.lundgren.hnreader.R
import io.lundgren.hnreader.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
