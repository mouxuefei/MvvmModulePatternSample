package com.fortunes.commonsdk.utils.sp

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import com.google.gson.Gson
import com.mou.basemvvm.BaseApplication
import java.lang.reflect.Field

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */

/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/10/25.
 * // 1. 创建映射实体类
 *  @PreferenceRename("user_info")
 *  class User:PreferenceSupport() {
 *  var username:String
 *  var age:Int
 *  var address:String
 *  }
 *  // 2. 进行读取
 *  val user = EasySharedPreferences.load(User::class.java)
 *  // 3. 进行修改
 *  // 直接使用load出来的user实例进行数值修改
 *  user.age = 16
 *  user.username = "haoge"
 *  // 修改完毕后，apply更新修改到SharedPreferences文件。
 *  user.apply()
 *  //有时候。我们会需要定义一下中间存储变量(此部分数据不需要同步存储到SP中的)。可以使用@PreferenceIgnore注解
 */
class EasySharedPreferences(clazz: Class<*>) : SharedPreferences.OnSharedPreferenceChangeListener {

    // 绑定的具体实体类。
    private val entity: PreferenceSupport
    // 所有的可操作变量
    private val fields = mutableMapOf<String, Field>()
    // 绑定的SharedPreference实例
    private val preferences: SharedPreferences
    // 存储待同步的数据的key值
    private val modifierKeys = mutableListOf<String>()

    init {
        if (!PreferenceSupport::class.java.isAssignableFrom(clazz)) {
            throw RuntimeException("The class must be subclass of PreferenceSupport")
        }

        entity = clazz.newInstance() as PreferenceSupport
        val name: String = getValid(clazz.getAnnotation(PreferenceRename::class.java)?.value, clazz.simpleName)
        preferences = BaseApplication.instance().applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        // 注册内容变动监听器
        preferences.registerOnSharedPreferenceChangeListener(this)
        // 读取所有的可操作变量字段
        var type = clazz
        while (type != PreferenceSupport::class.java) {
            for (field in type.declaredFields) {
                if (field.isAnnotationPresent(PreferenceIgnore::class.java)) {
                    // 指定过滤此字段
                    continue
                }

                val key = getValid(field.getAnnotation(PreferenceRename::class.java)?.value, field.name)

                if (!fields.containsKey(key)) {
                    // 对于父类、子类均存在的字段。使用子类的数据进行存储
                    fields[key] = field
                    if (!field.isAccessible) {
                        field.isAccessible = true
                    }
                }
            }
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            type = type.superclass as Class<*>
        }
        read()
    }

    private val handler = Handler(thread.looper) { msg ->
        return@Handler when (msg.what) {
            READ -> {
                // 更新指定字段的数据
                synchronized(modifierKeys) {
                    val keys = modifierKeys.toTypedArray()
                    modifierKeys.clear()
                    val map = preferences.all
                    for (key in keys) {
                        val field = fields[key]
                        val value = map[key]
                        if (field == null || value == null) {
                            container
                        } else {
                            readSingle(field, value)
                        }
                    }
                }
                true
            }
            WRITE -> {
                write()
                true
            }
            else -> false
        }
    }

    // 从SP中读取数据。注入到实体类中
    private fun read() {
        synchronized(this) {
            val map = preferences.all
            for ((name, field) in fields) {
                readSingle(field, map[name])
            }
        }
    }

    private fun readSingle(field: Field, value: Any?) {
        if (value == null) return
        val type: Class<*> = field.type
        try {
            val result: Any? = when {
                type == Int::class.java -> value as Int
                type == Long::class.java -> value as Long
                type == Boolean::class.java -> value as Boolean
                type == Float::class.java -> value as Float
                type == String::class.java -> value as String
                type == Byte::class.java -> (value as String).toByte()
                type == Short::class.java -> (value as String).toShort()
                type == Char::class.java -> (value as String).toCharArray()[0]
                type == Double::class.java -> (value as String).toDouble()
                type == StringBuilder::class.java -> StringBuilder(value as String)
                type == StringBuffer::class.java -> StringBuffer(value as String)
                GSON -> Gson().fromJson(value as String, type)
                else -> null
            }
            result?.let { field.set(entity, it) }
        } catch (e: ClassCastException) {

        }
    }

    // 将实体类中的数据。注入到SP容器中。
    private fun write() {
        synchronized(this) {
            preferences.unregisterOnSharedPreferenceChangeListener(this)
            val editor = preferences.edit()
            for ((name, fied) in fields) {
                val value = fied.get(entity)
                val type = fied.type
                when {
                    type == Int::class.java -> editor.putInt(name, value as? Int ?: 0)
                    type == Long::class.java -> editor.putLong(name, value as? Long ?: 0L)
                    type == Boolean::class.java -> editor.putBoolean(name, value as? Boolean
                            ?: false)
                    type == Float::class.java -> editor.putFloat(name, value as? Float ?: 0f)
                    type == String::class.java -> editor.putString(name, value as? String ?: "")
                    type == Byte::class.java
                            || type == Char::class.java
                            || type == Double::class.java
                            || type == Short::class.java
                            || type == StringBuilder::class.java
                            || type == StringBuffer::class.java
                    -> editor.putString(name, value.toString())
                    GSON -> value?.let { editor.putString(name, Gson().toJson(it)) }
                }
            }
            editor.apply()
            preferences.registerOnSharedPreferenceChangeListener(this)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // 将待更新的key值进行存储
        synchronized(modifierKeys) {
            key?.let { !modifierKeys.contains(it).let { modifierKeys.add(key) } }
        }
        // 延迟100毫秒进行数据更新
        if (handler.hasMessages(READ)) return
        handler.sendEmptyMessageDelayed(READ, 100)
    }


    private fun getValid(value: String?, default: String): String =
            if (value.isNullOrEmpty()) default else value as String

    fun apply() {
        if (handler.hasMessages(WRITE)) return
        handler.sendEmptyMessageDelayed(WRITE, 100)
    }

    companion object {

        private const val READ = 1
        private const val WRITE = 2

        private val container = mutableMapOf<Class<*>, EasySharedPreferences>()
        private val thread: HandlerThread by lazy {
            val thread = HandlerThread("shared_update_thread")
            thread.start()
            return@lazy thread
        }

        private val GSON by lazy { return@lazy exist("com.google.gson.Gson") }
        private fun exist(name: String): Boolean = try {
            Class.forName(name)
            true
        } catch (e: Exception) {
            false
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> load(clazz: Class<T>): T {
            synchronized(container) {
                container[clazz]?.let { return it.entity as T }
            }
            val instance = EasySharedPreferences(clazz)
            container[clazz] = instance
            return instance.entity as T
        }

        internal fun find(clazz: Class<*>): EasySharedPreferences {
            return container[clazz]
                    ?: throw RuntimeException("Could not find EasySharedPreferences by this clazz:[${clazz.canonicalName}]")
        }
    }

}

abstract class PreferenceSupport {
    fun apply() {
        // 将当前类中的修改。同步到sp中去(任务运行于子线程)
        EasySharedPreferences.find(javaClass).apply()
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
annotation class PreferenceRename(val value: String = "")

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class PreferenceIgnore