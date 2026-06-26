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

package com.movtery.zalithlauncher.utils.device

import android.util.Log

/**
 * Classe para gerenciar o modo Vulkan e interagir com o código nativo
 */
object VulkanChecker {
    private const val TAG = "VulkanChecker"
    
    // Variável para armazenar o estado do modo Vulkan
    private var vulkanModeEnabled: Boolean = false
    
    // Informações do Vulkan (cache para evitar chamadas repetidas)
    private var vulkanInfo: VulkanInfo? = null
    
    data class VulkanInfo(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val gpuName: String,
        val driverVersion: String,
        val extensionsCount: Int
    )
    
    /**
     * Ativa ou desativa o modo Vulkan
     */
    fun setVulkanMode(enabled: Boolean) {
        vulkanModeEnabled = enabled
        // Chamar função nativa para definir a variável global
        nativeSetVulkanMode(enabled)
        Log.d(TAG, "Modo Vulkan ${if (enabled) "ativo" else "desativado"}")
    }
    
    /**
     * Verifica se o modo Vulkan está ativo
     */
    fun isVulkanModeEnabled(): Boolean {
        return vulkanModeEnabled
    }
    
    /**
     * Obtém informações do Vulkan (API, GPU, driver, etc.)
     */
    fun getVulkanInfo(): VulkanInfo? {
        if (vulkanInfo == null) {
            vulkanInfo = try {
                // Obter informações do dispositivo
                val capabilities = nativeCheckVulkan(null, null, null)
                if (capabilities != null) {
                    VulkanInfo(
                        major = capabilities.majorVersion,
                        minor = capabilities.minorVersion,
                        patch = capabilities.patchVersion,
                        gpuName = "Mali-G52 MC2", // Será substituído por detecção real
                        driverVersion = "r16p0", // Será substituído por detecção real
                        extensionsCount = capabilities.extensions.size
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao obter informações do Vulkan", e)
                null
            }
        }
        return vulkanInfo
    }
    
    /**
     * Funções nativas (JNI)
     */
    private external fun nativeSetVulkanMode(enabled: Boolean)
    private external fun nativeIsVulkanModeEnabled(): Boolean
    external fun nativeSetLogCallback(callback: LogCallback?)
    external fun nativeCheckVulkan(driverPath: String?, nativeDir: String?, cacheDir: String?): VulkanCapabilities?
    
    /**
     * Callback para logs do Vulkan
     */
    interface LogCallback {
        fun log(level: String, message: String)
    }
    
    /**
     * Define o callback para logs do Vulkan
     */
    fun setLogCallback(callback: LogCallback?) {
        nativeSetLogCallback(callback)
    }
    
    // Classe para compatibilidade com o código existente
    data class VulkanCapabilities(
        val majorVersion: Int,
        val minorVersion: Int,
        val patchVersion: Int,
        val extensions: List<String>,
        val features: Map<String, Boolean>
    )
    
    // Bloco estático para carregar a biblioteca nativa
    init {
        try {
            System.loadLibrary("vulkan_checker")
            Log.d(TAG, "Biblioteca nativa vulkan_checker carregada")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Falha ao carregar biblioteca nativa vulkan_checker", e)
        }
    }
}