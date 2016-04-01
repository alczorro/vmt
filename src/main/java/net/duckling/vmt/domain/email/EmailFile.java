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
package net.duckling.vmt.domain.email;

import java.io.File;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_email_file")
public class EmailFile {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("clb_id")
	private int clbId;
	@FieldMapping("file_name")
	private String fileName;
	@FieldMapping("uploader_name")
	private String uploaderName;
	@FieldMapping("uploader_cstnet_id")
	private String uploaderCstnetId;
	@FieldMapping("uploader_umt_id")
	private String uploaderUmtId;
	@FieldMapping("email_id")
	private int emailId;
	
	private File file;
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getEmailId() {
		return emailId;
	}
	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClbId() {
		return clbId;
	}
	public void setClbId(int clbId) {
		this.clbId = clbId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploaderName() {
		return uploaderName;
	}
	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}
	public String getUploaderCstnetId() {
		return uploaderCstnetId;
	}
	public void setUploaderCstnetId(String uploaderCstnetId) {
		this.uploaderCstnetId = uploaderCstnetId;
	}
	public String getUploaderUmtId() {
		return uploaderUmtId;
	}
	public void setUploaderUmtId(String uploaderUmtId) {
		this.uploaderUmtId = uploaderUmtId;
	}
}

