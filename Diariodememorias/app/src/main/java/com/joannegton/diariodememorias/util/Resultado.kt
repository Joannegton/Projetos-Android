package com.joannegton.diariodememorias.util

// Este é um exemplo de uma classe selada em Kotlin. As classes seladas são usadas para representar
// um tipo restrito de hierarquia de classes. Em outras palavras, uma classe selada define um tipo
// limitado de subclasses que podem ser criadas a partir dela.
sealed class Resultado<out T> {
    // A classe `Sucesso` representa um caso de sucesso e contém os dados resultantes da operação bem-sucedida.
    // `T` é um tipo genérico, o que significa que pode ser qualquer tipo. Isso torna a classe `Success` flexível
    // para conter qualquer tipo de dados.
    data class Sucesso<out T>(val data: T) : Resultado<T>()
    // A classe `Falha` representa um caso de falha e contém a exceção que causou a falha.
    // Isso é útil para entender o que deu errado durante a operação.
    data class Falha<out T>(val data: T) : Resultado<T>()
}