package com.codelang.plugin;

import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangqi
 * @since 2018/2/8 11:27
 */

public class ClipHelper {

    public static void binder() {

        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = clazz.getDeclaredMethod("getService", String.class);
            getServiceMethod.setAccessible(true);
            IBinder iBinder = (IBinder) getServiceMethod.invoke(null, "clipboard");

            IBinder myBinder =
                    (IBinder) Proxy.newProxyInstance(
                            clazz.getClassLoader(),
                            new Class[]{IBinder.class},
                            new MyClipProxy(iBinder));

            Field field = clazz.getDeclaredField("sCache");
            field.setAccessible(true);
            Map<String, IBinder> map = (Map) field.get(null);

            //将我们的代理类invoke到ServiceManager的缓存中，
            // 每次系统从缓存中拿clipboard的服务的时候，拿到的都是我们的代理binder

            map.put("clipboard", myBinder);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}
