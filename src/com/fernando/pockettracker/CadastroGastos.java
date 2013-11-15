package com.fernando.pockettracker;

import java.util.List;

import com.fernando.pockettracker.Gasto.Gastos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity que demonstra o cadastro de gastos:
 * 
 * - ListActivity: para listar gastos
 * no banco - Gasto
 * 
 * @author Fernando Simao
 * 
 */
public class CadastroGastos extends ListActivity {
	protected static final int INSERIR_EDITAR = 1;
	protected static final int BUSCAR = 2;
	protected static final int UPLOAD = 3;

	public static RepositorioGasto repositorio;

	private List<Gasto> gastos;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		repositorio = new RepositorioGastoScript(this);
		atualizarLista();
	}

	protected void atualizarLista() {
		// Pega a lista de gastos e exibe na tela
		gastos = repositorio.listarGastos();

		// Adaptador de lista customizado para cada linha
		setListAdapter(new GastoListAdapter(this, gastos));
		
		double total = repositorio.getTotalGasto();
		Button totalGasto = (Button) findViewById(R.id.totalGasto);
		totalGasto.setText(String.format("%.2f", total));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERIR_EDITAR, 0, "Inserir Novo").setIcon(R.drawable.novo);
		menu.add(0, BUSCAR, 0, "Buscar").setIcon(R.drawable.pesquisar);
		//menu.add(0, UPLOAD, 0 , "Enviar dados para o servidor").setIcon(R.drawable.upload);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Menu clicado
		switch (item.getItemId()) {
		case INSERIR_EDITAR:
			// Abre a tela com o formulï¿½rio para adicionar
			startActivityForResult(new Intent(this, EditarGasto.class), INSERIR_EDITAR);
			break;
		case BUSCAR:
			// Abre a tela para buscar o gasto pelo nome
			startActivity(new Intent(this, BuscarGasto.class));
			break;
		
		case UPLOAD:
			// Abre a tela para buscar o gasto pelo nome
//			startActivity(new Intent(this, BuscarGasto.class));
			Toast.makeText(this, "Opção em desenvolvimento", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id) {
		super.onListItemClick(l, v, posicao, id);
		editarGasto(posicao);
	}

	// Recupera o id do gasto, e abre a tela de ediï¿½ï¿½o
	protected void editarGasto(int posicao) {
		// Usuï¿½rio clicou em algum gasto da lista
		// Recupera o gasto selecionado
		Gasto gasto = gastos.get(posicao);
		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, EditarGasto.class);
		// Passa o id do gasto como parï¿½metro
		it.putExtra(Gastos._ID, gasto.id);
		// Abre a tela de ediï¿½ï¿½o
		startActivityForResult(it, INSERIR_EDITAR);
	}

	@Override
	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarGasto retornar, seja se foi para adicionar vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			atualizarLista();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Fecha o banco
		repositorio.fechar();
	}
}
