package com.brassbullet.dbutils.xml;

import com.brassbullet.dbutils.xml.base.TableBase;
import java.util.ArrayList;
import java.util.List;

public class Table extends TableBase
{
	public Field getField(String name)
	{
		List allfields = new ArrayList(getPrimaryKey().getFields());
		allfields.addAll(getFields());
		int loop=0;
	  while ((loop<allfields.size())&&(!((Field)allfields.get(loop)).getName().equals(name)))
	  {
	  	loop++;
	  }
		if (loop<allfields.size())
		{
			return (Field)allfields.get(loop);
		}
		else
		{
			return null;
		}
	}
}
