package com.thecoolguy.rumaan.fileio

import androidx.test.InstrumentationRegistry
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.test.TestDriver
import androidx.work.test.WorkManagerTestInitHelper
import com.thecoolguy.rumaan.fileio.utils.createUploadWork
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
        val constrainedWork: OneTimeWorkRequest = createUploadWork("https://file.io/tEsT")
        WorkManager.getInstance().enqueue(constrainedWork)
        testDriver.setAllConstraintsMet(constrainedWork.id)

    }
}