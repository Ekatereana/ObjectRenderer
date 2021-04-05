package image.converting.pojo;

public class ImageMappingException extends Throwable {
    private static final String ERROR_HEADER = "\nERROR while parsing";
    private static final String SEPARATOR = ":::";
    private static final String FALL_WITH = "fall with message ";
    public ImageMappingException(String message, String fileName) {
        super(ERROR_HEADER + SEPARATOR + fileName + SEPARATOR + FALL_WITH + message);
    }

}
