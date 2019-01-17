import java.io.File;
import java.io.FilenameFilter;

public class FileFilter {

    public static File[] findSpam(File dirName) {


        return dirName.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.contains("spmsg"); }
        } );
    }

    public static File[] findHam(File dirName) {


        return dirName.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return !filename.contains("spmsg"); }
        } );
    }
}
