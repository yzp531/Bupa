package bupa;

import java.util.Vector;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import bupa.eff.EffBottonBase;

import com.bupa.R;

public class HealthDetails extends FinalActivity {

	@ViewInject(id = R.id.radioGroup_1)
	RadioGroup _radioGroup_1;
	@ViewInject(id = R.id.radioGroup_2)
	RadioGroup _radioGroup_2;
	@ViewInject(id = R.id.radioGroup_3)
	RadioGroup _radioGroup_3;
	@ViewInject(id = R.id.radioGroup_4)
	RadioGroup _radioGroup_4;
	@ViewInject(id = R.id.radioGroup_5)
	RadioGroup _radioGroup_5;
	@ViewInject(id = R.id.radioGroup_6)
	RadioGroup _radioGroup_6;
	@ViewInject(id = R.id.radioGroup_7)
	RadioGroup _radioGroup_7;
	@ViewInject(id = R.id.ratiotext)
	EditText et_hdl;
	@ViewInject(id = R.id.bloodtext)
	EditText et_hq;

	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private RadioButton radioButton4;
	private RadioButton radioButton5;
	private RadioButton radioButton6;
	private RadioButton radioButton7;

	private double _score = 0;

	@ViewInject(id = R.id.healthdetailsNextBtn, click = "btnNextClick")
	ImageButton nextButton;
	@ViewInject(id = R.id.healthdetailsBackBtn, click = "btnBackClick")
	ImageButton backButton;

	public Vector<RadioGroup> myRadioGroups = new Vector<RadioGroup>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ�����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_healthdetails);
		init();
	}

	public void init() {
		if (myRadioGroups.size() == 0) {

			myRadioGroups.add(_radioGroup_1);
			myRadioGroups.add(_radioGroup_2);
			myRadioGroups.add(_radioGroup_3);
			myRadioGroups.add(_radioGroup_4);
			myRadioGroups.add(_radioGroup_5);
			myRadioGroups.add(_radioGroup_6);
			myRadioGroups.add(_radioGroup_7);
		}

		nextButton
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		backButton
				.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);

	}

	public void btnNextClick(View v) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				/* Save data in Shared Preference */
				SharedPreferences prefs = getSharedPreferences("bupa_prefs",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				if (!"".equals(et_hdl.getText().toString().trim())) {
					editor.putString("hdl", et_hdl.getText().toString());
				} else {
					editor.putString("hdl", "5.0");
				}

				if (!"".equals(et_hq.getText().toString().trim())) {
					editor.putString("bp", et_hq.getText().toString());
				} else {
					editor.putString("bp", "125.0");
				}

				int selectedId = _radioGroup_1.getCheckedRadioButtonId();
				radioButton1 = (RadioButton) findViewById(selectedId);

				if ((radioButton1.getText().equals("YES"))) {
					editor.putBoolean("smoker", true);
				} else if ((radioButton1.getText().equals("NO"))) {
					editor.putBoolean("smoker", false);
				}

				selectedId = _radioGroup_2.getCheckedRadioButtonId();
				radioButton2 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton2.getText()));
				if ((radioButton2.getText().equals("YES"))) {
					editor.putBoolean("diabetic", true);
				} else if ((radioButton2.getText().equals("NO"))) {
					editor.putBoolean("diabetic", false);
				}

				selectedId = _radioGroup_3.getCheckedRadioButtonId();
				radioButton3 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton3.getText()));
				if ((radioButton3.getText().equals("YES"))) {
					editor.putBoolean("bpt", true);
				} else if ((radioButton3.getText().equals("NO"))) {
					editor.putBoolean("bpt", false);
				}

				selectedId = _radioGroup_4.getCheckedRadioButtonId();
				radioButton4 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton4.getText()));
				if ((radioButton4.getText().equals("YES"))) {
					editor.putBoolean("ra", true);
				} else if ((radioButton4.getText().equals("NO"))) {
					editor.putBoolean("ra", false);
				}

				selectedId = _radioGroup_5.getCheckedRadioButtonId();
				radioButton5 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton5.getText()));
				if ((radioButton5.getText().equals("YES"))) {
					editor.putBoolean("kidney", true);
				} else if ((radioButton5.getText().equals("NO"))) {
					editor.putBoolean("kidney", false);
				}

				selectedId = _radioGroup_6.getCheckedRadioButtonId();
				radioButton6 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton6.getText()));
				if ((radioButton6.getText().equals("YES"))) {
					editor.putBoolean("angina", true);
				} else if ((radioButton6.getText().equals("NO"))) {
					editor.putBoolean("angina", false);
				}
				editor.commit();

				selectedId = _radioGroup_7.getCheckedRadioButtonId();
				radioButton7 = (RadioButton) findViewById(selectedId);
				// Log.i("HealthQ", "Value " + (radioButton7.getText()));
				if ((radioButton7.getText().equals("NO"))) {
					if (!"".equals(et_hdl.getText().toString().trim())) {
						int hdl = Integer.parseInt(et_hdl.getText().toString());

						if (hdl > 12 || hdl < 1) {
							Toast.makeText(
									HealthDetails.this,
									"Choiesterol/HDL ratio must be between 1 to 12 ",
									Toast.LENGTH_LONG).show();
						} else {

							if (!"".equals(et_hq.getText().toString().trim())) {
								int hq = Integer.parseInt(et_hq.getText()
										.toString());
								if (hq > 210 || hq < 70) {
									Toast.makeText(
											HealthDetails.this,
											"Systolic blood pressure must be between 70 to 210 ",
											Toast.LENGTH_LONG).show();
								} else {
									Intent it = new Intent(HealthDetails.this,
											AFTest.class);
									startActivity(it);
									overridePendingTransition(
											R.anim.in_from_left,
											R.anim.out_stagnant);
								}
							} else {
								Intent it = new Intent(HealthDetails.this,
										AFTest.class);
								startActivity(it);
								overridePendingTransition(R.anim.in_from_left,
										R.anim.out_stagnant);
							}

						}
					} else if (!"".equals(et_hq.getText().toString().trim())) {
						int hq = Integer.parseInt(et_hq.getText().toString());
						if (hq > 210 || hq < 70) {
							Toast.makeText(
									HealthDetails.this,
									"Systolic blood pressure must be between 70 to 210 ",
									Toast.LENGTH_LONG).show();
						} else {
							Intent it = new Intent(HealthDetails.this,
									AFTest.class);
							startActivity(it);
							overridePendingTransition(R.anim.in_from_left,
									R.anim.out_stagnant);
						}
					} else {
						Intent it = new Intent(HealthDetails.this, AFTest.class);
						startActivity(it);
						overridePendingTransition(R.anim.in_from_left,
								R.anim.out_stagnant);
					}

				} else if ((radioButton7.getText().equals("YES"))) {
					// editor.putBoolean("afhave", false);
					AfrCount count = new AfrCount(HealthDetails.this);
					_score = count.checkee(1);

					if (!"".equals(et_hdl.getText().toString().trim())) {
						int hdl = Integer.parseInt(et_hdl.getText().toString());

						if (hdl > 12 || hdl < 1) {
							Toast.makeText(
									HealthDetails.this,
									"Choiesterol/HDL ratio must be between 1 to 12 ",
									Toast.LENGTH_LONG).show();
						} else {

							if (!"".equals(et_hq.getText().toString().trim())) {
								int hq = Integer.parseInt(et_hq.getText()
										.toString());
								if (hq > 210 || hq < 70) {
									Toast.makeText(
											HealthDetails.this,
											"Systolic blood pressure must be between 70 to 210 ",
											Toast.LENGTH_LONG).show();
								} else {
									Intent intent = new Intent(
											HealthDetails.this, AfrResult.class);
									intent.putExtra("score", _score);
									startActivity(intent);
									overridePendingTransition(
											R.anim.in_from_left,
											R.anim.out_stagnant);
								}
							} else {
								Intent intent = new Intent(HealthDetails.this,
										AfrResult.class);
								intent.putExtra("score", _score);
								startActivity(intent);
								overridePendingTransition(R.anim.in_from_left,
										R.anim.out_stagnant);
							}

						}
					} else if (!"".equals(et_hq.getText().toString().trim())) {
						int hq = Integer.parseInt(et_hq.getText().toString());
						if (hq > 210 || hq < 70) {
							Toast.makeText(
									HealthDetails.this,
									"Systolic blood pressure must be between 70 to 210 ",
									Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(HealthDetails.this,
									AfrResult.class);
							intent.putExtra("score", _score);
							startActivity(intent);
							overridePendingTransition(R.anim.in_from_left,
									R.anim.out_stagnant);
						}
					} else {
						Intent intent = new Intent(HealthDetails.this,
								AfrResult.class);
						intent.putExtra("score", _score);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_left,
								R.anim.out_stagnant);
					}

				}

			}
		});
	}

	public void btnBackClick(View v) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// Intent it = new Intent(HealthDetails.this,
				// BasicDetails.class);
				// startActivity(it);
				finish();
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_stagnant);
			}
		});
	}

}
