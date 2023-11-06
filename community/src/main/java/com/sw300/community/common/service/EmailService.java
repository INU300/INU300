package com.sw300.community.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String email, String imageUrl) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            /*// 원격 이미지의 URL
            String imageUrl = "http://s.pstatic.net/dthumb.phinf/?src=%22https%3A%2F%2Fs.pstatic.net%2Ftvcast.phinf%2F20221222_112%2Fa9d0Y_1671682776129lsIoA_JPEG%2F1671682768260.jpg%22&type=nf308_200&service=navermain";
            String text = "위로의 문구";
*/

            // HTML 이메일 본문 작성
            String emailContent = "<html><body>위로의 이미지<br>"
                    + "<img src=" + imageUrl + " alt=\"img\"'/></body></html>";
            helper.setText(emailContent, true);

            // 이메일 전송
            helper.setTo(email); // 수신자 이메일 주소 설정
            helper.setSubject("300커뮤니티의 유해성 글에 대한 피드백입니다.");

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리 코드 작성
        }
    }
}
