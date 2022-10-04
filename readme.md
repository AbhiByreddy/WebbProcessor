https://www.youtube.com/watch?v=1QPJd2Fl6i4


### Downloading the Data from MAST
[Download](https://mast.stsci.edu/portal/Mashup/Clients/Mast/Portal.html)

1. Advanced Search
2. Search for data
3. You can look for "objects in space" in "Object Name"
4. Try "Carina" or "NGC 3324"(An area in the Carina formation)
5. Mission: JWST
6. Click Search
7. Instrument: NIRCAM
8. Select All
9. Download (Basket Icon)
10. Filter 
11. Expand Files
12. Click the 2d.fits files (bottom ones that are massive)
13. Download fits files


### FitsLiberator
#### FitsLiberator takes the fits files we just downloaded and reveals the light data that makes the image seeable to the human eye through a process called "stretching"
[Download](https://noirlab.edu/public/products/fitsliberator/)

1. Try Asinh, and try to scroll around the bottom bit.
2. Scaled Peak Level: 1000 works for carina
3. Save as 16 bit tiff


### Pixinsight
#### Pixinsight lets you apply scripts to automatically fill star cores (null regions) with their nearest neighbor
[Download](https://pixinsight.com/)
[Script](https://www.dropbox.com/sh/1lsp88pt6nlbc29/AACp8W2Emy9FKDwYlbWA_1pDa?dl=0)

PixInsight has an active community/forum. There are probably many
scripts that we can look at then.... javascript

You can take a script from these active forums and change some values to your liking (will take some trial and error)
1. Apply script and fix star cores for each image (apply to all the filters)
1b. Can also bring fits files straight into Pixinsight
1c. You can also keep processing the image in Pixinsight instead of moving it to photoshop

### Photoshop/Gimp
#### Take filters and apply color to them
1. Take all filters and load into photoshop
2. The longest wavelength gets reddest color
3. The shortest wavelength gets bluest color
4. Additively combine images
5. Shift away from complete red and complete blue
6. Up the contrast
7. Turn down saturation a bit