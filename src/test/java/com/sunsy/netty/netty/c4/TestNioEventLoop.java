package com.sunsy.netty.netty.c4;

/**
 * NioEventLoop:
 *  NioEventLoop的重要组成: selector(两个), 线程, 任务队列，定时任务队列
 *  既会处理io事件，也会处理普通任务和定时任务
 *
 *  问题：
 *      1. selector何时被创建 --> 构造方法 --> openSelector()
 *          1.1 eventLoop为何有两个selector成员
 *              原生selector使用set存储SelectionKeys集合，遍历性能不高
 *              netty将其替换为基于数组的实现
 *              将selectedKeys与publicSelectedKeys替换
 *              unwrappedSelector为原始selector, selector为netty封装的selector
 *      2. eventLoop的nio线程在何时启动 --> 当首次调用execute方法时，通过state状态位控制线程只执行一次
 *          doStartThread()：
 *              将execute执行器线程赋值给eventLoop的thread
 *      3. 提交普通任务户不会结束select阻塞
 *          3.1 wakeup 方法中的代码如何理解
 *              只有其他线程提交任务时，才会调用selector的wakeup方法
 *          3.2 wakenUp 变量的作用是什么
 *              原子变量 如果有多个其他线程都来提交任务，为了避免wakeup被频发调用
 *      4. 每次循环时，什么时候会进入selectStrategy.SELECT分支
 *          selectStrategy.calculateStrategy(selectNowSupplier, hasTasks())
 *          当没有任务时，才会进入selectStrategy.SELECT阻塞nio线程
 *          当存在任务时，调用selectNow方法，顺便拿到io事件
 *          4.1 何时会select阻塞，阻塞多久
 *              timeoutMillis -- 超时时间 -- 无定时任务时为 1s + 0.5ms
 *              selectDeadLineNanos -- 截止时间 == 当前时间 + 1s
 *              当到达截止时间，有任务，有io事件都会结束阻塞
 *      5. nio空轮询bug在哪里体现，如何解决
 *          vm参数：io.netty.selectorAutoRebuildThreshold 或者默认值512
 *          重新创建一个selector，替换了旧的selector
 *          jdk在linux的selector
 *      6. ioRatio 控制什么，设置为100有何作用
 *          io事件与普通任务会相互影响 --> 控制处理io事件所占用的时间比例
 *          设置为100会让普通任务全部运行完再进入下次循环
 *      7. selectedKey 优化是怎么回事
 *      8. 在哪里区分不同事件类型
 *          在processSelectedKey方法中区分处理
 */
public class TestNioEventLoop {
    public static void main(String[] args) {

    }
}
