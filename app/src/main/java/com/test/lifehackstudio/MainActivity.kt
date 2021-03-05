package com.test.lifehackstudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.lifehackstudio.fragments.CompanyListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: loadCompanyList()
    }

    private fun loadCompanyList() {
        supportFragmentManager.beginTransaction()
                .add(R.id.hostContainer, CompanyListFragment())
                .commit()
    }
}