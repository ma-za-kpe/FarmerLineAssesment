package com.farmerline.mergedataforms.data.cache

import androidx.room.TypeConverter
import com.farmerline.mergedataforms.data.models.QuestionDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestionListConverter {
    @TypeConverter
    fun fromString(value: String): List<QuestionDto> {
        val listType = object : TypeToken<List<QuestionDto>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<QuestionDto>): String {
        return Gson().toJson(list)
    }
}

class MapTypeConverter {

    @TypeConverter
    fun fromMap(map: Map<String, String>?): String {
        return map?.map { "${it.key}:${it.value}" }?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toMap(data: String?): Map<String, String> {
        return data?.split(",")
            ?.map {
                val (key, value) = it.split(":")
                key to value
            }?.toMap() ?: emptyMap()
    }
}

