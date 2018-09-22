package com.kw.top.crash;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 收集错误类
 */
public class MyCrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	//系统默认的UncaughtException处理类
	private UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static MyCrashHandler INSTANCE = new MyCrashHandler();
	//程序的Context对象
	private Context mContext;

	/**
	 * 保证只有一个CrashHandler实例
	 */
	private MyCrashHandler() {
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 */
	public static MyCrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 *
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		saveToSDCard(saveCrashInfo2File(ex));
		mDefaultHandler.uncaughtException(thread, ex);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		mContext.startActivity(new Intent(mContext, InitActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 保存错误信息到文件中
	 *
	 * @param ex
	 * @return 返回文件名称, 便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		return result;
	}

	/**
	 * 追加文件：使用FileWriter
	 *
	 * @param content
	 */
	private static void saveToSDCard(String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				return;
			}
			String path = Environment.getExternalStorageDirectory() + "/crash.log";
			File file = new File(path);
			if (!file.exists() || file.isDirectory()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(path, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
