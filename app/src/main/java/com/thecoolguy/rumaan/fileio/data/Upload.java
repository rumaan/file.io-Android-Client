package com.thecoolguy.rumaan.fileio.data;

import com.github.kittinunf.fuel.core.FuelError;

/**
 * Created by rumaankhalander on 03/01/18.
 */

public interface Upload {
    void onUpload(String result);
    void progress(int progress);
    void onError(FuelError error);
}
