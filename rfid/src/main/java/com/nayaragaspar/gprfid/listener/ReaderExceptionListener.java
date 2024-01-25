package com.nayaragaspar.gprfid.listener;

import com.nayaragaspar.gprfid.producer.ExceptionProducer;
import com.thingmagic.ReadExceptionListener;
import com.thingmagic.ReaderException;

public class ReaderExceptionListener implements ReadExceptionListener {
    ExceptionProducer exceptionProducer;

    public ReaderExceptionListener(ExceptionProducer exceptionProducer) {
        this.exceptionProducer = exceptionProducer;
    }

    public void tagReadException(com.thingmagic.Reader r, ReaderException re) {
        try {
            if (!re.getMessage().equals("No soh FOund")) {
                System.out.println(">>>>> Reader Exception: " + r.paramGet("/reader/uri") + " " + re.getMessage());
                exceptionProducer.publishExceptionMessage(r, re);
            }
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }
}
