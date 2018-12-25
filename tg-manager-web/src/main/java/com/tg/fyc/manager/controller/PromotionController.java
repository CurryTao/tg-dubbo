package com.tg.fyc.manager.controller;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tg.fyc.common.DataGrid;
import com.tg.fyc.common.GloablErrorMessageEnum;
import com.tg.fyc.common.PageResult;
import com.tg.fyc.pojo.Promotion;
import com.tg.fyc.pojo.PromotionType;
import com.tg.fyc.promotion.api.PromotionApi;
import com.tg.fyc.promotion.api.PromotionTypeApi;

@RestController
@RequestMapping("promotion")
public class PromotionController {

	@Autowired
	private PromotionApi promotionApi;

	@Autowired
	private PromotionTypeApi promotionTypeApi;

	@RequestMapping("getPromotionTypeList")
	public List<PromotionType> getPromotionTypeList() {
		return promotionTypeApi.getPromotionTypeList();
	}
	
	@RequestMapping("add")
	public boolean add(@RequestBody Promotion promotion) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		promotion.setSellerId(name);
		try {
			promotionApi.insertSelective(promotion);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@RequestMapping("findPage")
	public DataGrid findPage(Integer currentPage,Integer pageSize,@RequestBody Promotion promotion) {
		return promotionApi.findPage(currentPage,pageSize,promotion);
	}

	@RequestMapping("findOne")
	public Promotion findOne(Long id) {
		return  promotionApi.selectByPrimaryKey(id);
	}

	@RequestMapping("update")
	public PageResult update(@RequestBody Promotion Promotion) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Promotion.setUpdateby(name);
		Promotion.setUpdatetime(new Date());
		try {
			promotionApi.update(Promotion);
			return  PageResult.success(null);
		} catch (Exception e) {
			return PageResult.error(GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getCode(), GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getMessage());
		}
	}
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination topicPageDestination;
	
	@RequestMapping("updateStatus")
	public PageResult updateStatus(String status,@RequestBody String [] ids) {
		
		try {
			promotionApi.updatestatus(ids,status);
			
			//重新调用freemarker生成静态页面，更新促销信息
			for (String string : ids) {
				final Promotion promotion = this.promotionApi.selectByPrimaryKey(Long.valueOf(string));
				jmsTemplate.send(topicPageDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(promotion.getGoodsIds()+"");
					}
				});
			}
			
			return  PageResult.success(null);
		} catch (Exception e) {
			return PageResult.error(GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getCode(), GloablErrorMessageEnum.ERROR_SYSTEM_ERROR.getMessage());
		}
	}

	@RequestMapping("del")
	public PageResult del(String ids) {
		PageResult pageResult=null;
		try {
			pageResult=promotionApi.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return PageResult.error(GloablErrorMessageEnum.ERROR_PUSH_REDIS.getCode(), GloablErrorMessageEnum.ERROR_PUSH_REDIS.getMessage());
		}
		return pageResult.success(null);
	}

}
