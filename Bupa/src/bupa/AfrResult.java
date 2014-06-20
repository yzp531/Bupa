package bupa;

import java.text.DateFormat;
import java.util.Date;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import bupa.eff.EffBottonBase;

import com.bupa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AfrResult extends FinalActivity {
	@ViewInject(id = R.id.timeview)
	TextView timeview;
	@ViewInject(id = R.id.resultview)
	TextView resultview;
	@ViewInject(id = R.id.openapp)
	ImageButton openapp;
	@ViewInject(id = R.id.download)
	ImageButton download;
	@ViewInject(id = R.id.riskview)
	ImageView riskview;
	@ViewInject(id = R.id.lastadvice)
	LinearLayout lastadvice;
	@ViewInject(id = R.id.lastkeep)
	LinearLayout lastkeep;
	@ViewInject(id = R.id.lastbotton)
	ImageButton lastbotton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.afr_result);
		Intent intent = getIntent();
		Double score = intent.getExtras().getDouble("score");
		System.out.println("---------------------------" + score);
		resultview.setText(Math.round(score * 10) / 10.0 + "%");

		if (score < 30) {
			riskview.setBackgroundResource(R.drawable.lowrisksmlalert);
		} else if (score >= 30 && score < 60) {
			riskview.setBackgroundResource(R.drawable.medrisksmlalert);
		} else {
			riskview.setBackgroundResource(R.drawable.highrisksmlalert);
			lastkeep.setVisibility(View.GONE);
			lastadvice.setVisibility(View.VISIBLE);
			lastbotton
					.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		}                                                               
		Date date = new Date();
		DateFormat dateFormatter = DateFormat
				.getDateInstance(DateFormat.DEFAULT);
		// SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = dateFormatter.format(date);
		timeview.setText(formattedDate);
		openapp.setOnClickListener(null);
		download.setOnClickListener(null);
		openapp.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
		download.setOnTouchListener(EffBottonBase.buttonOnAlphaChangeListener);
	}
}
