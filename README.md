# seconds-kill
分布式秒杀系统<br>
<h3>技术栈</h3><br>
JDK1.8、Maven、Mysql、IntelliJ IDEA、SpringBoot1.5.10、zookeeper3.4.6、kafka_2.11、redis-2.8.4、curator-2.10.0<br>


<h3>启动说明</h3><br>
启动前 请配置 application.properties 中相关redis、zk以及kafka相关地址，建议在Linux下安装使用。<br>
数据库脚本位于 src/main/resource/sql 下面，启动前请自行导入。<br>
配置完成，运行Application中的main方法，访问 http://localhost:8080/seckill/swagger-ui.html 进行API测试。<br>
秒杀商品页：http://localhost:8080/seckill ，部分功能待完成。<br>

