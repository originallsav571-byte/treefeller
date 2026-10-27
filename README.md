# Tree Feller (Daraxt kesuvchi)

Minecraft Java Edition **1.21.11** uchun Fabric modi.

## Nima qiladi

Qo'lingizga bolta olib daraxtning bitta tanasini kessangiz, shu tanaga ulangan yuqoridagi barcha loglar birdan yiqiladi.

- **Shift** bosib turib kesilsa, oddiy tarzda faqat bitta blok sinadi.
- Har bir qo'shimcha log boltaning chidamliligini 1 ga kamaytiradi. Bolta sinib qolmaydi: oxirgi 1 ta chidamlilik qoldiriladi va kesish shu yerda to'xtaydi.
- Atrofida barg bo'lmagan loglar (masalan, o'zingiz qurgan uy) kesilmaydi, faqat tabiiy daraxtlar.
- Faqat sindirilgan blokdan yuqoridagi, xuddi shu turdagi loglar yiqiladi. Daraxtning o'rtasidan kessangiz, pastki qismi joyida qoladi.
- Creative rejimida chidamlilik sarflanmaydi va blok tushmaydi.
- Bir martada ko'pi bilan 256 ta log, boshlang'ich blokdan 8 blok radiusda.

## O'rnatish (tayyor `.jar` bo'lgach)

1. Minecraft 1.21.11 uchun Fabric Loader (0.20.1 yoki yangisi) o'rnating.
2. Fabric API `0.141.6+1.21.11` (yoki shu versiyaga mos yangisi) faylini `mods` papkasiga tashlang.
3. `treefeller-1.0.0.jar` faylini ham `mods` papkasiga tashlang.

Mod server tomonida ishlaydi. Serverda o'rnatilsa, o'yinchilarga alohida o'rnatish shart emas.

## `.jar` faylni yig'ish

Kompyuteringizda JDK 21 yoki yangisi o'rnatilgan bo'lishi kerak. Loyiha papkasida:

```
./gradlew build
```

Windows'da: `gradlew.bat build`

Tayyor fayl `build/libs/treefeller-1.0.0.jar` bo'ladi (`-sources.jar` kerak emas). Birinchi yig'ish internetdan Minecraft va Fabric kutubxonalarini yuklagani uchun bir necha daqiqa olishi mumkin.

Boshqa yo'l: loyihani GitHub repozitoriyasiga yuklang. `.github/workflows/build.yml` fayli har bir yuklashda modni o'zi yig'adi, tayyor `.jar` ni Actions bo'limidagi "Artifacts" dan yuklab olasiz.

## Sozlash

`src/main/java/uz/treefeller/TreeFellerMod.java` boshidagi ikkita qiymatni o'zgartirish mumkin:

- `MAX_LOGS`: bir martada kesiladigan loglar soni
- `MAX_HORIZONTAL_RADIUS`: gorizontal bo'yicha eng uzoq masofa

## Litsenziya

CC0-1.0 (jamoat mulki). Erkin o'zgartiring va tarqating.
