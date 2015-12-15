/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import org.apache.commons.io.output.CountingOutputStream;

/**
 *
 * @author java
 */
public class CountingServletOutputStream extends ServletOutputStream {

    private final CountingOutputStream output;

    public CountingServletOutputStream(ServletOutputStream output) {
        this.output = new CountingOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException {
        output.write(b);
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    public long getByteCount() {
        return output.getByteCount();
    }

    @Override
    public boolean isReady() {
        System.out.println("READY z aaa");
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        System.out.println("stWriteListner-----------");
    }

}
