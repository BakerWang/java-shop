package com.enation.app.shop.promotion.groupbuy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyArea;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyAreaManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 团购地区管理 
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年8月29日 下午8:55:01 
 *
 */
@Service
public class GroupBuyAreaManager implements IGroupBuyAreaManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager#list(int, int)
	 */
	@Override
	public Page list(int pageNo, int pageSize) {
		String sql ="select * from es_groupbuy_area order by area_order ";
		return this.daoSupport.queryForPage(sql,pageNo, pageSize,GroupBuyArea.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager#listAll()
	 */
	@Override
	public List<GroupBuyArea> listAll() {
		String sql ="select * from es_groupbuy_area order by area_order ";
		return this.daoSupport.queryForList(sql,GroupBuyArea.class);
	}

	/*
	 * (non-Javadoc)
	 * @see IGroupBuyAreaManager#get(int)
	 */
	@Override
	public GroupBuyArea get(int area_id) {
		 
		return (GroupBuyArea)this.daoSupport.queryForObject("select * from es_groupbuy_area where area_id=?",  GroupBuyArea.class,area_id);
	}

	/*
	 * (non-Javadoc)
	 * @see IGroupBuyAreaManager#add(GroupBuyArea)
	 */
	@Override
	public void add(GroupBuyArea groupBuyArea) {
		this.daoSupport.insert("es_groupbuy_area", groupBuyArea);

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager#update(com.enation.app.shop.component.groupbuy.model.GroupBuyArea)
	 */
	@Override
	public void update(GroupBuyArea groupBuyArea) {
		this.daoSupport.update("es_groupbuy_area", groupBuyArea,"area_id="+groupBuyArea.getArea_id());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager#delete(java.lang.Integer[])
	 */
	@Override
	public void delete(Integer[] area_id) {
		if(area_id==null || area_id.length==0) return;
		String id_str = StringUtil.arrayToString(area_id, ",");
		this.daoSupport.execute("delete from es_groupbuy_area where area_id in ("+id_str+")");
	}


}
