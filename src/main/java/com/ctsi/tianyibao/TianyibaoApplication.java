package com.ctsi.tianyibao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.ctsi.tianyibao.*.mapper")
public class TianyibaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TianyibaoApplication.class, args);
    }
}
