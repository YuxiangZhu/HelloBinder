package com.ly.hellobinder.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.ly.hellobinder.R;
import com.ly.hellobinder.Student;
import com.ly.hellobinder.server.RemoteService;
import com.ly.hellobinder.server.Stub;
import com.ly.hellobinder.server.StudentManager;

import java.util.List;

/**
 * 客户端
 */
public class MainActivity extends AppCompatActivity {

    private StudentManager studentManager;
    private boolean isConnection = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnection) {
                    attemptToBindService();
                    return;
                }
                if (studentManager == null)
                    return;
                try {
                    //这个地方添加的数据，真正实现是在manager里面
                    Student student = new Student();
                    student.setName("朱朱朱");
                    student.setAge(30);
                    studentManager.addStudent(student);
                    Log.d("MainActivity", studentManager.getStudents().size()+"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void attemptToBindService() {

        Intent intent = new Intent();
        //5.0+的要求，传是报名应该为service所在进程的包名，比如第二个APP的包名
        intent.setPackage(getPackageName());
        //action没有严格使用service的路径，加了后缀1
        intent.setAction("com.ly.hellobinder.server1");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnection = true;
            studentManager = Stub.asInterface(service);
            if (studentManager != null) {
                try {
                    List<Student> students = studentManager.getStudents();
                    Log.d("MainActivity", students.size()+"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnection) {
            unbindService(serviceConnection);
        }
    }
}