package com.marcos.appdecafeteria.view

import android.os.Build
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.marcos.appdecafeteria.datasource.FirebaseSdk
import java.net.URLDecoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pagamento(
    initPoint: String?,
    produto: String?,
    preco: String?,
    navController: NavController
){

    var statusPagamento by remember {mutableStateOf("")}

    val dataAtualHora = LocalDateTime.now()
    val formatar = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    val formatarDataHora = dataAtualHora.format(formatar)
    val uuid = UUID.randomUUID().toString().substring(0,8)
    val numeroPedido = "${formatarDataHora}${uuid}"

    val firebaseSdk = FirebaseSdk()

    AndroidView(
        factory = {context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webChromeClient = WebChromeClient()
                webViewClient = object : WebViewClient(){
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                       if (url.startsWith("https://stackmobile.com.br/")){
                           post {
                               statusPagamento = "Aprovado"
                               firebaseSdk.salvarPedido(
                                   numero = numeroPedido,
                                   produto = produto.toString(),
                                   preco = preco.toString(),
                                   statusPagamento = statusPagamento,
                                   response = {statusPagamento ->
                                       if (statusPagamento) {
                                           navController.navigate("Pedidos")
                                       }
                                   }
                               )
                           }
                           return true
                       }else if (url.startsWith("https://www.google.com.br/")){
                            post {
                                statusPagamento = "Pendente"
                                firebaseSdk.salvarPedido(
                                    numero = numeroPedido,
                                    produto = produto.toString(),
                                    preco = preco.toString(),
                                    statusPagamento = statusPagamento,
                                    response = {statusPagamento ->
                                        if (statusPagamento) {
                                            navController.navigate("Pedidos")
                                        }
                                    }
                                )
                            }
                            return true
                        }
                        return false
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(URLDecoder.decode(initPoint,"UTF-8"))
            }
        }, update = {
            it.loadUrl(URLDecoder.decode(initPoint,"UTF-8"))
        }
    )
}