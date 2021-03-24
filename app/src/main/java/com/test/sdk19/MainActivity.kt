package com.test.sdk19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.sdk19.fragments.CompanyListFragment

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