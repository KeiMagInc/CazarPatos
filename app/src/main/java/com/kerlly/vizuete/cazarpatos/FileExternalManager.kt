package com.kerlly.vizuete.cazarpatos

import android.app.Activity
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class FileExternalManager(val actividad: Activity) : FileHandler{
    fun isExternalStorageWritable(): Boolean{
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    override fun SaveInformation(datosAGrabar:Pair<String,String>) {
        if(isExternalStorageWritable()) {
            FileOutputStream(
                File(
                    actividad.getExternalFilesDir(null),
                    SHAREDINFO_FILENAME
                )
            ).bufferedWriter().use { outputStream->
                outputStream.write(datosAGrabar.first)
                outputStream.write(System.lineSeparator())
                outputStream.write(datosAGrabar.second)
            }
        }
    }
    fun isExternalStorageReadable(): Boolean{
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    override fun ReadInformation():Pair<String,String> {
        if (isExternalStorageReadable()) {
            try {
                FileInputStream(
                    File(
                        actividad.getExternalFilesDir(null),
                        SHAREDINFO_FILENAME
                    )
                ).bufferedReader().use {
                    val datoLeido = it.readText()
                    if (datoLeido.isBlank()) return "" to ""
                    val textArray = datoLeido.split(System.lineSeparator())
                    val email = textArray.getOrElse(0) { "" }
                    val clave = textArray.getOrElse(1) { "" }
                    return (email to clave)
                }
            } catch (e: FileNotFoundException) {
                Log.e("FileExternalManager", "Archivo externo no encontrado.")
            }
        }
        return ("" to "")
    }
}