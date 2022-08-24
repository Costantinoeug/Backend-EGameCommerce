package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game,String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Game g set g.hidden = true where g.name = :name")
    int hideGame(String name);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Game g set g.hidden = false where g.name = :name")
    int displayGame(String name);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Game g set g.quantity = :quantity where g.name = :name")
    int updateQuantity(String name, int quantity);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Game g set g.price = :price where g.name = :name")
    int updatePrice(String name, float price);



    Game findByNameAndHiddenFalse(String name);

    Game findByName(String name);

    @Query("select g from Game g where g.name like  CONCAT('%',:name,'%') and g.hidden=false ")
    List<Game> findByNameContainingAndHiddenFalse(String name);

    List<Game> findByNameContaining(String name);


    @Query("select g from Game g where g.genre like  CONCAT('%',:genre,'%') and g.hidden=false ")
    List<Game> findByGenreContainingAndHiddenFalse(String genre);

    List<Game> findByGenreContaining(String genre);


    @Query("select g from Game g where g.platform like  CONCAT('%',:platform,'%') and g.hidden=false ")
    List<Game> findByPlatformContainingAndHiddenFalse(String platform);

    List<Game> findByPlatformContaining(String platform0);

    List<Game> findByPriceGreaterThanEqualAndHiddenFalse(float price);

    List<Game> findByPriceGreaterThanEqual(float price);

    List<Game> findByPriceLessThanEqualAndHiddenFalse(float price);

    List<Game> findByPriceLessThanEqual(float price);

    List<Game> findByPriceBetweenAndHiddenFalse(float p1, float p2);

    List<Game> findByPriceBetween(float p1, float p2);

    boolean existsByNameAndHiddenFalse(String name);


    boolean existsByName(String name);



    @Query("select g from Game g " +
            "where  ( g.name   LIKE CONCAT('%',:name,'%') or :name is null ) and " +
            "       (g.publisher is null or g.publisher   LIKE CONCAT('%',:pub,'%')  or :pub is null)  and " +
            "      ( g.developer is null or g.developer   LIKE CONCAT('%',:dev,'%')  or :dev is null)  and " +
            "       (g.pegi is null or g.pegi<=:pegi)  and " +
            "       (g.platform is null or g.platform   LIKE CONCAT('%',:platform,'%')  or :platform is null )  and " +
            "       (g.genre is null or g.genre   LIKE CONCAT('%',:genre,'%')  or   :genre is null)    and " +
            "       (g.price >= :priceMin ) and " +
            "       (g.price <= :priceMax ) and " +
            "       (g.quantity >= :quantity) and"+
            "       g.hidden=false")
    List<Game> advancedSearchAndHiddenFalse(String name, String pub, String dev, int pegi, String platform, String genre, float priceMin, float priceMax,int quantity);

    @Query("select g from Game g " +
            "where  ( g.name   LIKE CONCAT('%',:name,'%') or :name is null ) and " +
            "       (g.publisher is null or g.publisher   LIKE CONCAT('%',:pub,'%')  or :pub is null)  and " +
            "      ( g.developer is null or g.developer   LIKE CONCAT('%',:dev,'%')  or :dev is null)  and " +
            "       (g.pegi is null or g.pegi<=:pegi)  and " +
            "       (g.platform is null or g.platform   LIKE CONCAT('%',:platform,'%')  or :platform is null )  and " +
            "       (g.genre is null or g.genre   LIKE CONCAT('%',:genre,'%')  or   :genre is null)    and " +
            "       (g.price >= :priceMin ) and " +
            "       (g.price <= :priceMax ) and " +
            "       (g.quantity >= :quantity)" )
    List<Game> advancedSearch(String name, String pub, String dev, int pegi, String platform, String genre, float priceMin, float priceMax,int quantity);

}