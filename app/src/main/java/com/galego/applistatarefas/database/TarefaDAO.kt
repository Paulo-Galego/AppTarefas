package com.galego.applistatarefas.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.galego.applistatarefas.model.Tarefa

class TarefaDAO(context: Context):ITarefaDAO {

    private val escrita = DataBaseHelper(context).writableDatabase
    private val leitura = DataBaseHelper(context).readableDatabase

    override fun salvar(tarefa: Tarefa): Boolean {

        val conteudos = ContentValues()
        conteudos.put("${DataBaseHelper.COLUNA_DESCRICAO}", tarefa.descricao)

        try {
            escrita.insert(
                DataBaseHelper.NOME_TABELA_TAREFA,
                null,
                conteudos
            )
            Log.i("info_db", "Sucesso ao salvar")
        }catch (e: Exception){
            Log.i("info_db","Erro ao salvar")
            return false
        }
        return true
    }

    override fun atualizar(tarefa: Tarefa): Boolean {
        val args = arrayOf(tarefa.idTarefa.toString())
        val conteudo = ContentValues()
        conteudo.put("${DataBaseHelper.COLUNA_DESCRICAO}", tarefa.descricao)
        try {
            escrita.update(
                DataBaseHelper.NOME_TABELA_TAREFA,
                conteudo,
                "${DataBaseHelper.COLUNA_ID_TAREFA} = ?",
                args
            )

            Log.i("info_db", "Sucesso ao atualizar tarefa")
        }catch (e: Exception){
            Log.i("info_db","Erro ao atualizar tarefa")
            return false
        }
        return true
    }

    override fun remover(idTarefa: Int): Boolean {
        val args = arrayOf(idTarefa.toString())

        try {
            escrita.delete(
                DataBaseHelper.NOME_TABELA_TAREFA,
                "${DataBaseHelper.COLUNA_ID_TAREFA} = ?",
                args
            )

            Log.i("info_db", "Sucesso ao remover tarefa")
        }catch (e: Exception){
            Log.i("info_db","Erro ao remover tarefa")
            return false
        }
        return true
    }

    override fun listar(): List<Tarefa> {
        val listaTarefas = mutableListOf<Tarefa>()

        val sql = "SELECT ${DataBaseHelper.COLUNA_ID_TAREFA}, " +
                "${DataBaseHelper.COLUNA_DESCRICAO}, " +
                "strftime('%d/%m/%Y %H:%M', ${DataBaseHelper.COLUNA_DATA_CADASTRO}) as ${DataBaseHelper.COLUNA_DATA_CADASTRO} " +
                "FROM ${DataBaseHelper.NOME_TABELA_TAREFA}"

        val cursor = leitura.rawQuery(sql,null)

        //capturando o indice das colunas
        val indiceId = cursor.getColumnIndex(DataBaseHelper.COLUNA_ID_TAREFA)
        val indiceDescricao = cursor.getColumnIndex(DataBaseHelper.COLUNA_DESCRICAO)
        val indiceDataCadastro = cursor.getColumnIndex(DataBaseHelper.COLUNA_DATA_CADASTRO)

        //percorrendo o cursor e inserindo na lista
        while (cursor.moveToNext()){
            val idTarefa = cursor.getInt(indiceId)
            val descricao = cursor.getString(indiceDescricao)
            val data = cursor.getString(indiceDataCadastro)

            listaTarefas.add(
                Tarefa(idTarefa,descricao, data)
            )
        }


        return listaTarefas
    }
}