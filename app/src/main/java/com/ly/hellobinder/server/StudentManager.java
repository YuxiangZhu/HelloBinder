package com.ly.hellobinder.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.ly.hellobinder.Student;

import java.util.List;


/**
 * 这个类用来定义服务端 RemoteService 具备什么样的能力
 *
 */
public interface StudentManager extends IInterface {

    List<Student> getStudents() throws RemoteException;
    void addStudent(Student stu) throws RemoteException;
}
