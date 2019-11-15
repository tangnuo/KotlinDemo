package com.kedacom.util


/**
 * 反射工具类
 */
object RefUtil {

    @JvmStatic
    @Throws(Exception::class)
    fun getField(clazzName: String, target: Any, name: String): Any {
        return getField(Class.forName(clazzName), target, name)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun getField(clazz: Class<*>, target: Any, name: String): Any {
        val field = clazz.getDeclaredField(name)
        field.isAccessible = true
        return field.get(target)
    }

    @JvmStatic
    fun getFieldNoException(clazzName: String, target: Any, name: String): Any? {
        try {
            return getFieldNoException(Class.forName(clazzName), target, name)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    fun getFieldNoException(clazz: Class<*>, target: Any, name: String): Any? {
        try {
            return getField(clazz, target, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    @Throws(Exception::class)
    fun setField(clazzName: String, target: Any, name: String, value: Any) {
        setField(Class.forName(clazzName), target, name, value)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun setField(clazz: Class<*>, target: Any, name: String, value: Any) {
        val field = clazz.getDeclaredField(name)
        field.isAccessible = true
        field.set(target, value)
    }

    @JvmStatic
    fun setFieldNoException(clazzName: String, target: Any, name: String, value: Any) {
        try {
            setFieldNoException(Class.forName(clazzName), target, name, value)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun setFieldNoException(clazz: Class<*>, target: Any, name: String, value: Any) {
        try {
            setField(clazz, target, name, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    @Throws(Exception::class)
    operator fun invoke(clazzName: String, target: Any, name: String, vararg args: Any): Any {
        return invoke(Class.forName(clazzName), target, name, *args)
    }

    @JvmStatic
    @Throws(Exception::class)
    operator fun invoke(clazz: Class<*>, target: Any, name: String, vararg args: Any): Any {
        val parameterTypes: Array<Class<*>?> = arrayOfNulls(args.size)
        for (i in args.indices) {
            parameterTypes[i] = args[i].javaClass
        }

        val method = clazz.getDeclaredMethod(name, *parameterTypes)
        method.isAccessible = true
        return method.invoke(target, *args)
    }

    @JvmStatic
    @Throws(Exception::class)
    operator fun invoke(clazzName: String, target: Any, name: String,
                        parameterTypes: Array<Class<*>>, vararg args: Any): Any {
        return invoke(Class.forName(clazzName), target, name, parameterTypes, *args)
    }

    @JvmStatic
    @Throws(Exception::class)
    operator fun invoke(clazz: Class<*>, target: Any, name: String,
                        parameterTypes: Array<Class<*>>, vararg args: Any): Any {
        val method = clazz.getDeclaredMethod(name, *parameterTypes)
        method.isAccessible = true
        return method.invoke(target, *args)
    }

    @JvmStatic
    fun invokeNoException(clazzName: String, target: Any, name: String,
                          parameterTypes: Array<Class<*>>, vararg args: Any): Any? {
        try {
            return invokeNoException(Class.forName(clazzName), target, name, parameterTypes, *args)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    fun invokeNoException(clazz: Class<*>, target: Any, name: String,
                          parameterTypes: Array<Class<*>>, vararg args: Any): Any? {
        try {
            return invoke(clazz, target, name, parameterTypes, *args)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
