package com.zl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuolin
 * @program: mail
 * @date 2018/11/29
 * @description: ${description}
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {


    @Autowired
    MailComponent mailComponent;

    @Test
    public void sendSimpleMail () throws MessagingException {
        mailComponent.sendSimpleMail("zhuolin@vortexinfo.cn", "zhuolin@vortexinfo.cn", "这是主题",
                "C:\\Users\\zhuolin\\Desktop\\1.xls");
    }

    @Test
    public void sendHtmlMail () throws MessagingException, IOException {
        FileInputStream fileInputStream = new FileInputStream(ApplicationTest.class.getClassLoader().getResource("templates/mail.html").getPath());
        BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        mailComponent.sendHtmlMail("zhuolin@vortexinfo.cn", "zhuolin@vortexinfo.cn", "主题", buffer.toString());
    }

    @Test
    public void sendAttachmentsMail () throws MessagingException {
        mailComponent.sendAttachmentsMail("zhuolin@vortexinfo.cn", "zhuolin@vortexinfo.cn", "主题", "这是内容",
                "C:\\Users\\zhuolin\\Desktop\\1.xls", "C:\\Users\\zhuolin\\Desktop\\云组态.txt");
    }

    @Test
    public void sendInlineResourceMail () throws MessagingException {
        String rscId = "neo006";
        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        mailComponent.sendInlineResourceMail("zhuolin@vortexinfo.cn", "zhuolin@vortexinfo.cn", "主题", content,
                "C:\\Users\\zhuolin\\Desktop\\a0.jpg", rscId);
    }

    @Test
    public void sendTemplateMail () throws MessagingException {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("title", "title");
        dataMap.put("content", "这是内容");
        mailComponent.sendTemplateMail("zhuolin@vortexinfo.cn", "zhuolin@vortexinfo.cn", "主题",
               "mail", dataMap);
    }

}
