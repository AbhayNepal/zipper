package com.example.zipper;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
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



    public byte[] zipper(List<ByteArrayInputStream> filesToBeZipped) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            int i=0;
            for(ByteArrayInputStream inputStream: filesToBeZipped){
                zos.putNextEntry(new ZipEntry("file"+i));
                i+=1;
                int length;
                byte[] buffer = new byte[1024];
                while ((length=inputStream.read(buffer))>0){
                    zos.write(buffer,0,length);
                }
                zos.closeEntry();
                inputStream.close();
            }
            zos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HttpEntity downloadZip() throws IOException {
        List<ByteArrayInputStream> filesToBeZipped = new ArrayList<>();
        filesToBeZipped.add(new ByteArrayInputStream(file1Byte));
        filesToBeZipped.add(new ByteArrayInputStream(file2Byte));
        filesToBeZipped.add(new ByteArrayInputStream(file3Byte));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment","compressed.zip");
        return new ResponseEntity<>(zipper(filesToBeZipped),headers, HttpStatus.OK);
    }


}
