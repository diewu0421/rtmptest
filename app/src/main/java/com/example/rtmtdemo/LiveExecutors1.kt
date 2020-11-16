package com.example.rtmtdemo

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author Lance
 * @date 2019/5/9
 */
class LiveExecutors private constructor() {
    companion object {
        @Volatile
        var instance: LiveExecutors? = null
            get() {
                if (field == null) {
                    synchronized(LiveExecutors::class.java) {
                        if (instance == null) {
                            instance = LiveExecutors()
                        }
                    }
                }
                return instance
            }
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
        private val MAXIMUM_POOL_SIZE: Int = CPU_COUNT * 2 + 1
        private const val KEEP_ALIVE_SECONDS = 30
        private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(5)
        private var THREAD_POOL_EXECUTOR: ThreadPoolExecutor? = null

        init {
            val threadPoolExecutor = ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
                    sPoolWorkQueue)
            threadPoolExecutor.allowCoreThreadTimeOut(true)
            THREAD_POOL_EXECUTOR = threadPoolExecutor
        }
    }

    fun execute(runnable: Runnable?) {
        THREAD_POOL_EXECUTOR?.execute(runnable)
    }
}