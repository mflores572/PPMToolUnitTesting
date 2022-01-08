package com.loel.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Makes sure we are getting a valid project object by making sure the client
 * passes a valid object
 *
 * Currently this does not check the constraint on the database of unique
 * values. This is because this validation goes first before the
 * projectController.
 */
@Service
public class MapValidationErrorService {

	public ResponseEntity<?> mapValidationService(BindingResult result) {

		if (result.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
		}

		return null;

	}
}