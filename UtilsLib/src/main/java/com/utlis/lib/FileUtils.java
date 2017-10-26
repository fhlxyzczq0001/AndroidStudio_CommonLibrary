package com.utlis.lib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件创建Utils
 * 
 * @ClassName: FileUtils
 * @Description: TODO
 * @author: Administrator杨重诚
 * @date: 2016-3-22 下午2:40:04
 */
public class FileUtils {
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "";// 获取文件夹
	public static Context context = null;

	public FileUtils(Context context) {

		this.context = context;

	}

	// 在SD卡上创建一个文件夹

	public static void createSDCardDir(String floder) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File path1 = new File(floder);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}
		} else {
			ToastUtils.ErrorImageToast(context, "SD卡不存在", "short");
			return;
		}

	}

	// 创建文件
	public static void CreateFile(String floder, String filename) {
		L.e("路径：", SDPATH + floder + "/" + filename);
		File dir = new File(SDPATH + floder + "/" + filename);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}

	}

	// 创建文件
	public static void CreateBitmapFile(Context context, String floder,
			String filename) {
		L.e("路径：", SDPATH + floder + "/" + filename);
		File dir = new File(SDPATH + floder + "/" + filename);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				Bitmap bm = BitmapFactory.decodeResource(
						context.getResources(), android.R.drawable.ic_menu_save);
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(dir));
				bm.compress(Bitmap.CompressFormat.JPEG, 40, bos);
				bos.flush();
				bos.close();
				bm.recycle();
			} catch (Exception e) {
			}
		}

	}

	public static File createSDDirs(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

		}
		return dir;
	}

	public static boolean isFileExist(String floder, String filename) {
		File file = new File(floder + "/" + filename);
		file.isFile();
		return file.exists();
	}

	// 删除文件
	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	// 删除文件夹和文件夹里面的文件
	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static void floderIsExists(String floder) {
		try {
			File f = new File(floder);
			if (!f.exists()) {
				f.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除文件夹里面的文件
	public static void deleteFile(String path) {

		Log.e("path:", path);
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			/*
			 * else if (file.isDirectory()) deleteDir(); // 递规的方式删除文件夹
			 */}
		// dir.delete();// 删除目录本身
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void DeleteFile(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}

	public static String getPath(Context mcontext) {
		String sdPath = "/mnt/sdcard/";
		String sdk = android.os.Build.VERSION.SDK;// andorid
													// 4.0以上调用一下方法获取判断存储器路径
		if (Integer.parseInt(sdk) >= 14) {
			String[] storagePathList = new StorageList(mcontext)
					.getVolumePaths();
			if (storagePathList != null) {
				if (storagePathList.length >= 2) {
					sdPath = storagePathList[1];
				} else if (storagePathList.length == 1) {
					sdPath = storagePathList[0];
				}
			}
		}

		return sdPath;
	}

	public static class StorageList {
		private Context mcontext;
		private StorageManager mStorageManager;
		private Method mMethodGetPaths;

		public StorageList(Context context) {
			mcontext = context;
			if (mcontext != null) {
				mStorageManager = (StorageManager) mcontext
						.getSystemService(Activity.STORAGE_SERVICE);
				try {
					mMethodGetPaths = mStorageManager.getClass().getMethod(
							"getVolumePaths");
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}

		public String[] getVolumePaths() {
			String[] paths = null;
			try {
				paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return paths;
		}
	}

	/**
	 * 获取文件夹下所有图片绝对路径
	 * @Title: getFileName 
	 * @Description: TODO
	 * @param files
	 * @return
	 * @return: List<String>
	 */
	public static  List<String> getFileName(File[] files) {
		List<String> filePaths = new ArrayList<String>();
		if (files != null) {// 先判断目录是否为空，否则会报空指针
			for (File file : files) {
				if (file.isDirectory()) {
					L.i("zeng", "若是文件目录。继续读1" + file.getName().toString()
							+ file.getPath().toString());

					getFileName(file.listFiles());
					L.i("zeng", "若是文件目录。继续读2" + file.getName().toString()
							+ file.getPath().toString());
				} else {
					String fileName = file.getName();
					if (fileName.endsWith(".png") || fileName.endsWith(".jpg")
							|| fileName.endsWith(".jpeg")) {
						filePaths.add(file.getAbsolutePath());
					}
				}
			}
		}
		return filePaths;
	}


	/**
	 * read file
	 *
	 * @param filePath
	 * @return if file not exist, return null, else return content of file
	 * @throws RuntimeException if an error occurs while operator BufferedReader
	 */
	public static String readFile(String filePath) {
		String fileContent = "";
		File file = new File(filePath);
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file));
			reader = new BufferedReader(is);
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				fileContent += line + " ";
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileContent;
	}
	/**
	 * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
	 */
	public static String getAppCacheDir(Application mApp) {
		String sCacheDir;
		if (mApp.getExternalCacheDir() != null && SDCardUtil.ExistSDCard()) {
			sCacheDir = mApp.getExternalCacheDir().toString();
		} else {
			sCacheDir = mApp.getCacheDir().toString();
		}
		return sCacheDir;
	}
}
