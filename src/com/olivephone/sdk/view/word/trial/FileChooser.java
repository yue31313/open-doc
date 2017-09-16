package com.olivephone.sdk.view.word.trial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileChooser extends Activity {

	private String filePath = Environment.getExternalStorageDirectory()
			.getPath() + "/example.docx";
	MyBaseAdapter adapter;
	private static String parentPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(2);
		copyFileToSdcard();
		Intent intent = new Intent(FileChooser.this,
				OliveWordTrailDemoAcitivy.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.fromFile(new File(filePath)));
		startActivity(intent);
	}

	class MyBaseAdapter extends BaseAdapter {
		private String[] list;

		public MyBaseAdapter(String[] list) {
			this.list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(FileChooser.this);
				((TextView) convertView).setTextSize(35);
			}
			((TextView) convertView).setText(list[position]);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return list.length;
		}

		public void setList(String[] list) {
			this.list = list;
		}
	};

	class MyItemClickListener implements OnItemClickListener {
		String[] list;
		InputStream is;

		public MyItemClickListener(String[] list) {
			this.list = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			File file = new File(parentPath + list[position]);
			if (file.isFile()) {
				Intent intent = new Intent(FileChooser.this,
						OliveWordTrailDemoAcitivy.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.fromFile(file));
				startActivity(intent);
			} else {
				list = file.list();
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				parentPath = file.getAbsolutePath() + "/";
			}
		}

	}
	private void copyFileToSdcard() {
		InputStream inputstream 	= getResources().openRawResource(
				R.raw.example);
		byte[] buffer = new byte[1024];
		int count = 0;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filePath));
			while ((count = inputstream.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			Toast.makeText(FileChooser.this, "Check your sdcard", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
