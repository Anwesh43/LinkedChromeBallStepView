package com.anwesh.uiprojects.linkedchromeballstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.chromeballstepview.ChromeBallStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChromeBallStepView.create(this)
    }
}
