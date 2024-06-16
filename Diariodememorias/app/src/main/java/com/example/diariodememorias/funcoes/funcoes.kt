package com.example.diariodememorias.funcoes

import android.annotation.SuppressLint
import android.net.Uri
import com.example.diariodememorias.R
import com.example.diariodememorias.models.Livro
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.models.Pagina
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

private val auth = Firebase.auth
private val db = Firebase.firestore


fun criarConta(email: String, senha: String, resultado: (Boolean, String?) -> Unit) {
    auth.createUserWithEmailAndPassword(email, senha)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usuario = auth.currentUser
                val usuarioRef = db.collection("usuarios").document(usuario?.uid.toString())
                val usuarioData = hashMapOf(
                    "email" to email,
                    "senha" to senha,
                    "parceiroId" to null
                )
                usuarioRef.set(usuarioData)
                    .addOnSuccessListener {
                        resultado(true, null)
                    }
                    .addOnFailureListener{e ->
                        resultado(false, e.message)
                    }
            } else {
                resultado(false, task.exception?.message)
            }
        }
}

fun conectarparceiro(usuarioId: String?, parceiroId: String, resultado: (Boolean, String?) -> Unit){
    val userRef = db.collection("usuarios").document(usuarioId.toString())
    val parceiroRef = db.collection("usuarios").document(parceiroId)

    db.runTransaction { transacao ->
        transacao.update(userRef, "parceiroId", parceiroId)
        transacao.update(parceiroRef, "parceiroId", usuarioId)
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            resultado(true, null)
        } else {
            resultado(false, task.exception?.message)
        }
    }
}

fun adicionarMemoria(memoria: Memoria, usuarioId: String, compartilhadoCom: String?, resultado: (Boolean, String?) -> Unit) {
    val memoriaRef = db.collection("memories").document()
    val memoriaData = memoria.copy(
        compartilhadoCom = compartilhadoCom
    )

    memoriaRef.set(memoriaData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultado(true, null)
            } else {
                resultado(false, task.exception?.message)
            }
        }
}


fun pegarMemorias(usuarioId: String, parceiroId: String?, resultado: (List<Memoria>) -> Unit){
    db.collection("memories")
        .whereIn("compartilhadoCom", listOf(null, parceiroId))
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val memorias = task.result?.documents?.map { it.toObject(Memoria::class.java) }?.filterNotNull() ?: listOf()
                resultado(memorias)
            }
        }
    db.collection("memories")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val memorias = task.result?.documents?.map { it.toObject(Memoria::class.java) }?.filterNotNull() ?: listOf()
                resultado(memorias)
            }
        }
}

// Função para enviar mídia para o Firebase
fun enviarMidia(uri: Uri, resultado: (String) -> Unit){
    // Cria uma referência para o arquivo no Firebase Storage
    val storageRef = Firebase.storage.reference.child("media/${UUID.randomUUID()}")
    // Inicia o upload do arquivo
    val envioTask = storageRef.putFile(uri)

    envioTask.continueWithTask { task ->
        // Se o upload falhou, lança a exceção
        if (!task.isSuccessful){
            task.exception?.let { throw it }
        }
        // Se o upload foi bem-sucedido, obtém a URL de download do arquivo
        storageRef.downloadUrl
    }.addOnCompleteListener { task ->
        // Se a obtenção da URL de download foi bem-sucedida, retorna a URL
        // Se falhou, retorna "null"
        if (task.isSuccessful){
            val downloadUri = task.result
            resultado(downloadUri?.toString() ?: "null")
        } else{
            resultado("null")
        }
    }
}

suspend fun fetchMemories(usuarioId: String, parceiroId: String?): List<Memoria> {
    return suspendCancellableCoroutine { continuacao ->
        pegarMemorias(usuarioId, parceiroId){listaMemorias ->
            continuacao.resume(listaMemorias)
        }
    }
}

fun criarLivroDeMemorias(onResult: (Livro) -> Unit) {
    val paginas = listOf(
        Pagina(
            "Motivo 1: Porque você é carinhosa",
            "Você sempre sabe como me fazer sentir amado e especial. " +
                    "Seus abraços, seus beijos suaves e até os pequenos gestos, como segurar minha mão ou fazer meu pão, mostram o quanto você se importa. Seu carinho torna os nossos momentos melhores e mais felizes.\n" +
                    "Eu adoro como você sempre sabe quando preciso de um pouco mais de amor e atenção.",
            R.drawable.maozinhadada_carro
        ),
        Pagina(
            "Motivo 2: Porque você me apoia",
            "Você sempre está ao meu lado, me encorajando e acreditando em mim. Nos momentos difíceis, você me dá força e nos momentos de alegria, você celebra comigo. Seu apoio incondicional me faz sentir capaz de enfrentar qualquer desafio. Saber que posso contar com você, não importa o que aconteça, é algo que valorizo profundamente.",
            R.drawable.apoio
        ),
        Pagina(
            "Motivo 3: Porque você é inteligente",
            "Sua inteligência sempre me impressiona. Você tem uma maneira incrível de ver o mundo e entender como podemos melhorar. Eu adoro quando você quer aprender algo novo e compartilha seus conhecimentos comigo. Sua sabedoria torna nossas conversas mais interessantes e me inspira a crescer e aprender ao seu lado.",
            R.drawable.inteligente
        ),
        Pagina(
            "Motivo 4: Porque você me faz rir",
            "Você sempre sabe como me fazer rir, mesmo nos momentos meio tensos. Seu senso de humor é incrível e suas piadas bestas igual eu, sempre me deixam mais feliz. Adoro quando você me faz rir, até porque eu amo sorrir. Sua alegria é contagiante e faz cada dia mais leve e divertido.",
            R.drawable.zuada
        ),
        Pagina(
            "Motivo 5: Porque você é dedicada",
            "Você coloca tanto amor e esforço em tudo que faz. Seja no trabalho, nos seus hobbies ou nas nossas vidas juntos, sua dedicação é inspiradora. Admiro como você se entrega de coração em tudo, sempre dando o seu melhor. Sua determinação e compromisso me motivam todos os dias.",
            R.drawable.trabalhadora
        ),
        Pagina(
            "Motivo 6: Porque você é única",
            "Não existe ninguém como você. Seu jeito especial, suas qualidades e até suas manias te tornam única. Você tem uma combinação de características que fazem de você uma pessoa incrível e insubstituível. Adoro tudo em você e sou grato por ter você na minha vida.",
            R.drawable.elaaa
        ),
        Pagina(
            "Motivo 7: Porque você é minha melhor amiga",
            "Você é minha companheira para todas as horas. Confio em você para tudo e sei que sempre posso contar contigo. Adoro como podemos conversar sobre qualquer coisa e como você sempre entende o que estou sentindo. Sua amizade é um dos maiores presentes da minha vida. E se um dia resolvermos que não somos ideais, quero sempre ter você em minha vida.",
            R.drawable.motel_roupao
        ),
        Pagina(
            "Motivo 8: Porque você é forte e independente",
            "Admiro muito sua força e independência. Você enfrenta desafios com coragem e nunca desiste. Sua capacidade de cuidar de si mesma e de tomar decisões firmes me inspira. Eu me sinto sortudo por ter alguém tão incrível e determinada ao meu lado.",
            R.drawable.forte
        ),
        Pagina(
            "Motivo 9: Porque você topa tudo comigo",
            "Alem de todos esses motivos que me fazem amar e ser completamente apaixonado por você, posso contar contigo para as minhas brisas, esta sempre me acompanhando e não me julgando, isso é de suma importancia para o nosso relacionamento, o torna grandioso e nos aproxima cada vez mais.",
            R.drawable.pastel_dela
        ),
        Pagina(
            "Motivo 10: Porque você é Você",
            "Você é única e especial simplesmente por ser quem é. Sua essência, suas escolhas, suas experiências e até seus desafios moldaram a pessoa incrível que você é hoje. É essa autenticidade que me encanta e me faz admirar cada aspecto seu. Você é você, e isso é o que torna nossa relação tão única e significativa.\nUMA GRANDE GOSTOSA QUE AMO.",
            R.drawable.ndz_dela
        )
    )
    val livro = Livro("10 Motivos que Eu Amo Você", paginas)
    onResult(livro)
}

