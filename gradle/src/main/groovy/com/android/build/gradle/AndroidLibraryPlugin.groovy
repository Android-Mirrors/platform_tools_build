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
package com.android.build.gradle

import com.android.builder.BuildType
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.internal.BuildTypeData
import com.android.builder.VariantConfiguration
import com.android.build.gradle.internal.ProductionAppVariant
import com.android.build.gradle.internal.ProductFlavorData

class AndroidLibraryPlugin extends AndroidBasePlugin implements Plugin<Project> {

    AndroidLibraryExtension extension
    BuildTypeData debugBuildTypeData
    BuildTypeData releaseBuildTypeData

    @Override
    void apply(Project project) {
        super.apply(project)

        extension = project.extensions.create('android', AndroidLibraryExtension)
        setDefaultConfig(extension.defaultConfig)

        // create the source sets for the build type.
        // the ones for the main product flavors are handled by the base plugin.
        def debugSourceSet = project.sourceSets.add(BuildType.DEBUG)
        def releaseSourceSet = project.sourceSets.add(BuildType.RELEASE)

        debugBuildTypeData = new BuildTypeData(extension.debug, debugSourceSet, project)
        releaseBuildTypeData = new BuildTypeData(extension.release, releaseSourceSet, project)

        project.afterEvaluate {
            createAndroidTasks()
        }
    }

    void createAndroidTasks() {
        createLibraryTasks()
    }

    void createLibraryTasks() {
        ProductFlavorData defaultConfigData = getDefaultConfigData();

        def variantConfig = new VariantConfiguration(
                defaultConfigData.productFlavor, defaultConfigData.androidSourceSet,
                debugBuildTypeData.buildType, debugBuildTypeData.androidSourceSet,
                VariantConfiguration.Type.LIBRARY)

        ProductionAppVariant variant = new ProductionAppVariant(variantConfig)

        // Add a task to process the manifest(s)
        ProcessManifest processManifestTask = createProcessManifestTask(variant)

        // Add a task to crunch resource files
        def crunchTask = createCrunchResTask(variant)

        // Add a task to create the BuildConfig class
        def generateBuildConfigTask = createBuildConfigTask(variant, null)

        // Add a task to generate resource source files
        def processResources = createProcessResTask(variant, processManifestTask, crunchTask)

        // Add a compile task
        createCompileTask(variant, null/*testedVariant*/, processResources, generateBuildConfigTask)
    }

    @Override
    String getTarget() {
        return extension.target
    }
}
