@echo off
rem اسکریپت ساخت فایل امضا و سورس سیگنچر اختصاصی (ویندوز)
echo === ساخت کلید امضای دیجیتال (Keystore) ===
if not exist app mkdir app
keytool -genkeypair -v -keystore app\release-key.jks -alias persianwidget -keyalg RSA -keysize 2048 -validity 10000 -storepass persian1234 -keypass persian1234 -dname "CN=ویجت تاریخ شمسی, OU=Android, O=omidsa596, L=Tehran, C=IR"
echo.
echo کلید اختصاصی با موفقیت در app\release-key.jks ساخته شد.
pause
