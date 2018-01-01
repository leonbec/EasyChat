package com.leonbec.easychat.service

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.leonbec.easychat.model.Channel
import com.leonbec.easychat.utility.URL_GET_CHANNEL
import org.json.JSONException

/**
 * Created by leonbec on 2018/1/1.
 */
object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {
        val request = object : JsonArrayRequest(
                Method.GET,
                URL_GET_CHANNEL,
                null,
                Response.Listener { response ->
                    try {
                        for (x in 0 until response.length()) {
                            val channel = response.getJSONObject(x)
                            val name = channel.getString("name")
                            val desc = channel.getString("description")
                            val id = channel.getString("_id")

                            val newChannel = Channel(name, desc, id)
                            channels.add(newChannel)
                        }
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXCEPTION: ${e.message}")
                        complete(false)
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "Could not retrieve channels")
                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${AuthService.authToken}")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(request)
    }
}