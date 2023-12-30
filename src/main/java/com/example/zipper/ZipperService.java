package com.example.zipper;


import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Service
public class  ZipperService {
    private final String file1String="File 1 String is here";
    private final String file2String="file 2 String is here";
    private final String file3String="file 3 String is here";

    private final byte[] file1Byte = file1String.getBytes();
    private final byte[]  file2Byte = file2String.getBytes();
    private final byte[]  file3Byte = file3String.getBytes();

    public File convertToFile(byte[] byteArray,String fileName) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        File file = new File(fileName);
        Files.copy(inputStream,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
    }

    public byte[] zipper(List<File> filesToBeZipped) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for(File fileToBeZipped : filesToBeZipped){
                FileInputStream fis = new FileInputStream(fileToBeZipped);
                zos.putNextEntry(new ZipEntry(fileToBeZipped.getName()));
                int length;
                byte[] buffer = new byte[1024];
                while ((length=fis.read(buffer))>0){
                    zos.write(buffer,0,length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HttpEntity downloadZip() throws IOException {
        List<File> filesToBeZipped = new ArrayList<>();
        filesToBeZipped.add(convertToFile(file1Byte,"file1"));
        filesToBeZipped.add(convertToFile(file2Byte,"file2"));
        filesToBeZipped.add(convertToFile(file3Byte,"file3"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment","compressed.zip");
        return new ResponseEntity<>(zipper(filesToBeZipped),headers, HttpStatus.OK);
    }


}
