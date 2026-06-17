package com.example.my_list_itens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_list_itens.data.local.entity.History
import com.example.my_list_itens.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers


@HiltViewModel
class HistoryViewModel @Inject constructor (
    private val repository: HistoryRepository
) : ViewModel() {

    fun add( history : History) = viewModelScope.launch (  Dispatchers.IO) {
       repository.add(history)
    }

    fun getAll() = repository.getAll()

    fun delete( history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(history)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

}