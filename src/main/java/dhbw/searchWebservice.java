/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw;

import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nfi
 */

@RestController
public class searchWebservice {
    
    @RequestMapping("/search")
    public String webSiteSearch(@RequestParam(value = "query") String query, @RequestParam(value = "type") String type) {
        SpotifyRequest spotifyRequest = new SpotifyRequest(RequestType.SEARCH);
        Optional<String> stringOptional;
        RequestCategory category;
        category = RequestCategory.valueOf(type);
        String string = "";
        String json = "";
        try {
            stringOptional = spotifyRequest.performeRequestSearch(category, query, 10, "DE");
            if (stringOptional.isPresent()) {
                string = stringOptional.get();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<dhbw.pojo.search.track.Item> itemTrack = new ArrayList<>();
            List<dhbw.pojo.search.album.Item> itemAlbum = new ArrayList<>();
            List<dhbw.pojo.search.artist.Item> itemArtist = new ArrayList<>();
            List<SearchResultList> resultList = new ArrayList<>();
            switch (type) {
                case "TRACK":
                    SearchTrack track = mapper.readValue(string, SearchTrack.class);
                    itemTrack = track.getTracks().getItems();
                    for (dhbw.pojo.search.track.Item element : itemTrack) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                case "ALBUM":
                    SearchAlbum album = mapper.readValue(string, SearchAlbum.class);
                    itemAlbum = album.getAlbums().getItems();
                    for (dhbw.pojo.search.album.Item element : itemAlbum) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                case "ARTIST":
                    SearchArtist artist = mapper.readValue(string, SearchArtist.class);
                    itemArtist = artist.getArtists().getItems();
                    for (dhbw.pojo.search.artist.Item element : itemArtist) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                default:

            }
            SearchResult searchResult = new SearchResult();
            searchResult.setSearchTerm(query);
            searchResult.setSearchCategory(type);
            searchResult.setResults(resultList);
            json = mapper.writeValueAsString(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
    
}
