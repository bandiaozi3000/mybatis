package com.mybatis.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

@Intercepts(
        @Signature(
                type= StatementHandler.class,method ="parameterize",args =java.sql.Statement.class

        )
)
public class MypluginInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("Myplugin...intercept"+invocation.getMethod());
        Object target = invocation.getTarget();
        System.out.println("当前拦截到的对象是:"+target);
        //拿到代理对象的元数据
        MetaObject metaObject = SystemMetaObject.forObject(target);
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println("Sql用的参数是："+value);
        Object proceed = invocation.proceed();
        return proceed;
    }

    @Override
    public Object plugin(Object o) {
        System.out.println("Myplugin...plugin"+o);
        Object wrap = Plugin.wrap(o,this);
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置信息....."+properties);
    }
}
