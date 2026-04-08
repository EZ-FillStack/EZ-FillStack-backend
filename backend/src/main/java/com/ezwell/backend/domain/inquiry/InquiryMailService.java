package com.ezwell.backend.domain.inquiry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryMailService {
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
    /**
     * 문의 답변 이메일 발송
     *
     * @param toEmail   수신자 (문의한 유저 이메일)
     * @param title     문의 제목
     * @param reply     관리자 답변 내용
     */
	
	@Async // 별도의 스레드에서 실행. 메인 로직 속도에 영향x
	public void sendReplyEmail(String toEmail, String title, String reply) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject("[EGO] 문의 답변 안내: "+title);
			
			// 이메일 본문(HTML)
			String htmlBody = """
	                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
	                    <h2 style="color: #333;">문의 답변 안내</h2>
	                    <hr/>
	                    <p><strong>문의 제목:</strong> %s</p>
	                    <hr/>
	                    <h3>답변 내용</h3>
	                    <p style="background:#f9f9f9; padding:16px; border-radius:8px;">%s</p>
	                    <hr/>
	                    <p style="color:#888; font-size:12px;">본 메일은 발신 전용입니다.</p>
	                </div>
	                """.formatted(title, reply.replace("\n", "<br/>"));
			
			helper.setText(htmlBody,true);
			mailSender.send(message);
			log.info("[문의 답변 이메일 발송] 수신: {}",toEmail);
			
		}catch(MessagingException e) {
			log.error("[문의 답변 이메일 발송 실패] 수산: {}, 사유: {}", toEmail, e.getMessage());
			throw new RuntimeException("이메일 발송 중 오류가 발생했습니다.",e);
		}
	}
}
