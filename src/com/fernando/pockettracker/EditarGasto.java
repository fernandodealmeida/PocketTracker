package com.fernando.pockettracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fernando.pockettracker.Gasto.Gastos;

/**
 * Activity que utiliza o TableLayout para editar o gasto
 * 
 * @author Fernando Sim�o
 * 
 */
public class EditarGasto extends Activity {
	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 2;
	// Campos texto
	private EditText campoNome;
	private EditText campoValor;
	private Button campoData;
	private Long id;
	private int mYear, mMonth, mDay, mHour, mMin;

	private static final String CATEGORIA = "PocketTrackerLog";

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		try{
			
		
		setContentView(R.layout.form_editar_gasto);

		campoNome = (EditText) findViewById(R.id.campoNome);
		campoValor = (EditText) findViewById(R.id.campoValor);
		campoData = (Button) findViewById(R.id.campoData);
		
		final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
//
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		id = null;
		
		Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(Gastos._ID);

			if (id != null) {
				// � uma edi��o, busca o gasto...
				Gasto c = buscarGasto(id);
				campoNome.setText(c.nome);
				campoValor.setText(String.format("%.2f", c.valor));
						
//						String.valueOf(valorFormatado.format(c.valor)));
				
//				campoValor.setText(String.valueOf(c.valor));
				// campoData.setText(String.valueOf(c.data));

				Date date = new Date( c.data ); //new Date(c.data * 1000);
				campoData.setText(sdf.format(date));
			}
		} else {
			campoData.setText(sdf.format(cal.getTime()));
		}
		

		campoData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
//				Toast.makeText(EditarGasto.this, "Test", Toast.LENGTH_SHORT).show();
				showDialog(DATE_DIALOG_ID);
			}
		});

		ImageButton btCancelar = (ImageButton) findViewById(R.id.btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				// Fecha a tela
				finish();
			}
		});

		// Listener para salvar o gasto
		ImageButton btSalvar = (ImageButton) findViewById(R.id.btSalvar);
		btSalvar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				salvar();
			}
		});

		ImageButton btExcluir = (ImageButton) findViewById(R.id.btExcluir);

		if (id == null) {
			// Se id est� nulo, n�o pode excluir
			btExcluir.setVisibility(View.INVISIBLE);
		} else {
			// Listener para excluir o gasto
			btExcluir.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					excluir();
				}
			});
		}
		
		
		} catch (Exception e){
			Log.e(CATEGORIA,Log.getStackTraceString(e));
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Cancela para n�o ficar nada na tela pendente
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();
	}

	public void salvar() {

		int data = 0;
		try {
			data = Integer.parseInt(campoData.getText().toString());
		} catch (NumberFormatException e) {
			// ok neste exemplo, tratar isto em aplica��es reais
		}

		Gasto gasto = new Gasto();
		if (id != null) {
			// � uma atualiza��o
			gasto.id = id;
		}
		gasto.nome = campoNome.getText().toString();
	
		gasto.valor = Double.valueOf(campoValor.getText().toString());
		
//		gasto.data = Long.valueOf(data); // Alterar campo para date picker
    
		
       final Calendar dt = Calendar.getInstance();
    	dt.set(mYear,mMonth,mDay);
		
		gasto.data = dt.getTimeInMillis();
	//	gasto.data = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		/*
		Toast.makeText(EditarGasto.this,"dt: "+ 
		    sdf.format(dt.getTime()) +
			"\n ano: " +mYear
			+"\n mes: "	+mMonth
			+"\n dia: " +mDay
			, Toast.LENGTH_LONG).show();
			*/
		// Salvar
		salvarGasto(gasto);

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	public void excluir() {
		if (id != null) {
			excluirGasto(id);
		}

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	// Buscar o gasto pelo id
	protected Gasto buscarGasto(long id) {
		return CadastroGastos.repositorio.buscarGasto(id);
	}

	// Salvar o gasto
	protected void salvarGasto(Gasto gasto) {
		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(gasto.data);
		
		Toast.makeText(EditarGasto.this, "On salvarGasto: " + gasto.data,
		Toast.LENGTH_LONG);
		
		CadastroGastos.repositorio.salvar(gasto);
	}

	// Excluir o gasto
	protected void excluirGasto(long id) {
		CadastroGastos.repositorio.deletar(id);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return null;// new TimePickerDialog(this, mTimeListener, mHour,
						// mMin, true);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		
		Log.v(CATEGORIA, "onPrepareDialog id: "+id );
		
		switch (id) {

		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMin);
			break;

		}
	}

	private void updateDisplay() {
		/*
		 * simpleDate = new SimpleDateFormat();
		 * simpleDate.applyPattern("dd/MM/yyyy");
		 * btDate.setText(simpleDate.format(WakeActivity.c.getTime()));
		 *
		 */
		campoData = (Button) findViewById(R.id.campoData);
		
		campoData.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("/").append(mMonth + 1).append("/")
				.append(mYear)
				
				);
	}
	

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			EditarGasto.this.mYear = year;
			EditarGasto.this.mMonth = monthOfYear;
			EditarGasto.this.mDay = dayOfMonth;
			updateDisplay();
		}
	};
	

}

