package com.cabecinha84.zelcashui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.vaklinov.zcashui.Log;
import com.vaklinov.zcashui.OSUtil;


public class AppLock {
    private static File f;
    private static FileChannel channel;
    private static FileLock lock;

    public static boolean lock() {
        try {
        	String settingsDir = OSUtil.getSettingsDirectory();
            String fileName = "jstock.lock";
            f = new File(settingsDir + File.separator + fileName);
            channel = new RandomAccessFile(f, "rw").getChannel();
            lock = channel.tryLock();
            if(lock == null) {
                channel.close();
                return false;
            }
        } catch (Exception ex) {            
        	Log.warning("Error obtaining checking wallet lock due to: {0} {1}",
					ex.getClass().getName(), ex.getMessage());
        }
        return true;
    }

    public static void unlock() {
        // release and delete file lock
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                f.delete();
            }
        } catch(IOException e) {
        	Log.warning("Error ounlocking wallet lock due to: {0} {1}",
					e.getClass().getName(), e.getMessage());
        }        
    }
}