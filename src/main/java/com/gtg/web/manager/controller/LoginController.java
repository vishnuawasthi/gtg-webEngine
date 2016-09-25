package com.gtg.web.manager.controller;

import static com.gtg.processor.constants.Constants.BAD_REQUEST;
import static com.gtg.processor.constants.Constants.INTERNAL_SERVER_ERROR;
import static com.gtg.processor.constants.Constants.INVALID_CREDENTIALS;
import static com.gtg.processor.constants.Constants.LOGGEDOUT_SUCCESSFULLY;
import static com.gtg.processor.constants.Constants.LOGIN_ERROR_MESSAGE;
import static com.gtg.processor.constants.Constants.SERVICE_NOT_AVAILABLE;
import static com.gtg.processor.constants.Constants.STATUS_ERROR;
import static com.gtg.processor.constants.Constants.USER_NAME_REQUIRED;
import static com.gtg.processor.constants.Constants.USER_NOT_EXIST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtg.core.constants.GTGAsyncCallsType;
import com.gtg.processor.criteria.UserSearchCriteria;
import com.gtg.processor.events.GTGMQEvent;
import com.gtg.processor.exception.InvalidUserException;
import com.gtg.processor.manager.dto.LoginRequestDTO;
import com.gtg.processor.manager.dto.LoginResponseDTO;
import com.gtg.processor.manager.dto.UserDTO;
import com.gtg.processor.manager.service.LoginService;
import com.gtg.processor.manager.service.PushToRabbitMQService;
import com.gtg.web.manager.assembler.UserResourceAssembler;
import com.gtg.web.manager.resources.FailureResponseResource;
import com.gtg.web.manager.resources.ForgotPasswordResource;
import com.gtg.web.manager.resources.ForgotPasswordResponseResource;
import com.gtg.web.manager.resources.LoginResource;
import com.gtg.web.manager.resources.SuccessResponseResource;
import com.gtg.web.manager.resources.UserResource;

@RestController
public class LoginController extends BaseController {
	public static final Logger log = Logger.getLogger(LoginController.class);

	@Autowired
	private UserResourceAssembler assembler;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PushToRabbitMQService rabbitMQService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> login(@RequestBody LoginResource loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("login() - start");
		// Authentication auth =
		// SecurityContextHolder.getContext().getAuthentication();
		// String username = auth.getName(); //get logged in username

		System.out.println("loginRequest : " + loginRequest);

		LoginRequestDTO dto = new LoginRequestDTO(loginRequest.getUsername(), loginRequest.getPassword());
		LoginResponseDTO loginResponseDTO = null;
		try {
			loginResponseDTO = loginService.login(dto);
			log.info("login() - end");
			return new ResponseEntity<Object>(loginResponseDTO, HttpStatus.OK);
		} catch (InvalidUserException e) {
			log.error(" Error occured : ", e);
			loginResponseDTO = new LoginResponseDTO();
			loginResponseDTO.setStatus(BAD_REQUEST);
			loginResponseDTO.setApiKey(null);
			loginResponseDTO.setStatusCode(400);
			loginResponseDTO.setUserMessage(INVALID_CREDENTIALS);
			return new ResponseEntity<Object>(loginResponseDTO, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(" Error occured : ", e);
			loginResponseDTO = new LoginResponseDTO();
			loginResponseDTO.setStatus(STATUS_ERROR);
			loginResponseDTO.setApiKey(null);
			loginResponseDTO.setStatusCode(500);
			loginResponseDTO.setUserMessage(LOGIN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(loginResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/logout/{username}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> logout(@PathVariable("username") String username, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("logout() - start");
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
		boolean isLogout = false;
		try {
			if (!StringUtils.isEmpty(username)) {
				isLogout = loginService.logout(username);
				if (isLogout) {
					loginResponseDTO.setUserMessage(LOGGEDOUT_SUCCESSFULLY);
					loginResponseDTO.setStatusCode(200);
					loginResponseDTO.setStatus("OK");
				}
			} else {
				FailureResponseResource failureResponseResource = getFailureResponseResource(BAD_REQUEST,
						USER_NAME_REQUIRED, USER_NAME_REQUIRED);
				return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
			}
		} catch (InvalidUserException e) {
			log.error("Error   : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource(BAD_REQUEST, USER_NOT_EXIST,
					USER_NOT_EXIST);
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Error   : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource(INTERNAL_SERVER_ERROR,
					e.getMessage(), LOGIN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
		}
		log.info("logout() - end");
		return new ResponseEntity<Object>(loginResponseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "api/users/{id}", method = RequestMethod.GET)
	public HttpEntity<Object> getById(@PathVariable Long id) {
		log.info("getById() - start");
		UserDTO userDTO = loginService.getById(id);
		UserResource resource = assembler.toResource(userDTO);
		log.info("getById() - end");
		return new ResponseEntity<Object>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "/registration/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> saveUser(@RequestBody @Valid UserResource resource, BindingResult result) {
		log.info("saveUser() - start");
		try {
			if (result.hasErrors()) {
				FailureResponseResource failureResponseResource = getValidationMessages(result);
				log.info("saveUser() - end : error");
				return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
			}
			UserDTO userDTO = assembler.fromResource(resource);
			Long id = loginService.save(userDTO);
			if (id != null) {
				SuccessResponseResource successResponse = getSuccessResponseResource(id, "Created",
						"Your Request has been process successfully.");
				log.info("saveUser() - end");
				return new ResponseEntity<Object>(successResponse, HttpStatus.OK);
			}
		} catch (InvalidUserException e) {
			FailureResponseResource failureResponseResource = getFailureResponseResource("400", e.getMessage(),
					e.getMessage());
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("Error : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource("500", e.getMessage(),
					SERVICE_NOT_AVAILABLE);
			log.info("saveUser() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("saveUser() - end");
		return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<PagedResources<UserResource>> getAllUsers(
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "sortColumn", required = false) String sortColumn,
			@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable,
			PagedResourcesAssembler<UserDTO> pagedResourcesAssembler) {
		log.info("getAllUsers() - start");
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setPageable(pageable);
		criteria.setSortColumn(sortColumn);
		criteria.setSortOrder(sortOrder);

		Page<UserDTO> page = loginService.getAll(criteria);

		PagedResources<UserResource> pagedResource = null;
		if (page != null) {
			pagedResource = pagedResourcesAssembler.toResource(page, assembler);
			log.info("getAllUsers() - end");
			return new ResponseEntity<PagedResources<UserResource>>(pagedResource, HttpStatus.OK);
		}
		log.info("getAllUsers() - end :  Empty Response");
		return new ResponseEntity<PagedResources<UserResource>>(pagedResource, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/async/registration/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> saveUserAsync(@RequestBody @Valid UserResource resource, BindingResult result) {
		log.info("saveUserAsync() - end");
		try {
			if (result.hasErrors()) {
				FailureResponseResource failureResponseResource = getValidationMessages(result);
				log.info("saveUser() - end : error");
				return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
			}
			UserDTO userDTO = assembler.fromResource(resource);

			GTGMQEvent event = new GTGMQEvent();
			event.setAsyncCallType(GTGAsyncCallsType.REGISTRATION.name());
			event.setUserDTO(userDTO);
			rabbitMQService.pushUserToMQ(event);
			SuccessResponseResource successResponse = getSuccessResponseResource(null, "Created",
					"Your Request has been process successfully");
			log.info("saveUserAsync() - end");
			return new ResponseEntity<Object>(successResponse, HttpStatus.OK);
		}

		catch (Exception e) {
			log.error("Error : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource("500", e.getMessage(),
					SERVICE_NOT_AVAILABLE);
			log.info("saveUserAsync() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public HttpEntity<Object> forgotPassword(
			@RequestBody @Valid ForgotPasswordResource foPasswordResource,
			BindingResult result) {
		log.info("forgotPassword() - start");
		ForgotPasswordResponseResource response = new ForgotPasswordResponseResource();
		if (result.hasErrors()) {
			FailureResponseResource failureResponseResource = getValidationMessages(result);
			log.info("forgotPassword() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
		}

		try {
			UserDTO userDTO = new UserDTO();
			userDTO.setEmail(foPasswordResource.getEmail());
			userDTO.setUsername(foPasswordResource.getUsername());
			boolean isSuccess = loginService.forgotPasswordAsync(userDTO);

			if (isSuccess) {
				response.setStatus("200");
				response.setDescription("Password reset successful");
				response.setMessage("Your password has been reset and sent to your register email id.");
			}
		} catch (InvalidUserException e) {
			log.error("Error Occured while processing forgotPassword : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource("400", e.getMessage(),e.getMessage());
			log.info("forgotPassword() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			log.error("Error Occured while processing forgotPassword : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource("500", e.getMessage(),
					e.getMessage());
			log.info("forgotPassword() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("forgotPassword() - end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/async/forgotPassword", method = RequestMethod.POST)
	public HttpEntity<Object> forgotPasswordAsync(@RequestBody @Valid ForgotPasswordResource foPasswordResource,
			BindingResult result) {
		log.info("forgotPasswordAsync() - start");
		ForgotPasswordResponseResource response = new ForgotPasswordResponseResource();
		if (result.hasErrors()) {
			FailureResponseResource failureResponseResource = getValidationMessages(result);
			log.info("forgotPasswordAsync() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.BAD_REQUEST);
		}

		try {
			UserDTO userDTO = new UserDTO();
			userDTO.setEmail(foPasswordResource.getEmail());
			userDTO.setUsername(foPasswordResource.getUsername());
			GTGMQEvent event = new GTGMQEvent();
			event.setAsyncCallType(GTGAsyncCallsType.FORGOTPASSWORD.name());
			event.setUserDTO(userDTO);
			rabbitMQService.pushUserToMQ(event);
			response.setStatus("200");
			response.setDescription("Password reset successful");
			response.setMessage("Your password has been reset and sent to your register email id.");

		} catch (Exception e) {
			log.error("Error Occured while processing forgotPassword : ", e);
			FailureResponseResource failureResponseResource = getFailureResponseResource("500", e.getMessage(),
					e.getMessage());
			log.info("forgotPasswordAsync() - end : error");
			return new ResponseEntity<Object>(failureResponseResource, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("forgotPasswordAsync() - end");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
