//
// Turnip Vulkan Loader Stub
// This provides a placeholder for loadTurnipVulkan when Turnip driver is not available
//

#include <dlfcn.h>
#include <string.h>
#include <android/log.h>

#define LOG_TAG "TurnipLoader"

// Stub implementation - returns NULL to fall back to system Vulkan
// In a full implementation, this would load the Turnip Mesa driver
void *loadTurnipVulkan(const char *driver_path, const char *native_dir, const char *cache_dir) {
    (void)driver_path;
    (void)native_dir;
    (void)cache_dir;
    
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "loadTurnipVulkan stub called - falling back to system Vulkan");
    
    // Return NULL to indicate Turnip is not available, caller will fall back to libvulkan.so
    return NULL;
}
