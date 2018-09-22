package com.kw.top.utils.htmltext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kw.top.tools.GlideTools;

public class MImageGetter implements ImageGetter {
    Context c;
    Drawable drawable = null;
    TextView container;

    public MImageGetter(TextView text, Context c) {
        this.c = c;
        this.container = text;
    }

    final UrlDrawable urlDrawable = new UrlDrawable();

    @Override
    public Drawable getDrawable(String source) {
        Glide.with(c).asBitmap().load(source).apply(GlideTools.getOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap loadedImage, @Nullable Transition<? super Bitmap> transition) {
                        ViewGroup.LayoutParams params = container.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        container.setLayoutParams(params);

                        float scaleWidth = ((float) container.getMeasuredWidth())/loadedImage.getWidth();

                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth,scaleWidth);

                        loadedImage = Bitmap.createBitmap(loadedImage,0,0,loadedImage.getWidth(),loadedImage.getHeight(),matrix,true);
                        urlDrawable.bitmap = loadedImage;
                        urlDrawable.setBounds(0,0,loadedImage.getWidth(),loadedImage.getHeight());
                        container.invalidate();
                        container.setText(container.getText());
                    }
                });
        return urlDrawable;
    }

//    @Override
//    public Drawable getDrawable(String source) {
////		    InputStream is = null;
////			try {
////				is = c.getResources().getAssets().open(source);
////			} catch (IOException e1) {
////				e1.printStackTrace();
////			}
////            try {
////                TypedValue typedValue = new TypedValue();
////                typedValue.density = TypedValue.DENSITY_DEFAULT;
////                drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");
////                DisplayMetrics dm = c.getResources().getDisplayMetrics();
////        		int dwidth = dm.widthPixels-10;//padding left + padding right
////        		float dheight = (float)drawable.getIntrinsicHeight()*(float)dwidth/(float)drawable.getIntrinsicWidth();
////        		int dh = (int)(dheight+0.5);
////        		int wid = dwidth;
////                int hei = dh;
////                /*int wid = drawable.getIntrinsicWidth() > dwidth? dwidth: drawable.getIntrinsicWidth();
////                int hei = drawable.getIntrinsicHeight() > dh ? dh: drawable.getIntrinsicHeight();*/
////                drawable.setBounds(0, 0, wid, hei);
////
////                return drawable;
////            } catch (Exception e) {
////            	System.out.println(e);
////                return null;
////            }
//        final UrlDrawable urlDrawable = new UrlDrawable();
//        Glide.with(c).load(source).apply(GlideTools.getOptions()).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                Log.e("tag", "==============  drawable");
//                drawable = resource;
//                TypedValue typedValue = new TypedValue();
//                typedValue.density = TypedValue.DENSITY_DEFAULT;
//                DisplayMetrics dm = c.getResources().getDisplayMetrics();
//                int dwidth = dm.widthPixels - 10;//padding left + padding right
//                float dheight = (float) drawable.getIntrinsicHeight() * (float) dwidth / (float) drawable.getIntrinsicWidth();
//                int dh = (int) (dheight + 0.5);
//                int wid = dwidth;
//                int hei = dh;
//                /*int wid = drawable.getIntrinsicWidth() > dwidth? dwidth: drawable.getIntrinsicWidth();
//                int hei = drawable.getIntrinsicHeight() > dh ? dh: drawable.getIntrinsicHeight();*/
//                drawable.setBounds(0, 0, wid, hei);
//                Log.e("tag", "===============   wid " + wid + "  hei " + hei);
//                mTextView.invalidate();
//                mTextView.setText(mTextView.getText());
//            }
//        });
//
//        return drawable;
//    }

    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }

}
