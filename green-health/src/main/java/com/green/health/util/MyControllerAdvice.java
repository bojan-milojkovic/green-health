package com.green.health.util;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.green.health.util.exceptions.MyRestPreconditionsException;
import com.green.health.util.exceptions.UsernameNotFoundException;

@ControllerAdvice
public class MyControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(MyControllerAdvice.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequest_JsonFields(MethodArgumentNotValidException ex) {
		
		MyBadInputResponse bir = new MyBadInputResponse("Some fields in your request body are invalid", 
                ex.getMessage().split("((?<=[)], with [0-9]{1,2} error[(]s[)]):)|((?<=[)]) (?=throws))")[0]);
		
		 bir.setValidationErrors(
	                ex.getBindingResult().getFieldErrors()
	                                     .stream()
	                                     .map(l -> l.getDefaultMessage())
	                                     .collect(Collectors.toList()));
		logger.error(bir.toString());
		return bir;
	}
	
	@ExceptionHandler(MyRestPreconditionsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequest_TakenValues(MyRestPreconditionsException ex) {
		MyBadInputResponse bir = new MyBadInputResponse(ex.getDescription(), ex.getDetails());
		
		bir.setValidationErrors(ex.getErrors());
		logger.error(bir.toString());
		return bir;
	}
	
	@ExceptionHandler({JsonMappingException.class, EmptyResultDataAccessException.class, EntityNotFoundException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public MyBadInputResponse BadRequest_MissingEntity(RuntimeException ex) {
		MyBadInputResponse bir = new MyBadInputResponse("You are attempting to process an invalid object.", 
				ex.getLocalizedMessage());
		logger.error(bir.toString());
		return bir;
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public MyBadInputResponse BadCredentials(UsernameNotFoundException ex){
		logger.error("invalid credentials.");
		return new MyBadInputResponse("Your credentials are invalid.", 
				ex.getLocalizedMessage());
	}
	
	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badNumberInput(NumberFormatException ex) {
		MyBadInputResponse bir = new MyBadInputResponse("Invalid numerical value", 
				ex.getLocalizedMessage());
		logger.error(bir.toString());
		return bir;
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse databaseIntegrityViolation(SQLIntegrityConstraintViolationException ex) {
		MyBadInputResponse bir = new MyBadInputResponse("Database did not like one of the request values", 
				ex.getLocalizedMessage());
		logger.error(bir.toString());
		return bir;
	}
}