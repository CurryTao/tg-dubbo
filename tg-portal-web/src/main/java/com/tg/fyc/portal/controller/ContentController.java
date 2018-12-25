package com.tg.fyc.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tg.fyc.content.api.ContentApi;
import com.tg.fyc.pojo.Content;

@Controller
@RequestMapping("content")
public class ContentController {
	@Autowired
	private ContentApi contentApi;
	
	
	@RequestMapping("findByCategoryId")
	@ResponseBody
	public List<Content> findByCategoryId(Long categoryId){
		
		return contentApi.findByCategoryId(categoryId);
	}
	
	
	
}
