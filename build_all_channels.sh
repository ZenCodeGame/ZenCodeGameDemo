#!/bin/bash
set -e

channels=("Oppo" "Xiaomi" "Huawei")
types=("Release" "Debug")

# 构建输出目录
output_dir="./build_apks"
mkdir -p "$output_dir"

for channel in "${channels[@]}"; do
    for type in "${types[@]}"; do
        variant="${channel}${type}"
        echo "=========================="
        echo "开始构建 ${variant} 包"
        echo "=========================="

        # Huawei 渠道前检查 JSON
        if [[ "$channel" == "Huawei" && ! -f app/src/huawei/agconnect-services.json ]]; then
            echo "❌ agconnect-services.json 不存在，请检查路径 app/src/huawei/"
            exit 1
        fi

        # 构建 APK
        ./gradlew "assemble${variant}"

        # 构建完成后复制 APK 到统一目录
        lc_channel=$(echo "$channel" | tr '[:upper:]' '[:lower:]')
        lc_type=$(echo "$type" | tr '[:upper:]' '[:lower:]')
        apk_path=$(find ./app/build/outputs/apk/${lc_channel}/${lc_type} -name "*.apk" | head -n 1)
        echo "apk_path ${apk_path}"
        if [[ -f "$apk_path" ]]; then
            cp "$apk_path" "$output_dir/${variant}.apk"
            echo "APK 已保存到 $output_dir/${variant}.apk ✅"
        else
            echo "❌ 未找到 ${variant} APK"
        fi
    done
done

echo "=========================="
echo "所有渠道 Debug 和 Release 包构建完成 🎉"
echo "APK 已集中到 $output_dir"
echo "=========================="
