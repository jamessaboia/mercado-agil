package com.example.mercadogil.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mercadogil.data.dao.ProductDao
import com.example.mercadogil.data.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase(){
    abstract fun productDao(): ProductDao
}