<!-- TOC -->
* [说明](#说明)
* [统一规范](#统一规范)
* [starter列表](#starter列表)
* [starter使用方法](#starter使用方法)
  * [log-spring-boot-starter使用方法](#log-spring-boot-starter使用方法)
  * [general-model-starter使用方法](#general-model-starter使用方法)
  * [general-model-spring-boot-starter](#general-model-spring-boot-starter)
  * [web-spring-boot-starter](#web-spring-boot-starter)
  * [feign-spring-boot-starter](#feign-spring-boot-starter)
  * [json-starter](#json-starter)
  * [jackson-spring-boot-starter](#jackson-spring-boot-starter)
* [MDC实现日志追踪（添加traceId）](#mdc实现日志追踪添加traceid)
  * [实现步骤](#实现步骤)
* [logback格式](#logback格式)
<!-- TOC -->

# 说明

包含了一些常用的自定义的starter，使用的时候可以将本项目作为POM导入，并选择合适的start进行引入使用。该项目使用[https://github.com/pulllock/parent-pom](https://github.com/pulllock/parent-pom)作为父模块，可以根据实际需要选择保留或者去除，使用前请先将parent-pom发布到仓库中。

# 统一规范

- 统一所有接口返回值格式，包括REST接口、Feign接口等等
- 统一业务异常、错误码定义
- 统一所有日期相关的格式，包括接口参数、接口返回值、缓存、消息等
- 统一日志打印格式
- 统一接口日志打印
- 统一添加链路追踪唯一标识，包括REST接口请求、Feign接口请求、MQ消息、异步操作、自定义线程池、定时任务等等
- 统一项目中使用的Jackson配置，包括REST接口的序列化和反序列化、Feign接口的序列化和反序列化、缓存对象时候的序列化和反序列化、消息的序列化和反序列化、JSON工具类的序列化和反序列化等

# starter列表

每个starter的使用方法可参考`general-starter-sample`模块的示例。

- `general-constant-starter`：常用的常量和配置定义，包含：
  - 统一的日期时间格式定义
  - 统一的Jackson配置定义
- `log-spring-boot-starter`：日志记录、日志增加traceId实现链路追踪功能
- `general-model-starter`：常用的返回值、错误码等模型定义
- `general-model-spring-boot`：全局返回值包装、全局异常处理，同时集成了`general-model-starter`模块的功能
- `web-spring-boot-starter`：给RestTemplate添加TraceId以及记录请求调用日志
- `feign-spring-boot-starter`：给使用`Feign`方式的请求添加TraceId
- `json-starter`：基于Jackson的Json工具类，统一日期的序列化和反序列化格式等
- `jackson-spring-boot-starter`：自定义使用Jackson序列化和反序列化格式等
- `redis-spring-boot-starter`：自定义Redis缓存配置、自定义RedisTemplate配置、提供基于Redis的锁等功能

# starter使用方法

所有的starter使用，首先需要将general-starter-parent进行导入，代码如下：

```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>fun.pullock</groupId>
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

- 请求响应日志打印，自动开启
- traceId功能，自动开启
- 可引入一个`MdcTaskDecorator`，可在自定义线程池的时候指定该装饰器，用以将线程上下文传递到子线程中

使用步骤如下，首先在项目中引入`log-spring-boot-starter`模块：

```xml
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>log-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

可以在配置文件application.yml中禁用日志记录功能：

```yaml
general:
  starter:
    log:
      enabled: false
```

运行项目，尝试调用接口即可看到日志输出，输出日志如下：

```
2022-07-22 10:35:32.391 INFO [http-nio-8080-exec-2] [8230c21c47964341a5fc072e2ee103b9] support.log.starter.fun.pullock.LogFilter.logForRequest[133]: Request GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1
2022-07-22 10:35:32.393 INFO [http-nio-8080-exec-2] [8230c21c47964341a5fc072e2ee103b9] support.log.starter.fun.pullock.LogFilter.logForResponse[98]: Response GET /sample/log/get?id=123, client=0:0:0:0:0:0:0:1, status=200, payload=log get id: 123
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

可以在配置文件application.yml中禁用trace id功能：

```yaml
general:
  starter:
    log:
      trace:
        enabled: false
```

会在日志中自动添加TraceId。

## general-model-starter使用方法

在项目中引入`general-model-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
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
        <groupId>fun.pullock</groupId>
        <artifactId>general-model-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，会自动引入以下功能：

- 对返回值使用`Result`进行包装，如果不需要此功能可以使用配置`general.starter.wrap.result=false`进行关闭
- 会对全局异常进行处理，并使用`Result`进行包装，如果不需要此功能可以使用配置`general.starter.wrap.exception=false`进行关闭

## web-spring-boot-starter

`web-spring-boot-starter`会自动引入`log-spring-boot-starter`模块。

在项目中引入`web-spring-boot-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>web-model-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，会自动引入以下功能：

- 如果项目中没有配置`RestTemplate`，则会默认配置一个`RestTemplate`，并且会针对`RestTemplate`的请求在请求头中添加traceId，请求头的key为`x-request-id`；会打印请求和响应日志，如果不需要打印请求和响应日志，使用配置`general.starter.web.logEnable=false`进行关闭。 如果不使用`web-spring-boot-starter`模块中的`RestTemplate`，而使用自定义的`RestTemplate`，可以在自定义的`RestTemplate`中选择性的手动添加`support.web.starter.fun.pullock.ClientHttpRequestTraceIdInterceptor`以及`support.web.starter.fun.pullock.ClientHttpRequestLogInterceptor`来实现TraceId的添加以及日志的打印。

## feign-spring-boot-starter

`feign-spring-boot-starter`会自动引入`log-spring-boot-starter`模块。

在项目中引入`feign-spring-boot-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>feign-model-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，自动开启trace id功能，如果需要关闭该功能，需要在配置文件application.yml中禁用trace id功能：

```yaml
general:
  starter:
    feign:
      trace:
        enabled: false
```

如果想要禁用feign-spring-boot-starter功能，需要在application.yml中禁用：

```yaml
general:
  starter:
    feign:
        enabled: false
```

会针对使用了`Feign`方式的请求在请求头中自动添加TraceId。

自动将feign请求的Header数据传递到下游，如果需要关闭该功能，需要在配置文件application.yml中禁用Header传递功能：

```yaml
general:
  starter:
    feign:
      header:
        enabled: false
```

## json-starter

在项目中引入`json-starter`模块：

```
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>json-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，在项目代码中直接使用`Json.xxxx()`对应方法即可。

## jackson-spring-boot-starter

引入此模块后可以有以下功能：

- 自动修改JSON序列化和反序列化时日期的格式，`LocalDateTime`格式为`yyyy-MM-dd HH:mm:ss`，`LocalDate`格式为`yyyy-MM-dd`

使用步骤如下，首先在项目中引入`jackson-spring-boot-starter`模块：

```xml
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>jackson-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入模块后，无需任何配置，默认自动开启功能，如果引入模块之后需要关闭该功能，在配置文件application.yml中配置：

```yaml
general:
  starter:
    jackson:
      enabled: false
```

如需指定LocalDateTime和LocalDate的格式，在配置文件application.yml中配置：

```yaml
general:
    jackson:
      local-date-time-pattern: yyyy-MM-dd HH:mm:ss
      local-date-pattern: yyyy-MM-dd
```

## redis-spring-boot-starter

在项目中引入`redis-spring-boot-starter`模块：

```xml
<dependencies>
    <dependency>
        <groupId>fun.pullock</groupId>
        <artifactId>redis-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

引入该starter之后，会自动引入以下功能：

- 自定义Redis缓存通用配置：
  - 增加缓存命名空间前缀
  - 设置全局默认缓存时间
  - 指定key和value的序列化方式
  - 设置不缓存null值
- 创建一个`RedisTemplate<String, Object>`类型的`RedisTemplate`Bean，名称：`stringObjectRedisTemplate`，并且自定义key和value的序列化方式。
- 创建一个基于Redis的锁的Bean：RedisLock，提供以下两种方式的加锁解锁方式：
  - 不带客户端ID参数的加锁解锁方式：
    - `lock(String key, long timeout, long spinTimeout)`：基于自旋方式的阻塞加锁
    - `lock(String key, long timeout)`：基于自旋方式的阻塞加锁
    - `tryLock(String key, long timeout)`：非阻塞方式加锁
    - `unlock(String key)`：解锁
  - 带客户端ID参数的加锁解锁方式：
    - `lock(String key, long timeout, String clientId, long spinTimeout)`：基于自旋方式的阻塞加锁
    - `lock(String key, long timeout, String clientId)`：基于自旋方式的阻塞加锁
    - `tryLock(String key, long timeout, String clientId)`：非阻塞方式加锁
    - `unlock(String key, String clientId)`：解锁

除了starter中自定义的`RedisTemplate<String, Object>`，系统中默认还有两个其他类型的：
- `RedisTemplate<String, String>`类型的Bean，名称：`stringRedisTemplate`
- `RedisTemplate<Object, Object>`类型的Bean，名称：`redisTemplate`

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