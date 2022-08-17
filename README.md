
NTS-SPRINGBOOT-PLUGIN-FRAMEWORK
天溯SPRINGBOOT 插件化框架
===========================

1. 模块简介:  
本框架为基于 SPRING-BOOT 2 框架
以及 pf4j 搭建 插件化框架

2. 功能说明:
插件(jar)加载 / 卸载
插件 bean注册
插件 接口注册/注销
插件 支持mybatis/mybatisplus

3. 使用说明:  
略

4. 开发目录结构描述              
├── nts-springboot-plugin-framework-core   // 框架的主要业务  
├── nts-springboot-plugin-framework-starter  // spring boot starter  
│   └──                         // 代码路径  
├── README.md    // 描述说明  
└── pom.xml      // pom文件

---------------------------------------------------------------------

版本号: **V1.0.0 2022-04-14**  

说明:  
# 1、8月10日，2.15.0 hlms-rbac服务变更
# 2、功能变更  
## &ensp; a. 支持eam增量同步同步数据 用户部门角色，角色权限绑定关系（hlms-incrment-sync)  

# 3、版本合入
## release-2.12.3001 queryRoleAttrDict接口添加loginName和租户id支持
         
# 4、配置文件  
## &ensp; 暂无变更  

# 5、数据库升级脚本说明  
## liquibase脚本，如需要了解sql数据变更，具体请参见hlms-rbac-spring-boot-autoconfigure中的
## classpath:config/liquibase/changelog/2021070911_rbac2150_update.sql

# 6、其它相关设置  
## &ensp; 无