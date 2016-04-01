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
package net.duckling.vmt.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;
import net.duckling.vmt.domain.email.EmailLog;
import net.duckling.vmt.service.IMailService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件服务具体实现事宜
 * 
 * @author lvly
 * @since 2012-8-30
 */
@Service
public class MailServiceImpl implements IMailService {
	private static final Logger LOG = Logger.getLogger(MailServiceImpl.class);
	@Autowired
	private VmtConfig vmtConfig;
	
	private static Properties pro;
	private static Properties batchPro;
	/**
	 * 获得发送邮件的基本参数，单例
	 * @return pro Properties
	 * */
	private synchronized Properties getEmailParam(){
		if(pro==null){
			pro=new Properties();
			pro.setProperty("mail.smtp.host", vmtConfig.getStmpHost());
			pro.setProperty("mail.smtp.auth", vmtConfig.getStmpAuth());
			pro.setProperty("mail.pop3.host", vmtConfig.getPop3Auth());
		}
		return pro;
	}
	private synchronized Properties getBatchEmailParam(){
		if(batchPro==null){
			batchPro=new Properties();
			batchPro.setProperty("mail.smtp.host", vmtConfig.getStmpHost());
			batchPro.setProperty("mail.smtp.auth","false");
			batchPro.setProperty("mail.pop3.host", vmtConfig.getPop3Auth());
		}
		return batchPro;
	}
	public boolean sendAMail(Email mail,EmailGetter getter,List<EmailFile> files) {
		EmailLog el=new EmailLog();
		el.setSender(mail.getSenderCstnetId());
		el.setEmailTitle(mail.getTitle());
		el.setGetter(new String[]{getter.getGetterCstnetId()});
		try {
			Address[] address=new Address[]{new InternetAddress(getter.getGetterCstnetId())};
			MimeMessage msg=getMessage(address,mail,files);
			String sender=mail.getSenderCstnetId();
			if ((sender!= null)&& (sender.indexOf("@") != -1)) {
				cheat(msg, sender.substring(sender.indexOf("@")));
			}
			Transport.send(msg);
			LOG.info("Successfully send the mail to " + Arrays.toString(address));
			el.setSuccess(true);
			el.log();
		}catch (MessagingException|UnsupportedEncodingException e) {
			el.setSuccess(false);
			el.setDesc(e.getMessage());
			el.log();
			LOG.error("Exception occured while trying to send notification to: " + getter.getGetterCstnetId());
			LOG.error(e);
			LOG.debug("Details:", e);
		} 
		return false;
	}
	public boolean sendAMail(SimpleEmail mail){
		EmailLog el=new EmailLog();
		el.setSender("system");
		el.setEmailTitle(mail.getTitle());
		el.setGetter(mail.getAddress());
		try {
			String[]address=mail.getAddress();
			Address[] addressArray=new Address[address.length];
			int index=0;
			for(String str:address){
				if(!CommonUtils.isNull(str)){
					addressArray[index++]=new InternetAddress(str);	
				}
				index++;
			}
			MimeMessage msg=getMessage(addressArray,mail.getContent(), mail.getTitle());
			if ((vmtConfig.getEmailUserName() != null)&& (vmtConfig.getEmailUserName().indexOf("@") != -1)) {
				cheat(msg, vmtConfig.getEmailUserName().substring(vmtConfig.getEmailUserName().indexOf("@")));
			}
			Transport.send(msg);
			LOG.info("Successfully send the mail to " + Arrays.toString(address));
			el.setSuccess(true);
			el.log();

		} catch (MessagingException e) {
			LOG.error("Exception occured while trying to send notification to: " + Arrays.toString(mail.getAddress()));
			LOG.error(e);
			LOG.debug("Details:", e);
			el.setSuccess(false);
			el.setDesc(e.getMessage());
			el.log();
		}
		return false;
	}

	@Override
	public boolean sendEmail(SimpleEmail mail) {
		String[] address=mail.getAddress();
		LOG.debug("sendEmail() to: " +Arrays.toString(address));
		if(CommonUtils.isNull(address)){
			LOG.error("the address is empty!");
			return false ;
		}
		for(String email:address){
			mail.setAddress(new String[]{email});
			sendAMail(mail);
		}
		return true;
	}
	@Override
	public void sendCustomMail(Email email, List<EmailGetter> emailGetter,List<EmailFile> files) {
		for(EmailGetter eg:emailGetter){
			sendAMail(email, eg, files);
		}
	}
	private void cheat(MimeMessage mimeMessage, String serverDomain) throws MessagingException {
		mimeMessage.saveChanges();
		mimeMessage.setHeader("User-Agent", "Thunderbird 2.0.0.16 (Windows/20080708)");
		String messageid = mimeMessage.getHeader("Message-ID", null);
		messageid = messageid.replaceAll("\\.JavaMail.*", "@" + serverDomain + ">");
		mimeMessage.setHeader("Message-ID", messageid);
	}
	/**
	 * 包装类，用户的信息
	 * @author lvly
	 * @since 2013-6-21
	 */
	public static class EmailAuthenticator extends Authenticator {
		/**
		 * 账户密码包装类
		 */
		private final PasswordAuthentication passwordAuthentication;
		/**
		 * constructor
		 * @param userid userId
		 * @param password passWord
		 * */
		public EmailAuthenticator(String userid, String password) {
			passwordAuthentication = new PasswordAuthentication(userid, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return passwordAuthentication;
		}
	}
	private MimeMessage getMessage(Address[] address,Email email,List<EmailFile> files) throws MessagingException, UnsupportedEncodingException{
		Session session = Session.getInstance(getBatchEmailParam(),
				new EmailAuthenticator(email.getSenderCstnetId(), ""));
		session.setDebug(false);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(email.getSenderCstnetId(),email.getSenderName(),KEY.GLOBAL_ENCODE));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
			msg.setFrom(new InternetAddress(email.getSenderCstnetId()));
		}
		msg.setRecipients(Message.RecipientType.TO, address);
		try {
			msg.setSubject(MimeUtility.encodeText(email.getTitle() ,KEY.GLOBAL_ENCODE,"B"));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(),e);
		}
		msg.setSentDate(new Date());
		Multipart mp = new MimeMultipart();
	
		MimeBodyPart txtmbp = new MimeBodyPart();
		String contentType = "text/html;charset=UTF-8";
		txtmbp.setContent(email.getContent(), contentType);
		mp.addBodyPart(txtmbp);
		if(!CommonUtils.isNull(files)){
			for(EmailFile f:files){
				MimeBodyPart mbp = new MimeBodyPart(); 
		        FileDataSource fds = new FileDataSource(f.getFile()); // 得到数据源 
		        mbp.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart 
		        mbp.setFileName(MimeUtility.encodeText(f.getFileName(), KEY.GLOBAL_ENCODE,"B")); // 得到文件名同样至入BodyPart 
		        mp.addBodyPart(mbp); 
		        } 
			}
		
		msg.setContent(mp);
		return msg;
	}
	
	private MimeMessage getMessage(Address[] addressArray,String content,String title) throws  MessagingException{
		Session session = Session.getInstance(getEmailParam(),
				new EmailAuthenticator(vmtConfig.getEmailUserName(), vmtConfig.getEmailPassword()));
		session.setDebug(false);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(vmtConfig.getEmailUserName(),vmtConfig.getEmailDisplay(),KEY.GLOBAL_ENCODE));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
			msg.setFrom(new InternetAddress(vmtConfig.getEmailUserName()));
		}
		msg.setRecipients(Message.RecipientType.TO, addressArray);
		try {
			msg.setSubject(MimeUtility.encodeText( title ,KEY.GLOBAL_ENCODE,"B"));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(),e);
		}
		msg.setSentDate(new Date());
		Multipart mp = new MimeMultipart();
		MimeBodyPart txtmbp = new MimeBodyPart();
		String contentType = "text/html;charset=UTF-8";
		txtmbp.setContent(content, contentType);
		mp.addBodyPart(txtmbp);
		msg.setContent(mp);
		return msg;
	}

}
