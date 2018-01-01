package com.thecoolguy.rumaan.fileio.data.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UploadService {
    @Multipart
    @POST("file")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);
}
