package com.example.geogenie.Main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geogenie.BuildConfig
import com.example.geogenie.Fragments.Locations.locationListAdapter
import com.example.geogenie.geminiJson.geminiJsonResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class mainModel : ViewModel() {

    private val liveSights = MutableLiveData<geminiJsonResponse>()
    val SightList: LiveData<geminiJsonResponse>
        get() = liveSights

    val DetailedDescription = MutableLiveData<String>()

    private val Gemini = GenerativeModel(
        apiKey = BuildConfig.GEMINI_SECRET_API_KEY,
        modelName = "gemini-2.0-flash"
    )

    val locationsListAdapter = locationListAdapter()

    suspend fun getSuggestions(prompt: String) {
        val initialPrompt = "give famous places in "
        val responseDesc = "respond in json with name , rating and description"
        val geminiResponse = Gemini.generateContent(content(role = "user") {
            text(initialPrompt)
            text(prompt)
            text(responseDesc)
        })
        val properJson = geminiResponse.text!!.removePrefix("```json").removeSuffix("```")
        val gson = Gson()
        withContext(Dispatchers.Main) {
            liveSights.value = gson.fromJson(properJson, geminiJsonResponse::class.java)
        }
    }

    suspend fun getDetailedDescription(prompt: String){
        try{
            val responseDesc = "place detailed Description about"
            val geminiResponse = Gemini.generateContent(content(role = "user") {
                text(responseDesc)
                text(prompt)
            })
            withContext(Dispatchers.Main) {
                DetailedDescription.value = geminiResponse.text
            }
        }catch (e:Exception){
            //
        }
    }

}



