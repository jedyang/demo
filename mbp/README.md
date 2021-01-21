## Mybatis-plus使用遇到的问题记录

### MyBatis-plus-generator
#### datetime问题
自动生成的entity遇到数据库中的datetime类型默认生成`LocalDateTime`类型的属性。
但是在代码查询时汇报格式转换异常:

```
Error attempting to get column 'create_date' from result set.  Cause: java.sql.SQLException: Conversion not supported for type java.time.LocalDateTime
```
 

#####解决方案：

升级MySQL驱动
```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.19</version>
</dependency>
```