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
package net.duckling.vmt.common.domain;

import net.duckling.vmt.common.ldap.LdapMapping;
import net.duckling.vmt.common.ldap.LdapType;

/**
 * @author lvly
 * @since 2013-6-5
 */
@LdapMapping(objectClass={"foo"})
public class Foo {
	@LdapMapping(type = LdapType.DN)
	private String a;
	
	@LdapMapping(type=LdapType.RDN,value="rdn")
	private int b;
	
	@LdapMapping("c")
	private boolean c;
	
	@LdapMapping("d")
	private Integer[] d;
	
	@LdapMapping("e")
	private String[] e;
	
	@LdapMapping("f")
	private String f;
	
	@LdapMapping(type=LdapType.PASSWORD)
	private String g="password";
	
	
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public boolean isC() {
		return c;
	}
	public void setC(boolean c) {
		this.c = c;
	}
	public Integer[] getD() {
		return d;
	}
	public void setD(Integer[] d) {
		this.d = d;
	}
	public String[] getE() {
		return e;
	}
	public void setE(String[] e) {
		this.e = e;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	
	
}
