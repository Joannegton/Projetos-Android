package com.stackmobile.aplicativodefilmes.ui.theme.servidor


import com.stackmobile.aplicativodefilmes.ui.theme.model.Categoria
import com.stackmobile.aplicativodefilmes.ui.theme.model.Filme
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject


class Servidor @Inject constructor() {

    companion object{
        const val URL = "https://stackmobile.com.br/filmes"
    }

    private val httpClient = HttpClient{
        install(ContentNegotiation){
            json()
        }
        install(HttpCache)
    }

    suspend fun getCategorias(
        respostaDoServidor: (MutableList<Categoria>) -> Unit,
        mensagemErro: (String) -> Unit
    ){

        try {

            val respostaServidor = httpClient.get(urlString = URL)

              if (respostaServidor.status.value == 200){
                  val jsonObject = Json.parseToJsonElement(respostaServidor.bodyAsText()).jsonObject
                  val categorias = parseJson(jsonObject)
                  respostaDoServidor(categorias)
              }else{
                  mensagemErro("Erro ao carregar os dados")
              }

        }catch (r: ResponseException){
            mensagemErro("Erro de servidor ${r.printStackTrace()}")
        }catch (t: Throwable){
            mensagemErro("Throwable ${t.printStackTrace()}")
        }
    }

    private fun parseJson(jsonObject: JsonObject): MutableList<Categoria>{

        val listaCategorias = mutableListOf<Categoria>()
        val categoriasArray = jsonObject["categoria"]!!.jsonArray

        for (categoriaJson in categoriasArray) {

            val titulo = categoriaJson.jsonObject["titulo"]!!.jsonPrimitive.content
            val filmesJsonArray = categoriaJson.jsonObject["capas"]!!.jsonArray
            val listaFilmes = mutableListOf<Filme>()

            for (filmeJson in filmesJsonArray) {

                val filme = Filme(
                    capa = filmeJson.jsonObject["url_imagem"]!!.jsonPrimitive.content,
                    id = filmeJson.jsonObject["id"]!!.jsonPrimitive.int,
                    nome = filmeJson.jsonObject["nome"]!!.jsonPrimitive.content,
                    descricao = filmeJson.jsonObject["descricao"]!!.jsonPrimitive.content,
                    elenco = filmeJson.jsonObject["elenco"]!!.jsonPrimitive.content
                )
                listaFilmes.add(filme)

            }

            val categoria = Categoria(
                titulo,
                listaFilmes
            )
            listaCategorias.add(categoria)
        }
            return listaCategorias
    }

}