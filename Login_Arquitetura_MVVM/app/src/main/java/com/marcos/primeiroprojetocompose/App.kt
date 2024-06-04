package com.marcos.primeiroprojetocompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * A classe App é a classe de aplicação principal que inicializa o Hilt para a injeção de dependência.
 * Extende a classe Application e está anotada com @HiltAndroidApp para indicar ao Hilt que esta
 * é a aplicação que servirá como ponto de entrada para a injeção de dependência.
 */
@HiltAndroidApp
class App : Application()
