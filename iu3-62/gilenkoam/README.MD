### В репозиторие доступны теги:

`lr1`, `lr2`, `lr3`  - ЛР1, 2, 3 - собираются через cmake:

```shell
> mkdir build && cd build
> cmake ..
> cmake build .
```

`lr4` - ЛР4. Собирается долго, муторно и грязно. Не советую собирать. Требования:
```
CMake >= 3.16
C++ Compiler (с поддержкой C++17)
vcpkg - актуальный
```

Сборка:
```shell
> mkdir build && cd build
> $env:VCPKG_FORCE_SYSTEM_BINARIES=1
> cmake .. `
  -DCMAKE_TOOLCHAIN_FILE=C:\vcpkg\scripts\buildsystems\vcpkg.cmake `
  -DVCPKG_TARGET_TRIPLET=x64-windows `
  -A x64
> cmake --build . --config Release
```
