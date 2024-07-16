package com.marcos.appdecafeteria.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.marcos.appdecafeteria.model.Pedido

class FirebaseSdk {

    private val firestore = FirebaseFirestore.getInstance()

    fun salvarPedido(
       numero: String,
       produto: String,
       preco: String,
       statusPagamento: String,
       response: (Boolean) -> Unit
    ){

        val dadosPedidos = hashMapOf(
            "numero" to numero,
            "produto" to produto,
            "preco" to preco,
            "statusPagamento" to statusPagamento
        )

        firestore.collection("Pedidos").document(numero).set(dadosPedidos).addOnCompleteListener {
            response(true)
        }.addOnFailureListener {
            response(false)
        }

    }

    fun getPedidos(
       respostaServidor: (MutableList<Pedido>) -> Unit
    ){

        val listaPedidos: MutableList<Pedido> = mutableListOf()

        firestore.collection("Pedidos").get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                for (doc in task.result){
                    val pedido = doc.toObject(Pedido::class.java)
                    listaPedidos.add(pedido)
                    respostaServidor(listaPedidos)
                }
            }
        }
    }
}