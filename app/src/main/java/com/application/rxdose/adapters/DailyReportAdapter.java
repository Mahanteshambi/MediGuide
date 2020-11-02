package com.application.rxdose.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.R;
import com.application.rxdose.model.DailyMedicineModel;
import com.application.rxdose.model.TabletModel;

import java.util.List;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.MyViewHolder> {
    private List<DailyMedicineModel> dailyMedicineModels;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        LinearLayout tabletsIconContainer;

        MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.dayname);
            tabletsIconContainer = view.findViewById(R.id.tablets_icon_container);
        }
    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(mContext);
        int width = (int) mContext.getResources().getDimension(R.dimen.status_icon_width);
        int height = (int) mContext.getResources().getDimension(R.dimen.status_icon_height);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(width, height);
        vp.topMargin = (int) mContext.getResources().getDimension(R.dimen.status_icon_margin_top);
        vp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(vp);

        /*final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.rounded_imageview));
        } else {
            imageView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_imageview));
        }*/
        imageView.setImageBitmap(getRoundedCornerImage(
                drawableToBitmap(mContext.getResources().getDrawable(R.drawable.ic_launcher_background)),
                width));
        return imageView;
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public DailyReportAdapter(List<DailyMedicineModel> dailyMedicineModels) {
        this.dailyMedicineModels = dailyMedicineModels;
    }

    public DailyReportAdapter() {
    }

    public void setDailyMedicineModels(List<DailyMedicineModel> dailyMedicineModels) {
        this.dailyMedicineModels = dailyMedicineModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_status_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DailyMedicineModel dailyMedicineModel = dailyMedicineModels.get(position);
        holder.day.setText(dailyMedicineModel.getDay());
        int tabletsCount = dailyMedicineModel.getTabletTimeList().size();

        List<TabletModel> tabletModelList = dailyMedicineModel.getTabletModelList();
        for (int i = 0; i < tabletsCount; i++) {
            ImageView imageView = getImageView();
            if (i == tabletsCount - 1) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                lp.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.status_icon_margin_top);
                imageView.setLayoutParams(lp);
            }
            setImageView(tabletModelList.get(i).isCosumed(), imageView);
            holder.tabletsIconContainer.addView(imageView);
        }


//        setImageView(tabletModelList.get(0).isCosumed(), holder.tablet1ImgView);
//        setImageView(tabletModelList.get(1).isCosumed(), holder.tablet2ImgView);
//        setImageView(tabletModelList.get(2).isCosumed(), holder.tablet3ImgView);
    }

    private void setImageView(boolean isConsumed, ImageView imageView) {
        if (isConsumed) {
            imageView.setImageResource(R.drawable.consumed_icon);
        } else {
            imageView.setImageResource(R.drawable.uncheck);
        }
    }

    @Override
    public int getItemCount() {
        return dailyMedicineModels.size();
    }
}