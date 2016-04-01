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
package net.duckling.vmt.web.helper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.duckling.vmt.domain.KEY;

import org.apache.log4j.Logger;
/**
 * 把json写入response的工具类，因为IE的奇葩，才用
 * @author lvly
 * @since 2013-5-27
 */
public final class JSONHelper {
	private JSONHelper(){}
	private static final Logger LOG = Logger.getLogger(JSONHelper.class);
	/**
	 * 把json写入response
	 * @param response
	 * @param object
	 */
	public static void writeJSONObject(HttpServletResponse response,Object object) {
		PrintWriter writer = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding(KEY.GLOBAL_ENCODE);
			writer = response.getWriter();
			writer.write(object.toString());
		} catch (IOException e) {
			LOG.error("JSONHelper write json object IOException:"+e.getMessage());
			LOG.debug(e.getStackTrace());
		}finally {
			if (writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}
}