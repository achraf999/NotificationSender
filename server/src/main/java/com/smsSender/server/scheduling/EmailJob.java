package com.smsSender.server.scheduling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smsSender.server.repositories.ContactRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



public class EmailJob extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Configuration config;
	

	@Autowired
	ContactRepository fileRepo;
	
	
	String [] recipients;
	String subject;
	String body;
	


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("Executing Job with key {}", context.getJobDetail().getKey());
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String subject = jobDataMap.getString("subject");
		String body = jobDataMap.getString("body");
		int daysNum = jobDataMap.getInt("daysNum");
		String status = jobDataMap.getString("status");

		List<String> recipients = fileRepo.findWantedEmails(daysNum);
		List<String> mats = fileRepo.findWantedMats(daysNum);

		String[] to = recipients.stream().toArray(String[]::new);
		Map<String, Object> model = new HashMap<>();
		// Add wanted dynamic vars : 
		model.put("Msg", body);
		try {
			sendMail("smsender4@gmail.com", to, subject, model);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();

		}
	}

	/*
	 * @Override
	 * protected void executeInternal(JobExecutionContext context) throws JobExecutionException { 
	 * logger.info("Executing Job with key {}", context.getJobDetail().getKey()); 
	 * JobDataMap jobDataMap = context.getMergedJobDataMap(); 
	 *
	 * sendMail("smsender4@gmail.com", recipients, subject, body); }
	 */
	
	

	private void sendMail(String fromEmail, String[] toEmail, String subject, Map<String, Object> model) throws IOException, TemplateException {
		try {
			logger.info("Sending Email to {}" + Arrays.toString(toEmail));
			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(message,  MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			messageHelper.setSubject(subject);
			messageHelper.setText(html, true);
			messageHelper.setFrom(fromEmail);
			messageHelper.setTo(toEmail);

			mailSender.send(message);
			
			logger.info("Email succesfully sent  to {}" + Arrays.toString(toEmail));
		} catch (MessagingException ex) {
			logger.error("Failed to send email to {}", Arrays.toString(toEmail));
			ex.getStackTrace();
		}
	}



	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
