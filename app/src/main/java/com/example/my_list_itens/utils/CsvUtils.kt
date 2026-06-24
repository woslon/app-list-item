package com.example.my_list_itens.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.my_list_itens.data.local.entity.Item
object CsvUtils {

    fun generateCsv(
        items: List<Item>,
        includeHeader: Boolean
    ): String {

        return buildString {

            if (includeHeader) {
                append("Cliente,Item,Quantidade,Preço,Total,Data\n")
            }

            items.forEach { item ->
                append(
                    "${item.customer}," +
                            "${item.itemName}," +
                            "${item.quantity}," +
                            "${item.price}," +
                            "${item.price * item.quantity}," +
                            "${item.date}\n"
                )
            }
            val totalGeral = items.sumOf { it.price * it.quantity }
            append("\nTotal Geral,$totalGeral\n")
            println(totalGeral)
        }
    }

    fun shareCsv(
        context: Context,
        uri: Uri
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(intent, "Compartilhar CSV")
        )
    }

    fun saveCsv(
        context: Context,
        fileName: String,
        content: String
    ): Uri? {

        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.csv")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(
            MediaStore.Files.getContentUri("external"),
            contentValues
        )

        uri?.let {
            resolver.openOutputStream(it)?.use { output ->
                output.write(content.toByteArray())
            }
        }

        return uri
    }
}