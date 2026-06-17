package com.example.my_list_itens.data.local.dao.historyDao

import com.example.my_list_itens.data.local.entity.History
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface  HistoryDao {
     @Insert
     fun insert (history: History)

     @Query ( value = "SELECT * FROM History")
     fun  getAll() : Flow<List<History>>

     @Delete
     fun delete ( history : History )

     @Query (value = "DELETE FROM History")
     fun deleteAll()

}



