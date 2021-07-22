package com.light.example;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.light.compress.LightCompressCore;

import java.io.File;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;


public class MainActivity extends AppCompatActivity {
	ImageView ivCompress;
	ImageView ivImage;
	TextView tvInfo;
	TextView tvInfo1;
	TextView tvInfo2;
	Uri imageUri;
	String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic3.jpg";
	String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/save.jpg";
	final static String info1 = "压缩后:\n高度：%d，宽度：%d，占用内存：%dKB，文件大小：%dKB";
	final static String info2 = "原图片:\n高度：%d，宽度：%d，占用内存：%dKB，文件大小：%dKB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivCompress = findViewById(R.id.image_compress);
		ivImage = findViewById(R.id.image);
		tvInfo = findViewById(R.id.tv_info);
		tvInfo1 = findViewById(R.id.tv_info1);
		tvInfo2 = findViewById(R.id.tv_info2);
		DevicesUtil.printSupportAbis();

		String url = "https://upload.jdcf88.com/test/2018/5/25/15272095511667.jpg";
		for (int i=0;i<10;i++){
			Bitmap bitmap=BitmapFactory.decodeFile(path1);
			LightCompressCore.saveBitmapWithAssign(bitmap,30,savePath,true,400,400);
			ivCompress.setImageBitmap(BitmapFactory.decodeFile(savePath));
		}

//		//Light1.2新增写法：
//		Bitmap bitmap1 = Light.getInstance().source(imageUri).width(500).height(500).autoRotation(true).compress();
//		boolean result1 = Light.getInstance().source(imageUri).width(500).height(500).autoRotation(true).compress(path);
//		CompressArgs args = new CompressArgs.Builder().width(500).height(500).autoRecycle(true).build();
//		Bitmap bitmap2 = Light.getInstance().source(imageUri).compressArgs(args).compress();
//		boolean result2 = Light.getInstance().source(imageUri).compressArgs(args).compress(path);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("相册");
		menu.add("拍照");
		menu.add("从网络加载");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ("相册".equals(item.getTitle())) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 2);
		} else if ("拍照".equals(item.getTitle())) {
			imageUri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new ContentValues());
			Intent takePhotoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(takePhotoIntent, 1);
		} else if ("从网络加载".equals(item.getTitle())) {
//			startActivity(new Intent(MainActivity.this, NetActivity.class));
		}
		return true;
	}
}
