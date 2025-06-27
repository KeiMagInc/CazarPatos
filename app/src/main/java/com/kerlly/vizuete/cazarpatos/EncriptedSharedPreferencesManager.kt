package com.kerlly.vizuete.cazarpatos

import android.app.Activity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncriptedSharedPreferencesManager(actividad: Activity):FileHandler{
//Validar si existen datos en archivo de preferencia, y cargar
    val masterKey = MasterKey.Builder(actividad)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    val sharedPreferences = EncryptedSharedPreferences.create(actividad,//context
        "secret_shared_prefs",//filename
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    override fun SaveInformation (datosAGrabar:Pair<String, String>){
        val editor = sharedPreferences.edit()
        editor.putString (LOGIN_KEY, datosAGrabar.second)
        editor.putString(PASSWORD_KEY, datosAGrabar.second)
        editor.apply()
    }
    override fun ReadInformation ():Pair<String, String>{
        val email = sharedPreferences.getString(LOGIN_KEY, "").toString()
        val clave = sharedPreferences.getString(PASSWORD_KEY, "").toString()
        return (email to clave)
    }
}
