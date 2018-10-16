package com.green.health.ratings.entities.embeddable;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.user.entities.UserJPA;

@Embeddable
public class LinkUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private UserJPA userJpa;
	
	@ManyToOne
	@JoinColumn(name="link_id", referencedColumnName="id")
	private LinkJPA linkJpa;
	
	
}