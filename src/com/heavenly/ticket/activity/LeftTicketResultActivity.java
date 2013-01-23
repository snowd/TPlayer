package com.heavenly.ticket.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.heavenly.ticket.R;
import com.heavenly.ticket.adapter.LeftTicketStateAdapter;
import com.heavenly.ticket.transaction.BaseResponse;
import com.heavenly.ticket.transaction.LeftTicketTransaction;
import com.heavenly.ticket.transaction.LeftTicketTransaction.LeftTicketResponse;

public class LeftTicketResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_left_ticket);
		Intent intent = getIntent();
		initViews(intent);
		initData(intent);
	}
	
	private void initViews(Intent intent) {
		mTrainListView = (ListView) findViewById(R.id.train_ticket_list);
	}
	
	LeftTicketTransaction ticketTransaction;
	private void initData(Intent intent) {
		if (ticketTransaction == null) {
			ticketTransaction = new LeftTicketTransaction();
		}
		ticketTransaction.setParams(intent.getExtras());
		if (progress == null) {
			progress =  ProgressDialog.show(LeftTicketResultActivity.this, "", "正在查询请稍后", true);
		}
		new AsyncTask<Void, Void, LeftTicketResponse>() {
			@Override
			protected LeftTicketResponse doInBackground(Void... params) {
				BaseResponse resp = ticketTransaction.doAction();
				if (resp != null && resp instanceof LeftTicketResponse) {
					LeftTicketResponse response = (LeftTicketResponse) resp;
					return response;
				}
				return null;
			}

			@Override
			protected void onPostExecute(LeftTicketResponse result) {
				progress.dismiss();
				if (result.success) {
					LeftTicketStateAdapter adapter = new LeftTicketStateAdapter(
							LeftTicketResultActivity.this);
					adapter.setData(result.data);
					mTrainListView.setAdapter(adapter);
				} else {
					Toast.makeText(LeftTicketResultActivity.this, "操作失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_query_left_ticket, menu);
		return true;
	}
	
	private ListView mTrainListView;
	private ProgressDialog progress;
}
