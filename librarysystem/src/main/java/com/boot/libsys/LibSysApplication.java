package com.boot.libsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Lucky
 * @version 1.0
 * @since 2021/07/11 15:39
 */

@MapperScan(value = "com.boot.libsys.mapper")//开启扫描Mapper接口
@SpringBootApplication
public class LibSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibSysApplication.class, args);
    }
}
