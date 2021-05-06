package util;

import exception.CustomException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class FileUtil {

    public ByteArrayInputStream inputStreamToByteArrayInputStream(InputStream inputStream) throws CustomException {
        try {
            return new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            throw new CustomException("Ocurrió un error en el método inputStreamToByteArrayInputStream en FileUtil", e);
        }
    }

    public File inputStreamToFile(String pathname, InputStream inputStream) throws CustomException {
        try {
            File targetFile = new File(pathname);
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            return targetFile;
        } catch (IOException e) {
            throw new CustomException("Ocurrió un error en el método inputStreamToFile en FileUtil", e);
        }
    }


}
