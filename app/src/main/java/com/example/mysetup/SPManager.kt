package com.example.mysetup

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * SPManager is a utility class for serialization and deserialization using SharedPreferences.
 *
 * @property context The context used for accessing SharedPreferences.
 */
class SPManager(private val context: Context) {


    // Lazily initialize SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("prefkey", Context.MODE_PRIVATE)
    }


    // Lazily initialize Gson for JSON serialization and deserialization
    private val gson: Gson by lazy { Gson() }


    /**
     * Saves a list of bikes to SharedPreferences.
     *
     * @param list The list of bikes to be serialized and saved.
     */
    fun saveList(list: MutableList<Bike>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("listkey", json).apply()
    }


    /**
     * Retrieves a list of bikes from SharedPreferences.
     *
     * @return A mutable list of bikes, or an empty list if no data is found.
     */
    fun getList(): MutableList<Bike> {
        val json = sharedPreferences.getString("listkey", "")

        // Return an empty list if no data is found
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        }

        // Deserialize JSON to a mutable list of bikes using Gson
        val type = object : TypeToken<MutableList<Bike>>() {}.type
        return gson.fromJson(json, type)
    }

}