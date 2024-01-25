package com.nayaragaspar.gprfid.model.message;

public record ReaderExceptionMessage(String readerUri, String errorMessage, String localizedMessage) {

}
