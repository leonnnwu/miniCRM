package com.wuliang.TestCRM;

import com.wuliang.TestCRM.dao.ContactDAO;
import com.wuliang.TestCRM.entity.Contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CRMListActivity extends Activity {

	protected final int ADD_MENU_ID = Menu.FIRST;
	protected final int FIND_MENU_ID = Menu.FIRST + 1;
	ListView listView = null;
	String[] items = {"Modify Customer Info", "Remove Customer Info"};
	ContactDAO contactDAO = null;
	Cursor cursor = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.listview);
		
		contactDAO = new ContactDAO(getApplicationContext());
		listView = (ListView) findViewById(R.id.lv_list);
		contactDAO.createDatabase();
		
		//Select all the customer info
		cursor = contactDAO.findAll();
		//Match Adapter
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(CRMListActivity.this,
				R.layout.list, cursor, new String[]{"client_name", "contact_name", "tel"},
				new int[]{R.id.tv_showclient, R.id.tv_showcontact, R.id.tv_showtel});
		listView.setAdapter(adapter);
		
		//Long Press listview to invoke modify and remove dialog
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Move cursor to the pressed object
				cursor.moveToPosition(position);
				//Create a Dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(CRMListActivity.this);
				builder.setTitle(cursor.getString(1));
				builder.setItems(items, new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which == 0){
							//Pass the value of cursor to contact
							Contact contact = new Contact();
							contact.set_id(cursor.getInt(0));
							contact.setClient_name(cursor.getString(1));
							contact.setContact_name(cursor.getString(2));
							contact.setTel(cursor.getString(3));
							contact.setEmail(cursor.getString(4));
							contact.setAddress(cursor.getString(5));
							//Jump to Modify activity
							Intent intent = new Intent(CRMListActivity.this, CRMModActivity.class);
							//Contact have serialized, then can be inserted into intent directly
							intent.putExtra("contact", contact);
							startActivity(intent);
						}else{
							//Delete the customer information
							contactDAO.deleteContact(cursor.getInt(0));
							//Update the current Activity
							Intent intent = new Intent(CRMListActivity.this, CRMListActivity.class);
							startActivity(intent);
						}
					}
				});
				builder.create().show();
				return false;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Move the cursor to the pressed object
//				System.out.println(position);
				cursor.moveToPosition(position);
//				System.out.println(cursor.getString(1));
				//Create a dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(CRMListActivity.this);
				LayoutInflater inflater = CRMListActivity.this.getLayoutInflater();
				LinearLayout showDetail = (LinearLayout) inflater.inflate(R.layout.detail, null);
				((TextView)showDetail.findViewById(R.id.tv_showclienttitle)).setText(cursor.getString(1));
				((TextView)showDetail.findViewById(R.id.tv_showclient)).setText(cursor.getString(1));
				((TextView)showDetail.findViewById(R.id.tv_showcontact)).setText(cursor.getString(2));
				((TextView)showDetail.findViewById(R.id.tv_showtel)).setText(cursor.getString(3));
				((TextView)showDetail.findViewById(R.id.tv_showemail)).setText(cursor.getString(4));
				((TextView)showDetail.findViewById(R.id.tv_showaddress)).setText(cursor.getString(5));
				builder.setView(showDetail);
				builder.create().show();
			}
		});
	}
	
	//Set menu option
	public boolean onCreateOptionsMenu(Menu menu){
		//new a menu option
		MenuItem add_menu = menu.add(0, ADD_MENU_ID, 1, "Add");
		add_menu.setIcon(android.R.drawable.ic_menu_add);
		MenuItem find_menu = menu.add(0, FIND_MENU_ID, 1, "Search");
		find_menu.setIcon(android.R.drawable.ic_menu_search);
		return super.onCreateOptionsMenu(menu);
	}
	
	//Menu option invent process
	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
		case ADD_MENU_ID:
			//Jump to AddActivity
			Intent intent = new Intent(CRMListActivity.this, CRMAddActivity.class);
			startActivity(intent);
			break;
		case FIND_MENU_ID:
			//Popup find user dialog
			showDialog();
			break;
		}
		return true;
	}
	
	public void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(CRMListActivity.this);
		LayoutInflater inflater = CRMListActivity.this.getLayoutInflater();
		LinearLayout findForm = (LinearLayout)inflater.inflate(R.layout.find_dialog, null);
		builder.setView(findForm);
		builder.setTitle("Search Dialog");
		builder.setIcon(android.R.drawable.arrow_down_float);
		final EditText et_client = (EditText) findForm.getChildAt(1);
		final EditText et_contact = (EditText) findForm.getChildAt(3);
		final EditText et_tel = (EditText) findForm.getChildAt(5);
		builder.setPositiveButton("Submit", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String client = et_client.getText().toString();
				String contact = et_contact.getText().toString();
				String tel = et_tel.getText().toString();
				if(client.trim().equals("") && contact.trim().equals("") && tel.trim().equals("")){
					Toast.makeText(getApplicationContext(), "Return all the user", Toast.LENGTH_LONG).show();
				}
					Cursor cursor = contactDAO.findByInfo(client, contact, tel);
					SimpleCursorAdapter adapter = new SimpleCursorAdapter(CRMListActivity.this,
						R.layout.list, cursor, new String[]{"client_name", "contact_name", "tel"},
						new int[]{R.id.tv_showclient, R.id.tv_showcontact, R.id.tv_showtel});
					listView.setAdapter(adapter);
			}
		});
		builder.setNegativeButton("Return", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				et_client.setText("");
//				et_contact.setText("");
//				et_tel.setText("");
			}
		});
		builder.create().show();
	}

}
