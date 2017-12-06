package com.enation.app.shop.member.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.member.model.po.MemberCollect;
import com.enation.app.shop.member.service.IStoreCollectManager;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 收藏店铺	Action
 * @author xulipeng
 *
 */ 

@Controller
@RequestMapping("/api/b2b2c/store-collect")
public class StoreCollectApiController extends GridController{
	@Autowired
	private IStoreCollectManager storeCollectManager;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IShopManager storeManager;
	/**
	 * 添加收藏店铺
	 * @param member 店铺会员,StoreMember
	 * @param store_id 店铺Id,Integer
	 * @param collect	收藏店铺,MemberCollect
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="add-collect")
	public JsonResult addCollect(Integer store_id){
		Seller member=sellerManager.getSeller();
		if(member!=null){
			if(member.getStore_id()!=null && member.getStore_id().equals(store_id)){
				return JsonResultUtil.getErrorJson("不能收藏自己的店铺！"); 
			}
			MemberCollect collect = new MemberCollect();
			collect.setMember_id(member.getMember_id());
			collect.setStore_id(store_id);
			try {
				boolean result = storeCollectManager.isCollect(member.getMember_id(),store_id);
				
				//判断是否收藏过，如果为真，表示已经收藏
				if(result){
					return JsonResultUtil.getErrorJson("已经收藏过了！"); 
				}else{
					this.storeCollectManager.addCollect(collect);
					this.storeManager.addcollectNum(store_id);
					return JsonResultUtil.getSuccessJson("收藏成功！");
				}
				
			} catch (RuntimeException e) {
				return JsonResultUtil.getErrorJson(e.getMessage());
			}
		}else{
			return JsonResultUtil.getErrorJson("请登录！收藏失败！");
		} 
	}
	/**
	 * 删除收藏店铺
	 * @param celloct_id 收藏店铺Id,Integer
	 * @param store_id 店铺Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="del")
	public JsonResult del(Integer store_id,Integer celloct_id){
		try {
			//增加权限校验
			Seller member=sellerManager.getSeller();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！删除收藏失败！");
			}
			
			MemberCollect celloct = storeCollectManager.getCollect(celloct_id);
			if(celloct==null||!celloct.getMember_id().equals(member.getMember_id())){
				return JsonResultUtil.getSuccessJson("您没有操作权限");
			}
			
			if(!celloct.getStore_id().equals(store_id)){
				return JsonResultUtil.getSuccessJson("您没有操作权限");
			}
			
			this.storeCollectManager.delCollect(celloct_id);
			this.storeManager.reduceCollectNum(store_id);
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败，请重试！");
		} 
	} 
	
	
}
