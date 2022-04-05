## 部署方法

1. clone项目到本地
2. 修改后端项目的`src\main\resources`目录下`application.yml` 文件中的配置
2. 在 mysql 中创建数据库 `wish`，运行项目。
2. 默认端口为8888，可通过`localhost:8888/swagger-ui.html`来查看相关接口
2. 在IntelliJ IDEA中运行后端项目，为了保证项目成功运行，可以右键点击 `pom.xml` 选择 maven -> reimport ，并重启项目



## 配置文件说明

位于后端项目的`src\main\resources`目录下，由`application.yml` 与`logback.xml`(无需改动)组成，分别为项目基本配置与log相关配置。

该部分讲说明`application.yml`部分字段：

1. `datasourse`:数据库相关信息，默认数据库名为`wish`
2. `server.port`:默认端口为8888
2. `swagger.enable`:是否启用swagger
2. `app`:你的微信公众平台`id`与`secret`，用来获取登录授权信息