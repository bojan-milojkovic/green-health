package com.green.health.email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.thymeleaf.context.Context;

@Component
public class EmailUtil {

	public JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
    private String hostName;
	
	@Autowired
	public EmailUtil(JavaMailSender javaMailSender, VelocityEngine velocityEngine, String hostName) {
		this.javaMailSender = javaMailSender;
		this.velocityEngine = velocityEngine;
		this.hostName = hostName;
	}
	
	public void confirmRegistration(final String key, final String name, final String email) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", name);
		model.put("url", hostName+"/users/act/"+key);
		
		sendEmail("User activation", email, "user_activation.vm", model);
	}
	
	private void sendEmail(final String subject, final String to, final String templateName, final Map<String, Object> model) {
		try {
			//TODO: logger.debug("Sending the '"+subject+"' email ...");
			MimeMessage message = javaMailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message,
			        MimeMessageHelper.MULTIPART_MODE_NO,
			        StandardCharsets.UTF_8.name());
			helper.setFrom("donotreply@plotfinder.com");
			helper.setTo(to);
			helper.setSubject(subject);
			
			Context context = new Context();
			context.setVariables(model);
			
			helper.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/"+templateName, model), true);
			
			javaMailSender.send(message);
			System.out.println("Message sent successfully!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
