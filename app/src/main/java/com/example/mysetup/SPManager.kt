package com.example.mysetup

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SPManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("prefkey", Context.MODE_PRIVATE)
    }

    private val gson: Gson by lazy { Gson() }

    fun saveList(list: MutableList<RecyclerData>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("listkey", json).apply()
    }

    fun getList(): MutableList<RecyclerData> {
        val json = sharedPreferences.getString("listkey", "")
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        }

        val type = object : TypeToken<MutableList<RecyclerData>>() {}.type
        return gson.fromJson(json, type)
    }

}