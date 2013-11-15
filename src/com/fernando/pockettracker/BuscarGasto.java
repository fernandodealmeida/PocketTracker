package com.fernando.pockettracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Buscar o Gastos.
 * 
 * @author Fernando Simao
 * 
 */
public class BuscarGasto extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_buscar_gasto);

		ImageButton btBuscar = (ImageButton) findViewById(R.id.btBuscar);
		btBuscar.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Cancela para n�o ficar nada pendente na tela
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();
	}

	public void onClick(View view) {

		EditText nome = (EditText) findViewById(R.id.campoNome);
		EditText valor = (EditText) findViewById(R.id.campoValor);
		EditText data = (EditText) findViewById(R.id.campoData);

		// Recupera o nome do gasto
		String nomeGasto = nome.getText().toString();

		// Busca o gasto pelo nome
		Gasto gasto = buscarGasto(nomeGasto);

		if (gasto != null) {
			// Atualiza os campos com o resultado
			valor.setText(String.valueOf(gasto.valor));
//			data.setText(String.valueOf(gasto.data));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Date date = new Date ( gasto.data * 1000);
	        data.setText(sdf.format(date)); //Alterar campo para date picker
	        
		} else {
			// Limpa os campos
			valor.setText("");
			data.setText("");

			Toast.makeText(BuscarGasto.this, "Nenhuma compra encontrada", Toast.LENGTH_SHORT).show();
		}
	}

	protected Gasto buscarGasto(String nomeGasto) {
		Gasto gasto = CadastroGastos.repositorio.buscarGastoPorNome(nomeGasto);
		return gasto;
	}
}
