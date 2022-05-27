package com.expense.persistance

import androidx.room.TypeConverter
import com.expense.model.TransactionDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * Created by Jasvir Partap Singh on 23,May,2022
 */
class DataConverter {
    @TypeConverter
    fun fromCountryLangList(countryLang: List<TransactionDetails?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<TransactionDetails?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<TransactionDetails>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<TransactionDetails?>?>() {}.type
        return gson.fromJson<List<TransactionDetails>>(countryLangString, type)
    }
}