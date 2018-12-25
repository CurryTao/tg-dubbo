package com.tg.fyc.search.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.search.api.ItemSearchApi;

@RestController
@RequestMapping("itemsearch")
public class ItemSearchController {

	@Autowired
	private ItemSearchApi itemSearchApi;
	
	@RequestMapping("search")
	public Map search(@RequestBody Map searchMap) {
		
		return itemSearchApi.search(searchMap);
	}
	
}
