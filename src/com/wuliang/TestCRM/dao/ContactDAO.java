package com.wuliang.TestCRM.dao;

import java.util.List;

import com.wuliang.TestCRM.entity.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactDAO extends BaseDAO {

	public ContactDAO(Context ctx) {
		super(ctx);
	}
	
	public Cursor findAll(){
		return super.sd.query("contact", null, null, null, null, null, null);
	}
	
	public Cursor findByInfo(String client_name, String contact_name, String tel){
		client_name = "%" + client_name + "%";
		contact_name = "%" + contact_name + "%";
		tel = "%" + tel + "%";
		return super.sd.query("contact", null, "client_name LIKE ? " +
				"AND contact_name LIKE ? AND tel LIKE ?",
					new String[]{client_name, contact_name, tel}, null, null, null);
	}
	
	public int updateContact(Contact contact){
		ContentValues values = new ContentValues();
		values.put("client_name", contact.getClient_name());
		values.put("contact_name", contact.getContact_name());
		values.put("tel", contact.getTel());
		values.put("email", contact.getEmail());
		values.put("address", contact.getAddress());
		return super.sd.update("contact", values, "_id=?", new String[]{String.valueOf(contact.get_id())});
	}
	
	public int deleteContact(int _id){
		return super.sd.delete("contact", "_id=?", new String[]{String.valueOf(_id)});
	}
	
	public long insertContact(Contact  contact){
		ContentValues values = new ContentValues();
		values.put("client_name", contact.getClient_name());
		values.put("contact_name", contact.getContact_name());
		values.put("tel", contact.getTel());
		values.put("email", contact.getEmail());
		values.put("address", contact.getAddress());
		return super.sd.insert("contact", null, values);
	}
	

}
