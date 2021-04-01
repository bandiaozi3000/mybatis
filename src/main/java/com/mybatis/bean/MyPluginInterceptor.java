package com.mybatis.bean;//package cn.shopex.ecboot.web.config;
//
//import cn.shopex.ecboot.commons.util.UserInfoContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.mapping.SqlSource;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//import java.util.Properties;
//
///**
// * @author lm.sun
// * @create 2021/2/2 11:53 上午
// * @desc mybatis自定义拦截器
// **/
//@Component
//@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
//})
//@Slf4j
//public class MyPluginInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        // 获取 SQL 命令
//        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
//        Object[] args = invocation.getArgs();
//        Object parameter = args[1];
//        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
//        //判断当前是否是查询类型,若是则修改sql语句,默认查询条件加 'is_delete = 0'
//        if (SqlCommandType.SELECT.equals(sqlCommandType)) {
//            String sql = boundSql.getSql().toLowerCase();
//            //拼接sql
//            if (sql.contains("where")) {
//                sql = sql.concat(" and is_delete = 0");
//            } else {
//                sql = sql.concat(" where is_delete = 0");
//            }
//            log.info("SQL：{}", sql);
//            //通过反射修改sql语句
//            Field field = boundSql.getClass().getDeclaredField("sql");
//            field.setAccessible(true);
//            field.set(boundSql, sql);
//            //构建新的mapperStatement,并赋值给被代理方法参数
//            MappedStatement newMappedStatement = newMappedStatement(mappedStatement, new BoundSqlSqlSource(boundSql));
//            args[0] = newMappedStatement;
//            return invocation.proceed();
//        }
//        //获取私有成员变量
//        Field[] declaredFields = parameter.getClass().getDeclaredFields();
//        if (parameter.getClass().getSuperclass() != null) {
//            Field[] superField = parameter.getClass().getSuperclass().getDeclaredFields();
//            declaredFields = ArrayUtils.addAll(declaredFields, superField);
//        }
//        String isDelete = "";
//        for (Field field : declaredFields) {
//            //若当前操作为插入,则为createUserId,createdAt赋值
//            if ("createUserId".equals(field.getName()) && SqlCommandType.INSERT.equals(sqlCommandType)) {
//                field.setAccessible(true);
////                field.set(parameter, UserInfoContext.getCurrentUser().getUserId());
//            }
//            if ("createdAt".equals(field.getName()) && SqlCommandType.INSERT.equals(sqlCommandType)) {
//                field.setAccessible(true);
//                field.set(parameter, LocalDateTime.now());
//            }
//            //若当前操作为更新,则为updateUserId,updatedAt赋值
//            if ("updateUserId".equals(field.getName()) && SqlCommandType.UPDATE.equals(sqlCommandType)) {
//                field.setAccessible(true);
////                field.set(parameter, UserInfoContext.getCurrentUser().getUserId());
//            }
//            if ("updatedAt".equals(field.getName()) && SqlCommandType.UPDATE.equals(sqlCommandType)) {
//                field.setAccessible(true);
//                field.set(parameter, LocalDateTime.now());
//            }
//            //若当前操作为更新,获取参数isDelte的值
//            if ("isDelete".equals(field.getName()) && SqlCommandType.UPDATE.equals(sqlCommandType)) {
//                field.setAccessible(true);
//                isDelete = (String) field.get(parameter);
//            }
//            //判断isDelete值是否为1,若为1,表明当前操作为更新且更新删除标识为1->已删除,则为deleteAt,deleteUserId赋值
//            if ("deletedAt".equals(field.getName()) && "1".equals(isDelete)) {
//                field.setAccessible(true);
//                field.set(parameter, LocalDateTime.now());
//            }
//            if ("deleteUserId".equals(field.getName()) && "1".equals(isDelete)) {
//                field.setAccessible(true);
//                field.set(parameter, LocalDateTime.now());
//            }
//        }
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof Executor) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
//
//    @Override
//    public void setProperties(Properties prop) {
//    }
//
//    /**
//     * 构建新的MappedStatement对象
//     *
//     * @param ms
//     * @param newSqlSource
//     * @return MappedStatement
//     */
//    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
//        MappedStatement.Builder builder =
//                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
//        builder.resource(ms.getResource());
//        builder.fetchSize(ms.getFetchSize());
//        builder.statementType(ms.getStatementType());
//        builder.keyGenerator(ms.getKeyGenerator());
//        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
//            StringBuilder keyProperties = new StringBuilder();
//            for (String keyProperty : ms.getKeyProperties()) {
//                keyProperties.append(keyProperty).append(",");
//            }
//            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
//            builder.keyProperty(keyProperties.toString());
//        }
//        builder.timeout(ms.getTimeout());
//        builder.parameterMap(ms.getParameterMap());
//        builder.resultMaps(ms.getResultMaps());
//        builder.resultSetType(ms.getResultSetType());
//        builder.cache(ms.getCache());
//        builder.flushCacheRequired(ms.isFlushCacheRequired());
//        builder.useCache(ms.isUseCache());
//        return builder.build();
//    }
//
//    /**
//     * @author lm.sun
//     * @create 2021/2/2 11:48 上午
//     * @desc 静态内部类, 作为构建新MappedStatement的参数
//     **/
//    static class BoundSqlSqlSource implements SqlSource {
//        private final BoundSql boundSql;
//
//        public BoundSqlSqlSource(BoundSql boundSql) {
//            this.boundSql = boundSql;
//        }
//
//        @Override
//        public BoundSql getBoundSql(Object parameterObject) {
//            return boundSql;
//        }
//    }
//
//}