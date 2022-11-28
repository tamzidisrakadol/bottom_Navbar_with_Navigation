package com.example.bottomnavbarwithnavgation.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bottomnavbarwithnavgation.R;
import com.example.bottomnavbarwithnavgation.databinding.FragmentProfileBinding;
import com.example.bottomnavbarwithnavgation.model.CategoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Profile extends Fragment {

    FragmentProfileBinding fragmentProfileBinding;
    public static final int PDF_PICKUP_CODE = 1000;
    Uri pdfUrl;
    List<CategoryModel> categoryModelList;
    ProgressDialog progressDialog;
    String bookName,bookDesc,category ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryModelList = new ArrayList<>();
        loadCategories();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        fragmentProfileBinding.pdfImgBtn.setOnClickListener(v -> pickUPPdf());
        fragmentProfileBinding.bookCategoryTV.setOnClickListener(v -> {categoryDialouge();});
        fragmentProfileBinding.addBookBtn.setOnClickListener(v -> {
            validData();
        });
    }

    private void validData() {
         bookName = fragmentProfileBinding.bookNameEditText.getText().toString();
         bookDesc = fragmentProfileBinding.bookDescEditText.getText().toString();
         category = fragmentProfileBinding.bookCategoryTV.getText().toString();

        if (TextUtils.isEmpty(bookName)){
            fragmentProfileBinding.bookNameEditText.requestFocus();
            Toast.makeText(getContext(), "Enter Book Name", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(bookDesc)){
            fragmentProfileBinding.bookDescEditText.requestFocus();
            Toast.makeText(getContext(), "Enter Book dessscription", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(category)){
            fragmentProfileBinding.bookCategoryTV.requestFocus();
            Toast.makeText(getContext(), "select Category", Toast.LENGTH_SHORT).show();
        }
        else if(pdfUrl==null){
            Toast.makeText(getContext(), "pick Up Pdf", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadPdfToStorage();
        }


    }

    private void uploadPdfToStorage() {
        progressDialog.setMessage("uploading......");
        progressDialog.show();

        long timeStamp = System.currentTimeMillis();
        String fileNamePath = "Books/"+timeStamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileNamePath);
        storageReference.putFile(pdfUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()){
                            String uploadPDFUrl = ""+uriTask.getResult();
                            uploadpdfTODB(uploadPDFUrl,timeStamp);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadpdfTODB(String uploadpdfUrl,long timeStamp) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",timeStamp);
        map.put("title",bookName);
        map.put("description",bookDesc);
        map.put("category",category);
        map.put("url",uploadpdfUrl);
        map.put("timestamp",timeStamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(""+timeStamp)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void categoryDialouge() {
        String[] categoriesArray = new String[categoryModelList.size()];
        for (int i=0;i<categoryModelList.size();i++){
            categoriesArray[i] = categoryModelList.get(i).getCategory();
        }
        AlertDialog.Builder alertDialouge = new AlertDialog.Builder(getContext());
        alertDialouge.setTitle("PICK UP CATEGORY")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cat = categoriesArray[which];
                        fragmentProfileBinding.bookCategoryTV.setText(cat);
                    }
                }).show();

    }

    private void loadCategories(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CategoryModel categoryModel = dataSnapshot.getValue(CategoryModel.class);
                    categoryModelList.add(categoryModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pickUPPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf"),PDF_PICKUP_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK){
            if (requestCode==PDF_PICKUP_CODE){
                pdfUrl=data.getData();
            }
        }else {
            Toast.makeText(getContext(), "picking pdf canceled", Toast.LENGTH_SHORT).show();
        }
    }
}