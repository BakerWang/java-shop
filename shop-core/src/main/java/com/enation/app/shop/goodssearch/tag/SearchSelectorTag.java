package com.enation.app.shop.goodssearch.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goodssearch.service.IGoodsSearchManager;
import com.enation.app.shop.goodssearch.service.SearchEngineFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 商品搜索选择器标签
 * 
 * @author kingapex 2013-7-29下午5:40:46
 */
@Component
@Scope("prototype")
public class SearchSelectorTag extends BaseFreeMarkerTag {

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		IGoodsSearchManager goodsSearchManager = SearchEngineFactory
				.getSearchEngine();
		
		Map map = goodsSearchManager.getSelector();
		
		return map;

	}

}
