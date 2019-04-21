package com.itmayiedu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 根配置
 */
@Configuration
@ComponentScan("com.itmayiedu") //扫描整个Spring项目的，并创建bean 到Spring容器
public class RootConfig {
}
