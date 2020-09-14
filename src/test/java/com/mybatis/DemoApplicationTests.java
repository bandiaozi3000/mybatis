package com.mybatis;

import com.mybatis.bean.Area;
import com.mybatis.dao.AreaMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

//    @Autowired
//    public AreaMapper areaMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    public void contextLoads() {
    }

    /**
     * 1.获取SqlSessionFactory对象:
     *       解析文件的每一个信息保存在Configuration中,返回包含Configuration的DefaultSqlSessionFactory
     *       注意:MappedStatement：代表一个增删改查的详细信息
     * 2.获取SqlSession对象:
     *       返回一个DefaultSqlSession对象,包含Executor和Configuration;
     *       这一步会创建Executor对象
     * 3.获取接口的代理对象(MapperProxy):
     *       getMapper,使用MapperProxyFactory创建一个MapperProxy的代理对象
     *       代理对象里包含了,DefaultSqlSession(Executor)
     * 4.执行增删改查方法
     *
     * 总结:
     *     1.根据配置文件(全局,sql映射)初始化出Configuration对象
     *     2.创建一个DefaultSqlSessiond对象
     *           它里面包含Configuration以及Executor(根据全局配置文件中的defaultExecutorType创建出Executor)
     *     3.DefaultSqlSession.getMapper():拿到Mapper接口对应的MapperProxy
     *     4.MapperProxy里面有(DefaultSqlSession);
     *     5.执行增删改查方法:
     *           1).调用DefaultSqlSession的增删改查(Executor)
     *           2).会创建一个StatementHandler对象
     *                  (同时也会创建出ParameterHandler和ResultSetHandler）
     *           3).调用StatementHandler预编译参数以及设置参数值
     *              调用ParamerterHandler来给Sql设置参数
     *           4).调用StatementHandler的增删改查方法
     *           5).ResultSetHandler封装结果
     *     6.四大对象每个创建的时候都会有一个interceptorChain.pluginAll(parameterHandler)
     *
     *
     */
    @Test
    public void test(){
        System.out.println(sqlSessionFactory.getConfiguration());
        SqlSession sqlSession =sqlSessionFactory.openSession();
        AreaMapper areaMapper =sqlSession.getMapper(AreaMapper.class);
//       Page<Object> page = PageHelper.startPage(2, 2);
        List<Area> list = areaMapper.getArea(1);
        System.out.println(list.get(0));
//        for(Area area:list){
//            System.out.println(area.getId()+area.getName());
//        }
//        System.out.println("当前页数为:"+page.getPageNum());
//        System.out.println("总共页数为:"+page.getTotal());
//        System.out.println("每页的记录数为:"+page.getPageSize());
//        System.out.println("总页码为:"+page.getPages());

    }


    /**
     * 插件原理:
     *     1.四大对象每个创建的时候都会有一个interceptorChain.pluginAll(parameterHandler)
     *     2.获取到所有的interceptor(拦截器,插件需要实现的接口)
     *           调用interceptor.plugin(target),返回target包装后的对象
     *     3.插件机制:我们可以使用插件为目标对象创建一个代理对象,AOP(面向切面)
     *           我们的插件可以为四大对象创建出代理对象
     *           代理对象就可以拦截到四大对象的每一个执行
     *
     * 插件编写:
     *     1.编写Intercceptor实现类
     *     2.使用@Intercepts注解完成插件名
     *     3.将写好的插件注册到全局配置文件中
     *     4.若编写多个插件,动态对象将层层包裹,即配置文件后注册的插件包装在外层,所以运行的时候应该是后注册的插件方法先运行
     */


}
