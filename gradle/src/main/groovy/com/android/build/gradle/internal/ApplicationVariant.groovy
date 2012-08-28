/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.build.gradle.internal

import com.android.build.gradle.AndroidBasePlugin
import com.android.builder.AndroidBuilder
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.Compile
import com.android.builder.VariantConfiguration

/**
 * Represents something that can be packaged into an APK and installed.
 */
public interface ApplicationVariant {
    String getName()

    String getDescription()

    String getDirName()

    String getBaseName()

    VariantConfiguration getConfig()

    boolean getZipAlign()

    boolean isSigned()

    boolean getRunProguard()

    FileCollection getRuntimeClasspath()

    FileCollection getResourcePackage()

    Compile getCompileTask()

    List<String> getRunCommand()

    String getPackage()

    AndroidBuilder createBuilder(AndroidBasePlugin androidBasePlugin)
}
