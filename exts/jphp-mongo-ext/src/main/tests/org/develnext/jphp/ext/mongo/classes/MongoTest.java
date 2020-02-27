package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.LazyDBList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.develnext.jphp.ext.mongo.MongoJvmTestCase;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoTest extends MongoJvmTestCase {
    protected boolean availableMongoDb = false;

    @Before
    public void before() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectTimeout(3000);
        builder.socketTimeout(3000);
        builder.maxWaitTime(2000);
        builder.serverSelectionTimeout(2000);
        MongoClient client = new MongoClient(new ServerAddress(), builder.build());
        try {
            client.getAddress();
            availableMongoDb = true;
        } catch (Exception e) {
            System.err.println("Failed to connect MongoDB to test jphp-mongo-ext !!!");
        }

        if (availableMongoDb) {
            client.getDatabase("jphp-mongo-ext").drop();
        }
    }

    @Test
    public void testBasic() {
        if (availableMongoDb) {
            check("mongo/mongo_basic.php");
        }
    }
}
