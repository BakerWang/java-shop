package com.enation.app.shop.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.member.model.po.MemberCollect;
import com.enation.app.shop.member.service.IMemberCollectManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

/**
 * 收藏店铺	manager
 * @author xulipeng
 * @author Kanon 2016-3-2 ；6.0版本改造
 *
 */
@Service("memberCollectManager")
public class MemberCollectManager  implements IMemberCollectManager {

	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#addCollect(com.enation.app.b2b2c.core.model.MemberCollect)
	 */
	@Override
	public void addCollect(MemberCollect collect) {
		Integer num = this.daoSupport.queryForInt("select count(0) from es_member_collect where member_id=? and store_id=?", collect.getMember_id(),collect.getStore_id());
		if(num!=0){
			throw new RuntimeException("店铺已收藏！");
		}else{
			collect.setCreate_time(DateUtil.getDateline());
			this.daoSupport.insert("es_member_collect", collect);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#delCollect(java.lang.Integer)
	 */
	@Override
	public void delCollect(Integer member_id,Integer store_id) {
		this.daoSupport.execute("delete from es_member_collect where member_id=? and store_id=?",member_id,store_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#getList(java.lang.Integer, int, int)
	 */
	@Override
	public Page getList(Integer memberid,int page,int pageSize) {
		String sql = "select sd.shop_logo,s.shop_name,sd.shop_province,sd.shop_city,sd.shop_region,sd.link_phone,s.shop_id,m.id,m.create_time,m.member_id " +
				" from ( es_member_collect m INNER JOIN es_shop s  ON s.shop_id=m.store_id ) INNER JOIN es_shop_detail sd  ON s.shop_id = sd.shop_id " +
				" where s.shop_id in (select store_id from es_member_collect where member_id=?) and m.member_id=?";
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize, memberid, memberid);
		return webpage;
	}
	
	@Override
	public boolean isCollect(int memberId, int storeId){
		return this.daoSupport.queryForInt("SELECT COUNT(0) FROM es_member_collect WHERE store_id=? AND member_id=?", storeId,memberId) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.member.service.IStoreCollectManager#getCollect(java.lang.Integer)
	 */
	@Override
	public MemberCollect getCollect(Integer celloct_id) {
		MemberCollect collect = this.daoSupport.queryForObject(
				"select * from es_member_collect where id=?",
				MemberCollect.class, celloct_id);
		return collect;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.member.service.IMemberCollectManager#getCount(java.lang.Integer)
	 */
	@Override
	public int getCount(Integer member_id) {
		return this.daoSupport.queryForInt("SELECT COUNT(0) FROM es_member_collect WHERE  member_id=?", member_id);
	} 

}

