package com.fernando.pockettracker;

import android.content.Context;

/**
 * Repositorio para gastos que utiliza o SQLite
 * 
 * @author Fernando Simao
 * 
 */
public class RepositorioGastoScript extends RepositorioGasto {
//public class RepositorioGastoScript extends RepositorioGasto {
	// Script para fazer drop na tabela
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS gasto";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
			"create table gasto ( _id integer primary key autoincrement, " +
			"nome TEXT NOT NULL,valor REAL NOT NULL,data INTEGER NOT NULL DEFAULT (strftime('%s','now')));" };
			
			/*
			"insert into gasto(nome,valor,data) values('Chocolate','150.00',strftime('%s','now'));",
			"insert into gasto(nome,valor,data) values('Tapuccino','12.00',strftime('%s','now'));",
			"insert into gasto(nome,valor,data) values('Doces','10000',strftime('%s','now'));" };
		 	*/
	// Nome do banco
	private static final String NOME_BANCO = "pocket_tracker";

	// Controle de vers�o
	private static final int VERSAO_BANCO = 1;

	// Nome da tabela
	public static final String TABELA_GASTO = "gasto";

	// Classe utilit�ria para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// Cria o banco de dados com um script SQL
	public RepositorioGastoScript(Context ctx) {
		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, RepositorioGastoScript.NOME_BANCO, RepositorioGastoScript.VERSAO_BANCO,
				RepositorioGastoScript.SCRIPT_DATABASE_CREATE, RepositorioGastoScript.SCRIPT_DATABASE_DELETE);

		// abre o banco no modo escrita para poder alterar tamb�m
		db = dbHelper.getWritableDatabase();
	}

	// Fecha o banco
	@Override
	public void fechar() {
		super.fechar();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
