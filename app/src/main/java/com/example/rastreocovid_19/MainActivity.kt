package com.example.rastreocovid_19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    var RecyclerView:RecyclerView?=null
    var adaptadorpaises:PaisesAdapter?=null
    var listaPaises:ArrayList<País>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaPaises = ArrayList<País>()
        adaptadorpaises = PaisesAdapter(listaPaises!!,this)
        RecyclerView = findViewById(R.id.RecyclerCovid)
        RecyclerView!!.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        RecyclerView!!.adapter = adaptadorpaises


        val queue = Volley.newRequestQueue(this)
        val url = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/latest"


        val peticionDatosCovid = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
            for (index in 0..response.length()-1){
                try{
                    val paisJson = response.getJSONObject(index)
                    val nombrePais = paisJson.getString("countryregion")
                    val numeroConfirmador = paisJson.getInt("confirmed")
                    val numeroMuertos = paisJson.getInt("deaths")
                    val numeroRecuperados = paisJson.getInt("recovered")
                    val countryCodeJson = paisJson.getJSONObject("countrycode")
                    val codigoPais = countryCodeJson.getString("iso2")
                    val paisIndivual = País(nombrePais,numeroConfirmador,numeroMuertos,numeroRecuperados,codigoPais)
                    listaPaises!!.add(paisIndivual)
                }catch (e:JSONException){
                    Log.wtf("JsonError", e.localizedMessage)
                }
            }

            listaPaises!!.sortByDescending{ it.confirmados }
            adaptadorpaises!!.notifyDataSetChanged()
        },

        Response.ErrorListener { error ->
            Log.e("error_volley",error.localizedMessage)
        })
        queue.add(peticionDatosCovid)
    }
}