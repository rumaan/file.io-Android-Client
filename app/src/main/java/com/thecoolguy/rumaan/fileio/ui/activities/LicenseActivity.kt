package com.thecoolguy.rumaan.fileio.ui.activities

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense
import com.thecoolguy.rumaan.fileio.R

class LicenseActivity : MaterialAboutActivity() {

    override fun getActivityTitle(): CharSequence? = "Open Source Licenses"

    override fun getMaterialAboutList(context: Context): MaterialAboutList {
        val icon = getDrawable(context, R.drawable.ic_library)
        val apache = OpenSourceLicense.APACHE_2
        val mit = OpenSourceLicense.MIT

        val aospCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "AOSP Support Libraries", "2011", "The Android Open Source Project", apache)

        val archCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "Architecture Components", "2017", "The Android Open Source Project", apache)

        val malCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "MaterialAboutLibrary", "2016", "Daniel Stone", apache)

        val fuelCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "Fuel", "2018", "Kittinun Vantasin", mit)

        val npbCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "NumberProgressBar", "2014", "daimajia", mit)

        val permissionCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "Permission Dispatcher", "2016", "Shintaro Katafuchi, Marcel Schnelle, Yoshinori Isogai", apache)

        val crashCard = ConvenienceBuilder
                .createLicenseCard(context, icon, "CustomActivityOnCrash", "2015", "Eduard Ereza Martínez", apache)

        return MaterialAboutList(aospCard, archCard, malCard, fuelCard,
                npbCard, permissionCard, crashCard)
    }
}
