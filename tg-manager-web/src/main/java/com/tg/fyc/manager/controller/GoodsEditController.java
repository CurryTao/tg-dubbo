package com.tg.fyc.manager.controller;

import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.FastDFSClient;
import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Goods;
import com.tg.fyc.pojo.GoodsDeitVO;
import com.tg.fyc.pojo.Item;
import com.tg.fyc.sellergoods.api.GoodsEditApi;

@Controller
@RequestMapping("goodsEdit")
public class GoodsEditController {

	@Autowired
	private GoodsEditApi goodsEditApi;
	private Logger log=LoggerFactory.getLogger(GoodsEditController.class);

	@RequestMapping("findPage")
	@ResponseBody
	public DataGrid findPage2(Integer currentPage,Integer pageSize,@RequestBody Goods goods) {
		return goodsEditApi.findPage2(currentPage,pageSize,goods);
	}

	@RequestMapping("update")
	@ResponseBody
	public PageResult update(@RequestBody GoodsDeitVO goodsDeitVO) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		GoodsDeitVO findOne = goodsEditApi.findOne(goodsDeitVO.getGoods().getId());
		PageResult pageResult=null;
		if (!findOne.getGoods().getSellerId().equals(name)) {
			return pageResult.error(GloablErrorMessageEnum.ERROR_PARAM_ILLEGAL.getCode(), GloablErrorMessageEnum.ERROR_PARAM_ILLEGAL.getMessage());
		}
		try {
			pageResult=goodsEditApi.update(goodsDeitVO);

		} catch (Exception e) {
			log.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage(),e,e);
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage());
		}
		return pageResult.success(null);
	}

	@RequestMapping("pltijiao")
	@ResponseBody
	public PageResult pltijiao(String ids) {
		return goodsEditApi.pltijiao(ids);
	}

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination queueSolrDestination;

	@Autowired
	private Destination topicPageDestination;

	@RequestMapping("updatestatus")
	@ResponseBody
	public PageResult updatestatus(String ids,String status) throws Exception {
		PageResult pageResult=null;
		try {
			pageResult=goodsEditApi.updatestatus(ids,status);
			//审核通过
			if ("2".equals(status)) {
				//获取列表
				List<Item> list = goodsEditApi.findItemListByGoodsIdListAndStatus(ids, status);
				//转换为json
				final String jsonString = JSON.toJSONString(list);
				jmsTemplate.send(queueSolrDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(jsonString);
					}
				});
				String[] split = ids.split(",");
				for (final String goodsId : split) {
					jmsTemplate.send(topicPageDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(goodsId+"");
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return pageResult.error(GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getCode(), GloablErrorMessageEnum.ERROR_GUIGE_UPDATE.getMessage());
		}
		return PageResult.success(null);
	}

	@Autowired
	private Destination queueSolrDeleteDestination;

	@Autowired
	private Destination topicPageDeleteDestination;

	@RequestMapping("del")
	@ResponseBody
	public PageResult del(String ids) {
		String[] split = ids.split(",");
		for (final String string : split) {
			//itemSearchApi.deleteList(string);
			
			//这是从索引库里删除
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(string);
				}
			});
		}
		
		//这是删除html静态页面
		for (final String string : split) {
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(string);
				}
			});
		}
		return goodsEditApi.del(ids);
	}


	@Value("${FILE_SERVER_URL}")
	private String file_server_url;

	@RequestMapping("upload")
	@ResponseBody
	public PageResult upload(MultipartFile file) {
		String filename = file.getOriginalFilename();
		String string = filename.substring(filename.lastIndexOf(".")+1);
		try {
			FastDFSClient client=new FastDFSClient("classpath:fdfs_client.conf");
			String file2 = client.uploadFile(file.getBytes(),string);
			String url=file_server_url+file2;
			return PageResult.success(url);
		} catch (Exception e) {
			// TODO: handle exception
			return PageResult.error(GloablErrorMessageEnum.ERROR_FILE_UPLOAD.getCode(), GloablErrorMessageEnum.ERROR_FILE_UPLOAD.getMessage());
		}

	}

}
