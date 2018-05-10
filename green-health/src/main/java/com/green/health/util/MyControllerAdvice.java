package com.green.health.util;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.green.health.util.exceptions.MyResourceDoesNotExistException;
import com.green.health.util.exceptions.MyValueAlreadyTakenException;

@ControllerAdvice
public class MyControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequestJsonFields(MethodArgumentNotValidException ex){
		
		MyBadInputResponse bir = new MyBadInputResponse("Some fields in your request body are invalid", 
                ex.getMessage().split("((?<=[)], with [0-9]{1,2} error[(]s[)]):)|((?<=[)]) (?=throws))")[0]);
		
		 bir.setValidationErrors(
	                ex.getBindingResult().getFieldErrors()
	                                     .stream()
	                                     .map(l -> l.getDefaultMessage())
	                                     .collect(Collectors.toList()));
		 
		return bir;
	}
	
	@ExceptionHandler(MyValueAlreadyTakenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyBadInputResponse badRequest_TakenValues(MyValueAlreadyTakenException ex) {
		return new MyBadInputResponse("Some fields in your request body are already being used by other users", 
				ex.getDuplicateValue());
	}
	
	@ExceptionHandler(MyResourceDoesNotExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public MyBadInputResponse BadRequest_WrongEntity(MyResourceDoesNotExistException ex) {
		return new MyBadInputResponse("You are attempting to access a missing entity.", 
				ex.getMissingEntityMessage());
	}
}