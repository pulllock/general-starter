# 说明

包含了一些常用的自定义的starter，使用的时候可以将本项目作为POM导入，并选择合适的start进行引入使用。该项目使用[https://github.com/dachengxi/parent-pom](https://github.com/dachengxi/parent-pom)作为父模块，可以根据实际需要选择保留或者去除，使用前请先将parent-pom发布到仓库中。

# starter列表

每个starter的使用方法可参考`general-starter-sample`模块的示例。

- `log-spring-boot-starter`：日志记录、日志增加traceId实现链路追踪功能
- `general-model-starter`：常用的返回值、错误码等模型定义
- `general-model-spring-boot`：全局返回值包装、全局异常处理，同时集成了`general-model-starter`模块的功能

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

引入此模块后可以有如下功能：

- 请求响应日志打印，需要在配置中开启
- traceId功能，默认开启

使用步骤如下，首先在项目中引入`log-spring-boot-starter`模块：

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
- 各项目共用的通用的错误码使用：`CommonErrorCode`
- 自定义项目的`ErrorCode`需要实现接口：`BaseErrorCode`，示例如下：

```
public enum ErrorCode implements BaseErrorCode {
    
    USER_NOT_EXIST (10001, "用户不存在")
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

## general-model-spring-boot-starter

`general-model-spring-boot-starter`会自动引入`general-model-starter`模块的功能，如果引用了`general-model-spring-boot-starter`模块后，可以不用再单独引用`general-model-starter`模块。

在项目中引入`general-model-spring-boot-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>me.cxis</groupId>
        <artifactId>general-model-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，会自动引入以下功能：

- 对返回值使用`Result`进行包装，如果不需要此功能可以使用配置`general.starter.wrap.result=false`进行关闭
- 引入全局包装的同时，也会自动将`MappingJackson2HttpMessageConverter`转换器添加到转换器列表最前面，如果全局返回值包装功能关闭，则此功能也会自动关闭掉
  - 将`MappingJackson2HttpMessageConverter`放到最前面，可以解决方法返回String的时候统一包装报错的问题，另外需要注意，如果方法返回的是String，请在方法上添加`produces = MediaType.APPLICATION_JSON_VALUE`来进行配合使用
- 会对全局异常进行处理，并使用`Result`进行包装，如果不需要此功能可以使用配置`general.starter.wrap.exception=false`进行关闭

# MDC实现日志追踪（添加traceId）

日志追踪功能的流程如下：

1. 前端生成请求的ID，并添加到请求头中，带到服务端。（这一步可选）
2. 服务端（网关、请求处理系统）使用过滤器（拦截器）拦截请求，从请求中找到前端带来的请求ID或者创建一个新的请求ID，并将请求ID添加到MDC中
3. 打印日志
4. 如果还需要调用其他的服务，比如使用Feign调用或者使用RestTemplate调用，可以使用对应的过滤器（拦截器）设置相应的请求头，将请求ID传递到其他的系统中
5. 被调用的服务也使用第2步的过滤器（拦截器）来获取请求ID并设置到MDC中
6. 异步请求、自定义线程池、定时调度等需要自定义配置MDC

## 实现步骤

实现代码可参考`log-spring-boot-starter`模块。

1. 实现`TraceIdUtil`工具类
2. 实现过滤器`TraceFilter`，添加`TraceId`
3. 配置`logback.xml`配置文件
4. `Feign`调用上添加`TraceId`
5. `RestTemplate`调用上添加`TraceId`
6. 异步请求、自定义线程池、定时调用