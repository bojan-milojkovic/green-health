package com.green.health.herb.controller;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.service.HerbService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/herb")
public class HerbController {
	
	private HerbService herbServiceImpl;
	
	@Autowired
	public HerbController(HerbService herbServiceImpl) {
		this.herbServiceImpl = herbServiceImpl;
	}

	// .../gh/herb
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<HerbDTO> getAllHerbs(){
		return herbServiceImpl.getAll();
	}
	
	// .../gh/herb/id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody HerbDTO getOneById(@PathVariable("id") Long id) throws MyRestPreconditionsException{
		return herbServiceImpl.getOneById(id);
	}
	
	// .../gh/herb?name=maticnjak
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody HerbDTO getHerbByName(@RequestParam(value="name", required=true) String name) throws MyRestPreconditionsException{
		try {
			return herbServiceImpl.getHerbByLatinName(name);
		}catch (Exception e){
			return herbServiceImpl.getHerbByLocalName(name);
		}
	}
	
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageThumbnail(@PathVariable("id") final Long id) throws MyRestPreconditionsException {
		return herbServiceImpl.getHerbImage(id, "herb_THUMBNAIL");
	}
	
	@RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageLarge(@PathVariable("id") final Long id) throws MyRestPreconditionsException{
		return herbServiceImpl.getHerbImage(id, "herb");
	}
	
	
// .../gh/herb
	/*
---------------------------acebdf13572468
Content-Disposition: form-data; name="json"

{"description":"Stabljika je visoka oko 50cm, razgranata i cetrovouglasta. Stabljika, lisne drske i nervi su modri ili su ljubicasto crvenkasti, narocito u prolece kad nana nice iz zemlje. Listovi su dugacki 3 do 9cm, jajasto kopljasti, tanki, s gornje strane tamnozeleni, a s donje bledji, pri dnu se suzavaju u drske dugacke do 1cm. Po obodu su nejednako zupcasti. Glavni nerv je vrlo istaknut.","growsAt":"Gaji se sirom sveta.","srbName":"nana","latinName":"mentha piperita","whenToPick":"Berbu nane za destilaciju etarskog ulja treba vrsiti po najlepsem vremenu. Suvise mlada nana daje ulje slabog kvaliteta s malo mentola, a mnogo mentona. Precvetala biljka daje manje ulja i losijeg kvaliteta.","properties":"prijatan, blag i neskodljiv lek za umirivanje, protiv gasova, nadimanja i grceva, protiv teskog varenja, kao stomahik, nana ulazi u sastav cajeva za lecenje zuci. Nanino ulje ima slaba anesteticka svojstva i prijatan miris koji osvezava, zbog cega se upotrebljava i protiv gadjenja i povracanja. Rastvor ulja u alkoholu koristi spolja protiv bolova od neuralgije, reumatizma i nazeba. Mentol, naime, drazi nerve u kozi, lako isparava i zbog toga hladi. Nana se ne sme kuvati, vec samo preliti kljucalom vodom da ne izvetre lekoviti sastojci","warnings":"Pitoma nana je potpuno neskodljiva"}
---------------------------acebdf13572468
Content-Disposition: form-data; name="file"; filename="mint.jpg"
Content-Type: image/jpeg

<@INCLUDE *C:\Users\Lazaruss\Desktop\mint.jpg*@>
---------------------------acebdf13572468-- 
	*/
	@RequestMapping(value = "", method = RequestMethod.POST, headers="Content-Type=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addHerb (@RequestParam(value="json", required=true) final String json,
			@RequestParam(value="file", required=true) final MultipartFile file) throws MyRestPreconditionsException {

		try {
			HerbDTO model = (new ObjectMapper()).readValue(json, HerbDTO.class);
			
			model.setImage(file);
			herbServiceImpl.addNew(model);
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add Herb error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	// .../gh/herb/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, headers="Content-Type!=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody HerbDTO editHerb(@RequestBody @Valid HerbDTO model, @PathVariable("id") final Long id) throws MyRestPreconditionsException {
		return herbServiceImpl.edit(model, id);
	}
	
	// .../gh/herb/3
	@RequestMapping(value="/{id}", method = RequestMethod.POST, headers="Content-Type=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody HerbDTO editHerb(@RequestParam(value="file", required=true) final MultipartFile file,
										  @RequestParam(value="json", required=false) final String json,
										  @PathVariable("id") final Long id) 
												  throws MyRestPreconditionsException {
		try {
			HerbDTO model;
			if(RestPreconditions.checkString(json)){
				model = (new ObjectMapper()).readValue(json, HerbDTO.class);
			} else {
				model = new HerbDTO();
			}
			
			model.setImage(file);
			return herbServiceImpl.edit(model, id);
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add Herb error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	// .../gh/herb/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteHerb(@PathVariable("id") final Long id) throws MyRestPreconditionsException {
		herbServiceImpl.delete(id);
	}
}