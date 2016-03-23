package com.serenegiant.usbcamera;

import java.util.ArrayList;
import java.util.Stack;

import com.serenegiant.uvccamera.R;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDealActivity extends Activity {

	private ImageView tv;
	private ImageView tv2;
	private ImageView tv3;
	private Button bt;
	private Button bt2;
	private EditText et;

	int pos = 0;
	final int answer = 10;
	int tj = 0;
	private ArrayList<String> part1, part2;
	int left1, left2, right1, right2;
	int minDigNum = 10;
	int maxDigNum = 400;
	int ps[];
	int currentPixel;
	int answerNum = 6;
	int jiaodu = 0;
	
	Bitmap[] bmps = new Bitmap[answerNum];
	int cur = 0;

	private ArrayList<ArrayList<String>> leftParts, rightParts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_deal);

		tv = (ImageView) findViewById(R.id.tv);
		tv2 = (ImageView) findViewById(R.id.tv2);
		tv3 = (ImageView) findViewById(R.id.tv3);
		bt = (Button) findViewById(R.id.bt);
		et = (EditText) findViewById(R.id.et);
		bt2 = (Button) findViewById(R.id.bt2);
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				currentPixel++;
//				tv3.setBackgroundColor(ps[currentPixel]);
//				getRGB(ps[currentPixel]);
				tv2.setImageBitmap(bmps[cur++]);
				
			}
		});
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				openAlbumIntent.putExtra("return-data", true);
				startActivityForResult(openAlbumIntent, 202);
			}
		});

		part1 = new ArrayList<String>();
		part2 = new ArrayList<String>();

	}

	private void test(int[] p, int w, int h) {
		int[][] pp = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pp[i][j] = p[j * w + i];
			}
		}
		ArrayList<String> strList = new ArrayList<String>();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (pp[i][j] == 0xFF000000) {
					strList.add(i + "_" + j);
				}
			}
		}
		Log.i("blackNum", strList.size() + "");

		ArrayList<ArrayList<String>> partsList = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < strList.size(); i++) {
			if (partsList.size() == 0) {
				ArrayList<String> parts = new ArrayList<String>();
				parts.add(strList.get(i));
				partsList.add(parts);
			} else {
				int tag = 0;
				for (int k = partsList.size() - 1; k >= 0; k--) {
					int a = Integer.parseInt(strList.get(i).split("_")[0]);
					int b = Integer.parseInt(strList.get(i).split("_")[1]);
					if (a == 0) {
						a = w;
					}
					if (partsList.get(k).contains((a - 1) + "_" + (b - 1))
							|| partsList.get(k).contains((a) + "_" + (b - 1))
							|| partsList.get(k).contains((a - 1) + "_" + (b))) {
						partsList.get(k).add(strList.get(i));
						tag = 1;
						break;
					}
				}
				if (tag == 0) {
					ArrayList<String> parts = new ArrayList<String>();
					parts.add(strList.get(i));
					partsList.add(parts);
				}

			}
		}

		Log.i("parts1", "" + partsList.size());
		for (int i = 0; i < partsList.size(); i++) {
			if (partsList.get(i).size() < 100) {
				partsList.remove(i);
				i--;
			}
		}
		Log.i("parts2", "" + partsList.size());

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (!partsList.get(1).contains(i + "_" + j)) {
					p[j * w + i] = 0xFFFFFFFF;
				} else {
					p[j * w + i] = 0xFFAA0000;
				}
			}
		}
	}

	private void test2(int[] p, int w, int h) {
		int[][] pp = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pp[i][j] = p[j * w + i];
			}
		}
		ArrayList<Graph> strList = new ArrayList<Graph>();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Graph g = new Graph(i + "_" + j, i, j);
				if (pp[i][j] == 0xFF000000) {
					g.isBlack = true;
				} else {
					g.isBlack = false;
				}
				strList.add(g);
			}
		}
		ArrayList<ArrayList<String>> partsList = new ArrayList<ArrayList<String>>();
		int times = 0;
		times++;
		Log.i("times", strList.size() + "");
		Stack<Integer> s = new Stack<Integer>();
		while (strList.size() > 0) {
			ArrayList<String> parts = new ArrayList<String>();
			partsList.add(parts);
			Log.i("strSize", "" + strList.size());
			int temp = 0;
			while (temp != -1) {
				if (!strList.get(temp).visited) {
					strList.get(temp).visited = true;
					int a = strList.get(temp).x;
					int b = strList.get(temp).y;
					parts.add(a + "_" + b);
					int left, right, down, up;
					left = right = down = up = -1;
					for (int i = Math.max(0, temp - w); i < Math.min(temp + w,
							strList.size()); i++) {
						if (strList.get(i).p.equals((a - 1) + "_" + (b))) {
							left = i;
						}
						if (strList.get(i).p.equals((a + 1) + "_" + b)) {
							right = i;
						}
						if (strList.get(i).p.equals(a + "_" + (b + 1))) {
							down = i;
						}
						if (strList.get(i).p.equals(a + "_" + (b - 1))) {
							up = i;
						}
					}

					if (left != -1) {
						temp = left;
						if (right != -1) {
							s.push(right);
						}
						if (down != -1) {
							s.push(down);
						}
						if (up != -1) {
							s.push(up);
						}
					} else if (right != -1) {
						temp = right;
						if (down != -1) {
							s.push(down);
						}
						if (up != -1) {
							s.push(up);
						}
					} else if (down != -1) {
						temp = down;
						if (up != -1) {
							s.push(up);
						}
					} else if (up != -1) {
						temp = up;
					} else if (!s.empty()) {
						temp = s.pop();
					} else {
						temp = -1;
					}
				} else {
					if (!s.empty()) {
						temp = s.pop();
					} else {
						temp = -1;
					}
				}
			}

			for (int k = 0; k < strList.size(); k++) {
				if (strList.get(k).visited) {
					strList.remove(k);
					k--;
				}
			}
		}

		// getOther(temp, a, b, parts, strList, pp,w);

		for (int i = 0; i < partsList.size(); i++) {
			if (partsList.get(i).size() < 10) {
				partsList.remove(i--);
			}
		}

		for (ArrayList<String> parts : partsList) {
			for (String part : parts) {
				int x = Integer.parseInt(part.split("_")[0]);
				int y = Integer.parseInt(part.split("_")[1]);
				p[y * w + x] = 0xFFAA0000;
			}
		}
		// for (int i = 0; i < w; i++) {
		// for (int j = 0; j < h; j++) {
		// boolean tag = false;
		// if (!partsList.get(0).contains(i + "_" + j)) {
		// p[j * w + i] = 0xFFFFFFFF;
		// } else {
		// p[j * w + i] = 0xFFAA0000;
		// }
		// }
		// }
		Log.i("psize", partsList.size() + "/" + tj);
	}

	private boolean test3(int[] p, int[] p0, int w, int h) {
		tj++;
		int[][] pp = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pp[i][j] = p[j * w + i];
			}
		}
		ArrayList<Graph> gList = new ArrayList<Graph>();
		Graph2[][] points = new Graph2[w][h];
		int count = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				points[i][j] = new Graph2();
				points[i][j].x = i;
				points[i][j].y = j;
				if (pp[i][j] == 0xFF000000) {
					points[i][j].isBlack = true;
					Graph g = new Graph(i + "_" + j, i, j);
					gList.add(g);
					points[i][j].position = count;
					count++;
				} else {
					points[i][j].isBlack = false;
				}
			}
		}
		ArrayList<ArrayList<String>> partsList = new ArrayList<ArrayList<String>>();
		Stack<Integer> s = new Stack<Integer>();
		while (gList.size() > 0) {
			ArrayList<String> parts = new ArrayList<String>();
			partsList.add(parts);
			int temp = -1;
			for (int i = 0; i < gList.size(); i++) {
				if (!gList.get(i).visited) {
					temp = i;
					break;
				}
			}
			if (temp == -1) {
				break;
			}
			while (temp != -1) {
				if (!gList.get(temp).visited) {
					gList.get(temp).visited = true;
					int a = gList.get(temp).x;
					int b = gList.get(temp).y;
					parts.add(a + "_" + b);
					int left, right, down, up;
					left = right = down = up = -1;
					if (a - 1 >= 0 && !points[a - 1][b].visited) {
						left = points[a - 1][b].position;
					}
					if (a + 1 < w && !points[a + 1][b].visited) {
						right = points[a + 1][b].position;
					}
					if (b + 1 < h && !points[a][b + 1].visited) {
						down = points[a][b + 1].position;
					}
					if (b - 1 >= 0 && !points[a][b - 1].visited) {
						up = points[a][b - 1].position;
					}

					if (left != -1) {
						temp = left;
						if (right != -1) {
							s.push(right);
						}
						if (down != -1) {
							s.push(down);
						}
						if (up != -1) {
							s.push(up);
						}
					} else if (right != -1) {
						temp = right;
						if (down != -1) {
							s.push(down);
						}
						if (up != -1) {
							s.push(up);
						}
					} else if (down != -1) {
						temp = down;
						if (up != -1) {
							s.push(up);
						}
					} else if (up != -1) {
						temp = up;
					} else if (!s.empty()) {
						temp = s.pop();
					} else {
						temp = -1;
					}
				} else {
					if (!s.empty()) {
						temp = s.pop();
					} else {
						temp = -1;
					}
				}
			}

			// for (int k = 0; k < strList.size(); k++) {
			// if (strList.get(k).visited) {
			// strList.remove(k);
			// k--;
			// }
			// }
		}

		// getOther(temp, a, b, parts, strList, pp,w);
		Log.i("pas", partsList.size() + "");
		// 剔除不合理的黑块
		for (int i = 0; i < partsList.size(); i++) {
			if (partsList.get(i).size() < minDigNum
					|| partsList.get(i).size() > maxDigNum) {
				partsList.remove(i--);
			}
		}
		Log.i("pas", partsList.size() + "");
		for (int i = 0; i < partsList.size(); i++) {
			int l = getLeft(partsList.get(i));
			int r = getRight(partsList.get(i));
			int t = getTop(partsList.get(i));
			int b = getBottom(partsList.get(i));
			int total = (r - l) * (b - t);
			double bi = (r - l) / (double) (b - t);
			if (partsList.get(i).size() / (double) total < 0.7 || bi > 5
					|| bi < 0.2) {
				partsList.remove(i--);
			}
		}
		Log.i("pas", partsList.size() + "");

		int maxpos1 = 0, maxpos2 = 0;
		int max1, max2;
		max1 = partsList.get(0).size();

		for (int i = 1; i < partsList.size(); i++) {
			if (partsList.get(i).size() > max1) {
				max1 = partsList.get(i).size();
				maxpos1 = i;
			}
		}

		if (maxpos1 == 0) {
			max2 = partsList.get(1).size();
		} else {
			max2 = partsList.get(0).size();
		}
		for (int i = 1; i < partsList.size(); i++) {
			if (partsList.get(i).size() > max2 && i != maxpos1) {
				max2 = partsList.get(i).size();
				maxpos2 = i;
			}
		}

		int x1 = Integer.parseInt(partsList.get(maxpos1).get(0).split("_")[0]);
		int x2 = Integer.parseInt(partsList.get(maxpos2).get(0).split("_")[0]);

		if (x1 < x2) {
			part1 = partsList.get(maxpos1);
			part2 = partsList.get(maxpos2);
		} else {
			part1 = partsList.get(maxpos2);
			part2 = partsList.get(maxpos1);
		}

		Log.i("part12", part1.size() + "/" + part2.size());

		int left = getLeft(part1);
		int right = getRight(part2);

		Log.i("dian", left + "/" + getTop(part1) + "/" + getLeft(part2) + "/"
				+ getTop(part2));

		leftParts = new ArrayList<ArrayList<String>>();
		rightParts = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < partsList.size(); i++) {
			if (i != maxpos1 && i != maxpos2) {
				if (getLeft(partsList.get(i)) < left -5
						&&getTop(partsList.get(i))>getTop(part1)) {
					leftParts.add(partsList.get(i));
				}
				if (getRight(partsList.get(i)) > right + 10
						&&getTop(partsList.get(i))>getTop(part2)) {
					rightParts.add(partsList.get(i));
				}
			}
		}

		Log.i("lsize", leftParts.size() + "/" + rightParts.size());
		if (leftParts.size() < 2 || rightParts.size() < 2
				|| leftParts.size() != rightParts.size()) {
			int xa = getLeft(part1);
			int ya = getTop(part1);
			int xb = getLeft(part2);
			int yb = getTop(part2);

			int x = Math.abs(xa - xb);
			int y = ya - yb;
			double z = Math.sqrt(x * x + y * y);
			jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// ���սǶ�

			return false;
		}

		for (int i = 0; i < leftParts.size(); i++) {
			ArrayList<String> tempList = leftParts.get(i);
			for (int j = i + 1; j < leftParts.size(); j++) {
				if (getTop(leftParts.get(j)) < getTop(leftParts.get(i))) {
					leftParts.set(i, leftParts.get(j));
					leftParts.set(j, tempList);
					tempList = leftParts.get(i);
				}
			}
		}

		for (int i = 0; i < rightParts.size(); i++) {
			ArrayList<String> tempList = rightParts.get(i);
			for (int j = i + 1; j < rightParts.size(); j++) {
				if (getTop(rightParts.get(j)) < getTop(rightParts.get(i))) {
					rightParts.set(i, rightParts.get(j));
					rightParts.set(j, tempList);
					tempList = rightParts.get(i);
				}
			}
		}

		for (int i = 0; i < leftParts.size(); i++) {
			Log.i("leftparts", getTop(rightParts.get(i)) + "");
		}

		left1 = getTop(leftParts.get(0));
		int leftnum1 = 0;
		for (int i = 0; i < leftParts.size(); i++) {
			if (getTop(leftParts.get(i)) < left1) {
				left1 = getTop(leftParts.get(i));
				leftnum1 = i;
			}
		}
		if (leftnum1 == 0) {
			left2 = getBottom(leftParts.get(1));
		} else {
			left2 = getBottom(leftParts.get(0));
		}
		for (int i = 0; i < leftParts.size(); i++) {
			if (leftnum1 != i && getBottom(leftParts.get(i)) < left2) {
				left2 = getBottom(leftParts.get(i));
			}
		}

		right1 = getTop(rightParts.get(0));
		int rightnum1 = 0;
		for (int i = 0; i < rightParts.size(); i++) {
			if (getTop(rightParts.get(i)) < right1) {
				right1 = getTop(rightParts.get(i));
				rightnum1 = i;
			}
		}
		if (rightnum1 == 0) {
			right2 = getBottom(rightParts.get(1));
		} else {
			right2 = getBottom(rightParts.get(0));
		}
		for (int i = 0; i < rightParts.size(); i++) {
			if (rightnum1 != i && getBottom(rightParts.get(i)) < right2) {
				right2 = getBottom(rightParts.get(i));
			}
		}

		Log.i("landr", left1 + "/" + left2 + "/" + right1 + "/" + right2);

		return true;

		// for (int i = 0; i < w; i++) {
		// for (int j = 0; j < h; j++) {
		// boolean tag = false;
		// if (!partsList.get(0).contains(i + "_" + j)) {
		// p[j * w + i] = 0xFFFFFFFF;
		// } else {
		// p[j * w + i] = 0xFFAA0000;
		// }
		// }
		// }
	}
	
	private int getMiddle(ArrayList<String> array){
		int middle = 0;
		for(String m:array){
			middle += Integer.parseInt(m.split("_")[1]);
		}
		middle /= array.size();
		return middle;
	}

	private int getLeft(ArrayList<String> l) {
		int left = Integer.parseInt(l.get(0).split("_")[0]);
		for (int i = 1; i < l.size(); i++) {
			if (Integer.parseInt(l.get(i).split("_")[0]) < left) {
				left = Integer.parseInt(l.get(i).split("_")[0]);
			}
		}
		return left;
	}

	private int getRight(ArrayList<String> r) {
		int right = Integer.parseInt(r.get(0).split("_")[0]);
		for (int i = 1; i < r.size(); i++) {
			if (Integer.parseInt(r.get(i).split("_")[0]) > right) {
				right = Integer.parseInt(r.get(i).split("_")[0]);
			}
		}
		return right;
	}

	private int getTop(ArrayList<String> t) {
		int top = Integer.parseInt(t.get(0).split("_")[1]);
		for (int i = 1; i < t.size(); i++) {
			if (Integer.parseInt(t.get(i).split("_")[1]) < top) {
				top = Integer.parseInt(t.get(i).split("_")[1]);
			}
		}
		return top;
	}

	private int getBottom(ArrayList<String> b) {
		int bottom = Integer.parseInt(b.get(0).split("_")[1]);
		for (int i = 1; i < b.size(); i++) {
			if (Integer.parseInt(b.get(i).split("_")[1]) > bottom) {
				bottom = Integer.parseInt(b.get(i).split("_")[1]);
			}
		}
		return bottom;
	}

	private void getOther(int num, int a, int b, ArrayList<String> parts,
			ArrayList<Graph> strList, int[][] pp, int w) {
		if (strList.get(num).visited) {
			return;
		}
		Log.i("tj", num + "");
		parts.add(a + "_" + b);
		int left, right, down, up;
		left = right = down = up = -1;
		for (int i = Math.max(0, num - w); i < num + w; i++) {
			if (strList.get(i).p.equals((a - 1) + "_" + (b))) {
				left = i;
			}
			if (strList.get(i).p.equals((a + 1) + "_" + b)) {
				right = i;
			}
			if (strList.get(i).p.equals(a + "_" + (b + 1))) {
				down = i;
			}
			if (strList.get(i).p.equals(a + "_" + (b - 1))) {
				down = i;
			}
		}
		strList.get(num).visited = true;
		if (left != -1) {
			getOther(left, a - 1, b, parts, strList, pp, w);
		}
		if (right != -1) {
			getOther(right, a + 1, b, parts, strList, pp, w);
		}
		if (down != -1) {
			getOther(down, a, b + 1, parts, strList, pp, w);
		}
		Log.i("lrd", left + "/" + right + "/" + down);
	}

	class Graph {
		String p;
		boolean visited = false;
		int x, y;
		boolean isBlack = false;

		Graph(String p, int x, int y) {
			this.p = p;
			this.x = x;
			this.y = y;
		}

	}

	class Graph2 {
		boolean visited = false;
		int x, y;
		boolean isBlack = false;
		int position;

		Graph2() {

		}

		Graph2(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 202:
			tj = 0;
			answerNum = 6;
			try {
				pos = Integer.parseInt(et.getText().toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			Uri uri = data.getData();
			String picturePath = getPath(ImageDealActivity.this, uri);

			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(picturePath, options);
			int zoom = 1;
			while (options.outWidth / zoom > 2000
					|| options.outHeight / zoom > 2000) {
				zoom *= 2;
			}
			options.inSampleSize = zoom;
			options.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(picturePath, options);

			Matrix m = new Matrix();
			m.postScale((360 / (float) (options.outWidth)),
					(640 / (float) (options.outHeight)));
			Log.i("wandh", options.outWidth + "/" + options.outHeight);
			Log.i("wandh", (float) options.outWidth / 360 + "/"
					+ (float) options.outHeight / 640);

			bmp = Bitmap.createBitmap(bmp, 0, 0, options.outWidth,
					options.outHeight, m, true);
			Log.i("bmpw", bmp.getWidth() + "/" + bmp.getHeight());

			tv.setImageBitmap(bmp);
			int w = bmp.getWidth();
			int h = bmp.getHeight();
			int[] pixels = new int[w * h];
			int[] pixels0 = new int[w * h];
			Log.i("wandh", w * h + "/");

			bmp.getPixels(pixels, 0, w, 0, 0, w, h);
			bmp.getPixels(pixels0, 0, w, 0, 0, w, h);

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = setToBlackOrWhite(pixels[i]);
			}
			if (!test3(pixels, pixels0, w, h)) {
				Toast.makeText(ImageDealActivity.this, "试卷解析失败！", 0).show();

				Matrix mat = new Matrix();
				mat.preRotate(jiaodu);
				bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, mat, true);
				Bitmap model = Bitmap.createBitmap(bmp.getWidth(),
						bmp.getHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(model);
				canvas.drawColor(0xFFFFFFFF);
				canvas.drawBitmap(bmp, 0, 0, null);
				bmp = model;
				// mat.reset();
				// mat.postScale((360 / (float) (bmp.getWidth())),
				// (640 / (float) (bmp.getWidth())));
				// bmp =
				// Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getWidth(),mat,true);

				w = bmp.getWidth();
				h = bmp.getHeight();
				Log.i("wwhh", w + "/" + h);
				pixels = new int[w * h];
				pixels0 = new int[w*h];
				bmp.getPixels(pixels, 0, w, 0, 0, w, h);
				bmp.getPixels(pixels0, 0, w, 0, 0, w, h);
				for (int i = 0; i < pixels.length; i++) {
					pixels[i] = setToBlackOrWhite(pixels[i]);
				}
				getRGB(pixels[200 * w + 1]);
				test3(pixels, pixels0, w, h);
			}
			//试卷右侧部分的识别代码
//			double translate = (getLeft(rightParts.get(0)) - getRight(leftParts
//					.get(0))) / 446d;
//			String ans = "";
//			for(int i=0;i<leftParts.size();i++){
//				int middle = 0;
//				middle = getMiddle(leftParts.get(i));
//				
//				int x0 = getRight(leftParts.get(i))+(int)(223*translate);
//				int x1 = getRight(leftParts.get(i))+(int)(440*translate);
//				int y0 = middle - (int)(8*translate);
//				int y1 = middle + (int)(8*translate);
//				int dis = getMiddle(rightParts.get(i)) - middle;
//				double part = (433/21d)*translate;
//				
//				y0 += dis*10/21;
//				y1 += dis*10/21;
//				
//				int [] points = new int[11];
//				for(int j = 0;j<11;j++){
//					y0 += dis/21;
//					y1 += dis/21;
////					int [][] p = new int[(int)(222.5*(j+1))-(int)(222.5*j)][y1-y0];
//					for(int k = x0+(int)(part*j);k<x0+(int)(part*(j+1));k++){
//						for(int t = y0;t<y1;t++){
//							if(isBlack(pixels0[t*w+k])){
//								points[j]++;
//							}
//						}
//					}
//				}
//				int pos = 0;
//				int max = 0;
//				for(int j=0;j<11;j++){
//					Log.i("points","ps: "+points[j]);
//					if(points[j]>max){
//						pos = j;
//						max = points[j];
//					}
//				}
//				Log.i("pos",""+pos);
//				ans+= i+":"+(10-pos)+"\t";
//				if(i == 1){
//					Bitmap bmp2 = Bitmap.createBitmap(pixels,(middle-(int)(8*translate))*w+getRight(leftParts.get(i))+(int)(5*translate),
//							w,(int)(436*translate),(int)(16*translate),Bitmap.Config.ARGB_8888);
//					tv2.setImageBitmap(bmp2);
//				}
//			}
//			et.setText(ans);
			
			
			//下面是试卷左侧部分的识别代码
			double translate = (getLeft(rightParts.get(0)) - getRight(leftParts
					.get(0))) / 445d;
			int[] ls = new int[4];
			int[] rs = new int[4];
			ls[0] = (int) (translate * 30);
			rs[0] = (int) (translate * 107);
			ls[1] = (int) (translate * 135);
			rs[1] = (int) (translate * 212);
			ls[2] = (int) (translate * 259);
			rs[2] = (int) (translate * 336);
			ls[3] = (int) (translate * 364);
			rs[3] = (int) (translate * 441);
			int right0 = getRight(leftParts.get(0));

			int y0 = getTop(leftParts.get(0)) - 2;
			int y1 = getBottom(leftParts.get(0)) + 2;
			
			int dif = (getTop(rightParts.get(0))-y0+getBottom(rightParts.get(0))-y1)/2;
			Log.i("four",getTop(rightParts.get(0))+"/"+y0+"/"+getBottom(rightParts.get(0))+"/"+y1);
			Log.i("dif",dif+"");
			String answers = "";
			while (answerNum > 0) {
				for (int i = 0; i < 4 && answerNum > 0; i++) {
					bmps[6-answerNum] = Bitmap.createBitmap(pixels0,(y0+(int)((2*i+1)*dif/8d))*w+ls[i]+right0,w,rs[i]-ls[i],y1-y0,Bitmap.Config.ARGB_8888);
					int a, b, c, d;
					a = b = c = d = 0;
					for (int j = 0; j < 4; j++) {
						int x0 = right0 + ls[i] + j * (rs[i] - ls[i]) / 4;
						int x1 = right0 + ls[i] + (j + 1) * (rs[i] - ls[i]) / 4;
						for (int k = x0; k < x1; k++) {
							for (int t = y0+(int)((2*i+1)*dif/8d); t < y1+(int)((2*i+1)*dif/8d); t++) {
								if (j == 0 && isBlack(pixels[t * w + k])) {
									a++;
								}
								if (j == 1 && isBlack(pixels[t * w + k])) {
									b++;
								}
								if (j == 2 && isBlack(pixels[t * w + k])) {
									c++;
								}
								if (j == 3 && isBlack(pixels[t * w + k])) {
									d++;
								}
							}
						}
					}
					answerNum--;
					boolean hasAnswer = false;
					if (Math.max(Math.max(a, b), Math.max(c, d)) == a ) {
						Log.i("answer", "A" + a);
						answers += (6 - answerNum) + ":" + "A\t";
						hasAnswer = true;
					}
					if (Math.max(Math.max(a, b), Math.max(c, d)) == b ) {
						Log.i("answer", "B" + b);
						answers += (6 - answerNum) + ":" + "B\t";
						hasAnswer = true;
					}
					if (Math.max(Math.max(a, b), Math.max(c, d)) == c ) {
						Log.i("answer", "C" + c);
						answers += (6 - answerNum) + ":" + "C\t";
						hasAnswer = true;
					}
					if (Math.max(Math.max(a, b), Math.max(c, d)) == d ) {
						Log.i("answer", "D" + d);
						answers += (6 - answerNum) + ":" + "D\t";
						hasAnswer = true;
					}
					if (!hasAnswer) {
						answers += (6 - answerNum) + ":" + "X\t";
					}
				}
				
				for (ArrayList<String> parts : leftParts) {
					for (String part : parts) {
						int x = Integer.parseInt(part.split("_")[0]);
						int y = Integer.parseInt(part.split("_")[1]);
						pixels[y * w + x] = 0xFFAA0000;
					}
				}

				for (ArrayList<String> parts : rightParts) {
					for (String part : parts) {
						int x = Integer.parseInt(part.split("_")[0]);
						int y = Integer.parseInt(part.split("_")[1]);
						pixels[y * w + x] = 0xFFAA0000;
					}
				}
				int temp = y1 - y0;
				y0 = y1 + temp - 2;
				y1 += 2 * temp - 2;
			}


			if (rightParts.size() > 2) {
				for (int i = 2; i < rightParts.size(); i++) {
					int y = getTop(rightParts.get(i));
					int x_0 = getLeft(rightParts.get(i));
					int x_1 = getRight(rightParts.get(i));
					int smallW = x_1 - x_0;
					int[] temps = new int[(int)(20*translate)
							* (int) (2 * smallW)];
					Bitmap tempBitmap = Bitmap.createBitmap(pixels0, (y - (int) (0.8 * smallW)) * w
							+ x_0 - (int)(26*translate), w, (int)(20*translate),
							(int) (2 * smallW), Bitmap.Config.ARGB_8888);
					tempBitmap.getPixels(temps, 0, (int)(20*translate), 0, 0, (int)(20*translate), (int) (2 * smallW));
					tempBitmap = null;
					answers+=isTrue(temps, (int)(20*translate), (int) (2 * smallW));
					if(i == 3){
						for(int j=0;j<(int)(20*translate);j++){
							for(int t=0;t<(int) (2 * smallW);t++){
								getRGB(temps[t*(int)(20*translate)+j]);
							}
						}
					}
				}
			}
			et.setText(answers);
			Log.i("left12", left1 + "/" + left2);

			Bitmap bmp2 = Bitmap.createBitmap(pixels, 0, w, w,
					h, Bitmap.Config.ARGB_8888);
			tv2.setImageBitmap(bmp2);
			
			break;

		default:
			break;
		}
	}
	
	private boolean isTrue(int [] p,int w,int h){
		int count =0;
		int avg = 0;
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				int red = (p[j*w+i] & 0x00FF0000) >> 16;
				int green = (p[j*w+i] & 0x0000FF00) >> 8;
				int blue = (p[j*w+i] & 0x000000FF);
				avg = avg +red+green+blue;
			}
		}
		avg /= w*h;
		Log.i("avg",avg+"");
		for(int i=0;i<w;i++){
			int left = -1;
			int right = -1;
			for(int j=0;j<h;j++){
				int red = (p[j*w+i] & 0x00FF0000) >> 16;
				int green = (p[j*w+i] & 0x0000FF00) >> 8;
				int blue = (p[j*w+i] & 0x000000FF);
				if(avg - (red+green+blue)>45){
					if(left == -1|| left == i-1){
						left = i;
					}else{
						right = i;
					}
				}
			}
			if(left != -1 && right != -1){
				count ++;
			}
		}
		Log.i("count",count+"");
		return count >5?false:true;
	}
	

	private boolean isBlack(int a) {
		int red = (a & 0x00FF0000) >> 16;
		int green = (a & 0x0000FF00) >> 8;
		int blue = (a & 0x000000FF);
		if (red < 80 || green < 80 || blue < 80) {
			return true;
		} else {
			return false;
		}
	}

	public int setRedToBlack(int a) {
		int red = (a & 0x00FF0000) >> 16;
		int green = (a & 0x0000FF00) >> 8;
		int blue = (a & 0x000000FF);
		if (red < 125 && red - green > 15 && red - blue > 15) {
			return 0xFF000000;
		} else {
			return 0xFFFFFFFF;
		}
	}

	private int getRGB(int a) {
		int red = (a & 0x00FF0000) >> 16;
		int green = (a & 0x0000FF00) >> 8;
		int blue = (a & 0x000000FF);
		int temp = (red + green + blue) / 3;
		Log.i("rgb", red + "/" + green + "/" + blue);
		red = (temp << 16) & 0x00FF0000;
		green = (temp << 8) & 0x0000FF00;
		blue = (temp) & 0x000000FF;
		return 0xFF000000 | red | green | blue;
	}

	private int setToBlackOrWhite(int a) {
		int red = (a & 0x00FF0000) >> 16;
		int green = (a & 0x0000FF00) >> 8;
		int blue = (a & 0x000000FF);
		int temp = (red + green + blue) / 3;
		if (red < 80 || green < 80 || blue < 80) {
			red = blue = green = 0;
		} else {
			red = blue = green = 255;
		}
		red = (red << 16) & 0x00FF0000;
		green = (green << 8) & 0x0000FF00;
		blue = (blue) & 0x000000FF;
		return 0xFF000000 | red | green | blue;
	}

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (1) {
			case 1:

				break;

			default:
				break;
			}
		};
	};

}
