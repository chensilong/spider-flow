package org.spiderflow.core.executor.function;

import org.apache.ibatis.annotations.Param;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.core.utils.EmailUtils;
import org.spiderflow.executor.FunctionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2019-12-06
 *
 * @author Octopus
 */
@Component
@Comment("邮件常用方法")
public class EmailExecutor implements FunctionExecutor {
    @Autowired
    private JavaMailSender javaMailSender;
    // 发送者
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public String getFunctionPrefix() {
        return "mail";
    }

    @Comment("获取数据发送到邮件")
    @Example("${mail.send(list,mail)}")
    public void send(List<String> list, String mail) {
        StringBuffer log=new StringBuffer();
        list.forEach(e-> {
            log.append(e).append("<br><br>");
        });
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("爬取索取结果");
        message.setText(log.toString());
        message.setTo(mail);
        javaMailSender.send(message);
    }
}
