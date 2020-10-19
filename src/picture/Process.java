package picture;

import java.util.List;

public class Process {
    final int MAX_VALUE = 255;

    Picture picture;
    Picture outputPicture;

    public Process(Picture picture) {
        this.picture = picture;
    }

    public void invert() {
        outputPicture = Utils.createPicture(picture.getWidth(), picture.getHeight());
        for(int i = 0; i < picture.getWidth(); i++) {
            for(int j = 0; j < picture.getHeight(); j++) {
                Color colors = picture.getPixel(i, j);
                Color invertedColors = new Color(MAX_VALUE - colors.getRed(), MAX_VALUE - colors.getGreen(), MAX_VALUE - colors.getBlue());
                outputPicture.setPixel(i, j, invertedColors);
            }
        }
    }

    public void toGreyscale() {
        outputPicture = Utils.createPicture(picture.getWidth(), picture.getHeight());
        for(int i = 0; i < picture.getWidth(); i++) {
            for(int j = 0; j < picture.getHeight(); j++) {
                Color colors = picture.getPixel(i, j);
                int avgRGB = (colors.getRed() + colors.getGreen() + colors.getBlue()) / 3;
                Color greyscale = new Color(avgRGB, avgRGB, avgRGB);
                outputPicture.setPixel(i, j, greyscale);
            }
        }
    }

    //improved code so rotate only requires 1 function.
    public void rotate(int n) {
        outputPicture = picture;
        for(int i = 0; i < (n / 90); i++) {
            Picture outputPicture2 = Utils.createPicture(outputPicture.getHeight(), outputPicture.getWidth());
            for(int x = 0; x < outputPicture.getWidth(); x++) {
                for(int y = 0; y < outputPicture.getHeight(); y++) {
                    Color colors = outputPicture.getPixel(x, y);
                    outputPicture2.setPixel(outputPicture.getHeight() - y - 1, x, colors);
                }
            }
            outputPicture = outputPicture2;
        }
    }

    public void flipH() {
        outputPicture = Utils.createPicture(picture.getWidth(), picture.getHeight());
        for(int i = 0; i < picture.getWidth(); i++) {
            for(int j = 0; j < picture.getHeight(); j++) {
                Color colors = picture.getPixel(i, j);
                outputPicture.setPixel(picture.getWidth() - i - 1, j, colors);
            }
        }
    }

    public void flipV() {
        outputPicture = Utils.createPicture(picture.getWidth(), picture.getHeight());
        for(int i = 0; i < picture.getWidth(); i++) {
            for(int j = 0; j < picture.getHeight(); j++) {
                Color colors = picture.getPixel(i, j);
                outputPicture.setPixel(i, picture.getHeight() - j - 1, colors);
            }
        }
    }

    public void blend(List<Picture> pictures) {
        int minWidth = pictures.get(0).getWidth();
        int minHeight = pictures.get(0).getHeight();

        for(Picture picture : pictures) {
            if(picture.getWidth() < minWidth) {
                minWidth = picture.getWidth();
            }
            if(picture.getHeight() < minHeight) {
                minHeight = picture.getHeight();
            }
        }
        outputPicture = Utils.createPicture(minWidth, minHeight);
        for(int i = 0; i < minWidth; i++) {
            for(int j = 0; j < minHeight; j++) {
                // num of images = makes code modular, so code also works if there is not min width/height restriction.
                int numOfImages = 0;
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;
                for(Picture picture : pictures) {
                    totalRed += picture.getPixel(i, j).getRed();
                    totalGreen += picture.getPixel(i, j).getGreen();
                    totalBlue += picture.getPixel(i, j).getBlue();
                    numOfImages++;
                }
                //get the average colors of everything.
                int avgRed = totalRed / numOfImages;
                int avgGreen = totalGreen / numOfImages;
                int avgBlue = totalBlue / numOfImages;
                Color colors = new Color(avgRed, avgGreen, avgBlue);
                outputPicture.setPixel(i, j, colors);
            }
        }
    }

    public void blur() {
        outputPicture = Utils.createPicture(picture.getWidth(), picture.getHeight());
        for(int i = 0; i < picture.getWidth(); i++) {
            outputPicture.setPixel(i,0, picture.getPixel(i, 0));
        }
        for(int i = 0; i < picture.getWidth(); i++) {
            outputPicture.setPixel(i,picture.getHeight() - 1, picture.getPixel(i, picture.getHeight() - 1));
        }
        for(int j = 0; j < picture.getHeight(); j++) {
            outputPicture.setPixel(0, j, picture.getPixel(0, j));
        }
        for(int j = 0; j < picture.getHeight(); j++) {
            outputPicture.setPixel(picture.getWidth() - 1, j, picture.getPixel(picture.getWidth() - 1, j));
        }
        for(int i = 1; i < picture.getWidth() - 1; i++) {
            for(int j = 1; j < picture.getHeight() - 1; j++) {
                int numOfPixels = 0;
                int blurredRed = 0;
                int blurredGreen = 0;
                int blurredBlue = 0;
                for(int k = i - 1; k <= i + 1; k++){
                    for(int l = j - 1; l <= j + 1; l++){
                        blurredRed += picture.getPixel(k, l).getRed();
                        blurredGreen += picture.getPixel(k, l).getGreen();
                        blurredBlue += picture.getPixel(k, l).getBlue();
                        numOfPixels++;
                    }
                }
                blurredRed /= numOfPixels;
                blurredGreen /= numOfPixels;
                blurredBlue /= numOfPixels;
                Color colors = new Color(blurredRed, blurredGreen, blurredBlue);
                outputPicture.setPixel(i, j, colors);
            }
        }
    }

    private int getNumAFitsIntoNumB(int num1, int num2) {
        int counter = 0;
        while(num1 >= num2) {
            counter++;
            num1 -= num2;
        }
        return counter;
    }

    // extension project
    public void mosaic(List<Picture> pictures, int tileSize) {
        int minWidth = pictures.get(0).getWidth();
        int minHeight = pictures.get(0).getHeight();

        for(Picture picture : pictures) {
            if(picture.getWidth() < minWidth) {
                minWidth = picture.getWidth();
            }
            if(picture.getHeight() < minHeight) {
                minHeight = picture.getHeight();
            }
        }
        //trim the minWidth and minHeight:
        minWidth -= (minWidth % tileSize);
        minHeight -= (minHeight % tileSize);
        int k; // the counter which indexes the image in the list pictures.
        //creating picture to output
        outputPicture = Utils.createPicture(minWidth, minHeight);
        // j is the current row so it will need ro be called first
        for(int j = 0; j < minWidth; j++) {
            // k will be the index of the the image to be inserted into the picture.
            k = getNumAFitsIntoNumB(j, tileSize);
            for(int i = 0; i < minHeight; i++) {   // we iterate over each row first, hence nested for loop for i
                k = (k + getNumAFitsIntoNumB(i, tileSize)) % pictures.size(); //k cant be greater than length pictures.
                Color colors = pictures.get(k).getPixel(i, j);
                outputPicture.setPixel(i, j, colors);
            }
        }
    }



    public void outputImage(String destination) {
        Utils.savePicture(outputPicture, destination);
    }
}
