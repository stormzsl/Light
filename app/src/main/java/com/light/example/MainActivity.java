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

import com.light.body.CompressArgs;
import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.body.RxLight;
import com.light.core.Utils.MemoryComputeUtil;
import com.light.core.Utils.UriParser;
import com.light.core.listener.OnCompressFinishListener;

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
	final static String info1 = "压缩后:\n高度：%d，宽度：%d，占用内存：%dKB，文件大小：%dKB";
	final static String info2 = "原图片:\n高度：%d，宽度：%d，占用内存：%dKB，文件大小：%dKB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LightConfig config = new LightConfig();
		config.setAutoRotation(true);
		config.setAutoRecycle(true);
		Light.getInstance().setConfig(config);
		ivCompress = findViewById(R.id.image_compress);
		ivImage = findViewById(R.id.image);
		tvInfo = findViewById(R.id.tv_info);
		tvInfo1 = findViewById(R.id.tv_info1);
		tvInfo2 = findViewById(R.id.tv_info2);
		DevicesUtil.printSupportAbis();

		String url = "https://upload.jdcf88.com/test/2018/5/25/15272095511667.jpg";
		new Thread(() -> {
			Light.getInstance().compressFromHttp(url, new OnCompressFinishListener() {
				@Override
				public void onFinish(byte[] bytes) {
					runOnUiThread(() -> {
						if(bytes == null){
//							photoView.setImageResource(R.drawable.bg_preview_error);
						}else {
//							Bitmap bitmap = Light.getInstance().compress(bytes);
//							cache.put(urls.get(position), bitmap);
//							photoView.setImageBitmap(bitmap);
						}
					});
				}

				@Override
				public void onError(Throwable throwable) {
					throwable.printStackTrace();
//					photoView.setImageResource(R.drawable.bg_preview_error);
				}
			});
		}).start();

		Bitmap compressBitmap = Light.getInstance().compress(path1);
		ivCompress.setImageBitmap(compressBitmap);

//		//Light1.2新增写法：
//		Bitmap bitmap1 = Light.getInstance().source(imageUri).width(500).height(500).autoRotation(true).compress();
//		boolean result1 = Light.getInstance().source(imageUri).width(500).height(500).autoRotation(true).compress(path);
//		CompressArgs args = new CompressArgs.Builder().width(500).height(500).autoRecycle(true).build();
//		Bitmap bitmap2 = Light.getInstance().source(imageUri).compressArgs(args).compress();
//		boolean result2 = Light.getInstance().source(imageUri).compressArgs(args).compress(path);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && imageUri != null) {
			//效果同下
//			Light.setImage(ivCompress, imageUri);
			Bitmap compressBitmap = Light.getInstance().compress(imageUri);
			ivCompress.setImageBitmap(compressBitmap);
			Light.getInstance().compress(imageUri, path1);
//			Flowable.just(imageUri).compose(RxLight.compress()).subscribe(bitmap -> ivCompress.setImageBitmap(bitmap));
			//系统获取图片的方法
			String path = UriParser.getPathFromContentUri(imageUri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			Bitmap bitmap2 = BitmapFactory.decodeFile(path);
			ivImage.setImageBitmap(bitmap2);
			tvInfo1.setVisibility(View.VISIBLE);
			tvInfo2.setVisibility(View.VISIBLE);
			tvInfo1.setText(String.format(Locale.CHINA, info1, compressBitmap.getHeight(),
					compressBitmap.getWidth(), MemoryComputeUtil.getMemorySize(compressBitmap), new File(path1).length() / 1024));
			tvInfo2.setText(String.format(Locale.CHINA, info2, options.outHeight,
					options.outWidth, MemoryComputeUtil.getMemorySize(bitmap2), new File(path).length() / 1024));
		} else if (requestCode == 2 && data != null) {
			Uri imageUri = data.getData();
			String path = UriParser.getPathFromContentUri(imageUri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
//			Bitmap compressBitmap = Light.getInstance().compress(imageUri);
            CompressArgs args = new CompressArgs.Builder().width(1600)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .height(1600).autoRotation(true).compressFileSize(200)
                    .build();
			Light.getInstance().compress(imageUri, args, path1);
            Bitmap compressBitmap = Light.getInstance().compress(path1);
			ivCompress.setImageBitmap(compressBitmap);
//			Bitmap bitmap2 = BitmapFactory.decodeFile(path);
//			ivImage.setImageBitmap(bitmap2);
			tvInfo1.setVisibility(View.VISIBLE);
			tvInfo2.setVisibility(View.VISIBLE);
			tvInfo1.setText(String.format(Locale.CHINA, info1, compressBitmap.getHeight(),
					compressBitmap.getWidth(), MemoryComputeUtil.getMemorySize(compressBitmap), new File(path1).length() / 1024));
//			tvInfo2.setText(String.format(Locale.CHINA, info2, options.outHeight,
//					options.outWidth, MemoryComputeUtil.getMemorySize(bitmap2), new File(path).length() / 1024));
		}

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
			startActivity(new Intent(MainActivity.this, NetActivity.class));
		}
		return true;
	}
}
