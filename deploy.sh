#!/bin/bash

# Enforce strict error handling for pipeline reliability
set -euo pipefail

# Configuration variables
PROJECT_ROOT="."
UI_DIR="${PROJECT_ROOT}/app/src/main/java/com/workoutapp/ui"
AI_INPUT_FILE="ai_output.kt"
APK_PATH="${PROJECT_ROOT}/app/build/outputs/apk/debug/app-debug.apk"
PACKAGE_NAME="com.workoutapp"

# Ensure ADB is discoverable via Android SDK
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
export PATH="${ANDROID_HOME}/platform-tools:${PATH}"

echo "[*] Initializing local deployment pipeline..."

# 1. Integration
if [[ ! -f "$AI_INPUT_FILE" ]]; then
    echo "[-] Error: $AI_INPUT_FILE not found. Save the AI output first."
    exit 1
fi

echo "[*] Injecting generated code into project structure..."
mkdir -p "$UI_DIR"
cp "$AI_INPUT_FILE" "${UI_DIR}/GeneratedWorkoutUI.kt"

# 2. Static Application Security Testing (SAST) & Linting
# Catch vulnerabilities and enforce Kotlin structural rules before compilation
echo "[*] Executing static analysis (Detekt)..."
./gradlew detekt --quiet

# 3. Compilation
echo "[*] Compiling debug APK..."
./gradlew assembleDebug --quiet

# 4. Deployment
echo "[*] Pushing build to connected device/emulator..."
# Check if a device/emulator is connected before attempting install
DEVICE_COUNT=$(adb devices | grep -c 'device$' || true)
if [[ "$DEVICE_COUNT" -eq 0 ]]; then
    echo "[!] No Android device or emulator detected. APK is ready at: $APK_PATH"
    echo "[!] Connect a device or start an emulator, then run: adb install -r -t $APK_PATH"
    exit 0
fi
# -r replaces existing application, -t allows test packages
adb install -r -t "$APK_PATH"

# 5. Launch
echo "[*] Launching application..."
adb shell am start -n "${PACKAGE_NAME}/.MainActivity"

echo "[+] Pipeline execution completed successfully."