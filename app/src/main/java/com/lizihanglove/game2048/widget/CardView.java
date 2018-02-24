package com.lizihanglove.game2048.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lizihanglove.game2048.R;

public class CardView extends FrameLayout {

	public CardView(Context context) {
		super(context);

		LayoutParams lp = null;

		background = new View(getContext());
		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);

		Drawable drawble_init = getContext().getResources().getDrawable(R.drawable.shape_init);
		background.setBackgroundDrawable(drawble_init);

		addView(background, lp);

		label = new TextView(getContext());
		label.setTextSize(28);
		label.setGravity(Gravity.CENTER);


		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);

		setNum(0);
	}


	private int num = 0;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;

		if (num<=0) {
			label.setText("");
		}else{
			if (num >= 1024) {
				label.setTextSize(16);
			}
			label.setText(num+"");
		}

		switch (num) {
		case 0:
			Drawable drawble_0 = getContext().getResources().getDrawable(R.drawable.shape_0);
			label.setBackgroundDrawable(drawble_0);
			break;
		case 2:
			Drawable drawble_2 = getContext().getResources().getDrawable(R.drawable.shape_2);
			label.setBackgroundDrawable(drawble_2);
			break;
		case 4:
			Drawable drawble_4 = getContext().getResources().getDrawable(R.drawable.shape_4);
			label.setBackgroundDrawable(drawble_4);
			break;
		case 8:
			Drawable drawble_8 = getContext().getResources().getDrawable(R.drawable.shape_8);
			label.setBackgroundDrawable(drawble_8);
			break;
		case 16:
			Drawable drawble_16 = getContext().getResources().getDrawable(R.drawable.shape_16);
			label.setBackgroundDrawable(drawble_16);
			break;
		case 32:
			Drawable drawble_32 = getContext().getResources().getDrawable(R.drawable.shape_32);
			label.setBackgroundDrawable(drawble_32);
			break;
		case 64:
			Drawable drawble_64 = getContext().getResources().getDrawable(R.drawable.shape_64);
			label.setBackgroundDrawable(drawble_64);
			break;
		case 128:
			Drawable drawble_128 = getContext().getResources().getDrawable(R.drawable.shape_128);
			label.setBackgroundDrawable(drawble_128);
			break;
		case 256:
			Drawable drawble_256 = getContext().getResources().getDrawable(R.drawable.shape_256);
			label.setBackgroundDrawable(drawble_256);
			break;
		case 512:

			Drawable drawble_512 = getContext().getResources().getDrawable(R.drawable.shape_512);
			label.setBackgroundDrawable(drawble_512);
			break;
		case 1024:
			Drawable drawble_1024 = getContext().getResources().getDrawable(R.drawable.shape_1024);
			label.setBackgroundDrawable(drawble_1024);
			break;
		case 2048:
			Drawable drawble_2048 = getContext().getResources().getDrawable(R.drawable.shape_2048);
			label.setBackgroundDrawable(drawble_2048);
			break;
		default:
			Drawable drawble_default = getContext().getResources().getDrawable(R.drawable.shape_default);
			label.setBackgroundDrawable(drawble_default);
			break;
		}
	}

	public boolean equals(CardView o) {
		return getNum()==o.getNum();
	}
	
	protected CardView clone(){
		CardView c= new CardView(getContext());
		c.setNum(getNum());
		return c;
	}

	public TextView getLabel() {
		return label;
	}
	
	private TextView label;
	private View background;
}
