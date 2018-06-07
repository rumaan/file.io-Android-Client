package com.thecoolguy.rumaan.fileio

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.test.TestDriver
import androidx.work.test.WorkManagerTestInitHelper
import com.thecoolguy.rumaan.fileio.network.createWorkRequest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UploadWorkTest {

    // TODO: complete Workmanager tests

    @Test
    fun test() {
        val context = InstrumentationRegistry.getTargetContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        val testDriver: TestDriver = WorkManagerTestInitHelper.getTestDriver()
        val constrainedWork: OneTimeWorkRequest = createWorkRequest("https://file.io/tEsT")
        WorkManager.getInstance().enqueue(constrainedWork)
        testDriver.setAllConstraintsMet(constrainedWork.id)

    }
}