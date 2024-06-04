package com.marcos.primeiroprojetocompose.repositorio

import com.marcos.primeiroprojetocompose.data.DataSource
import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * A classe RepositorioMain atua como um repositório central para gerenciar dados e interações
 * com a fonte de dados, neste caso, a classe DataSource.
 * É escopada ao ciclo de vida de um ViewModel, utilizando Dagger Hilt para injeção de dependência.
 */
@ViewModelScoped
class RepositorioMain @Inject constructor(private val dataSource: DataSource) {

    /**
     * O método login delega a tarefa de verificar as credenciais de login para a instância de DataSource.
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @param respostaServidor Um callback para fornecer o resultado da tentativa de login.
     */
    fun login(email: String, senha: String, respostaServidor: RespostaServidor) {
        // Delegar a verificação de login para o DataSource
        dataSource.login(email, senha, respostaServidor)
    }
}
