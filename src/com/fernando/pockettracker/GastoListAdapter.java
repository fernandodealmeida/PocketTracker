package com.fernando.pockettracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter customizado que exibe o layout definido em smile_row.xml
 * 
 * As imagens sao exibidas no widget ImageView
 * 
 * @author Fernando Simao
 * 
 */
public class GastoListAdapter extends BaseAdapter {
	private Context context;
	private List<Gasto> lista;
	static final String TAG = "MEU_LOG";

	public GastoListAdapter(Context context, List<Gasto> lista) {
		this.context = context;
		this.lista = lista;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(int position) {
		return lista.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// Recupera o Gasto da posicao atual
		Gasto c = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.gasto_linha_tabela, null);

		// Atualiza o valor do TextView
		TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(c.nome);

		// NumberFormat valorFormatado = NumberFormat.getCurrencyInstance(new
		// Locale( "pt", "BR" ));
		TextView valor = (TextView) view.findViewById(R.id.valor);

		// valor.setText(String.valueOf(valorFormatado.format(c.valor)));
		valor.setText(String.valueOf(String.format("%.2f", c.valor)));

		if (position % 2 == 1) {
			// view.setBackgroundColor(Color.parseColor("#F2F4F7"));
		} else {
			view.setBackgroundColor(Color.parseColor("#F2F3E9"));
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date(c.data); // new Date ( c.data * 1000);

			// data.setText(String.valueOf(c.data));
			TextView data = (TextView) view.findViewById(R.id.data);
			data.setText(sdf.format(date));
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
		}
		return view;
	}
}
