package com.imageprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class DownLoadActivity extends Activity {
	
	private String BASE = "";
	private String[] pathName = {"feats.dat","seeta_fa_v1.1.bin","seeta_fd_frontal_v1.0.bin","seeta_fr_v1.0.bin","opencv_manager.apk"};
	
	private Button mInstall;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		
		mInstall = (Button) this.findViewById(R.id.install_btn);
		
		BASE = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/bin";
		File path = new File(BASE);
		if (!path.exists()) {// Ŀ¼���ڷ���false 
			path.mkdirs();// ����һ��Ŀ¼ 
		} 
		
		for (int i = 0; i < pathName.length; i++) {
			assetsDataToSD(BASE+"/"+pathName[i],pathName[i]);
		}
		
		mInstall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        if(isAppInstalled("org.opencv.engine")){
					//app installed
		        	Intent intent = new Intent(DownLoadActivity.this, MainActivity.class);
		        	startActivity(intent);
				} else {
					// ���ⲿActivity�����߿���������Ҫ����̫���ڲ�ʵ�֣�ֻ��Ҫ����һ��url��һ��context����  
			        AutoInstall.setUrl(Environment.getExternalStorageDirectory() + "/bin/opencv_manager.apk");  
			        AutoInstall.install(DownLoadActivity.this);  
					if(isAppInstalled("org.opencv.engine")){
						mInstall.setText("����ң�����������аɣ�");
					}
					
				}
			}
		});
	}
	
	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}
	
	/**
	 * ��assets���ļ����Ƶ�SD����
	 */
	private void assetsDataToSD(String fileName, String openstr) {
		InputStream myInput;
		try {
			OutputStream myOutput = new FileOutputStream(fileName);
			myInput = this.getAssets().open(openstr);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
