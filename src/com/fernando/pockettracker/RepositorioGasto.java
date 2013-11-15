package com.fernando.pockettracker;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.fernando.pockettracker.Gasto.Gastos;


/**
 * Repositorio para gastos que utiliza o SQLite internamente
 * @author Fernando Simao
 * 
 */
public class RepositorioGasto {
	private static final String CATEGORIA = "tracker";

	// Nome do banco
	private static final String NOME_BANCO = "pocket_tracker";
	// Nome da tabela
	public static final String NOME_TABELA = "gasto";

	protected SQLiteDatabase db;

	public RepositorioGasto(Context ctx) {
		// Abre o banco de dados j� existente
		db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
	}

	protected RepositorioGasto() {
		// Apenas para criar uma subclasse...
	}

	// Salva o gasto, insere um novo ou atualiza
	public long salvar(Gasto gasto) {
		long id = gasto.id;
		if (id != 0) {
			atualizar(gasto);
		} else {
			id = inserir(gasto);
		}

		return id;
	}

	// Insere um novo gasto
	public long inserir(Gasto gasto) {
		ContentValues values = new ContentValues();
		values.put(Gastos.NOME, gasto.nome);
		values.put(Gastos.VALOR, gasto.valor);
		values.put(Gastos.DATA, gasto.data);

		long id = inserir(values);
		return id;
	}

	// Insere um novo gasto
	public long inserir(ContentValues valores) {
		long id = db.insert(NOME_TABELA, "", valores);
		return id;
	}

	// Atualiza o gasto no banco. O id do gasto eh utilizado.
	public int atualizar(Gasto gasto) {
		ContentValues values = new ContentValues();
		values.put(Gastos.NOME, gasto.nome);
		values.put(Gastos.VALOR, gasto.valor);
		values.put(Gastos.DATA, gasto.data);

		String _id = String.valueOf(gasto.id);

		String where = Gastos._ID + "=?";
		String[] whereArgs = new String[] { _id };

		int count = atualizar(values, where, whereArgs);

		return count;
	}

	// Atualiza o gasto com os valores abaixo
	// A cl�usula where � utilizada para identificar o gasto a ser atualizado
	public int atualizar(ContentValues valores, String where, String[] whereArgs) {
		int count = db.update(NOME_TABELA, valores, where, whereArgs);
		return count;
	}

	// Deleta o gasto com o id fornecido
	public int deletar(long id) {
		String where = Gastos._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int count = deletar(where, whereArgs);

		return count;
	}

	// Deleta o gasto com os argumentos fornecidos
	public int deletar(String where, String[] whereArgs) {
		int count = db.delete(NOME_TABELA, where, whereArgs);
		return count;
	}

	// Busca o gasto pelo id
	public Gasto buscarGasto(long id) {
		// select * from gasto where _id=?
		Cursor c = db.query(true, NOME_TABELA, Gasto.colunas, Gastos._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Gasto gasto = new Gasto();

			// L� os dados
			gasto.id = c.getLong(0);
			gasto.nome = c.getString(1);
			gasto.valor = c.getDouble(2);
			gasto.data = c.getLong(3); // Mudar para getLong

			return gasto;
		}

		return null;
	}

	// Retorna um cursor com todos os gastos
	public Cursor getCursor() {
		try {
			return db.query(NOME_TABELA, Gasto.colunas, null, null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os gastos: " + e.toString());
			return null;
		}
	}

	// Retorna uma lista com todos os gastos
	public List<Gasto> listarGastos() {
		Cursor c = getCursor();

		List<Gasto> gastos = new ArrayList<Gasto>();

		if (c.moveToFirst()) {

			// Recupera os �ndices das colunas
			int idxId = c.getColumnIndex(Gastos._ID);
			int idxNome = c.getColumnIndex(Gastos.NOME);
			int idxValor = c.getColumnIndex(Gastos.VALOR);
			int idxData = c.getColumnIndex(Gastos.DATA);

			// Loop at� o final
			do {
				Gasto gasto = new Gasto();
				gastos.add(gasto);

				// recupera os atributos de gasto
				gasto.id = c.getLong(idxId);
				gasto.nome = c.getString(idxNome);
				gasto.valor = c.getDouble(idxValor);
				gasto.data = c.getLong(idxData);

			} while (c.moveToNext());
		}

		return gastos;
	}

	// Busca o gasto pelo nome "select * from gasto where nome=?"
	public Gasto buscarGastoPorNome(String nome) {
		Gasto gasto = null;

		try {
			// Idem a: SELECT _id,nome,placa,ano from gasto where nome = ?
			Cursor c = db.query(NOME_TABELA, Gasto.colunas, Gastos.NOME + "='" + nome + "'", null, null, null, null);

			// Se encontrou...
			if (c.moveToNext()) {

				gasto = new Gasto();

				// utiliza os m�todos getLong(), getString(), getInt(), etc para recuperar os valores
				gasto.id = c.getLong(0);
				gasto.nome = c.getString(1);
				gasto.valor = c.getDouble(2);
				gasto.data = c.getLong(3);
			}
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar o gasto pelo nome: " + e.toString());
			return null;
		}

		return gasto;
	}
	
	public double getTotalGasto(){
		try
		{
			Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM "+NOME_TABELA, null);
				if(cursor.moveToFirst()) {
				    return cursor.getDouble(0);
				}

		} catch (Exception e) {
			Log.e(CATEGORIA, "Exception: "+Log.getStackTraceString(e));
		}
		
		return 0;
	}

	// Busca gasto utilizando as configuracoes definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de Gasto
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
		return c;
	}

	// Fecha o banco
	public void fechar() {
		if (db != null) {
			db.close();
		}
	}
}
