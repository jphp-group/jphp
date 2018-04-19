package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.MongoClient;
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
    @Before
    public void before() {
        MongoClient client = new MongoClient();
        client.getDatabase("jphp-mongo-ext").drop();
    }

    @Test
    public void testBasic() {
        check("mongo/mongo_basic.php");
    }
}
