package com.example.my_list_itens.data.local.database

import com.example.my_list_itens.data.local.entity.Item
import com.example.my_list_itens.data.local.dao.itemDao.ItemDao
import com.example.my_list_itens.data.local.dao.historyDao.HistoryDao
import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.my_list_itens.data.local.entity.History


@Database(entities =
    [ Item::class, History::class ], version = 2
)

abstract  class AppDatabase : RoomDatabase(){
    abstract fun itemDao() : ItemDao
    abstract  fun historyDao() : HistoryDao


}
