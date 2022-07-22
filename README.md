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
2022-07-22 10:35:32.391 INFO [http-nio-8080-exec-2] [8230c21c47964341a5fc072e2ee103b9] me.cxis.starter.log.support.LogFilter.logForRequest[133]: Request GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1
2022-07-22 10:35:32.393 INFO [http-nio-8080-exec-2] [8230c21c47964341a5fc072e2ee103b9] me.cxis.starter.log.support.LogFilter.logForResponse[98]: Response GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1, status=200, payload=log get id: 123
```

日志的格式配置如下：

```
%date{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread] [%X{traceId}] %logger.%method[%L]: %message%n
```

- `%date{yyyy-MM-dd HH:mm:ss.SSS}`：日期时间，并指定格式
- `%level`：日志的等级
- `%thread`：线程名字
- `%X{traceId}`：自定义的traceId
- `%logger`：输出Logger的名称，和`%class`类名类似
- `%method`：方法名（会对性能有影响）
- `%L`：行数（会对性能有影响）
- `%message`：具体的日志消息
- `%n`：换行

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

# logback格式

- `%lo{length}`、`%logger{length}`：输出日志的名称，length可以指定长度
- `%C{length}`、`%class{length}`：输出类的全限定名，会对性能有影响
- `%d{pattern}`、`%date{pattern}`、`%d{pattern, timezone}`、`%date{pattern, timezone}`：输出日期，pattern中指定日期的格式
- `%L`：行号，会对性能有影响
- `%m`、`%msg`、`%message`：具体的日志
- `%M`、`%method`：方法名，会对性能有影响
- `%n`：换行
- `%p`、`%le`、`%level`：日志等级
- `%t`、`%thread`：线程名字
- `%X{key:-defaultValue}`：输出MDC中自定义的key对应的值