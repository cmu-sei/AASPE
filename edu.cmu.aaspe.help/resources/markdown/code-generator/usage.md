The following paragraphs detail how to generate CAmkES and C code from an AADL model. It assumes that you already have a complete AADL model that enforces the code generation patterns.

# Generating the CAmkES and partitions (C language) code
To generate the code, select the system implementation in the outline view in eclipse, right click on it and select **camkes - generate code**.

Then, once the code is generated, it creates a directory *instances/camkes*. The generated code is then available in that directory.


# Copy, compile and run the generated code
We assume you have all the dependencies to compile and build a system. To configure your system, read the following: https://wiki.sel4.systems/CAmkES

1. Checkout camkes using the repo tool. Do it like explained there:
```
mkdir camkes-project
cd camkes-project
repo init -u https://github.com/seL4/camkes-manifest.git
repo sync
```

2. Copy the directories generated in Eclipse in the *apps/* directory of the camkes installation.
3. Edit the *Kconfig* file at the root of the camkes installation. In the "Application" section, add a new line and include the apps imported from the generated code. For example, the Kconfig file will look like this:
```
menu "Applications"
    source "apps/capdl-loader-experimental/Kconfig"
    source "apps/uart/Kconfig"
    source "apps/epit/Kconfig"
    source "apps/pingimpl/Kconfig"
    source "apps/boardsecureinstance/Kconfig"
    source "apps/boarddebugbeagleboardinstance/Kconfig"
    source "apps/boarddebugkzminstance/Kconfig"
    source "apps/boarddebuginstance/Kconfig"
    source "apps/pingtimer/Kconfig"
    source "apps/keyboard/Kconfig"
```
4. Run the following command
```
make menuconfig
```
and make sure you configure the appropriate platform (beaglebone or KZM) and application.
5. Compile by invoking the following command
```
make clean all
```

# Run the generated application
You can run the generated application either with QEMU (kzm target) or directly on the target (Beaglebone Black).

## Kzm target
For the KZM target, you can directly use qemu like this:

```
qemu -M kzm -nographic -kernel images/capdl-loader-experimental-image-arm-imx31qemu -M kzm -nographic -kernel images/capdl-loader-experimental-image-arm-imx31
```


## BeagleBone Black

If you want to run the application on the Beaglebone black, you have to copy the binary on a micro SD card and then run it on the board. Follow the instructions there: https://sel4.systems/Info/Hardware/Beaglebone/


Main steps for the beaglebone:
1. Connect to the terminal
```
  screen /dev/ttyUSB0 115200
```
2. Insert the Micro SD card and load the binary
```
  fatload mmc 0 ${loadaddr} sel4test.bin
```
3. Start the binary
```
  go ${loadaddr}
```
