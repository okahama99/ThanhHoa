package com.example.thanhhoa.services.otp;

import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class OtpService {
    private OtpGenerator otpGenerator;
    private UserService userService;
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Async
    public void send(String to, String email, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("nhtn08101999@gmail.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.out.println("gửi mail sai rồi");
        }
    }

    public Boolean generateOTPForResetPassword(String email) throws MessagingException {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(email);
        System.out.println(otpValue);
        if (otpValue == -1)
        {
            return  false;
        }
        tblAccount user = userRepository.findByEmailAndStatus(email, Status.ACTIVE);
        // fetch user e-mail from database
        // generate emailDTO object
        send(email, buildSendOtpEmail(user.getUsername(), otpValue), "Lấy lại mật khẩu");
        // send generated e-mail
        return true;
    }
    /**
     * Method for validating provided OTP
     *
     * @param key - provided key
     * @param otpNumber - provided OTP number
     * @return boolean value (true|false)
     */
    public Boolean validateOTP(String key, Integer otpNumber)
    {
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }

    private String buildSendOtpEmail(String name, int otp) {
        return "<h1 style=\"color: #5e9ca0; text-align: center;\">--- Thanh Hoa ---</h1>\n" +
                "<h2 style=\"color: #2e6c80; text-align: center;\">Thay đổi mật khẩu:</h2>\n" +
                "<p>Xin ch&agrave;o&nbsp;"+ name + "<br />Ch&uacute;ng t&ocirc;i l&agrave; team Thanh Hoa,&nbsp;</p>\n" +
                "<p>Qu&yacute; kh&aacute;ch nhận được mail n&agrave;y l&agrave; v&igrave; qu&yacute; kh&aacute;ch (cũng c&oacute; thể l&agrave; ai đ&oacute; giả mạo danh nghĩa qu&yacute; kh&aacute;ch) thực hiện thay đổi mật khẩu tại Thanh Hoa, vui l&ograve;ng ghi lại m&atilde; otp dưới đ&acirc;y để tiếp tục tạo mật khẩu mới.</p>\n" +
                "<p>"+ otp + "&nbsp;</p>\n" +
                "<p>M&atilde; opt sẽ c&oacute; hiệu lực trong v&ograve;ng 5 ph&uacute;t.</p>\n" +
                "<p>Nếu đ&acirc;y kh&ocirc;ng phải do qu&yacute; kh&aacute;ch thực hiện thao t&aacute;c, c&oacute; thể y&ecirc;n t&acirc;m bỏ qua nội dung mail n&agrave;y.</p>\n" +
                "<p>Cảm ơn qu&yacute; kh&aacute;ch đ&atilde; sử dụng dịch vụ của ch&uacute;ng t&ocirc;i, xin ch&uacute;c qu&yacute; kh&aacute;ch c&oacute; một ng&agrave;y tốt l&agrave;nh.</p>\n" +
                "<p>Xin vui l&ograve;ng kh&ocirc;ng trả lời mail từ hệ thống.</p>\n" +
                "<p><strong>&nbsp;</strong></p>";
    }
}
