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
/**
 * 
 */
package net.duckling.vmt.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvly
 * @since 2013-8-1
 */
public class TreeNode {
	public TreeNode(Object data){
		this.data=data;
	}
	public TreeNode(){
		
	}
	private Object data;
	
	public boolean isDept(){
		return data instanceof VmtDepart;
	}
	public boolean isUser(){
		return data instanceof VmtUser;
	}
	private List<TreeNode> children=new ArrayList<TreeNode>();
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void addChildren(TreeNode node){
		this.children.add(node);
	}
}
