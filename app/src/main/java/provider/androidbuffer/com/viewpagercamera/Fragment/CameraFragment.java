package provider.androidbuffer.com.viewpagercamera.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import provider.androidbuffer.com.viewpagercamera.R;

/**
 * Created by incred-dev on 30/8/18.
 */

public class CameraFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 1000;
    private static final int CAMERA_REQUEST = 100;

    ImageView ivImage;
    Button btnCamera;

    public static CameraFragment newInstance(){
        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ivImage = view.findViewById(R.id.ivCamera);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check permission and open camera here
                checkAndOpenCamera();
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                openCameraApp();
            } else {
                Toast.makeText(this.getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
            String path = file.getPath();
            Log.d("file path",path);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a loss less format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ivImage.setImageBitmap(bitmap);
        }
    }

    private void checkAndOpenCamera(){
        //Check the permission for camera
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            //if permission is not granted than request permission
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION_CODE);

        } else {
            //if permission is granted than open Camera
            openCameraApp();
        }
    }

    private void openCameraApp(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }
}
