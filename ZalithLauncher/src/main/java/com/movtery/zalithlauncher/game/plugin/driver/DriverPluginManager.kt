/*
 * Zalith Launcher 2
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.movtery.zalithlauncher.game.plugin.driver

import android.content.Context
import android.content.pm.ApplicationInfo
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.game.plugin.ApkPlugin
import com.movtery.zalithlauncher.game.plugin.ApkPluginManager
import com.movtery.zalithlauncher.game.plugin.cacheAppIcon
import com.movtery.zalithlauncher.setting.AllSettings
import com.movtery.zalithlauncher.utils.device.VulkanChecker

/**
 * FCL 驱动器插件
 * [FCL DriverPlugin.kt](https://github.com/FCL-Team/FoldCraftLauncher/blob/main/FCLauncher/src/main/java/com/tungsten/fclauncher/plugins/DriverPlugin.kt)
 */
object DriverPluginManager: ApkPluginManager() {
    private val driverList: MutableList<Driver> = mutableListOf()

    @JvmStatic
    fun getDriverList(): List<Driver> = driverList.toList()

    private lateinit var currentDriver: Driver

    @JvmStatic
    fun setDriverById(driverId: String) {
        currentDriver = driverList.find { it.id == driverId } ?: driverList[0]
    }

    @JvmStatic
    fun getDriver(): Driver = currentDriver

    /**
     * Verifica se o modo Vulkan está ativo
     */
    @JvmStatic
    fun isVulkanModeEnabled(): Boolean {
        return VulkanChecker.isVulkanModeEnabled()
    }

    /**
     * Inicializa o driver, mas não configura variáveis EGL se o modo Vulkan estiver ativo
     */
    fun initDriver(context: Context) {
        driverList.clear()
        val applicationInfo = context.applicationInfo
        driverList.add(
            Driver(
                id = AllSettings.vulkanDriver.defaultValue,
                appName = "",
                appVersion = "",
                name = "Turnip",
                path = applicationInfo.nativeLibraryDir,
                isLauncher = true
            )
        )
        setDriverById(AllSettings.vulkanDriver.getValue())
    }

    /**
     * Configura o ambiente para o driver, mas pulsa variáveis EGL no modo Vulkan
     */
    fun setupDriverEnvironment() {
        if (isVulkanModeEnabled()) {
            // No modo Vulkan, não setar POJAVEXEC_EGL ou LIBGL_EGL
            android.util.Log.d("DriverPluginManager", "Modo Vulkan ativo: EGL desativado")
            return
        }
        // Resto do código original para drivers GL
    }

    /**
     * 通用 FCL 插件
     */
    override fun parseApkPlugin(
        context: Context,
        info: ApplicationInfo,
        loaded: (ApkPlugin) -> Unit
    ) {
        if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            val metaData = info.metaData ?: return
            if (metaData.getBoolean("fclPlugin", false)) {
                val driver = metaData.getString("driver") ?: return
                val nativeLibraryDir = info.nativeLibraryDir

                val packageManager = context.packageManager
                val packageName = info.packageName
                val appName = info.loadLabel(packageManager).toString()
                val appVersion = packageManager.getPackageInfo(packageName, 0).versionName ?: ""

                val plugin = Driver(
                    id = packageName,
                    appName = appName,
                    appVersion = appVersion,
                    name = driver,
                    summary = context.getString(R.string.settings_renderer_from_plugins, appName),
                    path = nativeLibraryDir,
                    isLauncher = false
                )

                driverList.add(plugin)

                runCatching {
                    cacheAppIcon(context, info)
                    loaded(plugin)
                }
            }
        }
    }
}