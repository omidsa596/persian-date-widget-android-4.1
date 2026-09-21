#!/bin/bash
echo "=== در حال ساخت کلید امضای دیجیتال برای نشر در بازار / مایکت / گوگل‌پلی ==="
KEYSTORE_PATH="app/release-key.jks"
ALIAS="persianwidget"
PASS="persian1234"

mkdir -p app
keytool -genkeypair -v \
  -keystore "$KEYSTORE_PATH" \
  -alias "$ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "$PASS" \
  -keypass "$PASS" \
  -dname "CN=ویجت تاریخ شمسی, OU=Android, O=omidsa596, L=Tehran, C=IR"

echo "✅ کلید اختصاصی با موفقیت در مسیر $KEYSTORE_PATH ایجاد شد."
echo "رمز عبور پیش‌فرض: $PASS"
echo "نام الیاس (Alias): $ALIAS"
