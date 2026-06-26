#ifndef VULKAN_CHECKER_H
#define VULKAN_CHECKER_H

#include <jni.h>

// Variável global para controlar o modo Vulkan (acessível em gl_bridge.c)
int g_vulkan_mode = 0;

// Função para definir o modo Vulkan (chamada do Kotlin)
JNIEXPORT void JNICALL
Java_com_movtery_zalithlauncher_utils_device_VulkanChecker_nativeSetVulkanMode(JNIEnv *env, jobject obj, jboolean enabled);

// Função para verificar se o modo Vulkan está ativo
JNIEXPORT jboolean JNICALL
Java_com_movtery_zalithlauncher_utils_device_VulkanChecker_nativeIsVulkanModeEnabled(JNIEnv *env, jobject obj);

#endif // VULKAN_CHECKER_H
