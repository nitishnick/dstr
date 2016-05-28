package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import converter.ItemConverter;
import model.item.Item;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by deoxys on 27.05.16.
 */
public class MongoItemDAO {
    private MongoCollection<Document> mongoColl;
    private final static String MONGO_DB = "dstr";
    private final static String MONGO_COLL = "items";

    public MongoItemDAO(MongoClient mongoClient) {
        this.mongoColl = mongoClient.getDatabase(MONGO_DB).getCollection(MONGO_COLL);
    }

    public Item createItem(Item item) {
        Document doc = ItemConverter.toDocument(item);
        this.mongoColl.insertOne(doc);
        item.setId(doc.get("_id").toString());
        return item;
    }

    public int updateItem(Item item) {
        Document doc = ItemConverter.toDocument(item);
        UpdateResult updRes = this.mongoColl.updateOne(
                eq("_id", new ObjectId(item.getId())), new Document("$set", doc));
        return (int) updRes.getModifiedCount();
    }

    public Item findItem(Item item) {
        Document doc = this.mongoColl.find(
                eq("_id", new ObjectId(item.getId()))).first();
        return ItemConverter.toItem(doc);
    }

    public List<Item> findAllItems() {
        List<Item> items = new ArrayList<>();
        MongoCursor<Document> cursor = this.mongoColl.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                items.add(ItemConverter.toItem(doc));
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public int deleteItem(Item item) {
        DeleteResult delRes = this.mongoColl.deleteOne(
                eq("_id", new ObjectId(item.getId())));
        return (int) delRes.getDeletedCount();
    }
}