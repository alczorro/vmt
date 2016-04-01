/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.vmt.service.impl;

import java.util.List;

import net.duckling.vmt.dao.INodeDAO;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IVmtIndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.stereotype.Service;
/**
 * @author lvly
 * @since 2013-6-21
 */
@Service
public class NodeServiceImpl implements INodeService{
	/**
	 * Node对应的dao
	 */
	@Autowired
	private INodeDAO nodeDAO;
	/**
	 * 属性更改service
	 */
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private IVmtIndexService indexService;
	@Override
	public LdapNode getNode(String dn) {
		return nodeDAO.getNode(dn);
	}
	@Override
	public void updateSonAndSelfDisplayName(String pdn, String newName) {
		DistinguishedName dn=new DistinguishedName(pdn);
		nodeDAO.updateSonsAndMyDisplayName(pdn,dn.size(),newName);
	}
	@Override
	public void updateMove(String dn, String moveto) {
		LdapNode node=getNode(moveto);
		attrService.update(dn, "vmt-current-display", node.getCurrentDisplay());
	}
	@Override
	public boolean isSymbolUsed(String pdn, String symbol) {
		return nodeDAO.isSymbolUsed(pdn, symbol);
	}
	public void updateSubObjectVisible(String dn,boolean visible,boolean updateSelf){
		List<ListView> views=searchService.searchAllByTree(dn);
		for(ListView view:views){
			if(!updateSelf&&view.getDn().equals(dn)){
				continue;
			}
			if(view.getType().equals(ListView.TYPE_USER)){
				indexService.updateUserVisible(view.getDn(),visible);
			}
			attrService.update(view.getDn(), "vmt-visible", visible);
		}
	}
	

}
