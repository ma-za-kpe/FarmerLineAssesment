package com.farmerline.mergedataforms.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.farmerline.mergedataforms.data.cache.daos.FormConfigDao
import com.farmerline.mergedataforms.data.cache.entity.FormConfigEntity

@Database(
    entities = [
        FormConfigEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(QuestionListConverter::class, MapTypeConverter::class)
abstract class MergeDatatabase : RoomDatabase() {
    abstract fun formConfigDao(): FormConfigDao
}

/** side note such that i don't forget
The abstract keyword is a non-access modifier, used for classes and methods: Abstract class: is a
restricted class that cannot be used to create objects (to access it, it must be inherited from
another class). Abstract method: can only be used in an abstract class, and it does not have a body.
 * */