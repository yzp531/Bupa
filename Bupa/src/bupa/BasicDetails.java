package bupa;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bupa.eff.EffBottonBase;

import com.bupa.R;

public class BasicDetails extends FinalActivity {

	@ViewInject(id = R.id.spinnerNthnicity)
	Spinner _spinnerNthnicity;
	@ViewInject(id = R.id.textAge)
	EditText textAge;
	@ViewInject(id = R.id.spinnerWeight)
	Spinner _spinnerWeight;
	@ViewInject(id = R.id.spinnerHeight)
	Spinner _spinnerHeight;
	@ViewInject(id = R.id.postcodeEditText)
	EditText postcodeEditText;

	@ViewInject(id = R.id.imageButtonNext, click = "btnClick")
	ImageButton nextButton;

	@ViewInject(id = R.id.imageButtonMaleSelected, click = "meleClick")
	ImageButton maleButton;
	@ViewInject(id = R.id.imageButtonFemale, click = "femaleClick")
	ImageButton femaleButton;

	private ArrayAdapter<?> _adapterNthnicity;
	private ArrayAdapter<?> _adapterWeight;
	private ArrayAdapter<?> _adapterHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ�����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();

	}

	public void init() {

		_adapterNthnicity = ArrayAdapter.createFromResource(this,
				R.array._dataNthnicity, android.R.layout.simple_spinner_item);
		_spinnerNthnicity.setAdapter(_adapterNthnicity);
		_adapterNthnicity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinnerNthnicity
				.setOnItemSelectedListener(new SpinnerCitySelectedListener());

		_adapterWeight = ArrayAdapter.createFromResource(this,
				R.array._dataWeight, android.R.layout.simple_spinner_item);
		_spinnerWeight.setAdapter(_adapterWeight);
		_adapterWeight
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinnerWeight
				.setOnItemSelectedListener(new SpinnerWeightSelectedListener());

		_adapterHeight = ArrayAdapter.createFromResource(this,
				R.array._dataHeight, android.R.layout.simple_spinner_item);
		_spinnerHeight.setAdapter(_adapterHeight);
		_adapterHeight
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinnerHeight
				.setOnItemSelectedListener(new SpinnerHeightSelectedListener());

		maleButton.setSelected(true);
		femaleButton.setSelected(false);
		femaleButton
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		maleButton
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		//
		nextButton
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);

	}

	public void putValue() {
		String sex = GlobalDataModel.getInstance().bupaUserData.sex = maleButton
				.isSelected() ? "male" : "female".toString();
		String enthnicity = GlobalDataModel.getInstance().bupaUserData.enthnicity = _spinnerNthnicity
				.getSelectedItem().toString();

		String age = textAge.getText().toString().trim();
		String weight = GlobalDataModel.getInstance().bupaUserData.weight = _spinnerWeight
				.getSelectedItem().toString();
		String height = GlobalDataModel.getInstance().bupaUserData.height = _spinnerHeight
				.getSelectedItem().toString();

		GlobalDataModel.getInstance().trace(
				maleButton.isSelected() ? "male" : "female");
		GlobalDataModel
				.getInstance()
				.trace("enthnicity:"
						+ GlobalDataModel.getInstance().bupaUserData.enthnicity);

		SharedPreferences prefs = getSharedPreferences("bupa_prefs",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("sex", sex);
		if (enthnicity.equals("Please Select")) {
			Toast.makeText(BasicDetails.this, "Please select your enthnicity ",
					Toast.LENGTH_LONG).show();
		} else {
			editor.putString("enthnicity", enthnicity);
		}

		editor.putString("age", age);
		editor.putString("weight", weight);
		editor.putString("height", height);
		editor.putString("postcodeEditText", postcodeEditText.getText()
				.toString());
		editor.commit();
	}

	boolean isFirst = false;

	class SpinnerCitySelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView tv = (TextView) arg1;
			tv.setTextColor(Color.WHITE);
			_adapterNthnicity.getItem(arg2);
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class SpinnerHeightSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView tv = (TextView) arg1;
			tv.setTextColor(Color.WHITE);
			_adapterHeight.getItem(arg2);
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class SpinnerWeightSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView tv = (TextView) arg1;
			tv.setTextColor(Color.WHITE);
			_adapterWeight.getItem(arg2);
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	public void btnClick(View v) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {

				if (!"Please Select :".equals(_spinnerNthnicity
						.getSelectedItem().toString())
						&& !"Please Select :".equals(_spinnerWeight
								.getSelectedItem().toString())
						&& !"Please Select :".equals(_spinnerHeight
								.getSelectedItem().toString())) {

					if (!"".equals(textAge.getText().toString().trim())) {
						int agetext = Integer.parseInt(textAge.getText()
								.toString());
						if (agetext <= 84 && agetext >= 25) {
							Intent it = new Intent(BasicDetails.this,
									HealthDetails.class);
							startActivity(it);
							overridePendingTransition(R.anim.in_from_left,
									R.anim.out_stagnant);
						} else {
							Toast.makeText(BasicDetails.this,
									"Age must be between 25 to 84",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(BasicDetails.this, "Age can't be empty",
								Toast.LENGTH_LONG).show();
					}

				} else {
					if ("Please Select :".equals(_spinnerNthnicity
							.getSelectedItem().toString())) {
						Toast.makeText(BasicDetails.this,
								"Please select your enthnicity ",
								Toast.LENGTH_LONG).show();
					} else if ("Please Select :".equals(_spinnerWeight
							.getSelectedItem().toString())) {
						Toast.makeText(BasicDetails.this,
								"Please select your weight ", Toast.LENGTH_LONG)
								.show();
					} else if ("Please Select :".equals(_spinnerHeight
							.getSelectedItem().toString())) {
						Toast.makeText(BasicDetails.this,
								"Please select your height ", Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});
	}

	public void meleClick(View v) {
		maleButton.setSelected(true);
		femaleButton.setSelected(false);
		maleButton.setBackgroundResource(R.drawable.maleselected);
		femaleButton.setBackgroundResource(R.drawable.femalenormal);
	}

	public void femaleClick(View v) {
		maleButton.setSelected(false);
		femaleButton.setSelected(true);
		maleButton.setBackgroundResource(R.drawable.malenormal);
		femaleButton.setBackgroundResource(R.drawable.femaleselected);
	}

	@Override
	protected void onPause() {
		putValue();
		super.onPause();
	}

}
