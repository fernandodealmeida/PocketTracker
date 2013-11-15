package com.fernando.pockettracker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Classe entidade para armazenar os gastos
 * 
 */
public class Gasto {

	public static String[] colunas = new String[] { Gastos._ID, Gastos.NOME, Gastos.VALOR, Gastos.DATA };

	/**
	 * Pacote do Content Provider. 
	 */
	public static final String AUTHORITY = "com.pockettraker.provider.gasto";

	public long id;
	public String nome;
	public Double valor;
	public long data;

	public Gasto() {
	}

	public Gasto(String nome, Double valor, long data) {
		super();
		this.nome = nome;
		this.valor = valor;
		this.data = data;
	}

	public Gasto(long id, String nome, Double valor, long data) {
		super();
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.data = data;
	}

	/**
	 * Classe interna para representar as colunas e ser utilizada por um Content
	 * Provider
	 * 
	 * Filha de BaseColumns que define (_id e _count), para seguir o padrao
	 * Android
	 */
	public static final class Gastos implements BaseColumns {
	
		private Gastos() {
		}
	
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/gastos");
	
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.gastos";
	
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.gastos";
	
		// Ordena��o default para inserir no order by
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
	
		public static final String NOME = "nome";
		public static final String VALOR = "valor";
		public static final String DATA = "data";
	
		// Metodo que constroi uma Uri para um Gasto especifico, com o seu id
		public static Uri getUriId(long id) {
			// Adiciona o id na URI default do /gastos
			Uri uriGasto = ContentUris.withAppendedId(Gastos.CONTENT_URI, id);
			return uriGasto;
		}
	}

	@Override
	public String toString() {
		return "Nome: " + nome + ", Valor: " + valor + ", Data: " + data;
	}
}
