package com.becklu;

import com.becklu.dao.UserDOMapper;
import com.becklu.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//@EnableAutoConfiguration
    @SpringBootApplication(scanBasePackages = {"com.becklu"})
//@RestController
//    @Controller
@MapperScan("com.becklu.dao")
public class App 
{
    @Autowired
    UserDOMapper userDOMapper;

//    @RequestMapping("/")
    public UserDO home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
//        if(null == userDO){
//            return "用户不存在";
//        }else{
//            return userDO.getName();
//        }
        if(null == userDO){
            return null;
        }else{
            return userDO;
        }
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
       SpringApplication.run(App.class,args);
    }
}
