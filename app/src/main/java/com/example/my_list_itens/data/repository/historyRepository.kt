package com.example.my_list_itens.data.repository

import com.example.my_list_itens.data.local.entity.History
import com.example.my_list_itens.data.local.dao.historyDao.HistoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class HistoryRepository @Inject constructor(
    private  val  historyDao : HistoryDao
) {
   fun add( history: History){
        historyDao.insert( history)
    }
    fun getAll()  = historyDao.getAll()

    fun delete( history: History) {
       historyDao.delete(history)
    }
   fun deleteAll()  = historyDao.deleteAll()

}