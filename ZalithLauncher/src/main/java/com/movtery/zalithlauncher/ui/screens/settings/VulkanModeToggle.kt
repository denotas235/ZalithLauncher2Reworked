/*
 * Configuração da UI para o Modo Vulkan
 * 
 * Este arquivo define as opções de UI relacionadas ao Modo Vulkan,
 * incluindo o toggle para ativar/desativar o modo.
 */
package com.movtery.zalithlauncher.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.movtery.zalithlauncher.setting.AllSettings
import com.movtery.zalithlauncher.utils.device.VulkanChecker

/**
 * Composable para o toggle do Modo Vulkan
 */
@Composable
fun VulkanModeToggle() {
    val context = LocalContext.current
    val isVulkanModeEnabled = remember { 
        mutableStateOf(AllSettings.vulkanMode.getValue())
    }
    
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Modo Vulkan API")
        Switch(
            checked = isVulkanModeEnabled.value,
            onCheckedChange = { enabled ->
                isVulkanModeEnabled.value = enabled
                AllSettings.vulkanMode.save(enabled)
                VulkanChecker.setVulkanMode(enabled)
                // Notificar o usuário para reiniciar o launcher
                if (enabled) {
                    android.widget.Toast.makeText(
                        context,
                        "Modo Vulkan ativo. Reinicie o launcher para aplicar as mudanças.",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
        if (isVulkanModeEnabled.value) {
            Text(
                text = "Renderer Vulkan nativo será usado. Certifique-se de ter o VulkanMod na pasta mods.",
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )
        }
    }
}
