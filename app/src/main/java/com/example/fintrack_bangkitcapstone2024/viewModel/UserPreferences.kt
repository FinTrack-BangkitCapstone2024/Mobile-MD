package com.example.fintrack_bangkitcapstone2024.viewModel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.fintrack_bangkitcapstone2024.response.ResonseReport.Data
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {



    suspend fun saveData(data: Data) {
        dataStore.edit { preferences ->
            val gson = Gson()
            val dataJson = gson.toJson(data)
            preferences[DATA] = dataJson
        }
    }

    fun getData(): Flow<Data?> {
        return dataStore.data.map { preferences ->
            val gson = Gson()
            val dataJson = preferences[DATA]
            if (dataJson != null) {
                gson.fromJson(dataJson, Data::class.java)
            } else {
                null
            }
        }
    }

    suspend fun saveUsahaId(usahaId: String) {
        dataStore.edit { preferences ->
            preferences[USAHA_ID] = usahaId
        }
    }

    fun getUsahaId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USAHA_ID] ?: ""
        }
    }

    fun getLoginSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_SESSION] ?: false
        }
    }

    suspend fun saveLoginSession(loginSession: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_SESSION] = loginSession
        }
    }

    suspend fun savePengeluaran(pengeluaran: List<Int>) {
        dataStore.edit { preferences ->
            preferences[PENGELUARAN] = pengeluaran.joinToString(",")
        }
    }


    suspend fun saveMasukan(masukan: List<Int>) {
        dataStore.edit { preferences ->
            preferences[MASUKAN] = masukan.joinToString(",")
        }
    }

    suspend fun saveDay(day: List<String>) {
        dataStore.edit { preferences ->
            preferences[DAY] = day.joinToString(",")
        }
    }

    fun getPengeluaran(): Flow<List<Int>> {
        return dataStore.data.map { preferences ->
            preferences[PENGELUARAN]?.split(",")?.map { it.toInt() } ?: listOf()
        }
    }

    fun getMasukan(): Flow<List<Int>> {
        return dataStore.data.map { preferences ->
            preferences[MASUKAN]?.split(",")?.map { it.toInt() } ?: listOf()
        }
    }

    fun getDay(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[DAY]?.split(",") ?: listOf()
        }
    }


    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }


    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }


    fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID] ?: ""
        }
    }


    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL] ?: ""
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }


    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }


    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun clearDataLogin() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_SESSION)
            preferences.remove(TOKEN)
            preferences.remove(NAME)
            preferences.remove(USER_ID)
            preferences.remove(EMAIL)
            preferences.remove(USAHA_ID)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val LOGIN_SESSION = booleanPreferencesKey("login_session")
        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("user_id")
        private val EMAIL = stringPreferencesKey("email")
        private val PENGELUARAN = stringPreferencesKey("pengeluaran")
        private val MASUKAN = stringPreferencesKey("masukan")
        private val DAY = stringPreferencesKey("day")
        private val USAHA_ID = stringPreferencesKey("usaha_id")
        private val DATA = stringPreferencesKey("data")


    }
}