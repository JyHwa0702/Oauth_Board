package com.example.demo.service.email;

import com.example.demo.config.auth.EmailProperties;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;



@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final EmailProperties emailProperties;


    @Transactional
    public String sendAuthCode(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            sendMail(email);

            return "1";
        } else if (byEmail.isEmpty()) {
            return "2";
        }

        return "3";
    }


    @Transactional
    public void sendMail(String email){

        //임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888)+111111);
        String subject ="TRIP VIEW 코드인증메일입니다.";
        String text ="코드번호는 "+authKey+" 입니다. 입력칸에 입력해주세요.";
        SimpleMailMessage message = createMessage(email, subject,text);
        redisUtil.setDataExpire(authKey,email,60*3L);

        try{
            emailSender.send(message);
        }catch (MailException es){
            log.error("sendSimplemessage 메소드 실행 실패 email:{}",email);
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    private SimpleMailMessage createMessage(String to, String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }

    @Transactional
    public Optional<User> verifyEmail(String authKey) throws ChangeSetPersister.NotFoundException{
        String email = redisUtil.getData(authKey);

        if(email == null){
            throw new ChangeSetPersister.NotFoundException();
        }
        redisUtil.deleteData(authKey);
        Optional<User> user = userRepository.findByEmail(email);

        return user;
    }


    public Optional<User> getUser(String email,String authKey,Model model) throws ChangeSetPersister.NotFoundException {

        String keyEmail = redisUtil.getData(authKey);
        log.info("try안에서의 keyEmail : "+ keyEmail);

        Optional<User> user = verifyEmail(authKey);

        if (!user.isPresent()){
            throw new ChangeSetPersister.NotFoundException();
        }
        return user;

    }
}

