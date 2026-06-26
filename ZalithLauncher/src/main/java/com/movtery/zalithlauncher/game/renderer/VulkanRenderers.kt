/*
 * Configuração do Renderer Vulkan para o Modo Vulkan
 * 
 * Este arquivo define as opções de renderer para o Modo Vulkan,
 * incluindo o renderer nativo do sistema.
 */
package com.movtery.zalithlauncher.game.renderer

import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.game.plugin.driver.DriverPluginManager

/**
 * Classe que gerencia os renderers disponíveis para o Modo Vulkan
 */
object VulkanRenderers {
    
    /**
     * Lista de renderers Vulkan disponíveis
     */
    val vulkanRenderers: List<Renderer> by lazy {
        listOf(
            Renderer(
                id = "vulkan",
                name = "Vulkan API (Nativo)",
                summary = "Renderer Vulkan nativo do sistema (Mali-G52)",
                isVulkan = true,
                isAvailable = { DriverPluginManager.isVulkanModeEnabled() },
                getRendererLibrary = { null }, // Não precisa de lib externa
                getRendererEnv = { 
                    mapOf(
                        "POJAV_RENDERER" to "vulkan",
                        "glfwstub.initEgl" to "false"
                    )
                },
                getRendererEGL = { null } // Não usa EGL
            )
        )
    }
    
    /**
     * Verifica se o renderer Vulkan está selecionado
     */
    fun isVulkanRendererSelected(): Boolean {
        return Renderers.getCurrentRenderer().id == "vulkan"
    }
    
    /**
     * Define o renderer Vulkan como atual
     */
    fun setVulkanRenderer() {
        Renderers.setCurrentRendererById("vulkan")
    }
}
