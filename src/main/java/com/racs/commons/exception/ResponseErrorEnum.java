package com.racs.commons.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.http.HttpStatus.OK;

public enum ResponseErrorEnum {
    EMPTY_RESULT,
    CONSTRAINT,
    VERSION,
    UNKNOWN,
    ID_NOT_FOUND,
    CODE_NOT_FOUND,
    NULL_ENTITY,
    NON_NEW_ENTITY,
    NULL_ID,
    FORM_VALIDATION,
    NOT_TYPE_OR_CLASS;

    @JsonIgnore
    public ResponseEntity<ResponseErrorEnum> asErrorResponse() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Error en el Encabezado", String.valueOf(true));
        return new ResponseEntity<>(this, headers, OK);
    }

}
