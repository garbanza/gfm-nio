package com.ferremundo.db;

import java.util.List;

import com.mongodb.DBCursor;

public class DBQueryI implements DBQuery<DBCursor> {

	private DBQueryI() {
		
	}

	@Override
	public DBCursor search(String where, String what) {

		return null;
	}

}
