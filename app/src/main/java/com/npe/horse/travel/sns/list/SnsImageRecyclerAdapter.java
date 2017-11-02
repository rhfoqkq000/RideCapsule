package com.npe.horse.travel.sns.list;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.npe.horse.travel.R;
import com.npe.horse.travel.UrlSingleton;
import com.npe.horse.travel.sns.write.ImageSingleton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class SnsImageRecyclerAdapter extends RecyclerView.Adapter<SnsImageRecyclerAdapter.ViewHolder> {

//    private LayoutInflater layoutInflater;
    private String[] imgArr;
    private RequestManager glide;
    private PopupWindow mPopupWindow ;
    private ContentResolver contentResolver;
    private Context context;
    private final String IMG_URL = UrlSingleton.getInstance().getSERVER_URL();

    public SnsImageRecyclerAdapter(RequestManager glide, Context context, ContentResolver contentResolver, String[] imgArr) {
        this.glide = glide;
        this.context = context;
        this.imgArr = imgArr;
        this.contentResolver = contentResolver;
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SnsImageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sns_image_recycler,viewGroup,false);
        return new SnsImageRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SnsImageRecyclerAdapter.ViewHolder holder, int position) {
//        Log.e("SnsImageRecyclerAdapter", imgArr.get(position).toString());
//        glide.load(IMG_URL+imgArr.get(position)).into(holder.imageView);
        holder.itemView.layout(0, 0, 0, 0);
        Picasso.with(holder.itemView.getContext())
                .load(IMG_URL+imgArr[position])
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(imgArr != null){
            return imgArr.length;
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sns_image);
//            imageView.setDrawingCacheEnabled(true);
//            imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
    }

//    public void addNew(ArrayList<Uri> imgArr)
//    {
//        this.imgArr = imgArr;
//        notifyDataSetChanged();
//    }

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