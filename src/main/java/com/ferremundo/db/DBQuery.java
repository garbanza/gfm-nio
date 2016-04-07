package com.ferremundo.db;

import java.util.List;

public interface DBQuery<T> {

	public T search(String where,String what);
}
