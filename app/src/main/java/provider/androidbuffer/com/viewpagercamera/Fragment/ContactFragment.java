package provider.androidbuffer.com.viewpagercamera.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import provider.androidbuffer.com.viewpagercamera.Adapter.ContactAdapter;
import provider.androidbuffer.com.viewpagercamera.CallBack.OnCall;
import provider.androidbuffer.com.viewpagercamera.Model.ContactModel;
import provider.androidbuffer.com.viewpagercamera.R;

/**
 * Created by incred-dev on 30/8/18.
 */

public class ContactFragment extends Fragment implements OnCall{

    private static final int CONTACT_REQUEST_CODE = 1000;
    private static final int CALL_PERMISSION_CODE = 1002;
    RecyclerView rvContact;
    ContactAdapter contactAdapter;
    ContactTask contactTask;
    String number;
    AlertDialog dialog;

    public static ContactFragment newInstance(){
        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        rvContact = view.findViewById(R.id.rvContact);
        setProgressDialog();
        checkAndShowContacts();
        return view;
    }

    private void setProgressDialog(){
        dialog = new SpotsDialog(getContext(), R.style.Custom);
    }

    private void checkAndShowContacts(){
        dialog.show();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //If request is not granted than request permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_REQUEST_CODE);
        } else {
            //fetch and show the contacts
            fetchAndShowContacts();

        }
    }

    @Override
    public void onCallClicked(String number) {
        checkAndMakeCall(number);
    }

    private void checkAndMakeCall(String callNumber){
        this.number = callNumber;
        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_PERMISSION_CODE);
        } else {
            //make call here
            makeCall();
        }
    }

    private void makeCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //make call here
                makeCall();
            } else {
                Toast.makeText(this.getContext(),"Call permission denied",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CONTACT_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //fetch and show contact here
                fetchAndShowContacts();
            } else {
                Toast.makeText(this.getContext(),"Contact permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        contactTask.cancel(true);
        super.onDestroyView();
    }

    private void fetchAndShowContacts(){
        contactTask = new ContactTask();
        contactTask.execute();
    }

    private List<ContactModel> getContacts(){
        ContentResolver cr = this.getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        List<ContactModel> contactModelList = new ArrayList<>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ContactModel model = new ContactModel();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
                    Cursor phCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id},null);
                    if (phCursor != null && phCursor.moveToNext()){
                        String phone = phCursor.getString(phCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        model.setName(name);
                        model.setNumber(phone);
                        contactModelList.add(model);
                    } else {
                        model.setName(name);
                        model.setNumber("");
                        contactModelList.add(model);
                    }
                    phCursor.close();
                }
            }
        }
        cursor.close();
        return contactModelList;
    }

    private void showContacts(List<ContactModel> contactModelList){
        dialog.hide();
        //Initialize the contact adapter and set into recycler view
        contactAdapter = new ContactAdapter(contactModelList,this);
        rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContact.setHasFixedSize(true);
        rvContact.setAdapter(contactAdapter);
    }

    private class ContactTask extends AsyncTask<Void, Void, List<ContactModel>>{

        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            return getContacts();
        }

        @Override
        protected void onPostExecute(List<ContactModel> contactModels) {
            showContacts(contactModels);
        }
    }

}
