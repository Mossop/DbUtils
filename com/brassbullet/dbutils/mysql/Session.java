package com.brassbullet.dbutils.mysql;

import com.brassbullet.dbutils.DbSession;
import com.brassbullet.dbutils.DbHandler;
import com.brassbullet.dbutils.xml.Database;

public class Session extends DbSession
{
	public Session(Handler handler, String user, String pass)
	{
		super(handler,user,pass);
	}
}
