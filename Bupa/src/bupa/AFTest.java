package bupa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bupa.eff.EffBottonBase;
import bupa.eff.ImageProcessing;
import bupa.AfrCount;
import com.bupa.R;
import com.hs.aflib.AFLibrary;
import com.hs.aflib.AFPoint;
import com.nineoldandroids.animation.ObjectAnimator;

public class AFTest extends FinalActivity {

	private Vibrator $vibrator = null;

	private int averageIndex = 0;
	private final int averageArraySize = 4;
	private final double[] averageArray = new double[averageArraySize];

	private int beatsIndex = 0;
	private final int beatsArraySize = 3;
	private final int[] beatsArray = new int[beatsArraySize];

	private final AtomicBoolean processing = new AtomicBoolean(false);
	private ArrayList<Double> imgAveArr = new ArrayList<Double>();
	private ArrayList<AFPoint> peakArr = new ArrayList<AFPoint>();
	private ArrayList<AFPoint> peakArr1 = new ArrayList<AFPoint>();
	private ArrayList<AFPoint> peakArr2 = new ArrayList<AFPoint>();
	private ArrayList<AFPoint> peakArr3 = new ArrayList<AFPoint>();
	private ArrayList<AFPoint> chartArr = new ArrayList<AFPoint>();
	private boolean testfinish = false;
	private boolean teststop = false;
	private double chartX = 0;
	private Camera camera = null;
	private int beats2 = 0;
	private int measureTime = 44;
	private int lowestADC = 12000;
	private int prPercent = 0; // Added for display percentage
	private WakeLock wakeLock = null;
	private double maxADC = 0;
	private long startTime = 0;
	private boolean initCameraOK = false; // Added for HTC One X problem
	private int result = 0;
	private int pulse_rate = 0;
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private boolean soundOn = true;

	double _score;

	public enum TYPE {
		GREEN, RED
	};

	private TYPE currentType = TYPE.GREEN;

	public TYPE getCurrent() {
		return currentType;
	}

	private Boolean isOpenCamera = false;

	@ViewInject(id = R.id.btnOnOff, click = "onOffListener")
	ImageButton btnOn;
	@ViewInject(id = R.id.textViewAF)
	TextView _textViewAF;
	@ViewInject(id = R.id.loading)
	ProgressBar imgloading;
	@ViewInject(id = R.id.imageButtonseeresult)
	ImageButton btnresult;
	@ViewInject(id = R.id.progress3barID)
	ImageView progbar3ID;
	@ViewInject(id = R.id.progress2barID)
	ImageView progbar2ID;
	@ViewInject(id = R.id.resultlayout)
	LinearLayout resultlayout;
	@ViewInject(id = R.id.imageButtonresult)
	ImageButton imageButtonresult;
	@ViewInject(id = R.id.toptextimg)
	ImageView toptextimg;
	@ViewInject(id = R.id.layoutidresult)
	RelativeLayout layoutidresult;
	@ViewInject(id = R.id.textresult)
	TextView textresult;
	@ViewInject(id = R.id.tvresult)
	TextView tvresult;
	@ViewInject(id = R.id.firstlayoutin)
	RelativeLayout firstlayoutin;
	@ViewInject(id = R.id.imageViewshow)
	ImageView imageViewshow;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ??????
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aftest);

		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		bottomImg1.setX(screenWidth);
		bottomImg2.setX(screenWidth * 2);

		preview = (SurfaceView) findViewById(R.id.preview1);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);

		int currentapiVersion = Integer.valueOf(android.os.Build.VERSION.SDK);

		if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
			previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

		btnOn.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		btnresult.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		imageButtonresult
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		$vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		// userSettings =
		// PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// putSharedPrefBoolean(userSettings, "soundOn", soundOn);
		// Animation anim = AnimationUtils.loadAnimation(this,
		// R.drawable.round_loading);
		// imgloading.startAnimation(anim);

	}

	public void onOffListener(View v) {

		GlobalDataModel.getInstance().trace("onOffClick:" + isOpenCamera);

		if (isOpenCamera == false) {
			openCamera();
			imgloading.setVisibility(View.VISIBLE);
			progbar2ID.setVisibility(View.VISIBLE);
			progbar3ID.setVisibility(View.GONE);
			btnresult.setVisibility(View.GONE);
			switchingState(1);
		} else {
			closeCamera();
			_textViewAF.setText("");
			btnOn.setBackgroundResource(R.drawable.btnpresstostart);
			switchingState(0);
		}
	};

	private void openCamera() {
		if (camera == null) {
			// complain("You need a back camera to run this app!");
		} else {
			// try {
			// _score = cvd_male_raw(40, 0, 0, 0, 1, 1, 0, 50, 6, 1, 5.0,
			// 180.0, 1, 10, 8.0);
			//
			// System.out.println("++++++++++++" + _score);
			//
			// } catch (NameNotFoundException e) {
			// e.printStackTrace();
			// }
			isOpenCamera = true;
			previewHolder.addCallback(surfaceCallback);
			camera.setPreviewCallback(previewCallback);
			if (camera != null) {
				Camera.Parameters parameters = camera.getParameters();

				List<String> focusModes = parameters.getSupportedFocusModes();

				if (focusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
				} else if (focusModes
						.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
					parameters
							.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
				}

				List<String> flashModes = parameters.getSupportedFlashModes();
				if (flashModes == null) {
					lowestADC = 0;
				} else {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				}

				camera.setParameters(parameters);
			}
			_textViewAF.setText("");
			btnOn.setBackgroundResource(R.drawable.btnpresstostart);
		}

		resetData();
	};

	private void closeCamera() {
		startTime = System.currentTimeMillis();
		beats2 = 0;
		isOpenCamera = false;
		imgloading.setVisibility(View.GONE);

		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			// btnOn.setBackgroundResource(R.drawable.btnpresstostart);

		}
		camera.stopPreview();

		if (!testfinish) {
			teststop = true;
			// btnOn.setBackgroundResource(R.drawable.btnpresstostart);
		}
	};

	// ???????
	private void resetData() {
		btnOn.setBackgroundResource(R.drawable.btnwhitebgd);

		imgAveArr = new ArrayList<Double>();
		peakArr = new ArrayList<AFPoint>();
		peakArr1 = new ArrayList<AFPoint>();
		peakArr2 = new ArrayList<AFPoint>();
		peakArr3 = new ArrayList<AFPoint>();

		chartArr = new ArrayList<AFPoint>();
		chartX = 0;
		beatsIndex = 0;
		averageIndex = 0;

		testfinish = false;
		teststop = false;
		prPercent = 0;
		maxADC = 0;
		startTime = System.currentTimeMillis();
		camera.startPreview();
	};

	private PreviewCallback previewCallback = new PreviewCallback() {

		@Override
		public void onPreviewFrame(byte[] data, Camera cam) {

			if (data == null)
				throw new NullPointerException();
			Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null)
				throw new NullPointerException();

			if (!processing.compareAndSet(false, true))
				return;

			long endTime = System.currentTimeMillis();
			double totalTimeInSecs = (endTime - startTime) / 1000d;

			int temp_prPercent = (int) ((totalTimeInSecs / (double) measureTime) * 100);

			if (temp_prPercent > 100) {
				startTime = System.currentTimeMillis();
				testfinish = true;
				maxADC = 0;
				// Vibrate for 500 milliseconds
				if (soundOn) {
					$vibrator.vibrate(200);
				}
				setLight(0); // Will also stop waveform
				_textViewAF.setText("");
				processing.set(false);
				return;
			}

			if (temp_prPercent > prPercent) {
				prPercent = temp_prPercent;
				_textViewAF.setText(String.valueOf(prPercent) + "%");
			}

			int width = size.width;
			int height = size.height;

			double imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(
					data.clone(), height, width);

			imgAvg = imgAvg * 100;

			if (chartX > 1) {
				chartArr.add(new AFPoint(chartX, imgAvg));
			}

			chartX++;
			imgAveArr.add(imgAvg);

			if (totalTimeInSecs > 8) {
				if (maxADC < imgAvg) {
					maxADC = imgAvg;
				}

				if (maxADC - imgAvg > 4000 || (imgAvg < lowestADC)
						|| imgAvg == 0) {
					startTime = System.currentTimeMillis();
					beats2 = 0;
					maxADC = 0;
					processing.set(false);
					setLight(0); // Will also stop waveform
					testfinish = true;
					// Vibrate for 500 milliseconds
					if (soundOn) {
						$vibrator.vibrate(200);
					}
					_textViewAF.setText("");
					return;
				}
			}

			int averageArrayAvg = 0;
			int averageArrayCnt = 0;

			for (int i = 0; i < averageArray.length; i++) {
				if (averageArray[i] > 0) {
					averageArrayAvg += averageArray[i];
					averageArrayCnt++;
				}
			}

			int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt)
					: 0;
			TYPE newType = currentType;
			if (imgAvg < rollingAverage) {
				newType = TYPE.RED;
				if (newType != currentType) {
					if (totalTimeInSecs > 8) {
						beats2++;
					}
					if (averageArray[averageArraySize - 1] > 0) {
						int maxPos = findMaxIndex(averageArray);
						peakArr.add(new AFPoint(chartX - averageArraySize
								+ maxPos, averageArray[maxPos]));
						if (totalTimeInSecs > 8 && totalTimeInSecs <= 20) {
							peakArr1.add(new AFPoint(chartX - averageArraySize
									+ maxPos, averageArray[maxPos]));
						} else if (totalTimeInSecs > 20
								&& totalTimeInSecs <= 32) {
							peakArr2.add(new AFPoint(chartX - averageArraySize
									+ maxPos, averageArray[maxPos]));
						} else if (totalTimeInSecs > 32
								&& totalTimeInSecs <= 44) {
							peakArr3.add(new AFPoint(chartX - averageArraySize
									+ maxPos, averageArray[maxPos]));
						}
					} else {
						int maxPos = findMaxIndex(averageArray);
						peakArr.add(new AFPoint(maxPos, averageArray[maxPos]));
						if (totalTimeInSecs > 8 && totalTimeInSecs <= 20) {
							peakArr1.add(new AFPoint(maxPos,
									averageArray[maxPos]));
						} else if (totalTimeInSecs > 20
								&& totalTimeInSecs <= 32) {
							peakArr2.add(new AFPoint(maxPos,
									averageArray[maxPos]));
						} else if (totalTimeInSecs > 32
								&& totalTimeInSecs <= 44) {
							peakArr3.add(new AFPoint(maxPos,
									averageArray[maxPos]));
						}
					}
				}
			} else if (imgAvg > rollingAverage) {
				newType = TYPE.GREEN;
			}

			if (averageIndex == averageArraySize)
				averageIndex = 0;
			averageArray[averageIndex] = (int) imgAvg;
			averageIndex++;

			// Transitioned from one state to another to the same
			if (newType != currentType) {
				currentType = newType;
			}

			if (totalTimeInSecs >= measureTime) {
				double bps = (beats2 / (totalTimeInSecs - 8));
				int dpm = (int) (bps * 60d);
				if (dpm < 30 || dpm > 180) {
					setLight(0); // Will also stop waveform
				} else {
					if (beatsIndex == beatsArraySize)
						beatsIndex = 0;
					beatsArray[beatsIndex] = dpm;
					beatsIndex++;

					pulse_rate = dpm;
					result = AFLibrary.computeAF(peakArr1, peakArr2, peakArr3);
					setLight(result);
				}
				// btnOn.setEnabled(false);
				startTime = System.currentTimeMillis();
				testfinish = true;

				// Vibrate for 500 milliseconds
				if (soundOn) {
					$vibrator.vibrate(200);
				}
			}

			if (testfinish || teststop) {
				// _textViewAF.setText("");
			}
			processing.set(false);
		}

	};

	private int findMaxIndex(double[] Arr) {
		int i = 0;
		double max = 0;
		max = Arr[0];
		for (int c = 0; c < Arr.length; c++) {
			if (Arr[c] > max) {
				i++;
			}
		}

		return i;
	}

	private void setLight(int i) {
		GlobalDataModel.getInstance().trace("setLight:" + i);
		closeCamera();
		AfrCount afrCount = new AfrCount(AFTest.this);
		if (i == 0) {
			_textViewAF.setText("");
			btnOn.setBackgroundResource(R.drawable.readybtn);
			switchingState(0);

		} else if (i == 1) {

			// btnOn.setBackgroundResource(R.drawable.normalbtn);
			if (pulse_rate > 95) {
			}
			_score = afrCount.checkee(0);
			setvisibility(_score);

		} else if (i == 2) {
			// btnOn.setBackgroundResource(R.drawable.results_2);
			_score = afrCount.checkee(0);
			setvisibility(_score);

		} else if (i == 3) {
			// btnOn.setBackgroundResource(R.drawable.results_3);
			switchingState(2);
			_score = afrCount.checkee(1);
			setvisibility(_score);

		} else if (i == 4) {
			// btnOn.setBackgroundResource(R.drawable.results_4);
			switchingState(2);
			_score = afrCount.checkee(1);
			setvisibility(_score);

		} else {
			_textViewAF.setText("");
			btnOn.setBackgroundResource(R.drawable.readybtn);
			switchingState(0);
		}

	}

	private void setvisibility(final double _score) {
		if (_score < 30) {
			imageViewshow.setBackgroundResource(R.drawable.lowriskalert);
		} else if (_score >= 30 && _score < 60) {
			imageViewshow.setBackgroundResource(R.drawable.r_btn_2);
		} else {
			imageViewshow.setBackgroundResource(R.drawable.r_btn_3);
		}
		progbar2ID.setVisibility(View.GONE);
		progbar3ID.setVisibility(View.VISIBLE);
		btnresult.setVisibility(View.VISIBLE);
		toptextimg.setVisibility(View.GONE);
		firstlayoutin.setVisibility(View.VISIBLE);
		btnOn.setOnClickListener(null);
		switchingState(2);
		btnresult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resultlayout.setVisibility(View.VISIBLE);
				btnresult.setVisibility(View.GONE);
				btnOn.setOnClickListener(null);
				toptextimg.setVisibility(View.GONE);
				firstlayoutin.setVisibility(View.GONE);
				layoutidresult.setVisibility(View.VISIBLE);
				switchingState(3);
			}
		});
		textresult
				.setText(String.valueOf(Math.round(_score * 10) / 10.0) + "%");
		tvresult.setText(String.valueOf(Math.round(_score * 10) / 10.0)
				+ "% - " + getResources().getString(R.string.testres_txt2));
		imageButtonresult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AFTest.this, AfrResult.class);
				intent.putExtra("score", _score);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_stagnant);
			}
		});

	}

	private Camera openBackFacingCameraGingerbread() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);

			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {
					GlobalDataModel.getInstance()
							.trace("Camera failed to open: "
									+ e.getLocalizedMessage());
				}
			}
		}

		return cam;
	}

	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			initCameraOK = true; // For onPause to use, so as to pause correctly
									// - Added for HTC One X problem
			try {
				camera.setPreviewDisplay(previewHolder);
				camera.setPreviewCallback(previewCallback);
			} catch (Throwable t) {
				GlobalDataModel.getInstance().trace(
						"PreviewDemo-surfaceCallback"
								+ "Exception in setPreviewDisplay()");
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			if (camera != null) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = get640480PreviewSize(parameters);
				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					GlobalDataModel.getInstance().trace(
							"Using width=" + size.width + " height="
									+ size.height);
				}
				camera.setParameters(parameters);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};

	private Camera.Size get640480PreviewSize(Camera.Parameters parameters) {
		Camera.Size result = null;

		// 1st search
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width == 640 && size.height == 480) {
				result = size;
				break;
			}
		}

		// 1st search fail, try 2nd search
		if (result == null) {
			for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
				if (size.width == 640 || size.height == 480) {
					result = size;
					break;
				}
			}
		}

		return result;
	}

	// public boolean putSharedPrefBoolean(SharedPreferences pref, String key,
	// boolean value) {
	// SharedPreferences.Editor editor = pref.edit();
	// editor.putBoolean(key, value);
	// return editor.commit();
	// }

	@ViewInject(id = R.id.bottomtextimg)
	ImageView bottomImg0;
	@ViewInject(id = R.id.bottomtextimg1)
	ImageView bottomImg1;
	@ViewInject(id = R.id.bottomtextimg2)
	ImageView bottomImg2;

	private int screenWidth;

	// ?????
	public void switchingState(int state) {
		float img0tx = -state * screenWidth;
		float img1tx = img0tx + screenWidth;
		float img2tx = img1tx + screenWidth;
		float img3tx = img2tx + screenWidth;

		ObjectAnimator.ofFloat(bottomImg0, "translationX", img0tx)
				.setDuration(300).start();
		ObjectAnimator.ofFloat(bottomImg1, "translationX", img1tx)
				.setDuration(300).start();
		ObjectAnimator.ofFloat(bottomImg2, "translationX", img2tx)
				.setDuration(300).start();
		ObjectAnimator.ofFloat(resultlayout, "translationX", img3tx)
				.setDuration(300).start();
	}

	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();

		isOpenCamera = false;

		if (camera == null) {
			camera = openBackFacingCameraGingerbread();
		}

		startTime = System.currentTimeMillis();
		//
		if (camera != null) {
			camera.stopPreview(); // Added for HTC One X problem
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		// Added for HTC One X problem
		if (initCameraOK == false) {
			return;
		}
		initCameraOK = false;

		wakeLock.release();

		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

}
