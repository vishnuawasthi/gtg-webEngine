package com.gtg.web.manager.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.gtg.processor.manager.dto.UserDTO;
import com.gtg.web.manager.controller.LoginController;
import com.gtg.web.manager.resources.UserResource;

@Service
public class UserResourceAssembler extends ResourceAssemblerSupport<UserDTO, UserResource> {

	public UserResourceAssembler() {
		super(UserDTO.class, UserResource.class);
	}

	@Override
	public UserResource toResource(UserDTO entity) {
		UserResource resource = instantiateResource(entity);
		resource =	dtoToResource(  resource, entity);
		resource.add(linkTo(methodOn(LoginController.class).getById(entity.getId())).withSelfRel());
		return resource;
	}
	
	public List<UserResource> toResources(List<UserDTO> entities) {
		List<UserResource> userResources = new ArrayList<UserResource>(1);
		for(UserDTO dto : entities) {
			UserResource resource = instantiateResource(dto);
			resource =	dtoToResource(  resource, dto);
			resource.add(linkTo(methodOn(LoginController.class).getById(dto.getId())).withSelfRel());
			userResources.add(resource);
		}
		return userResources;
	}
	
	

	public UserDTO fromResource(UserResource resource) {
		UserDTO entity = resourceToDTO(resource);
		return entity;
	}

	private UserDTO resourceToDTO(UserResource resource) {
		UserDTO dto = null;
		if (null != resource) {
			dto = new UserDTO();
			dto.setId(resource.getUserId());
			dto.setUsername(resource.getUsername());
			dto.setPassword(resource.getPassword());
			dto.setApiKey(resource.getApiKey());
			dto.setFirstName(resource.getFirstName());
			dto.setLastName(resource.getLastName());
			dto.setEmail(resource.getEmail());
			dto.setRoleId(resource.getRoleId());
			dto.setRoleName(resource.getRoleName());
			dto.setUpdated(resource.getUpdated());
			dto.setCreated(resource.getCreated());
			dto.setUpdatedBy(resource.getUpdatedBy());
			dto.setCreatedBy(resource.getCreatedBy());
			return dto;

		}

		return null;
	}

	private UserResource dtoToResource( UserResource resource, UserDTO dto) {
		//UserResource resource = null;

		if (null != dto) {
			//resource = new UserResource();
			resource.setUserId(dto.getId());
			resource.setUsername(dto.getUsername());
			resource.setPassword(dto.getPassword());
			resource.setApiKey(dto.getApiKey());
			resource.setFirstName(dto.getFirstName());
			resource.setLastName(dto.getLastName());
			resource.setEmail(dto.getEmail());
			resource.setRoleId(dto.getRoleId());
			resource.setRoleName(dto.getRoleName());
			resource.setUpdated(dto.getUpdated());
			resource.setCreated(dto.getCreated());
			resource.setUpdatedBy(dto.getUpdatedBy());
			resource.setCreatedBy(dto.getCreatedBy());
			return resource;

		}
		return null;
	}

}
