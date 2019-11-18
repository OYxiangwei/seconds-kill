package com.oy.secondskill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecondsKillApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecondsKillApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SecondsKillApplication.class, args);
        LOGGER.info("项目启动");
    }

}
