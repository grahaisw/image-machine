# image-machine 
Image Machine Apps is Android apps for grouping images on your phones and make it easier for people to access. 

This apps only has 2 entity “Machine” & “Machine Image” where the relation is one to many since one machine can have multiple images, so the Machine Image entity has Machine ID as foreign key, as shown below :

![image (5)](https://user-images.githubusercontent.com/17241973/145720408-2f9b908b-d0fe-4cba-b495-5fc0edd1ab1c.png)

Entity Machine has two unique attribute, the first one is Machine ID as primary key and the other is Machine Code for QR Code. Since this QR Code will be used for QR Code Reader to identify machine, so it’s must be unique and bind to the machine.    

![database_scheme](https://user-images.githubusercontent.com/17241973/145720448-fe6140af-d2c7-47c6-80fa-414c75f532fb.png)

This apps has 2 main menu “Machine Data” & “Code Reader”, Machine Data was built for user to create machine and add maximum 10 images for each machine and Code Reader for user who wants to access Machine Data faster with just scan QR Code.

This image machine apps was using 3rd party libraries, the libraries are : 
- wdullaer for datepicker : https://github.com/wdullaer/MaterialDateTimePicker
- dexter from Karumi for easy permission : https://github.com/Karumi/Dexter
- zxing for QR Code reader : https://github.com/zxing/zxing
