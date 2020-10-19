package picture;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Process process;

    switch(args[0]) {

      case "invert":
        process = new Process(Utils.loadPicture(args[1]));
        process.invert();
        process.outputImage(args[2]);
        break;

      case "grayscale":
        process = new Process(Utils.loadPicture(args[1]));
        process.toGreyscale();
        process.outputImage(args[2]);
        break;

      case "rotate":
        process = new Process(Utils.loadPicture(args[2]));
        process.rotate(Integer.parseInt(args[1]));
        process.outputImage(args[3]);
        break;

      case "flip":
        process = new Process(Utils.loadPicture(args[2]));
        switch(args[1]) {
          case "H":
            process.flipH();
            break;
          case "V":
            process.flipV();
            break;
          default:
            System.out.println("Please input 90, 180 or 270");
        }
        process.outputImage(args[3]);
        break;

      case "blend":
        process = new Process(Utils.createPicture(1, 1));
        List<Picture> inputLocations = new ArrayList<>();
        for(int i = 1; i < args.length - 1; i++) {
          inputLocations.add(Utils.loadPicture(args[i]));
        }
        process.blend(inputLocations);
        process.outputImage(args[args.length - 1]);
        break;

      case "blur":
        process = new Process(Utils.loadPicture(args[1]));
        process.blur();
        process.outputImage(args[2]);
        break;

        //extension to implement mosaic functionality
      case "mosaic":
        process = new Process(Utils.createPicture(1, 1));
        inputLocations = new ArrayList<>();
        for(int i = 2; i < args.length - 1; i++) {
            inputLocations.add(Utils.loadPicture(args[i]));
        }
        // mosaic takes list of images and tileSize as parameters.
        process.mosaic(inputLocations, Integer.parseInt(args[1]));
        process.outputImage(args[args.length - 1]);
        break;

      default:
        System.out.println("Function not yet implemented.");
    }
  }
}
