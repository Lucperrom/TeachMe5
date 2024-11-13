package com.mongodb.starter.rating;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.starter.util.MessageResponse;
import com.mongodb.starter.util.RestPreconditions;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Ratings", description = "The ratings management API")
public class RatingController {
    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;

    @Autowired
	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}


    //CREATE	

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Rating> create(@RequestBody Rating rating){
        logger.info("Recibiendo solicitud para crear entidad: {}", rating);
		Rating newRating = new Rating();
		Rating savedRating;
		BeanUtils.copyProperties(rating, newRating, "id");
		savedRating = this.ratingService.saveRating(newRating);
        logger.info("Entidad guardada: {}", savedRating);
		return new ResponseEntity<>(savedRating, HttpStatus.OK);
	}


    //DELETE	

	@DeleteMapping("{ratingId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> delete(@PathVariable("ratingId") int ratingId) {
		Rating rating = RestPreconditions.checkNotNull(ratingService.findRatingById(ratingId), "Rating", "ID", ratingId);
		ratingService.deleteRating(ratingId);
		return new ResponseEntity<>(new MessageResponse("Rating deleted!"), HttpStatus.OK);
	}

    //UPDATE

	@PutMapping("{ratingId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Rating> update(@PathVariable("ratingId") int ratingId, @RequestPart("rating") @Valid Rating rating) {
		Rating aux = RestPreconditions.checkNotNull(ratingService.findRatingById(ratingId), "Rating", "ID", ratingId);
		// Integer id = Integer.parseInt(userId);
		// User loggedUser = userService.findUser(id);
		// 	User paperUser = aux.getUser();
		// 	if (loggedUser.getId().equals(paperUser.getId())) {
		Rating res = ratingService.updateRating(rating, ratingId);
		return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //GET BY ID

	@GetMapping("{ratingId}")
	public ResponseEntity<Rating> findById(@PathVariable("ratingId") int ratingId) {
		Rating rating = RestPreconditions.checkNotNull(ratingService.findRatingById(ratingId), "Paper", "ID", ratingId);
			return new ResponseEntity<>(rating, HttpStatus.OK);
	} 

    @GetMapping
	public ResponseEntity<List<Rating>> findAll() {
	    return new ResponseEntity<>((List<Rating>) this.ratingService.findAll(), HttpStatus.OK);

	}


}
