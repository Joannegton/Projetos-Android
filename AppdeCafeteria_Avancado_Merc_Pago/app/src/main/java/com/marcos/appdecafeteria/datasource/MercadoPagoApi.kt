package com.marcos.appdecafeteria.datasource

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class MercadoPagoApi {

    companion object{
        const val ACCESS_TOKEN = "APP_USR-7284317230979856-070910-3a5a671356235d6cbc68f06851960b2c-1891861517"
    }

    val httpClient = HttpClient{
        install(ContentNegotiation){
            json()
        }
    }

    suspend fun pagamentoMercadoPago(
       title: String,
       quantity: String,
       unitPrice: String,
       respostaServidor: (String) -> Unit
    ){

        val jsonObjectMercadoPago = """
            {
              "back_urls":{
                 "failure":"",
                 "pending":"https://www.google.com.br/",
                 "success":"https://stackmobile.com.br/"
             },
              "purpose": "onboarding_credits",
              "items": [
            {
              "title": "$title",
              "quantity": $quantity,
              "unit_price": $unitPrice
            }
             ],
           }
            """.trimIndent()

        val response = httpClient.post(
            urlString = "https://api.mercadopago.com/checkout/preferences",
            block = {
                header("Content-Type","application/json")
                header("cache-control","no-cache")
                header("Authorization","Bearer $ACCESS_TOKEN")
                setBody(jsonObjectMercadoPago)
            }
        )

        try {

           if (response.status.isSuccess()){
               val jsonObjectResponse = Json.parseToJsonElement(response.bodyAsText()).jsonObject
               val initPoint = jsonObjectResponse["init_point"]!!.jsonPrimitive.content
               respostaServidor(initPoint)
               Log.d("respostaApi",response.bodyAsText())
           }else{
               respostaServidor("")
           }

        }catch (e: Exception){
            respostaServidor("")
        }
    }

}