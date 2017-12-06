package com.enation.app.shop.goods;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.framework.test.SpringTestSupport;
/**
 * 
 * 添加商品测试类
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 上午11:27:08
 */
public class GoodsAddTest extends SpringTestSupport{
	@Autowired 
	private IGoodsManager goodsManager;
	@Autowired
	private IMemberManager memberManager;
	@Test
	@Rollback(false)
	public void testAdd() throws Exception {
		memberManager.login("food");
		GoodsVo goodsVo =new GoodsVo();
		goodsVo.setGoods_name("苹果手机");
		goodsVo.setCategory_id(1);
		goodsVo.setShop_cat_id(1);
		goodsVo.setBrand_id(1);
		goodsVo.setSn("0003658474");
		goodsVo.setPrice(100d);
		goodsVo.setCost(105.00);
		goodsVo.setMktprice(150.00);
		goodsVo.setWeight(100.00);
		goodsVo.setGoods_transfee_charge(1);
		goodsVo.setIntro("手机");
		goodsVo.setHave_spec(1);
		goodsVo.setUnit("千克");
		List<GoodsParams> goodsParamsList =new ArrayList<GoodsParams>();
		GoodsParams goodsParams =new GoodsParams();
		goodsParams.setGoods_id(1);
		goodsParams.setParam_id(1);
		goodsParams.setParam_name("大小");
		goodsParams.setParam_value("128g");
		goodsParamsList.add(goodsParams);
		goodsVo.setGoodsParamsList(goodsParamsList);
		List<GoodsSkuVo> skuList =new ArrayList<GoodsSkuVo>();
		GoodsSkuVo goodsSku =new GoodsSkuVo();
		goodsSku.setCategory_id(1);
		goodsSku.setGoods_id(1);
		goodsSku.setGoods_name("苹果手机");
		goodsSku.setSn("0000265845");
		goodsSku.setPrice(100.00);
		goodsSku.setQuantity(2);
		goodsSku.setEnable_quantity(2);
		goodsSku.setSeller_id(1);
		goodsSku.setSeller_name("手机店");
		goodsSku.setWeight(100.00);
		List<SpecValueVo> specValues = new ArrayList<SpecValueVo>();
		SpecValueVo specValue =new SpecValueVo();
		specValue.setSpec_id(1);
		specValue.setSpec_value("重量");
		specValue.setSpec_type(0);
		specValues.add(specValue);
		goodsSku.setSpecList(specValues);
		skuList.add(goodsSku);
		goodsVo.setSkuList(skuList);
		goodsManager.add(goodsVo);
	}
}
