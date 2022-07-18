# 说明

包含了一些常用的自定义的starter，使用的时候可以将本项目作为POM导入，并选择合适的start进行引入使用。该项目使用[https://github.com/dachengxi/parent-pom](https://github.com/dachengxi/parent-pom)作为父模块，可以根据实际需要选择保留或者去除，使用前请先将parent-pom发布到仓库中。

# starter列表

每个starter的使用方法可参考general-starter-sample模块的示例。

- log-spring-boot-starter：日志记录
- general-model-starter：常用的返回值、错误码等模型定义

# starter使用方法

所有的starter使用，首先需要将general-starter-parent进行导入，代码如下：

```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>me.cxis</groupId>
            <artifactId>general-starter-parent</artifactId>
            <version>${project.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## log-spring-boot-starter使用方法

在项目中引入`log-spring-boot-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>me.cxis</groupId>
        <artifactId>log-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

在配置文件application.yml中启用日志记录功能：

```
general:
  starter:
    log:
      enable: true
```

运行项目，尝试调用接口即可看到日志输出，输出日志如下：

```
2022-07-18 10:54:58.435  INFO 3682 --- [nio-8080-exec-1] me.cxis.starter.log.support.LogFilter    : Request GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1
2022-07-18 10:54:58.486  INFO 3682 --- [nio-8080-exec-1] me.cxis.starter.log.support.LogFilter    : Response GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1, status=200, payload=log get id: 123
2022-07-18 10:54:58.677  INFO 3682 --- [nio-8080-exec-2] me.cxis.starter.log.support.LogFilter    : Request GET /favicon.ico, client=0:0:0:0:0:0:0:1, referer=http://localhost:8080/sample/log/get?id=123
2022-07-18 10:54:58.683  INFO 3682 --- [nio-8080-exec-2] me.cxis.starter.log.support.LogFilter    : Response GET /favicon.ico, client=0:0:0:0:0:0:0:1, referer=http://localhost:8080/sample/log/get?id=123, status=404
2022-07-18 10:55:59.048  INFO 3682 --- [nio-8080-exec-6] me.cxis.starter.log.support.LogFilter    : Request POST /sample/log/post, client=0:0:0:0:0:0:0:1, payload={"id": 234}
2022-07-18 10:55:59.113  INFO 3682 --- [nio-8080-exec-6] m.c.s.s.controller.SampleController      : log post, log: LogModel{id=234, content='null'}
2022-07-18 10:55:59.126  INFO 3682 --- [nio-8080-exec-6] me.cxis.starter.log.support.LogFilter    : Response POST /sample/log/post, client=0:0:0:0:0:0:0:1, status=200, payload={"id":234,"content":null}
```

## general-model-starter使用方法

在项目中引入`general-model-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>me.cxis</groupId>
        <artifactId>general-model-starter</artifactId>
    </dependency>
</dependencies>
```

在项目中直接引用相关的类即可：
- 返回值使用`Result`或者`PageResult`进行包装
- 分页请求参数继承`PageQuery`即可
- 如果方法返回空，可以使用`Result<Nil>`作为返回值 
- 自定义项目的`ErrorCode`如下：

```
public enum ErrorCode implements BaseErrorCode {
    
    SYSTEM_ERROR (1, "系统错误")
    ;

    ErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;
    
    private String errorMsg;
    
    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
```
- 如果需要抛出业务异常，请使用：`ServiceException`