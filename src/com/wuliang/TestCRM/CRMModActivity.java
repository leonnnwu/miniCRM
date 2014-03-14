package com.wuliang.TestCRM;


import com.wuliang.TestCRM.dao.ContactDAO;
import com.wuliang.TestCRM.entity.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CRMModActivity extends Activity {
	
	EditText et_client = null;
	EditText et_contact = null;
	EditText et_tel = null;
	EditText et_email = null;
	EditText et_address = null;
	Button bt_ok = null;
	Button bt_cancel = null;
	Contact contact = null;
	String client_name = null;
	String contact_name = null;
	String tel = null;
	String email = null;
	String address = null;
	ContactDAO contactDAO = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        et_client = (EditText) findViewById(R.id.et_client);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_email = (EditText) findViewById(R.id.et_email);
		et_address = (EditText) findViewById(R.id.et_address);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		contact = (Contact) this.getIntent().getSerializableExtra("contact");
		et_client.setText(contact.getClient_name());
		et_contact.setText(contact.getContact_name());
		et_tel.setText(contact.getTel());
		et_email.setText(contact.getEmail());
		et_address.setText(contact.getAddress());
		contactDAO = new ContactDAO(CRMModActivity.this);
		contactDAO.createDatabase();
		
		bt_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				client_name = et_client.getText().toString();
				contact_name = et_contact.getText().toString();
				tel = et_tel.getText().toString();
				email = et_email.getText().toString();
				address = et_address.getText().toString();
				if(!client_name.trim().equals("") && !contact_name.trim().equals("") && !tel.trim().equals("")){
					contact.setClient_name(client_name);
					contact.setContact_name(contact_name);
					contact.setTel(tel);
//					System.out.println(tel);
					contact.setEmail(email);
					contact.setAddress(address);
					int i = contactDAO.updateContact(contact);
					if(i == 0){
						Toast.makeText(CRMModActivity.this, "Modify Fail", Toast.LENGTH_LONG).show();
					}else{
						Intent intent = new Intent(CRMModActivity.this, CRMListActivity.class);
						startActivity(intent);
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please input the full info", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				et_client.setText("");
				et_contact.setText("");
				et_tel.setText("");
				et_email.setText("");
				et_address.setText("");
				Intent intent = new Intent(CRMModActivity.this, CRMListActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
