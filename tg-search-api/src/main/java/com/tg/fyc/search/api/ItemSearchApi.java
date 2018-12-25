package com.tg.fyc.search.api;

import java.util.List;
import java.util.Map;

public interface ItemSearchApi {

	public Map search(Map searchMap);
	
	
	public void importList(List list);
	
	
	public void deleteList(String string);
}
