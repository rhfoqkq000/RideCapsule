package com.example.horse.travel.sns.list;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.write.ImageSingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SnsImageRecyclerAdapter extends RecyclerView.Adapter<SnsImageRecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Uri> imgArr;
    private RequestManager glide;
    private PopupWindow mPopupWindow ;
    private ContentResolver contentResolver;
    private Context context;

    public SnsImageRecyclerAdapter(RequestManager glide, Context context, ContentResolver contentResolver) {
        this.glide = glide;
        this.context = context;
        this.contentResolver = contentResolver;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SnsImageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_select,viewGroup,false);
        return new SnsImageRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SnsImageRecyclerAdapter.ViewHolder holder, int position) {
        Log.e("SnsImageRecyclerAdapter", imgArr.get(position).toString());
        glide.load(imgArr.get(position)).into(holder.imageView);

//        glide.load(imgArr.get(position)).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                Bitmap b = Bitmap.createBitmap(drawableToBitmap(resource));
//                Uri savedUri = bitmapToUri(b, (new Random().nextInt(99999))+getFileName(imgArr.get(holder.getAdapterPosition())));
//                imgArr.set(holder.getAdapterPosition(), savedUri);
//                ImageSingleton.getInstance().setImgUri(imgArr);
//                Log.e("SnsImageRecyclerAdapter", ImageSingleton.getInstance().getImgUri().toString());
//                return false;
//            }
//        }).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = layoutInflater.inflate(R.layout.dialog_filterselect, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                Log.e("SnsImageSelectAdapter", "position:"+holder.getAdapterPosition());

                ImageView filterImg1 = popupView.findViewById(R.id.filter1);
                ImageView filterImg2 = popupView.findViewById(R.id.filter2);
                ImageView filterImg3 = popupView.findViewById(R.id.filter3);
                ImageView filterImg4 = popupView.findViewById(R.id.filter4);
                ImageView filterImg5 = popupView.findViewById(R.id.filter5);
                ImageView filterImg6 = popupView.findViewById(R.id.filter6);
                ImageView filterImg7 = popupView.findViewById(R.id.filter7);
                ImageView filterImg8 = popupView.findViewById(R.id.filter8);
                ImageView filterImg9 = popupView.findViewById(R.id.filter9);

                settingImages(filterImg1, imgArr, holder.getAdapterPosition(), bitmapTransform(new ToonFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg2, imgArr, holder.getAdapterPosition(), bitmapTransform(new SepiaFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg3, imgArr, holder.getAdapterPosition(), bitmapTransform(new SketchFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg4, imgArr, holder.getAdapterPosition(), bitmapTransform(new BlurTransformation(3)), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg5, imgArr, holder.getAdapterPosition(), bitmapTransform(new PixelationFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg6, imgArr, holder.getAdapterPosition(), bitmapTransform(new KuwaharaFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg7, imgArr, holder.getAdapterPosition(), bitmapTransform(new SwirlFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg8, imgArr, holder.getAdapterPosition(), bitmapTransform(new VignetteFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
                settingImages(filterImg9, imgArr, holder.getAdapterPosition(), bitmapTransform(new InvertFilterTransformation()), getFileName(imgArr.get(holder.getAdapterPosition())), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(imgArr != null){
            return imgArr.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item_select);
//            imageView.setDrawingCacheEnabled(true);
//            imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
    }

    public void addNew(ArrayList<Uri> imgArr)
    {
        this.imgArr = imgArr;
        notifyDataSetChanged();
    }

    private void settingImages(final ImageView filterImgView, final ArrayList<Uri> path, final int i, final RequestOptions option, final String fileName, final SnsImageRecyclerAdapter.ViewHolder holder){
        filterImgView.setDrawingCacheEnabled(true);
        glide.load(path.get(i))
                .apply(option)
                .into(filterImgView);
        filterImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ORIGINAL", path.toString()+",,,,, i:::"+i);
                glide.load(path.get(i))
                        .apply(option)
                        .into(holder.imageView);
                Bitmap filter1Bitmap = filterImgView.getDrawingCache();
                ArrayList<Uri> tempUriArr = ImageSingleton.getInstance().getImgUri();
                tempUriArr.set(i, bitmapToUri(filter1Bitmap, fileName));
                ImageSingleton.getInstance().setImgUri(tempUriArr);
                Log.e("TEMP", ImageSingleton.getInstance().getImgUri().toString());
                mPopupWindow.dismiss();
            }
        });
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private Uri bitmapToUri(Bitmap originalBitmap, String fileName) {
        try {
            File f = new File(context.getCacheDir(), fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Bitmap resized = Bitmap.createScaledBitmap(originalBitmap,
                    (int)(originalBitmap.getWidth()*0.8), (int)(originalBitmap.getHeight()*0.8), true);
            resized.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return Uri.fromFile(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void saveFile(Uri sourceUri, File destination){
        try {
            File source = new File(sourceUri.getPath());
            FileChannel src = new FileInputStream(source).getChannel();
            FileChannel dst = new FileOutputStream(destination).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}