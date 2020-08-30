package com.green.health.parents;

import java.util.Set;

import com.green.health.security.entities.UserSecurityJPA;

public interface RatingsParent extends PojoParent {
	
	long getRatingOnes();
	
	long getRatingTwos();
	
	long getRatingThrees();
	
	long getRatingFours();
	
	long getRatingFives();
	
	void setRatingOnes(long ratingOnes);
	
	void setRatingTwos(long ratingTwoos);
	
	void setRatingThrees(long ratingThrees);
	
	void setRatingFours(long ratingFours);
	
	void setRatingFives(long ratingFives);
	
	Set<UserSecurityJPA> getRaters();
	
	default public void addNewRating(int ratings, UserSecurityJPA user){
		switch(ratings){
			case 1:
				setRatingOnes(getRatingOnes()+1);
				break;
			case 2:
				setRatingTwos(getRatingTwos()+1);
				break;
			case 3:
				setRatingThrees(getRatingThrees()+1);
				break;
			case 4:
				setRatingFours(getRatingFours()+1);
				break;
			default:
				setRatingFives(getRatingFives()+1);
				break;
		};
		
		getRaters().add(user);
	}
	
	default public double calculateRating() {
		long total = getRatingOnes() + getRatingTwos() + getRatingThrees() + getRatingFours() + getRatingFives();
		return (double)(getRatingOnes() + 2*getRatingTwos() + 3*getRatingThrees() + 4*getRatingFours() + 5*getRatingFives())/
				(total!=0 ? total : 1);
	}
}
