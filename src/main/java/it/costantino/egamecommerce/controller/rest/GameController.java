package it.costantino.egamecommerce.controller.rest;

import it.costantino.egamecommerce.dto.GameDTO;
import it.costantino.egamecommerce.entities.Game;
import it.costantino.egamecommerce.services.GameServices;
import it.costantino.egamecommerce.support.Utils;
import it.costantino.egamecommerce.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController

@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameServices gameServices;

    @PostMapping("/add")
    public ResponseEntity addGame(@RequestBody GameDTO gameDTO){
        try{
            if (gameDTO.getName()==null || gameDTO.getPrice()==null || gameDTO.getQuantity()==null)
                return new ResponseEntity("INVALID_GAME",HttpStatus.BAD_REQUEST);
            Game game=gameServices.addGame(gameDTO);
            return new ResponseEntity(game,HttpStatus.OK);
        }catch(AlreadyExistingGameException aege){
            return new ResponseEntity("ERROR_GAME_ALREADY_EXISTING", HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//addGame

    @PutMapping("/updatequantity")
    public ResponseEntity updateQuantityAndPurchaseBooking(@RequestParam(value="name") String name,@RequestParam(value = "quantity") Integer quantity){
        try{
            GameDTO gameDTO=gameServices.updateQuantityAndPurchaseBooking(name,quantity);
            return new ResponseEntity(gameDTO,HttpStatus.OK);
        }catch(NotExistingGameException nege){
            return new ResponseEntity("GAME_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(InvalidQuantityException iqe){
            return new ResponseEntity("INSERT_A_VALID_QUANTITY", HttpStatus.BAD_REQUEST);
        }
        catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//updateQuantity

    @PutMapping("/updateprice")
    public ResponseEntity updatePrice (@RequestParam(value="name") String name, @RequestParam(value = "price") Float price){
        try{
            GameDTO gameDTO=gameServices.updatePrice(name,price);
            return new ResponseEntity(gameDTO,HttpStatus.OK);
        }catch(NotExistingGameException nege){
            return new ResponseEntity("INSERT_A_VALID_GAME", HttpStatus.BAD_REQUEST);
        }catch(InvalidPriceException ipe){
            return new ResponseEntity("INSERT_A_VALID_PRICE", HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//updateQuantity

    @PutMapping("/hide")
    public ResponseEntity hideGame (@RequestParam(value="name")String name){
        try{
            int x=gameServices.hideGame(name);
            boolean result=x>0;
            return new ResponseEntity(result,HttpStatus.OK);
        }catch (NotExistingGameException nege){
            return new ResponseEntity("GAME_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/display")
    public ResponseEntity displayGame (@RequestParam(value="name")String name){
        try{
            int x=gameServices.displayGame(name);
            boolean result=x>0;
            return new ResponseEntity(result,HttpStatus.OK);
        }catch (NotExistingGameException nege){
            return new ResponseEntity("GAME_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/show")
    public ResponseEntity showGames(@RequestParam(value="name",required = false) String name,
                                    @RequestParam(value="pub",required = false) String pub,
                                    @RequestParam(value="dev",required = false) String dev,
                                    @RequestParam(value="pegi",required = false, defaultValue = "nott") String pegiString,
                                    @RequestParam(value="platform",required = false) String platform,
                                    @RequestParam(value="genre",required = false) String genre,
                                    @RequestParam(value="priceMin",required = false, defaultValue = "nott") String priceMinString,
                                    @RequestParam(value="priceMax",required = false, defaultValue = "nott") String priceMaxString,
                                    @RequestParam(value="quantity",required = false, defaultValue = "nott") String quantityString,
                                    @RequestParam(value="isAdmin") boolean isAdmin){
        try{
                List<Game> games=new LinkedList<>();

                Integer pegi=(pegiString.equals("nott"))?Integer.MAX_VALUE:Integer.parseInt(pegiString);
                Float priceMin= (priceMinString.equals("nott")) ? Float.MIN_VALUE : Float.parseFloat(priceMinString);
                Float priceMax=(priceMaxString.equals("nott"))?Float.MAX_VALUE:Float.parseFloat(priceMaxString);
                Integer quantity=(quantityString.equals("nott"))?0:Integer.parseInt(quantityString);

                if (isAdmin) {

                    if (name != null && platform == null && genre == null && priceMin == Float.MIN_VALUE && priceMax == Float.MIN_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByNameContaining(name);

                    else if (name == null && platform != null && genre == null && priceMin == Float.MIN_VALUE && priceMax == Float.MIN_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPlatformContaining(platform);

                    else if (name == null && platform == null && genre != null && priceMin == Float.MIN_VALUE && priceMax == Float.MIN_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByGenreContaining(genre);

                    else if (name == null && platform == null && genre == null && priceMin != Float.MIN_VALUE && priceMax != Float.MIN_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceBetween(priceMin, priceMax);

                    else if (name == null && platform == null && genre == null && priceMin != Float.MIN_VALUE && priceMax == Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceGreaterThanEqual(priceMin);

                    else if (name == null && platform == null && genre == null && priceMin == Float.MIN_VALUE && priceMax > Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceLessThanEqual(priceMax);

                    else
                        games = gameServices.showByAdvancedSearch(name, pub, dev, pegi, platform, genre, priceMin, priceMax, quantity);

                }
                else{
                    if (name != null && platform == null && genre == null && priceMin == Float.MIN_VALUE && priceMax == Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByNameContainingAndHiddenFalse(name);

                    else if (name == null && platform != null && genre == null && priceMin == Float.MIN_VALUE && priceMax == Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPlatformContainingAndHiddenFalse(platform);

                    else if (name == null && platform == null && genre != null && priceMin == Float.MIN_VALUE && priceMax == Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByGenreContainingAndHiddenFalse(genre);

                    else if (name == null && platform == null && genre == null && priceMin != Float.MIN_VALUE && priceMax != Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceBetweenAndHiddenFalse(priceMin, priceMax);

                    else if (name == null && platform == null && genre == null && priceMin != Float.MIN_VALUE && priceMax == Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceGreaterThanEqualAndHiddenFalse(priceMin);

                    else if (name == null && platform == null && genre == null && priceMin == Float.MIN_VALUE && priceMax != Float.MAX_VALUE && pub==null && dev==null && quantity==0)
                        games = gameServices.showByPriceLessThanEqualAndHiddenFalse(priceMax);

                    else
                        games = gameServices.showByAdvancedSearchAndHiddenFalse(name, pub, dev, pegi, platform, genre, priceMin, priceMax, quantity);

                }
                LinkedList<GameDTO> gamesDTO=new LinkedList<>();
                for(Game g: games)
                    gamesDTO.addLast(new GameDTO(g));

                return new ResponseEntity(gamesDTO,HttpStatus.OK);

        }catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN-",HttpStatus.BAD_REQUEST);
        }
    }//showGames



}
