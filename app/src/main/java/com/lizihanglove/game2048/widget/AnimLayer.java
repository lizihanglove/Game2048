package com.lizihanglove.game2048.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.lizihanglove.game2048.util.Constant;

public class AnimLayer extends FrameLayout {

	public AnimLayer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayer();
	}

	public AnimLayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayer();
	}

	public AnimLayer(Context context) {
		super(context);
		initLayer();
	}
	
	private void initLayer(){
	}
	
	public void createMoveAnim(final CardView from, final CardView to, int fromX, int toX, int fromY, int toY){
		
		final CardView c = getCard(from.getNum());
		
		LayoutParams lp = new LayoutParams(Constant.CARD_WIDTH, Constant.CARD_WIDTH);
		lp.leftMargin = fromX* Constant.CARD_WIDTH;
		lp.topMargin = fromY* Constant.CARD_WIDTH;
		c.setLayoutParams(lp);
		
		if (to.getNum()<=0) {
			to.getLabel().setVisibility(View.INVISIBLE);
		}
		TranslateAnimation ta = new TranslateAnimation(0, Constant.CARD_WIDTH*(toX-fromX), 0, Constant.CARD_WIDTH*(toY-fromY));
		ta.setDuration(100);
		ta.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				to.getLabel().setVisibility(View.VISIBLE);
				recycleCard(c);
			}
		});
		c.startAnimation(ta);
	}
	
	private CardView getCard(int num){
		CardView c;
		if (cardViews.size()>0) {
			c = cardViews.remove(0);
		}else{
			c = new CardView(getContext());
			addView(c);
		}
		c.setVisibility(View.VISIBLE);
		c.setNum(num);
		return c;
	}
	private void recycleCard(CardView c){
		c.setVisibility(View.INVISIBLE);
		c.setAnimation(null);
		cardViews.add(c);
	}
	private List<CardView> cardViews = new ArrayList<CardView>();
	
	public void createScaleTo1(CardView target){
		ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(100);
		target.setAnimation(null);
		target.getLabel().startAnimation(sa);
	}
	
}
