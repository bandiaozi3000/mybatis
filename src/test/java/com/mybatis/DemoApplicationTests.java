package com.mybatis;

import com.mybatis.bean.Area;
import com.mybatis.dao.AreaMapper;
import com.mybatis.service.AreaService;
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

    @Autowired
    public AreaMapper areaMapper;

    @Autowired
    public AreaService areaService;

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


    /**
     * 测试Mapper执行流程
     * 测试之前出了一个小问题:invalid bound statement.原因:因为用的是mybatis-plus,但是配置文件配置项用的还是mybatis的配置.
     * 所以配置文件不生效,这点需要注意!由此可知生成statement时需要找方法对应的xml,当存在的时候才会生成正确statement.
     * 现在开始看Mapper执行流程:
     *   1.areaMapper是一个代理类,执行代理方法:mapperMethod.execute(sqlSession, args).mapperMethod封装了方法的唯一id和查询类型
     *
     *   2.MybatisMapperMethod.execute.此时查询类型是select,且返回类型为list,走executeForMany(76).
     *
     *   3.sqlSession.selectList(158).此时sqlSession为SqlSessionTemplate.
     *     SqlSessionTemplate.selectList.this.sqlSessionProxy.selectList(),走代理方法SqlSessionInterceptor.invoke().
     *     getSqlSession(),该方法会创建一个session.SqlSessionUtils(105)->sessionFactory.openSession->openSessionFromDataSource
     *     返回new DefaultSqlSession(configuration, executor, autoCommit).其中创建executor时,configuration.newExecutor里面有一个
     *     方法interceptorChain.pluginAll.此时就是对executor执行插件代理.
     *
     *   4.层层代理,执行到DefaultSqlSession.selectList.   executor.query(147)
     *
     *   5.此时executor是一个代理类,执行代理方法.走Plugin.invoke.此时有拦截器插件的话会走插件,例如分页插件就是在此时执行的.
     *   接下来大致流程为:CachingExecutor.query->BaseExecutor.query->queryFromDatabase->queryFromDatabase
     *   ->doQuery->MybatisSimpleExecutor.doQuery->StatementHandler.query->PreparedStatement.execute
     *   ->resultSetHandler.handleResultSets即可得到返回结果.
     *
     */
    @Test
    public void testMapper(){
        List<Area> list = areaMapper.getArea(1);
        System.out.println(list);
    }


    /**
     * 测试Spring事务
     * 生成代理时机和SpringAop一样,在AbstractAutowireCapableBeanFactory.doCreateBean.initializeBean(593)
     * .applyBeanPostProcessorsAfterInitialization(1766).遍历bean后置处理器,得知起作用的是
     * InfrastructureAdvisorAutoProxyCreator.查看该处理器作用
     *
     * InfrastructureAdvisorAutoProxyCreator:
     *   AbstractAutoProxyCreator.createProxy创建代理类.原理和AOP并无差别.
     * 执行事务方法,方法具体执行为TransactionInterceptor.invoke->TransactionAspectSupport.invokeWithinTransaction
     * 可以看到有三个比较核心的方法:(176)invocation.proceedWithInvocation(),completeTransactionAfterThrowing,commitTransactionAfterReturning
     * 分别对应着事务执行,异常事务回滚,和事务提交. 大致流程即如上所述.
     *
     */
    @Test
    public void testSpringTransaction(){
        areaService.testTransaction();
        System.out.println(areaService);
    }


}
