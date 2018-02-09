package com.codelang.plugin;

import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangqi
 * @since 2018/2/8 11:33
 */

public class MyClipProxy implements InvocationHandler {
    IBinder mBase;

    public MyClipProxy(IBinder iBinder) {
        this.mBase = iBinder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //拦截原系统类查询本地是否有这个代理的方法
        if ("queryLocalInterface".equals(method.getName())) {
            //我们这里要创建我们自己的系统类，然后返回
            //1.拿到系统的aidl类中的stub，因为这个对象本来就是个代理,而且源码执行了
//            static private IClipboard getService() {
//                synchronized (sStaticLock) {
//                    if (sService != null) {
//                        return sService;
//                    }
//                    IBinder b = ServiceManager.getService("clipboard");
//                    sService = IClipboard.Stub.asInterface(b);
//                    return sService;
//                }

//            mService = IClipboard.Stub.asInterface(b);
//

            //IBinder
//            public static android.content.IClipboard asInterface(android.os.IBinder obj) {
//                if ((obj == null)) {
//                    return null;
//                }
//                android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR); // Hook点
//                if (((iin != null) && (iin instanceof android.content.IClipboard))) {
//                    return ((android.content.IClipboard) iin);
//                }
//                return new android.content.IClipboard.Stub.Proxy(obj);
//            }

// }


            Class<?> mStubClass = Class.forName("android.content.IClipboard$Stub");
            //2.在拿到IClipboard本地对象类
            Class<?> mIClipboard = Class.forName("android.content.IClipboard");
            //3.创建我们自己的代理
            return Proxy.newProxyInstance(mStubClass.getClassLoader(),
                    new Class[]{mIClipboard},//返回IClipboard类
                    new MyClip(mBase, mStubClass));//代理IClipboard$Stub类
        }

        //不是这个方法还是返回原系统的执行
        return method.invoke(mBase, args);
    }
}
