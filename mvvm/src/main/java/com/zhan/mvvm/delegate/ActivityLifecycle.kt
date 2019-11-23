package com.zhan.mvvm.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.zhan.mvvm.common.Clazz

/**
 *  @author: HyJame
 *  @date:   2019-11-20
 *  @desc:   TODO
 */
object ActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val cache by lazy { HashMap<String, ActivityDelegate>() }

    private lateinit var activityDelegate: ActivityDelegate
    private lateinit var mvmActivityDelegate: ActivityDelegate

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

        if (activity is IMvmActivity) {
            mvmActivityDelegate = MvmActivityDelegateImpl(activity)
            mvmActivityDelegate.onCreate(savedInstanceState)
        }

        //forwardDelegateFunction(activity) { activityDelegate.onCreate(savedInstanceState) }
    }

    override fun onActivityStarted(activity: Activity?) {
        forwardDelegateFunction(activity) { activityDelegate.onStart() }
    }

    override fun onActivityResumed(activity: Activity?) {
        forwardDelegateFunction(activity) { activityDelegate.onResume() }
    }

    override fun onActivityPaused(activity: Activity?) {
        forwardDelegateFunction(activity) { activityDelegate.onPause() }
    }

    override fun onActivityStopped(activity: Activity?) {
        forwardDelegateFunction(activity) { activityDelegate.onStop() }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        forwardDelegateFunction(activity) {
            activityDelegate.onDestroy()
            cache.clear()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        forwardDelegateFunction(activity) { activityDelegate.onSaveInstanceState(activity, outState) }
    }

    private fun forwardDelegateFunction(activity: Activity?, block: () -> Unit) {

        if (activity !is IActivity) return

        val key = activity.javaClass.name

        activityDelegate = cache[key] ?: ActivityDelegateImpl(activity).also { cache[key] = it }

        block()
    }
}