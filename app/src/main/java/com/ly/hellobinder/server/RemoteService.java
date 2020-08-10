package com.ly.hellobinder.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.ly.hellobinder.Student;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 作者： Alex
 * 日期： 2020-08-04
 * 签名： 保持学习
 * <p>
 * ----------------------------------------------------------------
 */
public class RemoteService extends Service {
    private List<Student> list = new ArrayList<>();
    private static final String TAG = "RemoteService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        list.add(new Student("小明", 12));
        list.add(new Student("小红", 13));
        list.add(new Student("小军", 14));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: " );
        return mBinder;
    }
    private final Stub mBinder = new Stub(){

        @Override
        public List<Student> getStudents() throws RemoteException {
            synchronized (this) {
                if (list != null) {
                    return list;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addStudent(Student stu) throws RemoteException {
            synchronized (this) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (stu == null)
                    return;
//                stu.setName("新来的啊");
                //收到客户端添加了一个学生的请求，于是我在我的List里面加上这个学生。。
                list.add(stu);
                Log.e(TAG, "Server: "+stu.getName() );

            }
        }
    };
}
