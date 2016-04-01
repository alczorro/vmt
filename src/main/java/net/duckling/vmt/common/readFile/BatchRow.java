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
package net.duckling.vmt.common.readFile;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;

/**
 * @author lvly
 * @since 2013-8-27
 */
public class BatchRow {
	private List<BatchCell> cells=new ArrayList<BatchCell>();
	private String dn;
	private boolean needAdd;
	private boolean canDo=true;
	private boolean changed;
	private String custom;
	
	
	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
	
	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean isChanged) {
		this.changed = isChanged;
	}

	public void checkCells(){
		for(BatchCell cell:cells){
			if(cell.isChanged()){
				changed=true;
			}
			if(!CommonUtils.isNull(cell.getStatus())){
				canDo=false;
			}
		}
	}
	
	public List<BatchCell> getCells() {
		return cells;
	}
	public void setCells(List<BatchCell> cells) {
		this.cells = cells;
	}
	public boolean isCanDo() {
		return canDo;
	}

	public void setCanDo(boolean canAdd) {
		this.canDo = canAdd;
	}

	public boolean isNeedAdd() {
		return needAdd;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public void setNeedAdd(boolean needAdd) {
		this.needAdd = needAdd;
	}

	public void addCell(String before,String after,String key){
		BatchCell cell=new BatchCell(before,after,key);
		cells.add(cell);
	}
	public void addCell(String before,String after,String key,String error){
		BatchCell cell=new BatchCell(before,after,key);
		cell.setStatus(error);
		cells.add(cell);
	}
}
