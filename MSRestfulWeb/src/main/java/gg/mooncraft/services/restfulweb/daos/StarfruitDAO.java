package gg.mooncraft.services.restfulweb.daos;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.StarfruitData;
import gg.mooncraft.services.restfulweb.models.starfruit.StarfruitCollection;
import gg.mooncraft.services.restfulweb.models.starfruit.StarfruitCollectionItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class StarfruitDAO {
    
    public static @NotNull CompletableFuture<StarfruitData> loadStarfruitData(@NotNull UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> new StarfruitData(loadSkillsData(uniqueId).join(), loadCollectionsData(uniqueId).join()));
    }
    
    private static @NotNull CompletableFuture<Map<String, Integer>> loadSkillsData(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT starfruit.adventuring.user_unique_id, starfruit.adventuring.level, starfruit.archaeology.level, starfruit.construction.level, starfruit.dueling.level, starfruit.farming.level, starfruit.fishing.level, starfruit.foraging.level, starfruit.mining.level, starfruit.smithing.level, starfruit.warfare.level, starfruit.zoology.level FROM sf_skills_adventuring adventuring JOIN sf_skills_archaeology archaeology USING (user_unique_id) JOIN sf_skills_construction construction USING (user_unique_id) JOIN sf_skills_dueling dueling USING (user_unique_id) JOIN sf_skills_farming farming USING (user_unique_id) JOIN sf_skills_fishing fishing USING (user_unique_id) JOIN sf_skills_foraging foraging USING (user_unique_id) JOIN sf_skills_mining mining USING (user_unique_id) JOIN sf_skills_smithing smithing USING (user_unique_id) JOIN sf_skills_warfare warfare USING (user_unique_id) JOIN sf_skills_zoology zoology USING (user_unique_id) WHERE user_unique_id = ?;")
                .with(uniqueId)
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new HashMap<>();
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            
            Map<String, Integer> map = new HashMap<>();
            map.put("Adventuring", resultSetWrapper.get("adventuring.level", Integer.class));
            map.put("Archaeology", resultSetWrapper.get("archaeology.level", Integer.class));
            map.put("Construction", resultSetWrapper.get("construction.level", Integer.class));
            map.put("Dueling", resultSetWrapper.get("dueling.level", Integer.class));
            map.put("Farming", resultSetWrapper.get("farming.level", Integer.class));
            map.put("Fishing", resultSetWrapper.get("fishing.level", Integer.class));
            map.put("Foraging", resultSetWrapper.get("foraging.level", Integer.class));
            map.put("Mining", resultSetWrapper.get("mining.level", Integer.class));
            map.put("Smithing", resultSetWrapper.get("smithing.level", Integer.class));
            map.put("Warfare", resultSetWrapper.get("warfare.level", Integer.class));
            map.put("Zoology", resultSetWrapper.get("zoology.level", Integer.class));
            return map;
        });
    }
    
    private static @NotNull CompletableFuture<List<StarfruitCollection>> loadCollectionsData(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT * FROM (SELECT unique_id AS 'User', 'PvE' AS 'Type', material AS 'Name', amount FROM starfruit.sf_itemgathering_pve UNION SELECT unique_id AS 'User',  'Mining' AS 'Type', material AS 'Name', amount FROM starfruit.sf_itemgathering_mining UNION SELECT unique_id AS 'User',  'Foraging' AS 'Type', material AS 'Name', amount FROM starfruit.sf_itemgathering_foraging UNION SELECT unique_id AS 'User',  'Farming' AS 'Type', material AS 'Name', amount FROM starfruit.sf_itemgathering_farming UNION SELECT unique_id AS 'User',  'Fishing' AS 'Type', identifier AS 'Name', amount FROM starfruit.sf_itemgathering_fishing UNION SELECT unique_id AS 'User',  'Zoology' AS 'Type', CONCAT_WS(':', tier, identifier) AS 'Name', amount FROM starfruit.sf_itemgathering_zoology UNION SELECT unique_id AS 'User',  'Archaeology Artifacts' AS 'Type', CONCAT_WS(':', type, identifier) AS 'Name', amount FROM starfruit.sf_itemgathering_archaeology_artifacts UNION SELECT unique_id AS 'User',  'Archaeology Fossils' AS 'Type', CONCAT_WS(':', tier, identifier) AS 'Name', amount FROM starfruit.sf_itemgathering_archaeology_fossils UNION SELECT unique_id AS 'User',  'Augments' AS 'Type', CONCAT_WS(':', type, identifier) AS 'Name', amount FROM starfruit.sf_itemgathering_augments UNION SELECT unique_id AS 'User',  'Miscellaneous' AS 'Type', CONCAT_WS(':', type, identifier) AS 'Name', amount FROM starfruit.sf_itemgathering_miscellaneous) t1 WHERE User = ?;")
                .with(uniqueId)
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<StarfruitCollection> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                
                String name = resultSetWrapper.get("Type", String.class);
                String itemName = resultSetWrapper.get("Name", String.class);
                int itemAmount = resultSetWrapper.get("Amount", Integer.class);
                
                StarfruitCollection collection = list.stream().filter(starfruitCollection -> starfruitCollection.name().equals(name)).findFirst().orElse(null);
                if (collection == null) {
                    StarfruitCollection starfruitCollection = new StarfruitCollection(name, new ArrayList<>());
                    StarfruitCollectionItem starfruitCollectionItem = new StarfruitCollectionItem(itemName, itemAmount);
                    starfruitCollection.items().add(starfruitCollectionItem);
                    
                    list.add(starfruitCollection);
                    return;
                }
                StarfruitCollectionItem collectionItem = collection.items().stream().filter(starfruitCollectionItem -> starfruitCollectionItem.name().equals(itemName)).findFirst().orElse(null);
                if (collectionItem == null) {
                    StarfruitCollectionItem starfruitCollectionItem = new StarfruitCollectionItem(itemName, itemAmount);
                    collection.items().add(starfruitCollectionItem);
                    return;
                }
                collectionItem = new StarfruitCollectionItem(collectionItem.name(), collectionItem.amount() + itemAmount);
                collection.items().remove(collectionItem);
                collection.items().add(collectionItem);
            });
            return list;
        });
    }
    
}