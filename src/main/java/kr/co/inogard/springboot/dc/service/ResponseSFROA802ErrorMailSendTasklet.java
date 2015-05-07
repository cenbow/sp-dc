package kr.co.inogard.springboot.dc.service;

import java.util.Iterator;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class ResponseSFROA802ErrorMailSendTasklet implements Tasklet {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseSFROA802ErrorMailSendTasklet.class);
	
	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender mailSender;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		String recipientTemp = "";
		if("responseSFROA0802ExportToExternal".equals(chunkContext.getStepContext().getJobName())){
			recipientTemp = "twinmoon@inogard.co.kr"; // TODO : �ش� Job�� ����� ������ �� �̸��� �ּҸ� �����´�.
		}
		final String recipient	= recipientTemp;
		final String subject	= "Batch Job["+chunkContext.getStepContext().getJobName()+"] ���࿡�� ������ �߻��Ͽ����ϴ�.";
		String exitDescription 	= "";
		for(Iterator<StepExecution> iter = chunkContext.getStepContext().getStepExecution().getJobExecution().getStepExecutions().iterator(); iter.hasNext(); ){
			StepExecution se = iter.next();
			if("FAILED".equals(se.getExitStatus().getExitCode())){
				exitDescription += se.getExitStatus().getExitDescription() + "\r\n";
			}
		}
		final String body = exitDescription; // ���� ������ ���� ����
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
            	mimeMessage.setFrom(new InternetAddress("system@inogard.co.kr"));
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(body);
            }
        };

        try {
            this.mailSender.send(preparator);
        }
        catch (MailException ex) {
            log.error("���� �߼� ����", ex);
        }
        
		return RepeatStatus.FINISHED;
	}

}
