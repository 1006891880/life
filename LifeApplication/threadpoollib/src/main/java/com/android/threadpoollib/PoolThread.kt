package com.android.threadpoollib

import androidx.annotation.NonNull
import com.android.threadpoollib.callback.AsyncCallback
import com.android.threadpoollib.callback.ThreadCallback
import com.android.threadpoollib.config.ThreadConfigs
import com.android.threadpoollib.deliver.AndroidDeliver
import com.android.threadpoollib.deliver.JavaDeliver
import com.android.threadpoollib.factory.MyThreadFactory
import com.android.threadpoollib.utils.DelayCallableExecutor
import com.android.threadpoollib.utils.DelayRunnableExecutor
import com.android.threadpoollib.utils.ThreadUtils
import com.android.threadpoollib.wrapper.CallableWrapper
import com.android.threadpoollib.wrapper.RunnableWrapper
import java.util.concurrent.*
import kotlin.math.max
import kotlin.math.min

class PoolThread :Executor{

    /**
     * 线程池
     */
    private var pool: ExecutorService? = null
    /**
     * 默认线程名字
     */
    private var defName: String? = null
    /**
     * 默认线程回调
     */
    private var defCallback: ThreadCallback? = null
    /**
     * 默认线程传递
     */
    private var defDeliver: Executor? = null

    /**
     * 确保多线程配置没有冲突
     */
    private var local: ThreadLocal<ThreadConfigs>? = null

    constructor(type: Int, size: Int, priority: Int, name: String, callback: ThreadCallback, deliver: Executor, pool: ExecutorService) {
        var pool: ExecutorService? = pool
        if (pool == null) { //创建线程池
            pool = createPool(type, size, priority,name)
        }
        defName = name
        this.pool = pool
        defCallback = callback
        defDeliver = deliver
        local = ThreadLocal()
    }


    /**
     * 为当前的任务设置线程名。
     * @param name              线程名字
     * @return                  PoolThread
     */
    fun setName(name: String?): PoolThread {
        getLocalConfigs().threadName = name
        return this
    }


    /**
     * 设置当前任务的线程回调，如果未设置，则应使用默认回调。
     * @param callback          线程回调
     * @return                  PoolThread
     */
    fun setCallback(callback:ThreadCallback):PoolThread {
        getLocalConfigs().threadCallback = callback
        return this
    }

    /**
     * 设置当前任务的延迟时间.
     * 只有当您的线程池创建时，它才会产生效果。
     * @param time              时长
     * @param unit              time unit
     * @return                  PoolThread
     */
    fun setDelay(
        time: Long,
        unit: TimeUnit
    ): PoolThread {
        val delay = unit.toMillis(time)
        getLocalConfigs().delayTime = Math.max(0, delay)
        return this
    }

    /**
     * 设置当前任务的线程传递。如果未设置，则应使用默认传递。
     * @param deliver           thread deliver
     * @return                  PoolThread
     */
    fun setDeliver(deliver: Executor?): PoolThread {
        getLocalConfigs().executor = deliver
        return this
    }


    /**
     * 启动任务
     * 这个是实现接口Executor中的execute方法
     * 在将来的某个时间执行给定的命令。该命令可以在新线程、池线程或调用线程中执行，这取决于`Executor`实现。
     * 提交任务无返回值
     * @param runnable              task，注意添加非空注解
     */
    override fun execute(@NonNull runnable: Runnable) { //获取线程thread配置信息
        val configs =  getLocalConfigs()
        //设置runnable任务
        var runnableWrap = RunnableWrapper(configs).setRunnable(runnable)
        //启动任务
        DelayRunnableExecutor.getInstance().postDelay(configs.delayTime, pool!!, runnableWrap)
        //重置线程Thread配置
        resetLocalConfigs()
    }


    /**
     * 启动异步任务，回调用于接收可调用任务的结果。
     * @param callable              callable
     * @param callback              callback
     * @param <T> type
    </T> */
    fun <T> async(@NonNull callable: Callable<T>, callback: AsyncCallback) {
        val configs = getLocalConfigs()
        configs.asyncCallback = callback
        val callableExecutor =  CallableWrapper(configs,callable)
        DelayCallableExecutor<T>().postDelay(configs.delayTime, callableExecutor)
        resetLocalConfigs()
    }

    /**
     * 发射任务
     * 提交任务有返回值
     * @param callable              callable
     * @param <T>                   type
     * @return [Future]
    </T> */
    fun <T> submit(callable: Callable<T>): Future<T>? {
        val result: Future<T>
        var callableWrap = CallableWrapper(getLocalConfigs(), callable)
        result = pool!!.submit(callableWrap)
        resetLocalConfigs()
        return result
    }


    /**
     * 获取要创建的线程池。
     * @return                      线程池
     */
    fun getExecutor(): ExecutorService? {
        return pool
    }


    /**
     * 销毁的时候可以调用这个方法
     */
    fun close() {
        if (local != null) {
            local!!.remove()
            local = null
        }
    }

    /**
     * 创建线程池，目前支持以下四种
     * @param type                  类型
     * @param size                  数量size
     * @param priority              优先级
     * @return
     */
    private fun createPool(
        type: Int,
        size: Int,
        priority: Int,
        name: String
    ): ExecutorService? {
        return when (type) {
            //它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
            ThreadBuilder.TYPE_CACHE -> Executors.newCachedThreadPool(MyThreadFactory(priority,name))
            //线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
            ThreadBuilder.TYPE_FIXED -> Executors.newFixedThreadPool(size, MyThreadFactory(priority,name))
            //有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
            ThreadBuilder.TYPE_SCHEDULED -> Executors.newScheduledThreadPool(size, MyThreadFactory(priority,name))
            //内部只有一个核心线程，所有任务进来都要排队按顺序执行
            ThreadBuilder.TYPE_SINGLE -> Executors.newSingleThreadExecutor(MyThreadFactory(priority,name))
            else -> Executors.newSingleThreadExecutor(MyThreadFactory(priority,name))
        }
    }


    /**
     * 当启动任务或者发射任务之后需要调用该方法
     * 重置本地配置，置null
     */
    @Synchronized
    private fun resetLocalConfigs() {
        local!!.set(null)
    }


    /**
     * 注意需要用synchronized修饰，解决了多线程的安全问题
     * 获取本地配置参数
     * @return
     */
    @Synchronized
    private fun getLocalConfigs(): ThreadConfigs {
        var configs: ThreadConfigs? = local!!.get()
        if (configs == null) {
            configs = ThreadConfigs()
            configs.threadName = defName
            configs.threadCallback = defCallback
            configs.executor = defDeliver
            local!!.set(configs)
        }
        return configs
    }


    class ThreadBuilder private constructor(size: Int, type: Int, pool: ExecutorService?) {
        private var type: Int
        private var size: Int
        private var pool: ExecutorService?
        init {
            this.size = max(1, size)
            this.type = type
            this.pool = pool
        }
        var priority = Thread.NORM_PRIORITY
        var name: String? = null
        var callback: ThreadCallback? = null
        var deliver: Executor? = null
        /**
         * 将默认线程名设置为“已使用”。
         */
        fun setName(@NonNull name: String): ThreadBuilder {
            if (name.isNotEmpty()) {
                this.name = name
            }
            return this
        }

        /**
         * 将默认线程优先级设置为“已使用”。
         */
        fun setPriority(priority: Int): ThreadBuilder {
            this.priority = priority
            return this
        }

        /**
         * 将默认线程回调设置为“已使用”。
         */
        fun setCallback(callback: ThreadCallback): ThreadBuilder {
            this.callback = callback
            return this
        }

        /**
         * 设置默认线程交付使用
         */
        fun setDeliver(deliver: Executor?): ThreadBuilder {
            this.deliver = deliver
            return this
        }

        /**
         * 创建用于某些配置的线程管理器。
         * @return                  对象
         */
        fun build(): PoolThread { //最大值
            priority = max(Thread.MIN_PRIORITY, priority)
            //最小值
            priority = min(Thread.MAX_PRIORITY, priority)
            size = max(1, size)
            if (name == null || name!!.isEmpty()) { // 如果没有设置名字，那么就使用下面默认的线程名称
                name = when (type) {
                    TYPE_CACHE -> "CACHE"
                    TYPE_FIXED -> "FIXED"
                    TYPE_SINGLE -> "SINGLE"
                    TYPE_SCHEDULED -> "TYPE_SCHEDULED"
                    else -> "POOL_THREAD"
                }
            }
            if (deliver == null) {
                deliver = if (ThreadUtils.isAndroid) {
                    AndroidDeliver.getAndroidDeliver()
                } else {
                    JavaDeliver.getJavaDeliver()
                }
            }
            return PoolThread(type, size, priority, name!!, callback!!, deliver!!, pool!!)
        }

        companion object {
            const val TYPE_CACHE = 0
            const val TYPE_FIXED = 1
            const val TYPE_SINGLE = 2
            const val TYPE_SCHEDULED = 3
            /**
             * 通过Executors.newSingleThreadExecutor()创建线程池
             * 内部只有一个核心线程，所有任务进来都要排队按顺序执行
             */
            fun create(pool: ExecutorService?): ThreadBuilder {
                return ThreadBuilder(1, TYPE_SINGLE, pool)
            }

            /**
             * 通过Executors.newCachedThreadPool()创建线程池
             * 它是一个数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
             */
            fun createCacheable(): ThreadBuilder {
                return ThreadBuilder(0, TYPE_CACHE, null)
            }

            /**
             * 通过Executors.newFixedThreadPool()创建线程池
             * 线程数量固定的线程池，全部为核心线程，响应较快，不用担心线程会被回收。
             */
            fun createFixed(size: Int): ThreadBuilder {
                return ThreadBuilder(size, TYPE_FIXED, null)
            }

            /**
             * 通过Executors.newScheduledThreadPool()创建线程池
             * 有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
             */
            fun createScheduled(size: Int): ThreadBuilder {
                return ThreadBuilder(size, TYPE_SCHEDULED, null)
            }

            /**
             * 通过Executors.newSingleThreadPool()创建线程池
             * 内部只有一个核心线程，所有任务进来都要排队按顺序执行
             * 和create区别是size数量
             */
            fun createSingle(): ThreadBuilder {
                return ThreadBuilder(0, TYPE_SINGLE, null)
            }
        }


    }
}